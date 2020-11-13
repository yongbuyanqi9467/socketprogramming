package cn.example.server;

import java.io.UnsupportedEncodingException;

public class StringTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "中国";

        byte[] bytes = str.getBytes("UTF-8");
        for(int i = 0;i < bytes.length;i++) {
            System.out.println(bytes[i]);
        }
    }
}
