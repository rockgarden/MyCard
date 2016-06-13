package com.rockgarden.Encrypt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * Created by rockgarden on 16/5/24.
 */
public class RSAUtil {
    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /*
     * 生成 RSA 的 公钥 和 私钥
     */
    public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);   // 512-65536 & 64的倍数
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static RSAPublicKey getPublicKey(Map<String, Object> keyMap){
        RSAPublicKey publicKey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
        return publicKey;
    }
    public static RSAPrivateKey getPrivateKey(Map<String, Object> keyMap){
        RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
        return privateKey;
    }

    /*
     * 公钥加密
     */
    public static byte[] encrypt(byte[] data, RSAPublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherBytes = cipher.doFinal(data);
        return cipherBytes;
    }

    /*
     * 私钥解密
     */
    public static byte[] decrypt(byte[] data, RSAPrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] plainBytes = cipher.doFinal(data);
        return plainBytes;
    }

}
