package n2lf.wirelesscontroller.service;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import n2lf.wirelesscontroller.service.SocketClientService;
import n2lf.wirelesscontroller.utilities.ModelManager;
import n2lf.wirelesscontroller.utilities.Utilities;

public class OverlayService extends Service
{
	SocketClientService.SyncedLinkedList syncedLinkedList;
	SocketClientService socketClientService;
    String modelName;
    @Override
    public android.os.IBinder onBind(Intent intent){
        modelName = intent.getStringExtra("modelName");
        return new OSBinder();
    }

    public void loadOverlay(){//等待SocketClientService调用
        try{
            ModelManager.getModelFromFile(this , modelName);
        }
        catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误");
            builder.setMessage(e.toString());
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(android.content.DialogInterface p1, int p2){
                        OverlayService.this.stopSelf();
                        socketClientService.getActionSender().setToStop();
                        p1.dismiss();
                    }
                });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setType(Utilities.getLayoutParamsType());
            dialog.show();
        }
        
    }
    
    
    public class OSBinder extends android.os.Binder{
        public void setSyncedLinkedList(SocketClientService.SyncedLinkedList list){
            syncedLinkedList = list;
        }

        public void setBindedService(SocketClientService service){
            socketClientService = service;
        }
        
        public OverlayService getOverlayService(){
            return OverlayService.this;
        }
    }
    
}
