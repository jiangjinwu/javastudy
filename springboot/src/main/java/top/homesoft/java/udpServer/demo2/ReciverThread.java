package top.homesoft.java.udpServer.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReciverThread implements Runnable {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int MAX_BUFF_SIZE = 1024 * 10;
    private DatagramChannel channel;
    private Selector selector;
    private int port = 6001;

    public ReciverThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
            //设置为非阻塞模式
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(port));
            //将通道注册至selector，监听只读消息（此时服务端只能读数据，无法写数据）
            channel.register(selector, SelectionKey.OP_READ);
            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
                        iterator.remove();
                        if (key.isReadable()) {
                            //接收数据
                            doReceive(key);
                        }
                    } catch (Exception e) {
                        // logger.error("SelectionKey receive exception", e);
                        try {
                            if (key != null) {
                                key.cancel();
                                key.channel().close();
                            }
                        } catch (ClosedChannelException cex) {
                            //logger.error("Close channel exception", cex);
                        }
                    }
                }
            }
        } catch (IOException e) {
            //logger.error("selector.select exception", e);
        }


    }

    private void doReceive(SelectionKey key) throws IOException {
        String content = "";
        DatagramChannel sc = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFF_SIZE);
        buffer.clear();
        sc.receive(buffer);
        buffer.flip();
        while (buffer.hasRemaining()) {
            byte[] buf = new byte[buffer.limit()];
            buffer.get(buf);
            content += new String(buf);
        }
        buffer.clear();

        logger.info(content);
	       /* logger.debug("receive content="+content);
	        if(StringUtils.isNotBlank(content)) {
	            doSave(content);
	        }*/
    }
}
