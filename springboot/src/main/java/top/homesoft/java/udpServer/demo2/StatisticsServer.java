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


public class StatisticsServer {

    static StatisticsServer statisticsServer = null;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    //每次发送接收的数据包大小
    private final int MAX_BUFF_SIZE = 1024 * 10;
    //服务端监听端口，客户端也通过该端口发送数据
    private int port = 6003;
    private DatagramChannel channel;
    private Selector selector;

    private ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

    public void init() throws IOException {
        //创建通道和选择器
        selector = Selector.open();
        channel = DatagramChannel.open();
        //设置为非阻塞模式
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        //将通道注册至selector，监听只读消息（此时服务端只能读数据，无法写数据）
        channel.register(selector, SelectionKey.OP_READ);

        //使用线程的方式，保证服务端持续等待接收客户端数据
        es.scheduleWithFixedDelay(new Runnable() {
            public void run() {
            }
        }, 0L, 2L, TimeUnit.MINUTES);

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
        StatisticsServer ss = new StatisticsServer();
        ss.init();
    }

    public synchronized static StatisticsServer getInstance() {
        if (statisticsServer == null) {
            statisticsServer = new StatisticsServer();
        }
        return statisticsServer;
    }

    private StatisticsServer() {

    }

}
