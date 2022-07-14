package com.github.vladyslavbabenko.passwordkeeper.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESUtils {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEY_VALUE = System.getenv("USERNAME");
    private static Key key;

    /**
     * Initializes key for AES
     */
    private synchronized void initializeKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(KEY_VALUE.getBytes());
            keyGen.init(128, random);
            key = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts input string via AES
     *
     * @param inputString string to encrypt
     * @return encrypted string via AES
     */
    public String getEncryptString(String inputString) {
        if (key == null) {
            initializeKey();
        }

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(inputString.getBytes(CHARSET_NAME));
            return Base64.getEncoder().encodeToString(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypts input string via AES
     *
     * @param inputString string to decrypt
     * @return decrypted string via AES
     */
    public String getDecryptString(String inputString) {
        if (key == null) {
            initializeKey();
        }

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(Base64.getDecoder().decode(inputString));
            return new String(doFinal, CHARSET_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}