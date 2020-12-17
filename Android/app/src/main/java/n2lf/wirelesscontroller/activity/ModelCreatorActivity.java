package n2lf.wirelesscontroller.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnTouchListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import n2lf.wirelesscontroller.utilities.Utilities;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.app.AlertDialog;

public class ModelCreatorActivity extends Activity
{
    private RelativeLayout relativeLayout;
    private TextView textView;
    private boolean isOnAddEvent = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getActionBar().hide();
        this.setContentView(relativeLayout = new RelativeLayout(this));
        textView = new TextView(this);
        textView.setTextSize(Utilities.字体大小);
        textView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rLP.addRule(RelativeLayout.CENTER_VERTICAL);
        relativeLayout.addView(textView,rLP);
        relativeLayout.post(new CreatFloatButton(this));//延时添加按钮，否则会throw
    }
    
    private boolean addButtonStarted(){
        isOnAddEvent = true;
        textView.setText(Utilities.点击选取位置[0]);//点击以选取按钮位置
        relativeLayout.setBackgroundColor(Utilities.CreatorBackgroundColor);
        relativeLayout.setOnTouchListener(new onButtonAddTouchListener());
        return true;
    }
    
    private boolean addFinished(){
        isOnAddEvent = false;
        textView.setText("");
        relativeLayout.setBackgroundColor(Color.alpha(0));
        relativeLayout.setOnTouchListener(null);//it is ok
        return true;
    }
    
    
    private class onButtonAddTouchListener implements OnTouchListener
    {
        private int lastX;//你可以用float，但会出现按键漂移问题
        private int lastY;
        private float onDownX;
        private float onDownY;
        OverviewButton overviewButton;
        int buttonSize;
        @Override
        public boolean onTouch(View p1, MotionEvent event)
        {
            switch(event.getAction()){
                case event.ACTION_DOWN:
                    lastX = (int) (onDownX = event.getX());
                    lastY = (int) (onDownY = event.getY());
                    overviewButton = new OverviewButton(ModelCreatorActivity.this);
           /*       tempButton.setHeight(Utilities.getDefaultButtonSize(ModelCreatorActivity.this));
                    tempButton.setWidth(Utilities.getDefaultButtonSize(ModelCreatorActivity.this));
                    这个不行*/
                    buttonSize = Utilities.getDefaultButtonSize(ModelCreatorActivity.this);
                    overviewButton.setHeight(buttonSize);//用来保存大小，后面直接获取按钮大小
                    overviewButton.setWidth(buttonSize);
                    overviewButton.setX(onDownX-buttonSize/2);//否则按钮会在点击位置的右下角
                    overviewButton.setY(onDownY-buttonSize/2);    
                    relativeLayout.addView(overviewButton,buttonSize,buttonSize);
                    return true;
                case event.ACTION_MOVE:
                    overviewButton.setX(event.getX()-buttonSize/2);
                    overviewButton.setY(event.getY()-buttonSize/2);
                    return true;
                case event.ACTION_UP:
                    overviewButton.editThis();
                    return true;
                default:
                    return false;
            }
        }   
    }
    
    
    private class OverviewButton extends Button{
        Context context;
        OverviewButton(Context context){
            super(context);
            this.context = context;
        }

        
        private int lastX;//你可以用float，但会出现按键漂移问题
        private int lastY;
        private float onDownX;
        private float onDownY;
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            switch(event.getAction()){
                case event.ACTION_DOWN:
                    lastX = (int) (onDownX = event.getRawX());
                    lastY = (int) (onDownY = event.getRawY());
                    return true;
                case event.ACTION_MOVE:
                    this.setX(-lastX+(int)event.getRawX()+(int)this.getX());
                    this.setY(-lastY+(int)event.getRawY()+(int)this.getY());
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    return true;
                case event.ACTION_UP:
                    if((  Math.abs(onDownY-event.getRawY()) + Math.abs(onDownX-event.getRawX()) )< Utilities.偏移量){//判断是否点击按钮
                        this.editThis();//call edit dialog
                        return true;
                    }
                default:
                    return false;
            }
        }
        
        public void editThis(){
            System.out.println("Called editThis method");
        }
    }
    
    
    
    private class CreatFloatButton extends Button implements Runnable,OnDismissListener,OnMenuItemClickListener
    {
        Context context;
        WindowManager windowManager;
        WindowManager.LayoutParams layoutParams;
        PopupMenu popuMenu;
        CreatFloatButton(Context context){//创建button对象 & 为后来的悬浮按钮做准备
            super(context);
            this.context = context;
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            layoutParams = new WindowManager.LayoutParams();
            popuMenu = new PopupMenu(ModelCreatorActivity.this , this);
            for(String i :Utilities.添加界面的按键文字){
                popuMenu.getMenu().add(i);
            }
            popuMenu.setOnDismissListener(this);
            popuMenu.setOnMenuItemClickListener(this);
        }
       
        
        //处理按钮移动和按钮单击操作
        private int lastX;//你可以用float，但会出现按键漂移问题
        private int lastY;
        private float onDownX;
        private float onDownY;
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            switch(event.getAction()){
                case event.ACTION_DOWN:
                    lastX = (int) (onDownX = event.getRawX());
                    lastY = (int) (onDownY = event.getRawY());
                    return true;
                case event.ACTION_MOVE:
                    layoutParams.x -= lastX-event.getRawX();
                    layoutParams.y -= lastY-event.getRawY();
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    windowManager.updateViewLayout(this , layoutParams);
                    return true;
                case event.ACTION_UP:
                    if((  Math.abs(onDownY-event.getRawY()) + Math.abs(onDownX-event.getRawX()) )< Utilities.偏移量){//判断是否点击按钮
                        popuMenu.show();
                        return true;
                    }
                    return false;
            }
            return false;
        }  
 
        
        
        //implements
        
        //运行此方法时添加按键
        @Override
        public void run()
        {
            layoutParams.type = layoutParams.FIRST_SUB_WINDOW+5;
            /*
             如果没有设置FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方不能点击。
             如果设置了FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方能够点击、但是弹窗上的EditText无法输入、键盘也不会弹出来。
             如果设置了FLAG_NOT_TOUCH_MODAL，那么屏幕上弹窗之外的地方能够点击、弹窗上的EditText也可以输入、键盘能够弹出来。
             FLAG_FULLSCREEN Activity窗口全屏，状态栏不显示。
            */
            layoutParams.flags = layoutParams.FLAG_NOT_TOUCH_MODAL|layoutParams.FLAG_NOT_FOCUSABLE|layoutParams.FLAG_FULLSCREEN;
            layoutParams.gravity=Gravity.TOP|Gravity.LEFT;
            layoutParams.format= PixelFormat.RGBA_8888;
            layoutParams.alpha= Utilities.按钮默认透明度;
            layoutParams.x = windowManager.getDefaultDisplay().getWidth()>>1;
            layoutParams.y = windowManager.getDefaultDisplay().getHeight()>>1;//除2
            layoutParams.width = (layoutParams.height = Utilities.getDefaultButtonSize(ModelCreatorActivity.this));
            windowManager.addView(this , layoutParams);
        }  
        
        @Override
        public void onDismiss(PopupMenu p1)
        {
        }

        @Override
        public boolean onMenuItemClick(MenuItem p1)
        {
            if(p1.getTitle().toString()==Utilities.添加界面的按键文字[0]){//完成
                if(isOnAddEvent){
                    addFinished();//处理背景变化和 onTouchEvent
                }else{
                    ModelCreatorActivity.this.finish();//此时应该保存模板
                    }
            }else if(p1.getTitle().toString()==Utilities.添加界面的按键文字[1]){//添加按钮
                addButtonStarted();
            }else{//添加触摸板
                
            }
            return false;
        }
    }
}
