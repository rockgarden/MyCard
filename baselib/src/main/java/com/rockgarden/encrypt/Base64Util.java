package com.rockgarden.encrypt;

import java.io.IOException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * Created by rockgarden on 16/5/24.
 * TODO:Find BASE64Encoder
 */
public class Base64Util {
    public static String encryptBase64(byte[] data){
        return new BASE64Encoder().encode(data);
    }

    public static String decryptBase64(String data) throws IOException {
        byte[] resultBytes = new BASE64Decoder().decodeBuffer(data);
        return new String(resultBytes);
    }
}
