package cn.example.udp.bio;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * UDP 搜索，用于搜索服务支持方
 */
public class UDPSearcher {

    private static final int LISTEN_PORT = 30000;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("UDPSearcher Started.");
       Listener listen = listen();
        //完成
        System.out.println("UDPSearcher Finished.");
        sendBroadcast();

        // 读取任意键盘信息后可以退出
        System.in.read();

        List<Device> devices = listen.getDevicesAndClose();
        for(Device device : devices) {
            System.out.println("Device:" + device.toString());
        }

        System.out.println("UDPSearcher Finished.");

    }

    private static Listener listen() throws InterruptedException {
        System.out.println("UDPSearcher start listen.");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT,countDownLatch);
        listener.start();
        countDownLatch.await();
        return listener;
    }

    private static void sendBroadcast() throws IOException {
        System.out.println("UDPSearcher sendBroadcast Started.");
        //作为搜索方，让系统自动分配端口
        DatagramSocket ds = new DatagramSocket();
        //构建一份回送数据
        String requestData = MessageCreator.buildWithPort(LISTEN_PORT);
        byte[] requestDataBytes = requestData.getBytes();
        //直接根据发送者构建一份回送信息
        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes,requestDataBytes.length);

        // 20000端口，广播地址
        requestPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        requestPacket.setPort(20000);
        ds.send(requestPacket);
        ds.close();

        //完成
        System.out.println("UDPSearcher snedBroadcast Finished");
    }

    private static class Device {
        final int port;
        final String ip;
        final String sn;

        public Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    /**
     * 实现监听类
     */
    private static class Listener extends Thread {
        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Device> devices = new ArrayList<>();
        private boolean done = false;
        private DatagramSocket ds = null;

        public Listener(int listenPort,CountDownLatch countDownLatch) {
            super();
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            super.run();
            // 通知已启动
            countDownLatch.countDown();
            try{
                // 监听回送端口
                ds = new DatagramSocket(listenPort);
                while(!done) {
                    //构建接收实体
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePack = new DatagramPacket(buf,buf.length);

                    //接收
                    ds.receive(receivePack);
                    //打印接收者的信息与发送者的信息
                    // 发送者的ip地址
                    String ip = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();
                    String data = new String(receivePack.getData(),0,dataLen);
                    System.out.println("UDPSearcher receive from ip:" + ip + "\tport:" + port+"\tdata:" + data);
                    String sn = MessageCreator.parseSn(data);
                    if(sn != null) {
                        Device device = new Device(port,ip,sn);
                        devices.add(device);
                    }
                }
            }catch (Exception ignored){

            }finally {
                close();
            }

            System.out.println("UDPSearcher listener finished");
        }

        private void close() {
            if(ds != null) {
                ds.close();
                ds = null;
            }
        }

        List<Device> getDevicesAndClose() {
            done = true;
            close();
            return devices;
        }
    }
}
