import java.awt.*;

public class Starter {

    int port = 37385;
    public BytesQueue bytesQueue;
    Starter(int port){
        bytesQueue = new BytesQueue();
        if (port > 1024){
            this.port=port;
        }
    }
    public void startServer(){
            new Thread(new SocketServerService(port,bytesQueue)).start();
            try {
                new Thread(new Controller_wrapper(port,bytesQueue,new Robot())).start();
            }catch (Exception e){
                e.printStackTrace();
            }

    }
}
