package com.rockgarden.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by rockgarden on 16/5/24.
 */
public class MessageDigestUtil {
    public static String encryptMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        byte[] resultBytes = md5.digest();

        String resultString = BytesToHex.fromBytesToHex(resultBytes);
        return resultString;
    }

    public static String getMD5OfFile(String path) throws Exception{
        FileInputStream fis = new FileInputStream(new File(path));
        DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("MD5"));
        // 流输入
        byte[] buffer = new byte[1024];
        int read = dis.read(buffer, 0, 1024);
        while (read != -1){
            read = dis.read(buffer, 0, 1024);
        }

        MessageDigest md = dis.getMessageDigest();
        byte[] resultBytes = md.digest();
        String resultString = BytesToHex.fromBytesToHex(resultBytes);
        return resultString;
    }

    public static String encryptSHA(byte[] data) throws NoSuchAlgorithmException{
        MessageDigest sha = MessageDigest.getInstance("SHA-512");
        sha.update(data);
        byte[] resultBytes = sha.digest();

        String resultString = BytesToHex.fromBytesToHex(resultBytes);
        return resultString;
    }

    public static byte[] initHmacKey() throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    public static String encryptHmac(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKey);
        byte[] resultBytes = mac.doFinal(data);

        String resultString = BytesToHex.fromBytesToHex(resultBytes);
        return resultString;
    }
}
