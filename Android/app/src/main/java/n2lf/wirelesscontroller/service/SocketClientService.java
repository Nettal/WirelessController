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

public class SocketClientService extends Service
{
	public static SyncedLinkedList actionQueue;
	
	@Override
	public void onStart(Intent intent, int startId){
		//TODO dialog of starting socket
		if(actionQueue == null){
			actionQueue = new SyncedLinkedList();
		}
		MessageHandler messageHandler = new MessageHandler(this);
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
				while(true){
					if(actionQueue.isEmpty()){
						try{
							Thread.sleep(5);}
						catch (InterruptedException e){
							e.printStackTrace();}
					}else{
						bw.write(actionQueue.getAndRemoveLast());
						bw.flush();
					}
				}
				//bw.close();
				//socket.close();
			}
			catch (IOException e){
				
			}
		}
	}
	
	class MessageHandler extends Handler{
		Context context;
		MessageHandler(Context context){
			this.context = context;
		}

		@Override
		public void handleMessage(Message msg)
		{
			
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
