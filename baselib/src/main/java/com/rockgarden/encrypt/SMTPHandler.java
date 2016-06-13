package com.rockgarden.Encrypt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by rockgarden on 16/5/24.
 */
public class SMTPHandler {
    public static void main(String[] args) {
        // 用户名密码
        String sender = "cnsmtp01@163.com";
        String receiver = "cnsmtp02@163.com";
        String password = "computer";

        // 将用户名和密码进行Base64编码
        String userBase64 = Base64Util.encryptBase64(sender.substring(0,
                sender.indexOf("@")).getBytes());
        String passBase64 = Base64Util.encryptBase64(password.getBytes());
        try {
            Socket socket = new Socket("smtp.163.com", 25);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            PrintWriter writter = new PrintWriter(outputStream, true);
            System.out.println(reader.readLine());

            // HELO
            System.out.println(reader.readLine());

            // AUTH LOGIN >>>Base64
            writter.println("AUTH LOGIN");
            System.out.println(reader.readLine());
            writter.println(userBase64);
            System.out.println(reader.readLine());
            writter.println(passBase64);
            System.out.println(reader.readLine());

            // Set "MAIL FROM" and "RCPT TO"
            writter.println("MAIL FROM:<" + sender + ">");
            System.out.println(reader.readLine());
            writter.println("RCPT TO:<" + receiver + ">");
            System.out.println(reader.readLine());

            // Set "DATA"
            writter.println("DATA");
            System.out.println(reader.readLine());

            writter.println("FROM:" + sender);
            writter.println("TO:" + receiver);
            writter.println("Content-Type: text/plain;charset=\"gb2312\"");
            writter.println();
            writter.println(".");
            writter.println("");
            System.out.println(reader.readLine());

            // 发送完毕了，和服务器拜拜
            writter.println("RSET");
            System.out.println(reader.readLine());
            writter.println("QUIT");
            System.out.println(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
