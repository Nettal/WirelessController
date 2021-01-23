package n2lf.wirelesscontroller.service;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupMenu;
import n2lf.wirelesscontroller.service.SocketClientService;
import n2lf.wirelesscontroller.utilities.ModelManager;
import n2lf.wirelesscontroller.utilities.Utilities;
import n2lf.wirelesscontroller.utilities.colorpicker.ColorUtil;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.content.Context;

public class OverlayService extends Service
{
	SocketClientService.SyncedLinkedList syncedLinkedList;
	SocketClientService socketClientService;
    String modelName;
    WindowManager windowManager;
    WindowManager.LayoutParams windowManagerLP;
    RelativeLayout relativeLayout;
    ToolButton toolButton;
	
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
        relativeLayout = new RelativeLayout(OverlayService.this);
        for(int i = 0 ; i< modelManager.getKeyCodeButtonPropList().length ; i++){
            new KeyButton(relativeLayout , modelManager.getKeyCodeButtonPropList()[i] , syncedLinkedList);
        }
        windowManagerLP.type = Utilities.getLayoutParamsType();
        /**
         如果没有设置FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方不能点击。
         如果设置了FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方能够点击、但是弹窗上的EditText无法输入、键盘也不会弹出来。
         如果设置了FLAG_NOT_TOUCH_MODAL，那么屏幕上弹窗之外的地方能够点击、弹窗上的EditText也可以输入、键盘能够弹出来。
         FLAG_FULLSCREEN Activity窗口全屏，状态栏不显示。
		// SOFT_INPUT_ADJUST_NOTHING 软键盘不调整任何
        **/ 
        windowManagerLP.flags = windowManagerLP.FLAG_NOT_TOUCH_MODAL|windowManagerLP.FLAG_NOT_FOCUSABLE|windowManagerLP.FLAG_FULLSCREEN|windowManagerLP.FLAG_LAYOUT_IN_SCREEN;
        windowManagerLP.alpha = 1f;
        windowManagerLP.format = android.graphics.PixelFormat.RGBA_8888;
        windowManagerLP.gravity = android.view.Gravity.TOP|android.view.Gravity.LEFT;
        windowManager.addView(relativeLayout , windowManagerLP);
		toolButton = new ToolButton(OverlayService.this , windowManager, modelManager.getToolButtonProp());
		toolButton.bringToFront();
		//为什么？？
		windowManagerLP.flags = windowManagerLP.FLAG_NOT_TOUCH_MODAL|windowManagerLP.FLAG_NOT_FOCUSABLE|windowManagerLP.FLAG_FULLSCREEN|windowManagerLP.FLAG_LAYOUT_IN_SCREEN;
    }
	
	public void stopOverlay(){
		if(windowManager!=null && relativeLayout!=null){
			windowManager.removeView(relativeLayout);
		}
		if(windowManager!=null && toolButton!=null){
			windowManager.removeView(toolButton);
		}
		if(socketClientService!=null){
			socketClientService.getActionSender().setToStop();
		}
		//setToStop会unbind这个
		this.stopSelf();
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
            if(keyCode==-1){//没有设置Keycode
                return false;
            }
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
    
    public class ToolButton extends android.widget.Button implements android.widget.PopupMenu.OnMenuItemClickListener , ModelManager.ToolButtonPropInterface{
        PopupMenu popuMenu;
		WindowManager.LayoutParams layoutParams;
		WindowManager windowManager;
        ToolButton(Context context ,WindowManager manager , ModelManager.ToolButtonProperties toolButtonProp){
            super(context);
            popuMenu = new PopupMenu(getContext() , this);//按钮点击时的菜单
            for(String i :Utilities.悬浮界面的按键文字){
                popuMenu.getMenu().add(i);
            }
            popuMenu.setOnMenuItemClickListener(this);
			windowManager = manager;
			layoutParams = new WindowManager.LayoutParams();
			layoutParams.type = Utilities.getLayoutParamsType();
			/**
			 如果没有设置FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方不能点击。
			 如果设置了FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方能够点击、但是弹窗上的EditText无法输入、键盘也不会弹出来。
			 如果设置了FLAG_NOT_TOUCH_MODAL，那么屏幕上弹窗之外的地方能够点击、弹窗上的EditText也可以输入、键盘能够弹出来。
			 FLAG_FULLSCREEN Activity窗口全屏，状态栏不显示。
			 // SOFT_INPUT_ADJUST_NOTHING 软键盘不调整任何
			 **/ 
			layoutParams.flags = windowManagerLP.FLAG_NOT_TOUCH_MODAL|windowManagerLP.FLAG_NOT_FOCUSABLE|windowManagerLP.FLAG_FULLSCREEN|windowManagerLP.FLAG_LAYOUT_IN_SCREEN;
			layoutParams.alpha = 1f;
			layoutParams.format = android.graphics.PixelFormat.RGBA_8888;
			layoutParams.gravity = android.view.Gravity.TOP|android.view.Gravity.LEFT;
			layoutParams.x = (int) toolButtonProp.getX(getContext());
			layoutParams.y = (int) toolButtonProp.getY(getContext());
			layoutParams.width = layoutParams.height = Utilities.getMinSizeByRatio(getContext() , Utilities.DefaultButtonSizeScreenRatio);
			manager.addView(this , layoutParams);
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
                    layoutParams.x =(-(int)lastX+(int)event.getRawX()+layoutParams.x);//转换成int以防止出现过大的误差，导致按钮漂移
                    layoutParams.y =(-(int)lastY+(int)event.getRawY()+layoutParams.y);
                    lastX = event.getRawX();
                    lastY = event.getRawY();
					windowManager.updateViewLayout(this , layoutParams);
                    return true;
                case event.ACTION_UP:
                    if((  Math.abs(onDownY-event.getRawY()) + Math.abs(onDownX-event.getRawX()) )< Utilities.DEFAULT_TEXT_SIZE){//判断是否点击按钮
                        popuMenu.show();
                        return true;
                    }
                default:
                    return false;
            }
        }
        
        @Override
        public boolean onMenuItemClick(MenuItem p1){
            if(p1.getTitle().toString()==Utilities.悬浮界面的按键文字[0]){//关闭
				popuMenu.dismiss();
				OverlayService.this.stopOverlay();
			}else if(p1.getTitle().toString()==Utilities.悬浮界面的按键文字[1]){//禁用/启用
				if(windowManagerLP.flags == (windowManagerLP.FLAG_NOT_TOUCH_MODAL|windowManagerLP.FLAG_NOT_FOCUSABLE|windowManagerLP.FLAG_FULLSCREEN|windowManagerLP.FLAG_LAYOUT_IN_SCREEN)){
					windowManagerLP.flags = windowManagerLP.FLAG_NOT_FOCUSABLE|windowManagerLP.FLAG_NOT_TOUCHABLE;
				}else{
					windowManagerLP.flags = windowManagerLP.FLAG_NOT_TOUCH_MODAL|windowManagerLP.FLAG_NOT_FOCUSABLE|windowManagerLP.FLAG_FULLSCREEN|windowManagerLP.FLAG_LAYOUT_IN_SCREEN;
				}
				windowManager.updateViewLayout(relativeLayout , windowManagerLP);
			}
            return false;
        }

		@Override
		public void bringToFront(){
			super.bringToFront();
			windowManager.updateViewLayout(this , layoutParams);
		}
		
		@Override
        public float getXScreenRatio(){
            return (float)getX()/(float)Utilities.getScreenWidth(getContext());
        }

		@Override
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
