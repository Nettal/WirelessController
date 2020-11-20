import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerService implements Runnable{

    int port ;
    BytesQueue bytesQueue;
    Socket socket;
    InputStream inputStream;
    ServerSocket serviceSocket;
    SocketServerService(int port,BytesQueue bytesQueue){
        this.port = port;
        try {
            System.out.println("SocketServerService: Starting ServerSocket: "+port);
            this.serviceSocket = new ServerSocket(port);
            this.bytesQueue = bytesQueue;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            new Thread(new SocketServerService(port,bytesQueue)).start();
        }

    }
    private void acceptRequest() throws Exception {
        socket = serviceSocket.accept();
        System.out.println("SocketServerService: connected ");
        while(true) {
            //获取socket。这个方法是阻的
            inputStream = socket.getInputStream();
            byte[] buf = new byte[1024];
            int length = inputStream.read(buf);
            if(length <= 0){
                System.err.println("SocketServerService: disconnected");
                break;
            }
       //     System.out.println("DEBUG:SocketServerService: "+new String(buf, 0, length));
            bytesQueue.add(buf);
        }
    }

    private void acceptRequest_wrapper(){
        while (true){
            try {
                System.out.println("SocketServerService: A new acceptRequest is created!");
                this.acceptRequest();
            } catch (Exception e) {
                System.err.println("SocketServerService: acceptRequest failed,retry");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            acceptRequest_wrapper();
        }catch (Throwable e){
            System.err.println("SocketServerService: acceptRequest_wrapper failed!,retry");
            e.printStackTrace();
            new Thread(new SocketServerService(port,bytesQueue)).start();
        }
    }
}
