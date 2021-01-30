package n2lf.wirelesscontroller;

import java.awt.*;
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("SocketServerService: Accepting actions...");
            while (true){//OMM OKP OKR OMP OMR OMW SCB
                String string = bufferedReader.readLine();
                if (string == null) {
                    break;
                }
                if (string.charAt(2)=='M') {//OMM
                    for (int i = 5; i < string.length(); i++) {
                        if (string.charAt(i) == ';') {
                            handler.handleMouseMove(MouseInfo.getPointerInfo().getLocation().x +
                                            Integer.parseInt(string , 4 , i  , 10) ,
                                                    MouseInfo.getPointerInfo().getLocation().y +
                                            Integer.parseInt(string , i+1 , string.length() , 10));
                            break;
                        }
                    }
                    continue;
                }
                if (string.charAt(0)=='O') {
                    int parseInt = Integer.parseInt(string, 4, string.length(), 10);
                    if (string.charAt(1)=='K') {
                        if (string.charAt(2)=='P') {//OKP
                            handler.handleKeyPress(parseInt);
                        }else {//OKR
                            handler.handleKeyRelease(parseInt);
                        }
                        continue;
                    }
                    if (string.charAt(1)=='M') {
                        if (string.charAt(2)=='P') {//OMP
                            handler.handleMousePress(parseInt);
                        }else if (string.charAt(2)=='R'){//OMR
                            handler.handleMouseRelease(parseInt);
                        }else {//OMW
                            handler.handleMouseWheel(parseInt);
                        }
                        //continue;
                    }
                }else {//SCB
                    for (int i = 5; i < string.length(); i++) {
                        if (string.charAt(i) == ';') {
                            int lines = Integer.parseInt(string , 4 , i  , 10);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(string , i+1 , string.length());
                            for (int k = 1; k < lines; k++) {
                                stringBuilder.append(System.lineSeparator());
                                stringBuilder.append(bufferedReader.readLine());
                            }
                            handler.handleSetClipboard(stringBuilder.toString());
                            break;
                        }
                    }

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
        void handleMouseMove(int x, int y);
        void handleKeyPress(int keycode);
        void handleKeyRelease(int keycode);
        void handleMousePress(int buttons);
        void handleMouseRelease(int buttons);
        void handleMouseWheel(int wheelAmt);
        void handleSetClipboard(String setClipboard);
    }
}
