package top.homesoft.java.udpServer.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatisticsServer2 {


    Logger logger = LoggerFactory.getLogger(this.getClass());
    //每次发送接收的数据包大小
    private final int MAX_BUFF_SIZE = 1024 * 10;
    //服务端监听端口，客户端也通过该端口发送数据
    private int port = 6000;


    private ScheduledExecutorService es = Executors.newScheduledThreadPool(10);

    public void init() throws IOException {
        //创建通道和选择器


        //使用线程的方式，保证服务端持续等待接收客户端数据
        for (int i = 0; i < 5; i++) {
            es.scheduleWithFixedDelay(new ReciverThread(port + i), 0L, 2L, TimeUnit.SECONDS);
        }


    }

    //处理接收到的数据
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

    public static void main(String[] args) throws IOException {
        StatisticsServer2 ss = new StatisticsServer2();
        ss.init();
    }

}
