# gera a chave e o certificado da CA

# gera a chave da CA
openssl genrsa -out cakey.pem 4096

# gera o certificado auto assinado da CA
openssl req -x509 -new -key cakey.pem -out ca.cer -days 1461 -subj '/CN=CA dev'



# gera a chave e o certificado do servidor como localhost

# gera uma chave para o servidor
openssl genrsa -out serverkey.pem 2048

# gera um certificado sem assinatura para o servidor
openssl req -new -key serverkey.pem -out server.csr -subj '/CN=localhost'

# usa a CA para assinar o certificado do servidor
openssl x509 -req -in server.csr -CA ca.cer -CAkey cakey.pem -CAcreateserial -sha256 -days 365 -out server.cer

# apaga o certificado sem assinatura
rm server.csr

# concatena o certificado da CA
cat server.cer ca.cer >> server_chain.pem

# junta a cadeira de certificação com a chave em um p12
openssl pkcs12 -export -in server_chain.pem -inkey serverkey.pem -out server.p12 -password pass:serverpass
