package n2lf.wirelesscontroller.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import n2lf.wirelesscontroller.utilities.ModelManager;
import n2lf.wirelesscontroller.utilities.Utilities;
import n2lf.wirelesscontroller.utilities.colorpicker.ColorUtil;

public class OverlayService extends Service {
    public static final String ON_MOUSE_PRESS = String.valueOf('A');
    public static final String ON_MOUSE_RELEASE = String.valueOf('B');
    public static final String ON_KEY_PRESS = String.valueOf('C');
    public static final String ON_KEY_RELEASE = String.valueOf('D');
    public static final String ON_MOUSE_WHEEL = String.valueOf('E');
    public static final String ON_MOUSE_MOVE = String.valueOf('F');
    public static final String SET_CLIP_BOARD = String.valueOf('G');
    private SocketClientService.Sender sender;
    private SocketClientService socketClientService;
    private String modelName;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowManagerLP;
    private RelativeLayout relativeLayout;
    private ToolButton toolButton;

    @Override
    public android.os.IBinder onBind(Intent intent) {
        modelName = intent.getStringExtra("modelName");
        return new OSBinder();
    }

    public void loadOverlay() {//等待SocketClientService调用
        ModelManager modelManager;
        try {
            modelManager = ModelManager.getModelFromFile(this, modelName);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误");
            builder.setMessage(e.toString());
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface p1, int p2) {
                    OverlayService.this.stopSelf();
                    socketClientService.getActionSender().stop(null);
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
        new TouchPadButton(relativeLayout);
        for (int i = 0; i < modelManager.getKeyCodeButtonPropList().length; i++) {
            new KeyButton(relativeLayout, modelManager.getKeyCodeButtonPropList()[i]).bringToFront();
        }
        windowManagerLP.type = Utilities.getLayoutParamsType();
        /**
         如果没有设置FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方不能点击。
         如果设置了FLAG_NOT_FOCUSABLE，那么屏幕上弹窗之外的地方能够点击、但是弹窗上的EditText无法输入、键盘也不会弹出来。
         如果设置了FLAG_NOT_TOUCH_MODAL，那么屏幕上弹窗之外的地方能够点击、弹窗上的EditText也可以输入、键盘能够弹出来。
         FLAG_FULLSCREEN Activity窗口全屏，状态栏不显示。
         // SOFT_INPUT_ADJUST_NOTHING 软键盘不调整任何
         **/
        windowManagerLP.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowManagerLP.alpha = 1f;
        windowManagerLP.format = android.graphics.PixelFormat.RGBA_8888;
        windowManagerLP.gravity = android.view.Gravity.TOP | android.view.Gravity.START;
        windowManager.addView(relativeLayout, windowManagerLP);
        toolButton = new ToolButton(OverlayService.this, windowManager, modelManager.getToolButtonProp());
        toolButton.bringToFront();
        //为什么？？
        windowManagerLP.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
    }

    public SocketClientService.Sender getSender() {
        return sender;
    }

    public void setSender(SocketClientService.Sender sender) {
        this.sender = sender;
    }

    public void stopOverlay(boolean shouldSetToStop) {
        if (windowManager != null && relativeLayout != null) {
            windowManager.removeView(relativeLayout);
        }
        if (windowManager != null && toolButton != null) {
            windowManager.removeView(toolButton);
        }
        if (socketClientService != null && shouldSetToStop) {
            socketClientService.getActionSender().stop(null);
        }
        //setToStop会unbind这个
        this.stopSelf();
    }

    public class KeyButton extends android.widget.Button {
        int keyCode;
        boolean isMouseKeyCode;

        KeyButton(ViewGroup viewGroup, ModelManager.KeyCodeButtonProperties prop) {
            super(viewGroup.getContext());
            keyCode = prop.getKeyCode();
            isMouseKeyCode = prop.isMouseKeyCode();
            if (prop.getButtonColorString() != null) {
                this.getBackground().setAlpha(0);
                this.getBackground().setColorFilter(ColorUtil.convertToColorInt(prop.getButtonColorString()), PorterDuff.Mode.SRC);
            }
            if (prop.getButtonName() != null) {
                this.setText(prop.getButtonName());
            }
            this.setTextColor(prop.getTextColor());
            this.setX(prop.getX(getContext()));
            this.setY(prop.getY(getContext()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            }
            viewGroup.addView(this, prop.getWidth(getContext()), prop.getHeight(getContext()));
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (keyCode == -1) {//没有设置Keycode
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isMouseKeyCode) {
                        getSender().send(ON_MOUSE_PRESS + keyCode);
                    } else {
                        getSender().send(ON_KEY_PRESS + keyCode);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    if (isMouseKeyCode) {
                        getSender().send(ON_MOUSE_RELEASE + keyCode);
                    } else {
                        getSender().send(ON_KEY_RELEASE + keyCode);
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    public class TouchPadButton extends android.widget.Button {

        TouchPadButton(ViewGroup viewgroup) {
            super(viewgroup.getContext());
            this.setAlpha(0f);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            viewgroup.addView(this, layoutParams);
        }

        float lastX, lastY, downY, mouseWheelY;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    lastY = event.getY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 2) {//双指：鼠标滚轮
                        float eventRatio = Utilities.getScreenHeight(getContext()) * Utilities.MouseWheelRatio;
                        for (; event.getY(1) - mouseWheelY > eventRatio; mouseWheelY += eventRatio) {
                            sender.send(ON_MOUSE_WHEEL + -1);
                        }
                        for (; event.getY(1) - mouseWheelY < -eventRatio; mouseWheelY -= eventRatio) {
                            sender.send(ON_MOUSE_WHEEL + 1);
                        }
                        if ((int) (event.getX(0) - lastX) == 0 && (int) (event.getY(0) - lastY) == 0) {
                            return true;
                        }
                        sender.send(ON_MOUSE_MOVE + (int) (event.getX(0) - lastX) + ";" + (int) (event.getY(0) - lastY));
                        lastX = event.getX(0);
                        lastY = event.getY(0);
                    } else {
                        if ((int) (event.getX() - lastX) == 0 && (int) (event.getY() - lastY) == 0) {
                            return true;
                        }
                        sender.send(ON_MOUSE_MOVE + (int) (event.getX() - lastX) + ";" + (int) (event.getY() - lastY));
                        lastX = event.getX();
                        lastY = event.getY();
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mouseWheelY = downY = event.getY(1);
                    return true;
                case MotionEvent.ACTION_POINTER_UP:
                    mouseWheelY = downY;
                    return true;
                default:
                    return false;
            }
        }
    }

    public static class ClipboardDialog extends AlertDialog.Builder {
        AlertDialog alertDialog;
        EditText editText;
        SocketClientService.Sender sender;

        public ClipboardDialog(Context context, final SocketClientService.Sender sender) {
            super(context);
            this.sender = sender;
            this.setTitle("剪贴板");
            editText = new EditText(context);
            editText.setAllCaps(false);
            editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            this.setView(editText);
            this.setPositiveButton("发送", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface p1, int p2) {
                    if (editText.getText() == null || editText.getText().length() == 0) {
                        return;
                    }
                    try {
                        sender.send(SET_CLIP_BOARD + URLEncoder.encode(editText.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("错误");
                        builder.setMessage(e.toString());
                        builder.setCancelable(false);
                        builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface p1, int p2) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.getWindow().setType(Utilities.getLayoutParamsType());
                        dialog.show();
                    }
                }
            });
            this.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface p1, int p2) {
                    ClipboardDialog.this.alertDialog.dismiss();//Useless
                }
            });
        }

        @Override
        public AlertDialog show() {
            alertDialog = super.create();
            alertDialog.getWindow().setType(Utilities.getLayoutParamsType());
            alertDialog.show();
            alertDialog.getWindow().setDimAmount(0f);
            return alertDialog;
        }
    }

