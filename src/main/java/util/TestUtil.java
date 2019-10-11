package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.Socket;

import packet.Packet;
import server.Server;

public class TestUtil {
    
    public static Socket connect() throws IOException {
        int port = 4444;
        
        Socket ret = null;
        final int MAX_ATTEMPTS = 50;
        int attempts = 0;
        do {
            try {
                ret = new Socket("127.0.0.1", port);
            } catch (ConnectException ce) {
                try {
                    if (++attempts > MAX_ATTEMPTS)
                        throw new IOException("Exceeded max connection attempts", ce);
                    Thread.sleep(300);
                } catch (InterruptedException ie) {
                    throw new IOException("Unexpected InterruptedException", ie);
                }
            }
        } while (ret == null);
        ret.setSoTimeout(3000);
        return ret;
    }

    public static void startServer() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Server server = new Server(4444);
                    server.serve();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }
    
    public static Packet nextPacket(ObjectInputStream in) throws IOException {
        while (true) {
            Packet ret = null;
            try {
                ret = (Packet) (in.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (ret != null)
                return ret;
        }
    }
}
