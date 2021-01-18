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

public class SocketClientService extends Service
{
	public SyncedLinkedList actionQueue;
	static final int ACTION_SENDER_ERROR = 128;
	static final int ACTION_SENDER_SUCCESS = 127;
	private boolean isOnStopped;
	ProgressDialog progressDialog;
	
	@Override
	public void onStart(Intent intent, int startId){
		if(startId !=1){
			return;
		} //判断是否多开
		isOnStopped = false;
		if(actionQueue == null){
			actionQueue = new SyncedLinkedList();
		}
		MessageHandler messageHandler = new MessageHandler(this);
		progressDialog = new ProgressDialog(SocketClientService.this);
		progressDialog.setTitle("Please Wait");
		progressDialog.setMessage("Connecting");
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setType(Utilities.getLayoutParamsType());
		progressDialog.show();
		new ActionSender(messageHandler ,intent.getStringExtra("ip"), intent.getIntExtra("port" , Utilities.DefaultPort)).start();
	}
	
    @Override
    public IBinder onBind(Intent p1){
        return null;
    }

	class ActionSender extends Thread
	{
		Handler handler;
		String ip;
		int port;
		ActionSender(Handler handler , String ip , int port){
			this.handler = handler;
			this.ip = ip;
			this.port = port;
		}
		
		@Override
		public void run(){
			try{
				Socket socket = new Socket(ip, port);
				BufferedWriter bw = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream()));
				Message message = new Message();
				message.what = ACTION_SENDER_SUCCESS;
				handler.sendMessage(message);
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
				Message message = new Message();
				message.what = ACTION_SENDER_ERROR;
				message.obj = e;
				handler.sendMessage(message);
				isOnStopped = true;
			}
		}
	}
	
	class SCSBinder extends android.os.Binder{
		public void stopActionSender(){
			isOnStopped = true;
		}
		
		public boolean isOnStopped(){
			return isOnStopped;
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
					progressDialog.dismiss();
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
				case ACTION_SENDER_SUCCESS:
					progressDialog.dismiss();
					break;
			}
		}
	}
	
	class SyncedLinkedList extends java.util.LinkedList<String>{
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
