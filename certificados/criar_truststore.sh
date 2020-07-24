rm -f truststore.jks
keytool -import -keystore truststore.jks -storepass password -file ca.cer -noprompt
