package n2lf.wirelesscontroller.service;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.content.Context;
import android.os.Message;
import n2lf.wirelesscontroller.utilities.Utilities;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.IOException;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.ComponentName;

public class SocketClientService extends Service
{
	public SyncedLinkedList actionQueue;
	static final int ACTION_SENDER_ERROR = 128;
    static final int ACTION_SENDER_SUCCESS = 127;
    static final int ACTION_SENDER_STOPPED = 126;
	MessageHandler messageHandler;
	
	@Override
	public void onStart(Intent intent, int startId){
		if(actionQueue == null){
			actionQueue = new SyncedLinkedList();
		}
		if(messageHandler == null){
			messageHandler = new MessageHandler(this);
		}
		new ActionSender(messageHandler ,intent.getStringExtra("ip"), intent.getIntExtra("port" , Utilities.DefaultPort) , intent.getStringExtra("modelName")).startAndGetDialog().show();
	}
	
    @Override
    public IBinder onBind(Intent p1){
        return null;
    }

	class ActionSender extends Thread//一一对应原则，一个model一个对象
	{
		private Handler handler;
		private String ip;
		private int port;
		private boolean isStopped;
		private ProgressDialog progressDialog;
		ServiceConnection connection;
        String modelName;
        
		ActionSender(Handler handler , String ip , int port , String modelName){
			this.handler = handler;
			this.ip = ip;
			this.port = port;
			this.isStopped = false;
            this.modelName = modelName;
			progressDialog = new ProgressDialog(getApplicationContext());
			progressDialog.setTitle("请等待");
			progressDialog.setMessage("连接中...");
			progressDialog.setCancelable(false);
			progressDialog.setButton("取消" , 
				new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2){
						progressDialog.dismiss();
						ActionSender.this.isStopped = true;
					}
				});
			progressDialog.getWindow().setType(Utilities.getLayoutParamsType());
            connection = new ServiceConnection(){
                @Override
                public void onServiceConnected(ComponentName p1, IBinder p2){
                    ((OverlayService.OSBinder)p2).setBindedService(SocketClientService.this);
                    ((OverlayService.OSBinder)p2).setSyncedLinkedList(actionQueue);
                }
                @Override
                public void onServiceDisconnected(ComponentName p1){

                }};
		}
		
		@Override
		public void run(){
			try{
                System.out.println("ip:"+ip+",port:"+port+",ModelName:"+modelName);
				Socket socket = new Socket(ip, port);
				BufferedWriter bw = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));
				progressDialog.dismiss();//Success
                Message message = new Message();
                message.what = ACTION_SENDER_SUCCESS;
				handler.sendMessage(message);
                bindService(new Intent(getApplicationContext() , OverlayService.class) ,connection , 1 );
				while(true){
					if(isStopped){
						break;}
					if(actionQueue.isEmpty()){
						try{
							Thread.sleep(Utilities.ThreadSleepTime);}
						catch (InterruptedException e){
							e.printStackTrace();}
					}else{
						bw.write(actionQueue.getAndRemoveLast());
						bw.flush();
					}
				}
				bw.close();
				socket.close();
				stopSelf();
			}
			catch (IOException e){
				progressDialog.dismiss();
                unbindService(connection);
				if(isStopped){
					return;}
				isStopped = true;
				Message message = new Message();
				message.what = ACTION_SENDER_ERROR;
				message.obj = e;
				handler.sendMessage(message);
			}
		}

		public ProgressDialog startAndGetDialog(){
			super.start();
			return progressDialog;
		}
		
		public boolean isStopped(){
			return isStopped;
		}
		
		public void setToStop(){
			this.isStopped = true;
		}
	}
	
	class MessageHandler extends Handler{
		Context context;
		MessageHandler(Context context){
			this.context = context;
		}

		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
				case ACTION_SENDER_ERROR:
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("错误");
					builder.setMessage(msg.obj.toString());
					builder.setCancelable(false);
					builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
							@Override
							public void onClick(android.content.DialogInterface p1, int p2){
								SocketClientService.this.stopSelf();
							}
						});
					AlertDialog dialog = builder.create();
					dialog.getWindow().setType(Utilities.getLayoutParamsType());
					dialog.show();
					return;
                case ACTION_SENDER_SUCCESS:
                    return;
			}
		}
	}
	
	public class SyncedLinkedList extends java.util.LinkedList<String>{
		public synchronized String getAndRemoveLast(){
			String s = this.getLast();
			this.removeLast();
			return s;
		}

		@Override
		public synchronized void addFirst(String e){
			super.addFirst(e);
		}
	}
}
