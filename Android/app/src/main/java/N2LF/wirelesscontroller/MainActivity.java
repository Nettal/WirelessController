package N2LF.wirelesscontroller;

import android.app.*;
import android.os.*;
import android.widget.TextView;
import android.text.InputType;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.widget.EditText;
import android.provider.Settings;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener{
    public static final float DefaultHW = 0.0666666f;//   1/15
    public static final int Default_port = 37385;
    
   // public static ArrayList attributeList = new ArrayList<Attribute>();
    
    
    Button button1 = null;
    Button button2 = null;
    EditText editText1 = null;
    TextView textView1 = null;
    SocketClientService SCS;
    

	@Override
	public void onClick(View p1){
        if(SocketClientService.loopStatus == 0){
            if(!Settings.canDrawOverlays(this)){
                Toast.makeText(this,"请授予悬浮窗权限",Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
                return;
            }
            if( !loadAttribute()){
                Toast.makeText(this ,"需要一个按键模板" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,EditorActivity.class));
            }else{
            Intent intent=new Intent(this,SocketClientService.class);
            intent.putExtra("ip","127.0.0.1");
            intent.putExtra("port",37385);
            SocketClientService.loopStatus = 1;
		    this.startService(intent);
            this.bindService(intent , connection , Context.BIND_AUTO_CREATE);
            Toast.makeText(this ,"Started" , Toast.LENGTH_LONG).show();
            button1.setText("Stop");
            }
        }else{
         SocketClientService.loopStatus = 0;
         unbindService(connection);
         stopService(new Intent(this,SocketClientService.class));
         button1.setText("Start");
        } 
	}

    private boolean loadAttribute()
    {
        byte[] bytes = new byte[1024];
        File file = this.getExternalFilesDir(null);
        File jsonfile = new File(file.toString()+"/model.obj");
        try
        {
            FileInputStream fis = new FileInputStream(jsonfile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
            FloatWindowService.attributeList = (ArrayList<Attribute>)objectInputStream.readObject();
            objectInputStream.close();
            fis.close();
            
            return true;
        }
        catch (ClassNotFoundException | IOException e)
        {
            return false;
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		editText1 = (EditText)findViewById(R.id.mainEditText1);
        textView1 = (TextView)findViewById(R.id.mainTextView1);
		button1 = (Button) findViewById(R.id.mainButton1);
		button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.mainButton2);
        button2.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1){
                    File file = MainActivity.this.getExternalFilesDir(null);
                    File jsonfile = new File(file.toString()+"/model.obj");
                    jsonfile.delete();
                }
        });
    } 
    
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder ibinder) {
            SocketClientService.SCSBinder binder = (SocketClientService.SCSBinder)ibinder;
            SCS = binder.getService();
            SCS.setStatusChangeListener(new SocketClientService.OnStatusChange(){
                    @Override
                    public void onChange(Object obj){//005
                        Toast.makeText(MainActivity.this,"网络出现错误",Toast.LENGTH_SHORT).show();
                        button1.setText("Start");
                        SocketClientService.loopStatus =0;
                        textView1.setText(obj.toString());//006
                    }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    protected void onStop()
    {
     //   unbindService(connection);
        super.onStop();
    }
}
