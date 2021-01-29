package n2lf.wirelesscontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerService extends Thread{
    final IMessageHandler handler;
    final int port;
    SocketServerService(int port , IMessageHandler handler){
        this.handler = handler;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.println("SocketServerService: Starting ServerSocket: "+port);
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("SocketServerService: Waiting for connection...");
            Socket socket = serverSocket.accept();
            System.out.println("SocketServerService: Accepting actions...");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true){//OMM OKP OKR OMP OMR OMW SCB
                String string = bufferedReader.readLine();
                if (string == null) {
                    break;
                }
                if (string.charAt(2)=='M') {//OMM
                    handler.handleMouseMove(string.substring(4));
                    continue;
                }
                if (string.charAt(0)=='O') {
                    if (string.charAt(1)=='K') {
                        if (string.charAt(2)=='P') {//OKP
                            handler.handleKeyPress(string.substring(4));
                        }else {//OKR
                            handler.handleKeyRelease(string.substring(4));
                        }
                        continue;
                    }
                    if (string.charAt(1)=='M') {
                        if (string.charAt(2)=='P') {//OMP
                            handler.handleMousePress(string.substring(4));
                        }else if (string.charAt(2)=='R'){//OMR
                            handler.handleMouseRelease(string.substring(4));
                        }else {//OMW
                            handler.handleMouseWheel(string.substring(4));
                        }
                        //continue;
                    }
                }else {//SCB
                    int lines = Integer.parseInt(string.substring(4).split(";" , 2)[0]);
                    //从下标为4的字符开始截取，使用split来以;为分隔符，获取首个以;结尾的数组，便是行数；
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(string.substring(string.indexOf(";")+1));
                    for (int i = 1; i < lines; i++) {
                        stringBuilder.append(System.lineSeparator());
                        stringBuilder.append(bufferedReader.readLine());
                    }
                    handler.handleSetClipboard(stringBuilder.toString());
                    //continue;
                }
            }
            bufferedReader.close();
            socket.close();
            serverSocket.close();
            System.out.println("SocketServerService: This connection was closed");
            new SocketServerService(port , handler).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface IMessageHandler{
        void handleMouseMove(String onMouseMove);
        void handleKeyPress(String onKeyPress);
        void handleKeyRelease(String onKeyRelease);
        void handleMousePress(String onMousePress);
        void handleMouseRelease(String onMouseRelease);
        void handleMouseWheel(String onMouseWheel);
        void handleSetClipboard(String setClipboard);
    }
}