    public class ToolButton extends android.widget.Button implements android.widget.PopupMenu.OnMenuItemClickListener, ModelManager.ToolButtonPropInterface {
        PopupMenu popuMenu;
        WindowManager.LayoutParams layoutParams;
        WindowManager windowManager;

        ToolButton(Context context, WindowManager manager, ModelManager.ToolButtonProperties toolButtonProp) {
            super(context);
            popuMenu = new PopupMenu(getContext(), this);//按钮点击时的菜单
            for (String i : Utilities.悬浮界面的按键文字) {
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
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            layoutParams.alpha = 1f;
            layoutParams.format = android.graphics.PixelFormat.RGBA_8888;
            layoutParams.gravity = android.view.Gravity.TOP | android.view.Gravity.START;
            layoutParams.x = (int) toolButtonProp.getX(getContext());
            layoutParams.y = (int) toolButtonProp.getY(getContext());
            layoutParams.width = layoutParams.height = Utilities.getMinSizeByRatio(getContext(), Utilities.DefaultButtonSizeScreenRatio);
            manager.addView(this, layoutParams);
        }

        //处理按钮移动和按钮单击操作
        private float lastX;//你可以用float，但会出现按键漂移问题
        private float lastY;
        private float onDownX;
        private float onDownY;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = onDownX = event.getRawX();
                    lastY = onDownY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    layoutParams.x = (-(int) lastX + (int) event.getRawX() + layoutParams.x);//转换成int以防止出现过大的误差，导致按钮漂移
                    layoutParams.y = (-(int) lastY + (int) event.getRawY() + layoutParams.y);
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    windowManager.updateViewLayout(this, layoutParams);
                    return true;
                case MotionEvent.ACTION_UP:
                    if ((Math.abs(onDownY - event.getRawY()) + Math.abs(onDownX - event.getRawX())) < Utilities.DEFAULT_TEXT_SIZE) {//判断是否点击按钮
                        popuMenu.show();
                        return true;
                    }
                default:
                    return false;
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem p1) {
            if (p1.getTitle().toString().equals(Utilities.悬浮界面的按键文字[0])) {//关闭
                popuMenu.dismiss();
                OverlayService.this.stopOverlay(true);
            } else if (p1.getTitle().toString().equals(Utilities.悬浮界面的按键文字[1])) {//禁用/启用
                if (windowManagerLP.flags == (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)) {
                    windowManagerLP.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                } else {
                    windowManagerLP.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                }
                windowManager.updateViewLayout(relativeLayout, windowManagerLP);
            } else if (p1.getTitle().toString().equals(Utilities.悬浮界面的按键文字[2])) {//剪贴板
                new ClipboardDialog(getContext(), sender).show();
            }
            return false;
        }

        @Override
        public void bringToFront() {
            super.bringToFront();
            windowManager.updateViewLayout(this, layoutParams);
        }

        @Override
        public float getXScreenRatio() {
            return getX() / (float) Utilities.getScreenHeight(getContext());
        }

        @Override
        public float getYScreenRatio() {
            return getY() / (float) Utilities.getScreenWidth(getContext());
        }
    }

    public class OSBinder extends android.os.Binder {
        public void setSender(SocketClientService.Sender sender) {
            OverlayService.this.setSender(sender);
        }

        public void setBindedService(SocketClientService service) {
            socketClientService = service;
        }

        public OverlayService getOverlayService() {
            return OverlayService.this;
        }
    }

}
