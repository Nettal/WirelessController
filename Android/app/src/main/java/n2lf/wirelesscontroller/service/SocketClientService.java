package n2lf.wirelesscontroller.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import n2lf.wirelesscontroller.utilities.Utilities;

public class SocketClientService extends Service {
    static final int ACTION_SENDER_ERROR = 128;
    static final int ACTION_SENDER_SUCCESS = 127;
    private MessageHandler messageHandler;
    private ActionSender actionSender;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (messageHandler == null) {
            messageHandler = new MessageHandler(this, SocketClientService.this);
        }
        actionSender = new ActionSender(messageHandler, intent.getStringExtra("ip"), intent.getIntExtra("port", Utilities.DefaultPort), intent.getStringExtra("modelName"));
        actionSender.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent p1) {
        return null;
    }

    public ActionSender getActionSender() {
        return actionSender;
    }

    public class ActionSender extends Thread//一一对应原则，一个model一个对象
    {
        private Sender sender;

        private OutputStreamWriter osw;
        private Socket socket;
        private final Handler handler;
        private final String ip;
        private final int port;
        private boolean isStopped;
        private boolean isBinded;
        private final ProgressDialog progressDialog;
        private final ServiceConnection connection;
        private final String modelName;
        private OverlayService overlayService;

        ActionSender(Handler handler, String ip, int port, String modelName) {
            this.handler = handler;
            this.ip = ip;
            this.port = port;
            this.isStopped = false;
            this.isBinded = false;
            this.modelName = modelName;
            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setTitle("请等待");
            progressDialog.setMessage("连接中...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface p1, int p2) {
                    progressDialog.dismiss();
                    ActionSender.this.stop(null);
                }
            });
            progressDialog.getWindow().setType(Utilities.getLayoutParamsType());
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName p1, IBinder p2) {//onBind结束才会执行此方法
                    ((OverlayService.OSBinder) p2).setBindedService(SocketClientService.this);
                    ((OverlayService.OSBinder) p2).setSender(sender);
                    overlayService = ((OverlayService.OSBinder) p2).getOverlayService();
                    overlayService.loadOverlay();
                }

                @Override
                public void onServiceDisconnected(ComponentName p1) {

                }
            };
        }

        @Override
        public void run() {
            try {
                socket = new Socket(ip, port);
                osw = new OutputStreamWriter(socket.getOutputStream());
                sender = new Sender(osw, SocketClientService.this);
                progressDialog.dismiss();//Success
                if (isStopped)
                    return;
                Intent intent = new Intent(getApplicationContext(), OverlayService.class);
                intent.putExtra("modelName", modelName);
                if (!isBinded) {
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                }
                isBinded = true;
            } catch (IOException e) {
                stop(e);
            }
        }

        public void start() {
            super.start();
            progressDialog.show();
        }

        public void stop(Exception e) {
            if (isStopped)
                return;
            isStopped = true;
            if (sender != null)
                try {
                    sender.flush();
                    sender.close();
                } catch (Exception ignored) {
                }
            if (osw != null)
                try {
                    osw.close();
                } catch (Exception ignored) {
                }
            if (socket != null)
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            progressDialog.dismiss();
            if (isBinded) {
                overlayService.stopOverlay(false);
                unbindService(connection);
                isBinded = false;
            }
            stopSelf();
            if (e != null) {//是否意外停止，return为没有意外停止 比如ProgressDialog点击取消
                Message message = new Message();
                message.what = ACTION_SENDER_ERROR;
                message.obj = e;
                handler.sendMessage(message);
            }

        }
    }

    private static class MessageHandler extends Handler {
        SocketClientService service;
        Context context;

        MessageHandler(Context context, SocketClientService service) {
            super(Looper.getMainLooper());
            this.context = context;
            this.service = service;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ACTION_SENDER_ERROR) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("错误");
                builder.setMessage(msg.obj.toString());
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        service.stopSelf();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setType(Utilities.getLayoutParamsType());
                dialog.show();
            }
        }
    }

    public static class Sender extends BufferedWriter {
        private final SocketClientService service;
        private final ReentrantLock lock = new ReentrantLock();

        public Sender(Writer out, SocketClientService service) {
            super(out);
            this.service = service;
        }

        public void send(final String s) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        write(s + System.lineSeparator());
                        flush();
                    } catch (Exception e) {
                        service.getActionSender().stop(e);
                    }
                }
            }).start();
        }
    }
}
