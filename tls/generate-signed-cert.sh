#!/bin/sh

SCRIPT_NAME=$(basename "$0")

if [ $# -eq 0 ]ls ; then
  echo "Provide arguments."
  echo "Usage:"
  echo "$SCRIPT_NAME <base-path> <CN> <out-pass>"
  echo ""
  echo "Example:"
  echo "$SCRIPT_NAME sensor_module_1 sensor_module_1.docker.ahf changeit"
  echo ""
  echo ""
  echo "This will generate a keystore with a signed certificate on base-path.jks"
  echo "(TODO: Improve this message)"
  exit 1
fi

base_path=$1
cn=$2
out_pass=$3
ca_pass=changeit
cacrt_path=$(dirname "$(readlink -f "$0")")/ca.crt
cakey_path=$(dirname "$(readlink -f "$0")")/ca.key
cacerts_path=$(dirname "$(readlink -f "$0")")/cacerts.jks

rm -f "${base_path}.key" "${base_path}.csr" "${base_path}.p12" "${base_path}.jks"

# Generate key
openssl genrsa \
        -des3 \
        -out "${base_path}.key" \
        -passout "pass:${out_pass}" \
        4096

# Generate certificate sign request
openssl req \
        -new \
        -key "${base_path}.key" \
        -out "${base_path}.csr" \
        -passin "pass:${out_pass}" \
        -subj "/C=SE/L=Europe/O=ArrowheadFramework/OU=DockerTools/CN=$cn"

# Generate signed certificate given the CA files
openssl x509 \
        -req \
        -days 364 \
        -in "${base_path}.csr" \
        -CA "${cacrt_path}" \
        -CAkey "${cakey_path}" \
        -out "${base_path}.crt" \
        -passin "pass:${ca_pass}" \
        -set_serial 01

# Package into p12 keystore
cat "${base_path}.crt" "${cacrt_path}" > "${base_path}-ca.crt"
openssl pkcs12 \
        -export \
        -in "${base_path}-ca.crt" \
        -inkey "${base_path}.key" \
        -out "${base_path}.p12" \
        -name "${base_path}" \
        -CAfile "${cacrt_path}" \
        -caname "ca" \
        -passin "pass:${out_pass}" \
        -passout "pass:${out_pass}"

# Package into jks keystore
keytool -importkeystore \
        -srckeystore "${base_path}.p12" \
        -srcstoretype pkcs12 \
        -destkeystore "${base_path}.jks" \
        -deststoretype jks \
        -deststorepass "${out_pass}" \
        -srcstorepass "${out_pass}"

# Package into PEM format for CURL
openssl pkcs12 \
        -in ${base_path}.p12 \
        -out ${base_path}.pem \
        -passin "pass:${out_pass}" \
        -passout "pass:${out_pass}"
keytool -export \
        -alias "dockerca" \
        -file dockerca.der \
        -keystore "${cacerts_path}" \
        -storepass changeit
openssl x509 \
        -inform der \
        -in dockerca.der \
        -out dockerca.pem

# Clean up, only leave jks. Modify as needed.
rm -f "${base_path}.key" "${base_path}.csr" "${base_path}.p12"
