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

public class SocketClientService extends Service
{
	public SyncedLinkedList actionQueue;
	static final int ACTION_SENDER_ERROR = 128;
	MessageHandler messageHandler;
	String modelName;
	
	@Override
	public void onStart(Intent intent, int startId){
		if(actionQueue == null){
			actionQueue = new SyncedLinkedList();
		}
		if(messageHandler == null){
			messageHandler = new MessageHandler(this);
		}
		modelName = intent.getStringExtra("modelName");
		new ActionSender(messageHandler ,intent.getStringExtra("ip"), intent.getIntExtra("port" , Utilities.DefaultPort) , this).startAndGetDialog().show();
	}
	
    @Override
    public IBinder onBind(Intent p1){
        return null;
    }

	class ActionSender extends Thread
	{
		private Handler handler;
		private String ip;
		private int port;
		private boolean isOnStopped;
		private ProgressDialog progressDialog;
		
		ActionSender(Handler handler , String ip , int port , Context context){
			this.handler = handler;
			this.ip = ip;
			this.port = port;
			this.isOnStopped = false;
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("请等待");
			progressDialog.setMessage("连接中...");
			progressDialog.setCancelable(false);
			progressDialog.setButton("取消" , 
				new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2){
						progressDialog.dismiss();
						ActionSender.this.isOnStopped = true;
					}
				});
			progressDialog.getWindow().setType(Utilities.getLayoutParamsType());
		}
		
		@Override
		public void run(){
			try{
				Socket socket = new Socket(ip, port);
				BufferedWriter bw = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));
				progressDialog.dismiss();//Success
				while(true){
					if(isOnStopped){
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
				if(isOnStopped){
					return;}
				isOnStopped = true;
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
		
		public boolean isOnStopped(){
			return isOnStopped;
		}
		
		public void setToStop(){
			this.isOnStopped = true;
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
					break;
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
