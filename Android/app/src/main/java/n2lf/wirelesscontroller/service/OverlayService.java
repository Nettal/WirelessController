package n2lf.wirelesscontroller.service;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.WindowManager;
import n2lf.wirelesscontroller.service.SocketClientService;
import n2lf.wirelesscontroller.utilities.ModelManager;
import n2lf.wirelesscontroller.utilities.Utilities;
import n2lf.wirelesscontroller.utilities.colorpicker.ColorUtil;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.graphics.Color;

public class OverlayService extends Service
{
	SocketClientService.SyncedLinkedList syncedLinkedList;
	SocketClientService socketClientService;
    String modelName;
    WindowManager windowManager;
    WindowManager.LayoutParams windowManagerLP;
    LinearLayout linearLayout;
    
    @Override
    public android.os.IBinder onBind(Intent intent){
        modelName = intent.getStringExtra("modelName");
        return new OSBinder();
    }

    public void loadOverlay(){//等待SocketClientService调用
        ModelManager modelManager;
        try{
            modelManager = ModelManager.getModelFromFile(this , modelName);
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
            return;
        }
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerLP = new WindowManager.LayoutParams();
        linearLayout = new LinearLayout(OverlayService.this);
        for(int i = 0 ; i< modelManager.getKeyCodeButtonPropList().length ; i++){
            KeyButton button = new KeyButton(linearLayout , modelManager.getKeyCodeButtonPropList()[i] , syncedLinkedList);
        }
        //ToolButton MUST be the least
        ToolButton toolButton = new ToolButton(linearLayout, modelManager.getToolButtonProp());
        linearLayout.addView(toolButton);
        windowManagerLP.type = Utilities.getLayoutParamsType();
        /**
         如果没有设置FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方不能点击。
         如果设置了FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方能够点击、但是弹窗上的EditText无法输入、键盘也不会弹出来。
         如果设置了FLAG_NOT_TOUCH_MODAL，那么屏幕上弹窗之外的地方能够点击、弹窗上的EditText也可以输入、键盘能够弹出来。
         FLAG_FULLSCREEN Activity窗口全屏，状态栏不显示。
        **/ 
        windowManagerLP.flags = windowManagerLP.FLAG_NOT_TOUCH_MODAL|windowManagerLP.FLAG_NOT_FOCUSABLE|windowManagerLP.FLAG_FULLSCREEN;
        windowManagerLP.alpha = 1f;
        windowManagerLP.format= android.graphics.PixelFormat.RGBA_8888;
        windowManagerLP.gravity=android.view.Gravity.TOP|android.view.Gravity.LEFT;
        windowManager.addView(linearLayout , windowManagerLP);
    }
    
    public class KeyButton extends android.widget.Button{
        SocketClientService.SyncedLinkedList list;
        int keyCode;
        boolean isMouseKeyCode;
        KeyButton(ViewGroup viewgroup , ModelManager.KeyCodeButtonProperties prop , SocketClientService.SyncedLinkedList list){
            super(viewgroup.getContext());
            this.list = list;
            keyCode = prop.getKeyCode();
            isMouseKeyCode = prop.isMouseKeyCode();
            if(prop.getButtonColorString()!=null){
                this.getBackground().setAlpha(0);
                this.getBackground().setColorFilter(ColorUtil.convertToColorInt(prop.getButtonColorString()) , android.graphics.PorterDuff.Mode.SRC);
            }
            if(prop.getButtonName()!=null){
                this.setText(prop.getButtonName());
            }
            this.setTextColor(prop.getTextColor());
            this.setX(prop.getX(getContext()));
            this.setY(prop.getY(getContext()));
            this.setAutoSizeTextTypeWithDefaults(android.widget.Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            viewgroup.addView(this , prop.getWidth(getContext()) , prop.getHeight(getContext()));
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event){
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(isMouseKeyCode){
                        list.addFirst("OMP"+keyCode);
                    }else{
                        list.addFirst("OKP"+keyCode);
                    }
                    return true;
                case MotionEvent.ACTION_UP | MotionEvent.ACTION_CANCEL:
                    if(isMouseKeyCode){
                        list.addFirst("OMR"+keyCode);
                    }else{
                        list.addFirst("OKP"+keyCode);
                    }
                    return true;
                default:
                    return false;
            }
        }
    }
    
    public class ToolButton extends android.widget.Button{
        ToolButton(ViewGroup viewGroup , ModelManager.ToolButtonProperties toolButtonProp){
            super(viewGroup.getContext());
            this.setX(toolButtonProp.getX(getContext()));
            this.setY(toolButtonProp.getY(getContext()));
            int size = Utilities.getMinSizeByRatio(getContext() , Utilities.DefaultButtonSizeScreenRatio);
            viewGroup.addView(this , size , size);
        }
        
        //处理按钮移动和按钮单击操作
        private float lastX;//你可以用float，但会出现按键漂移问题
        private float lastY;
        private float onDownX;
        private float onDownY;
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            switch(event.getAction()){
                case event.ACTION_DOWN:
                    lastX = onDownX = event.getRawX();
                    lastY = onDownY = event.getRawY();
                    return true;
                case event.ACTION_MOVE:
                    this.setX(-(int)lastX+(int)event.getRawX()+(int)this.getX());//转换成int以防止出现过大的误差，导致按钮漂移
                    this.setY(-(int)lastY+(int)event.getRawY()+(int)this.getY());
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    return true;
                case event.ACTION_UP:
                    if((  Math.abs(onDownY-event.getRawY()) + Math.abs(onDownX-event.getRawX()) )< Utilities.偏移量){//判断是否点击按钮
                        //TODO
                        return true;
                    }
                default:
                    return false;
            }
        }
        
        public float getXScreenRatio(){
            return (float)getX()/(float)Utilities.getScreenHeight(getContext());
        }

        public float getYScreenRatio(){
            return (float)getY()/(float)Utilities.getScreenWidth(getContext());
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
