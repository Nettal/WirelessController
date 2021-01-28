package n2lf.wirelesscontroller;

import java.awt.AWTException;

public class Main {
    public static void main(String[] args) {
        try {
            new SocketServerService(getPort(args) , new MessageHandler()).start();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static int getPort(String[] args){
        int port = 37385;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("Usage: [port]");
            System.out.println("Will use default port: 37385");
        }
        return port;
    }
}
