package cn.example.udp;

import java.net.DatagramSocket;
import java.nio.channels.DatagramChannel;

/**
 * UDP提供者，用于提供服务
 */
public class UDPProvider {

    public static void main(String[] args) {
        System.out.println("UDPProvider Started.");
        // 作为接收者，指定一个端口用于数据接收
        DatagramSocket ds = new DatagramSocket(20000);
    }
}
