package N2LF.wirelesscontroller;
import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import java.util.ArrayList;
import android.os.Binder;
import android.widget.Toast;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Gallery.LayoutParams;
import android.os.Build;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.content.res.Configuration;
import android.widget.RelativeLayout;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.view.Gravity;


public class FloatWindowService extends Service{
    boolean isHide = false;
    public static ArrayList attributeList;
    RelativeLayout relativeLayout;
    WindowManager windowManager ;
    Button hideButton;

    @Override
    public IBinder onBind(Intent intent){
        return new FWSBinder();
    }
    
    
    //binder用来通信，使得***获取service实例
    public class FWSBinder extends Binder{
        public FloatWindowService getService(){
            return  FloatWindowService.this;//实例
        }
    }
    
    
    ///////////////////////*处理部分*///////////////////
    @Override
    public void onCreate()
    {
     if(attributeList == null) attributeList = new ArrayList<Attribute>();
        addView();
        super.onCreate();
    }

    private void addView(){
        windowManager =(WindowManager) getSystemService(WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        int width = windowManager.getDefaultDisplay().getWidth();
        final WindowManager.LayoutParams layoutParams =  initLayoutParam();
        relativeLayout = new TouchRelativeLayout(this);

        for(int i =1;i <=attributeList.size();i++){// size从1开始
            Attribute attribute = (Attribute)attributeList.get(i-1);//索引以0开始。size以1开始
            ExtraButton exbutton = new ExtraButton(this , attribute);
            exbutton.setX(attribute.getX()*width);
            exbutton.setY(attribute.getY()*height);
            relativeLayout.addView(exbutton ,(int)( attribute.getWidth()*width) ,(int)(attribute.getHeight()*height));
            }
            windowManager.addView(relativeLayout , layoutParams);
        //*//////////////////////////******////////////////
        final WindowManager.LayoutParams layoutParamsbutton =  initLayoutParam();
        layoutParamsbutton.height=150;
        layoutParamsbutton.width=150;
        layoutParamsbutton.x=0;
        layoutParamsbutton.y=0;
        hideButton = new Button(this){
            @Override
            public boolean onTouchEvent(MotionEvent event){
                if(event.getAction()==event.ACTION_UP){
                    if(!isHide){
                        layoutParams.flags=layoutParams.FLAG_NOT_TOUCHABLE|layoutParams.FLAG_NOT_FOCUSABLE;
                        windowManager.updateViewLayout(relativeLayout,layoutParams);
                        isHide=true;
                        this.setText("显示");
                    }else{
                        layoutParams.flags = layoutParams.FLAG_NOT_TOUCH_MODAL|layoutParams.FLAG_NOT_FOCUSABLE|layoutParams.FLAG_FULLSCREEN;
                    
                        windowManager.updateViewLayout(relativeLayout,layoutParams);
                        isHide=false;
                        this.setText("隐藏");
                    }
                    return true;
                }
                return false;
            }
        };
        hideButton.setBackgroundColor(Color.WHITE);
        hideButton.setText("隐藏");
        hideButton.setTextColor(Color.RED);
        windowManager.addView(hideButton,layoutParamsbutton);
        //////////////*************////////////////////
    }
    private WindowManager.LayoutParams initLayoutParam()
    {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //如果没有设置FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方不能点击。
        //如果设置了FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方能够点击、但是弹窗上的EditText无法输入、键盘也不会弹出来。
        //如果设置了FLAG_NOT_TOUCH_MODAL，那么屏幕上弹窗之外的地方能够点击、弹窗上的EditText也可以输入、键盘能够弹出来。
        //FLAG_FULLSCREEN Activity窗口全屏，状态栏不显示。
        layoutParams.flags = layoutParams.FLAG_NOT_TOUCH_MODAL|layoutParams.FLAG_NOT_FOCUSABLE|layoutParams.FLAG_FULLSCREEN;
        layoutParams.gravity=Gravity.TOP|Gravity.LEFT;
        layoutParams.format= PixelFormat.RGBX_8888;
        layoutParams.alpha= 0.3f;
        
        return layoutParams;
  
    }
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        System.out.println(newConfig);
    }
    //////////////////////*结束处理部分*/////////////////

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        for(int i =1;i <=attributeList.size();i++){// size从1开始
            windowManager.removeView(relativeLayout);
        }
        if(hideButton!=null){
            windowManager.removeView(hideButton);
        }
        attributeList.clear();
        super.onDestroy();
        System.out.println("stop FWS");
        stopSelf();
        
    } 

}
