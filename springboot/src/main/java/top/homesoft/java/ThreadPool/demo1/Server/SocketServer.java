package top.homesoft.java.ThreadPool.demo1.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer implements Runnable {
    private int ServiceThreadNum = 4;
    private ExecutorService pool = Executors.newFixedThreadPool(ServiceThreadNum);

    public SocketServer() {
    }

    ;

    //@Override
    public void run() {
        for (int i = 1; i <= ServiceThreadNum; i++) {
            Runnable serviceTask = new SocketServiceThread(i + "");
            pool.submit(serviceTask);
        }
    }

//    public static void main(String[] args) {
//        SocketServer server = new SocketServer();
//        server.run();
//    }
}
