package cn.example.udp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class DatagramClientTest {

    public static void main(String[] args) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String str = scanner.next();
            buf.put("Hello server".getBytes());
            buf.flip();
            datagramChannel.send(buf,new InetSocketAddress("localhost",9898));
            buf.clear();
        }

        datagramChannel.close();
    }
}
