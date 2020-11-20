package N2LF.wirelesscontroller;
import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketClientService extends Service
{
   // public static LinkedList queue;
    public static SyncedLinkedList eventQueue;
    public static int loopStatus = 0;// 1:looping  0:stopping
    public static final int Error =-1;
    public static final int Success = 10;
    SCSHandler handler = new SCSHandler();
    String ip;
    int port;
    OnStatusChange onStatusChangeListener;
    

    @Override
    public void onCreate(){
        if(eventQueue==null){//创建队列
            eventQueue = new SyncedLinkedList();
        }
    //    System.out.println("on creat");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port",MainActivity.Default_port);
   //     System.out.println("on startcommand");
        return super.onStartCommand(intent, flags, startId);
    }
    

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopService(new Intent(SocketClientService.this , FloatWindowService.class));
        stopSelf();
    }
    
    
    ///////////////*通信*/////////////////
    //消息处理SocketClientServiceHandler
    public class SCSHandler extends Handler{
        @Override
        public void handleMessage(Message msg){//003
            switch(msg.what){
            case SocketClientService.Error ://error
            onStatusChangeListener.onChange(msg.obj);//004
            break;
            case SocketClientService.Success ://success
            Intent intent = new Intent(SocketClientService.this , FloatWindowService.class);
            intent.putExtra("ip",ip);
            intent.putExtra("port",ip);
            startService(intent);
            break;
            }
           // super.handleMessage(msg);
        }
        
    }
    
    
    //binder用来通信，使得activity获取service实例
    public class SCSBinder extends Binder{
        public SocketClientService getService(){
            return SocketClientService.this;//实例
        }
    }
    
    
    @Override//绑定时
    public IBinder onBind(Intent intent){
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port",37385);
        System.out.println("on bind");
        new SocketClientThread(ip,port).start();//创建主处理线程
        return new SCSBinder();
    }
    
    //监听器
    public void setStatusChangeListener(OnStatusChange onStatusChange){
        this.onStatusChangeListener = onStatusChange;
    }


    //用来规范方法
    public interface OnStatusChange{
        void onChange(Object obj);
    }
    
    /////////////////*通信结束*//////////////
    
    
    //主处理线程
    public class SocketClientThread extends Thread {
        String ip;
        int port;
        SocketClientThread(String ip, int port){
            this.ip = ip;
            this.port= port;
        }
        @Override
        public void run(){
            try{
                System.out.println("run socket");
                Thread.sleep(10);//某些情况下的null
               Socket socket = new Socket(ip, port);
               BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  
                Message msg = new Message();
                msg.what = Success;
                msg.obj = "Success";//001
                handler.sendMessage(msg);//002
                System.out.println("Success run socket");
                while(true){//处理队列事件
                    if(eventQueue.isEmpty()){
                        if(loopStatus ==0){break;}
                        Thread.sleep(10);
                        }else{
            //        System.out.println(queue.getLast());
                    bw.write(eventQueue.getLast());
                    bw.flush();
                    eventQueue.removeLast();
                    }
                }
                
                bw.close();
                socket.close();
                stopService(new Intent(SocketClientService.this , FloatWindowService.class));
                System.out.println("stop SCS");
                stopSelf();
            }
            catch (Exception e){
                Message msg = new Message();
                msg.what = Error;
              //  msg.what = Success;
                System.out.println("err socket");
                msg.obj = e;
                handler.sendMessage(msg);
                stopSelf();
            }
        }
    }
}
