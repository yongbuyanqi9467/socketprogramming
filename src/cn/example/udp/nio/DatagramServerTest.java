package cn.example.udp.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class DatagramServerTest {

    public static void main(String[] args) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();

        datagramChannel.configureBlocking(false);
        datagramChannel.bind(new InetSocketAddress(9898));
        Selector sc = Selector.open();
        datagramChannel.register(sc, SelectionKey.OP_READ);

        while (sc.select() > 0) {
            Iterator<SelectionKey> iterator = sc.selectedKeys().iterator();

            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if(key.isReadable()) {
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    datagramChannel.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(),0,buf.limit()));
                    buf.clear();
                }
            }

            iterator.remove();
        }
    }
}
