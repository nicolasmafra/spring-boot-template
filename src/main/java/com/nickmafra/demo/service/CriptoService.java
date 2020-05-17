package com.nickmafra.demo.service;

import com.nickmafra.demo.infra.exception.AppRuntimeException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CriptoService {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 65536; // 2^16
    private static final int KEY_LENGTH = 128; // 2^16
    private static final int SALT_LENGTH = 16;

    private SecretKeyFactory secretKeyFactory;
    private SecureRandom secureRandom;

    public CriptoService() throws NoSuchAlgorithmException {
        this.secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        this.secureRandom = new SecureRandom();
    }

    private byte[] encodeChars(char[] chars, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(chars, salt, ITERATION_COUNT, KEY_LENGTH);
            Key key = secretKeyFactory.generateSecret(spec);
            return key.getEncoded();
        } catch (Exception e) {
            throw new AppRuntimeException("Erro ao gerar hash.", e);
        }
    }

    private byte[] newSalt() {
        byte[] bytes = new byte[SALT_LENGTH];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    private String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] fromBase64(String string) {
        return Base64.getDecoder().decode(string);
    }

    public String ofuscarSenha(String senha) {
        if (senha.isEmpty()) {
            throw new AppRuntimeException("Não é possível ofuscar senha vazia.");
        }
        byte[] salt = newSalt();
        byte[] encoded = encodeChars(senha.toCharArray(), salt);
        return toBase64(salt) + ":" + toBase64(encoded);
    }

    public boolean conferirSenhaOfuscada(String senha, String hash) {
        if (senha.isEmpty()) {
            return false;
        }
        byte[] salt;
        byte[] encoded;
        try {
            String[] slices = hash.split(":");
            salt = fromBase64(slices[0]);
            encoded = fromBase64(slices[1]);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new AppRuntimeException("Hash inválido.");
        }
        byte[] check = encodeChars(senha.toCharArray(), salt);
        return Arrays.equals(encoded, check);
    }
}
