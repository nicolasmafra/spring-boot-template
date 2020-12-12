package com.nickmafra.demo.service;

import com.nickmafra.demo.infra.exception.AppRuntimeException;
import com.nickmafra.demo.infra.properties.SslProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Component
public class X509Service {

    private static final String ENCRYPTATION_ALG = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final Charset ENCRYPTATION_CHARSET = StandardCharsets.UTF_8;

    @Autowired
    private SslProperties properties;

    @Autowired
    private ResourceLoader resourceLoader;

    private InputStream loadCertificate() throws IOException {
        String path = properties.getKeyStore();
        String location = path.startsWith("classpath:") || path.startsWith ("file:")
                ? path : ("file:" + path);
        log.debug("Carregando arquivo no caminho=" + location);
        return resourceLoader.getResource(location).getInputStream();
    }

    public KeyStore getKeyStore() {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            throw new AppRuntimeException(e);
        }
        try {
            keyStore.load(loadCertificate(), properties.getKeyStorePassword().toCharArray());
        } catch (NoSuchAlgorithmException e) {
            throw new AppRuntimeException(e);
        } catch (IOException | CertificateException e) {
            throw new AppRuntimeException("O certificado não pôde ser carregado.", e);
        }
        return keyStore;
    }

    public RSAPrivateKey getPrivateKey() {
        try {
            RSAPrivateKey key = (RSAPrivateKey) getKeyStore().getKey(
                    properties.getKeyStoreAlias(),
                    properties.getKeyStorePassword().toCharArray());
            Objects.requireNonNull(key);
            return key;
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | NullPointerException e) {
            throw new AppRuntimeException("Erro ao carregar chave privada", e);
        }
    }

    public RSAPublicKey getPublicKey() {
        try {
            return (RSAPublicKey) getKeyStore().getCertificate(properties.getKeyStoreAlias()).getPublicKey();
        } catch (KeyStoreException e) {
            throw new AppRuntimeException("Erro ao carregar chave pública", e);
        }
    }

    private byte[] doCrypt(int cipherMode, Key key, byte[] inBytes) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPTATION_ALG);
        } catch (Exception e) {
            throw new AppRuntimeException("Erro ao criar criptografia", e);
        }
        try {
            cipher.init(cipherMode, key);
        } catch (InvalidKeyException e) {
            throw new AppRuntimeException("Chave inválida para a criptografia.", e);
        }
        try {
            return cipher.doFinal(inBytes);
        } catch (Exception e) {
            throw new AppRuntimeException("Não foi possível aplicar a criptografia no conteúdo.", e);
        }
    }

    public String encrypt(String decryptedText) {
        byte[] bytesOriginais = decryptedText.getBytes(ENCRYPTATION_CHARSET);
        byte[] bytesCriptografados = doCrypt(Cipher.ENCRYPT_MODE, getPublicKey(), bytesOriginais);
        return Base64.getEncoder().encodeToString(bytesCriptografados);
    }

    public String decrypt(String encryptedText) {
        byte[] bytesCriptografados = Base64.getDecoder().decode(encryptedText);
        byte[] bytesOriginais = doCrypt(Cipher.DECRYPT_MODE, getPrivateKey(), bytesCriptografados);
        return new String(bytesOriginais, ENCRYPTATION_CHARSET);
    }
}
