package cn.example.constants;

public class UDPConstants {

    //公用头部
    public static byte[] HEADER = new byte[]{7,7,7,7,7,7,7,7};
    //服务器固化UDP端口号
    public static int PORT_SERVER = 30201;
    //客户端回送端口
    public static int PORT_CLIENT_RESPONSE = 30202;
}
