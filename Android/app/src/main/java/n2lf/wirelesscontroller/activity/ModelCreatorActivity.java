package n2lf.wirelesscontroller.activity;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View.OnTouchListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import n2lf.wirelesscontroller.utilities.Utilities;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class ModelCreatorActivity extends Activity
{
    LinearLayout linearLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getActionBar().hide();
        this.setContentView(linearLayout = new LinearLayout(this));
        linearLayout.post(new CreatFloatButton(this));
    }
    
    class CreatFloatButton extends Button implements Runnable,OnDismissListener,OnMenuItemClickListener
    {
        Context context;
        WindowManager windowManager;
        WindowManager.LayoutParams layoutParams;
        PopupMenu popuMenu;
        CreatFloatButton(Context context){
            super(context);
            this.context = context;
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            layoutParams = new WindowManager.LayoutParams();
            popuMenu = new PopupMenu(ModelCreatorActivity.this , this);
            popuMenu.getMenu().add("返回");
            popuMenu.setOnDismissListener(this);
            popuMenu.setOnMenuItemClickListener(this);
        }
       
        
        private int lastX;//你不可以用float，会出现按键漂移问题
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
                    if((  Math.abs(onDownY-event.getRawY()) + Math.abs(onDownX-event.getRawX()) )< Utilities.偏移量){
                        popuMenu.show();
                    }
                    return false;
            }
            return false;
        }  
 
        
        
        //implements
        @Override
        public void run()
        {
            int maxInt = Math.max(windowManager.getDefaultDisplay().getWidth() , windowManager.getDefaultDisplay().getHeight());
            layoutParams.type = layoutParams.FIRST_SUB_WINDOW+5;
            layoutParams.flags = layoutParams.FLAG_NOT_TOUCH_MODAL|layoutParams.FLAG_NOT_FOCUSABLE|layoutParams.FLAG_FULLSCREEN;
            layoutParams.gravity=Gravity.TOP|Gravity.LEFT;
            layoutParams.format= PixelFormat.RGBA_8888;
            layoutParams.alpha= Utilities.按钮默认透明度;
            layoutParams.x = windowManager.getDefaultDisplay().getWidth()>>1;
            layoutParams.y = windowManager.getDefaultDisplay().getHeight()>>1;//除2
            layoutParams.width = maxInt/Utilities.按钮默认大小的除数;
            layoutParams.height = maxInt/Utilities.按钮默认大小的除数;
            windowManager.addView(this , layoutParams);
        }  
        
        @Override
        public void onDismiss(PopupMenu p1)
        {
        }

        @Override
        public boolean onMenuItemClick(MenuItem p1)
        {
            switch(p1.getTitle().toString()){
               case "返回":
                   ModelCreatorActivity.this.finish();
                   return true;
            }
            return false;
        }
    }
}
