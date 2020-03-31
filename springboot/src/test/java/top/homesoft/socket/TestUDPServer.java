package top.homesoft.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class TestUDPServer {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(6001));

        ByteBuffer buf = ByteBuffer.allocate(1024);

        /*阻塞，等待发来的数据*/

        while (true) {
            buf.clear();
            channel.receive(buf);
            /*设置缓冲区可读*/
            buf.flip();

            /*循环读出所有字符*/
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
        }

    }


}
