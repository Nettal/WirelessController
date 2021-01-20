package n2lf.wirelesscontroller.service;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class OverlayService extends Service
{
	SocketClientService.SyncedLinkedList syncedLinkedList;
	SocketClientService bindedService;
    @Override
    public IBinder onBind(Intent p1){
        return new android.os.Binder(){
			public void setSyncedLinkedList(SocketClientService.SyncedLinkedList list){
				syncedLinkedList = list;
			}
			
			public void setBindedService(SocketClientService service){
				bindedService = service;
			}
		};
    }

}
