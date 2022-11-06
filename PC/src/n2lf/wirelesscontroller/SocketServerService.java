package n2lf.wirelesscontroller;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SocketServerService extends Thread {
    public static final char ON_MOUSE_PRESS = 'A';
    public static final char ON_MOUSE_RELEASE = 'B';
    public static final char ON_KEY_PRESS = 'C';
    public static final char ON_KEY_RELEASE = 'D';
    public static final char ON_MOUSE_WHEEL = 'E';
    public static final char ON_MOUSE_MOVE = 'F';
    public static final char SET_CLIP_BOARD = 'G';
    final IMessageHandler handler;
    final int port;

    SocketServerService(int port, IMessageHandler handler) {
        this.handler = handler;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.println("SocketServerService: Starting ServerSocket: " + port);
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("SocketServerService: Waiting for connection...");
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("SocketServerService: Accepting actions...");
            while (true) {//OMM OKP OKR OMP OMR OMW SCB
                String string = bufferedReader.readLine();
                System.err.println(string);
                if (string == null) {
                    break;
                }
                switch (string.charAt(0)) {
                    case ON_MOUSE_MOVE -> {
                        for (int i = 2; i < string.length(); i++) {
                            if (string.charAt(i) == ';') {
                                handler.handleMouseMove(MouseInfo.getPointerInfo().getLocation().x +
                                                Integer.parseInt(string, 1, i, 10),
                                        MouseInfo.getPointerInfo().getLocation().y +
                                                Integer.parseInt(string, i + 1, string.length(), 10));
                                break;
                            }
                        }
                    }

                    case SET_CLIP_BOARD ->
                            handler.handleSetClipboard(URLDecoder.decode(string.substring(1), StandardCharsets.UTF_8));
                    default -> {
                        final int parseInt = Integer.parseInt(string, 1, string.length(), 10);
                        switch (string.charAt(0)) {
                            case ON_MOUSE_PRESS -> handler.handleMousePress(parseInt);
                            case ON_MOUSE_RELEASE -> handler.handleMouseRelease(parseInt);
                            case ON_KEY_PRESS -> handler.handleKeyPress(parseInt);
                            case ON_KEY_RELEASE -> handler.handleKeyRelease(parseInt);
                            case ON_MOUSE_WHEEL -> handler.handleMouseWheel(parseInt);
                            default -> throw new IllegalStateException("Unexpected value: " + string.charAt(0));
                        }
                    }
                }
            }
            bufferedReader.close();
            socket.close();
            serverSocket.close();
            System.out.println("SocketServerService: This connection was closed");
            new SocketServerService(port, handler).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface IMessageHandler {
        void handleMouseMove(int x, int y);

        void handleKeyPress(int keycode);

        void handleKeyRelease(int keycode);

        void handleMousePress(int buttons);

        void handleMouseRelease(int buttons);

        void handleMouseWheel(int wheelAmt);

        void handleSetClipboard(String setClipboard);
    }
}
