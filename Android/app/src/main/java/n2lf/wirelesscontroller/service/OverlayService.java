package n2lf.wirelesscontroller.service;
import android.app.Service;
import android.content.Intent;

public class OverlayService extends Service
{
	SocketClientService.SyncedLinkedList syncedLinkedList;
	SocketClientService bindedService;
    @Override
    public android.os.IBinder onBind(Intent p1){
        
        return new OSBinder();
    }

    public class OSBinder extends android.os.Binder{
        public void setSyncedLinkedList(SocketClientService.SyncedLinkedList list){
            syncedLinkedList = list;
        }

        public void setBindedService(SocketClientService service){
            bindedService = service;
        }
    }
    
}
