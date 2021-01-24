package n2lf.wirelesscontroller.activity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.app.AlertDialog;
import n2lf.wirelesscontroller.R;
import android.content.DialogInterface;
import android.widget.ScrollView;
import android.view.LayoutInflater;
import android.widget.EditText;
import n2lf.wirelesscontroller.utilities.colorpicker.ColorPickerView;
import n2lf.wirelesscontroller.utilities.colorpicker.ColorUtil;
import android.text.Editable;
import n2lf.wirelesscontroller.utilities.KeyCode;
import n2lf.wirelesscontroller.utilities.SearchableDialog;
import n2lf.wirelesscontroller.utilities.ModelManager;
import java.io.IOException;
import java.io.FileNotFoundException;


public class ModelEditorActivity extends Activity
{
    private RelativeLayout relativeLayout;
    private TextView onAddTextView;
    private ToolButton toolButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getActionBar().hide();
        this.setContentView(relativeLayout = new RelativeLayout(this));
        onAddTextView = new TextView(this);
        onAddTextView.setText("");
        onAddTextView.setTextSize(Utilities.DEFAULT_TEXT_SIZE);
        RelativeLayout.LayoutParams rLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rLP.addRule(RelativeLayout.CENTER_IN_PARENT);//线性布局在action_down时不会显示button bug?
        relativeLayout.addView(onAddTextView,rLP);
        toolButton = new ToolButton(this , relativeLayout);
		try{
			ModelManager modelManager = ModelManager.getModelFromFile(this , getIntent().getStringExtra("modelName"));
			for(int i =0 ; i<modelManager.getKeyCodeButtonPropList().length ; i++){
				new KeyCodeButton(relativeLayout , modelManager.getKeyCodeButtonPropList()[i]);
			}
		}
		catch(FileNotFoundException e){
			System.out.println(e);
		}
		catch (ClassNotFoundException e){
			showError(e);
		}
		catch (IOException e){
			showError(e);
		}
    }
    
    
    private boolean addButtonStarted(){
        onAddTextView.setText(Utilities.点击选取位置[0]);//点击以选取按钮位置
        relativeLayout.setBackgroundColor(Utilities.EditorBackgroundColor);
        relativeLayout.setOnTouchListener(new onButtonAddTouchListener());
        return true;
    }
    
    private boolean addFinished(){
        onAddTextView.setText("");
        relativeLayout.setBackgroundColor(Color.alpha(0));
        relativeLayout.setOnTouchListener(null);//it is ok
        return true;
    }
    
	
	private void showError(Object o){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("错误");
		builder.setMessage(o.toString());
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(android.content.DialogInterface p1, int p2){

				}
			});
		AlertDialog dialog = builder.create();
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O){//安卓7的待遇
			dialog.getWindow().setType(android.view.WindowManager.LayoutParams.TYPE_PHONE);
		}
		dialog.show();
	}
    
    private class onButtonAddTouchListener implements OnTouchListener
    {
        private int lastX;//你可以用float，但会出现按键漂移问题
        private int lastY;
        private float onDownX;
        private float onDownY;
        KeyCodeButton keyCodeButton;
        int buttonSize;
        @Override
        public boolean onTouch(View p1, MotionEvent event)
        {
            switch(event.getAction()){
                case event.ACTION_DOWN:
                    lastX = (int) (onDownX = event.getX());
                    lastY = (int) (onDownY = event.getY());
                    keyCodeButton = new KeyCodeButton(ModelEditorActivity.this);
                    buttonSize = Utilities.getMinSizeByRatio(ModelEditorActivity.this , Utilities.DefaultButtonSizeScreenRatio);
                    keyCodeButton.setX(onDownX-buttonSize/2);//否则按钮会在点击位置的右下角
                    keyCodeButton.setY(onDownY-buttonSize/2);    
                    relativeLayout.addView(keyCodeButton,buttonSize,buttonSize);
                    toolButton.bringToFront();//置于等层
                    return true;
                case event.ACTION_MOVE:
                    keyCodeButton.setX(event.getX()-buttonSize/2);
                    keyCodeButton.setY(event.getY()-buttonSize/2);
                    return true;
                case event.ACTION_UP:
                    keyCodeButton.editThis();//must edit at once
                    return true;
                default:
                    return false;
            }
        }   
    }
    
    /*预览按钮*/
    public class KeyCodeButton extends Button
    {
        private AlertDialog buttonEditorDialog;
        private AlertDialog.Builder tempBuilder;//每次setView()
        private int keyCodeIndex;
		private String buttonColorString;
        //dialog 控件
        private Button textColorButton;
        private Button buttonColorButton;
        private Button buttonMappingButton;
        private EditText buttonHeightEditText;
        private EditText buttonWidthEditText;
        private EditText buttonNameEditText;
        private EditText buttonARGBEditText;
        private EditText stringARGBEditText;
        private TextView titleTextView;
		
        KeyCodeButton(Context context){
            super(context);
            keyCodeIndex = -1;
            //原生按钮
            this.setAllCaps(false);//字母自动大写
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){//安卓8+的待遇
				this.setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);//根据文字多少改变字体大小
			}
			tempBuilder = new AlertDialog.Builder(context);
            //获取编辑dialog的控件
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_modelEditor_buttton,null);
            (textColorButton = dialogView.findViewById(R.id.dialog_editor_button_textColor)).setOnClickListener(
                new OnClickListener(){
                    @Override
                    public void onClick(View p1){
						//颜色选择
						ColorPickerView colorPickerView = new ColorPickerView(getContext());
						colorPickerView.setAlphaSliderVisible(true);//这个不能去掉，否则会出现error
                        colorPickerView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener(){
                                @Override
                                public void onColorChanged(int color){
                                    stringARGBEditText.setText(ColorUtil.convertToARGB(color).toString());
                                    KeyCodeButton.this.setTextColor(color);
                                }
                            });
                        colorPickerView.setColor(KeyCodeButton.this.getTextColors().getDefaultColor());
                        tempBuilder.setView(colorPickerView);
                        int size = Utilities.getMinSizeByRatio(KeyCodeButton.this.getContext(),Utilities.DialogScreenRatio);
                        AlertDialog alertDialog = tempBuilder.show();
						alertDialog.getWindow().setDimAmount(0f);//去除黑色遮罩;
						alertDialog.getWindow().setLayout(size , size);
                    }
                });
                
            (buttonColorButton = dialogView.findViewById(R.id.dialog_editor_button_buttonColor)).setOnClickListener(
                new OnClickListener(){
                    @Override
                    public void onClick(View p1){
						ColorPickerView colorPickerView = new ColorPickerView(getContext());
						colorPickerView.setAlphaSliderVisible(true);
                        colorPickerView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener(){
                                @Override
                                public void onColorChanged(int color){
                                    buttonARGBEditText.setText(ColorUtil.convertToARGB(color).toString());
                                    KeyCodeButton.this.setButtonColor(color);
                                }
                            });
                        colorPickerView.setColor(KeyCodeButton.this.getButtonColorInt());
                        tempBuilder.setView(colorPickerView);
                        int size = Utilities.getMinSizeByRatio(KeyCodeButton.this.getContext(),Utilities.DialogScreenRatio);
                        AlertDialog alertDialog = tempBuilder.show();
						alertDialog.getWindow().setDimAmount(0f);//去除黑色遮罩;
						alertDialog.getWindow().setLayout(size , size);
                    }
                });
                
            (buttonMappingButton = dialogView.findViewById(R.id.dialog_editor_button_buttonMapping)).setOnClickListener(
                new OnClickListener(){
                    @Override
                    public void onClick(View p1){
                        new SearchableDialog(KeyCodeButton.this.getContext() , KeyCode.getAllKeyName() , keyCodeIndex , new SearchableDialog.IndexChangeListener(){
								@Override
								public void onIndexChange(int index){
									keyCodeIndex = index;
									buttonMappingButton.setText("已选："+KeyCode.getAllKeyName()[index]);
									buttonNameEditText.setText(KeyCode.getAllKeyName()[index]);
								}
							});
                    }
                });
            buttonMappingButton.setAllCaps(false);
            
            (buttonWidthEditText = dialogView.findViewById(R.id.dialog_editor_editText_buttonWidth)).addTextChangedListener(
                new sizeEditTextWatcher(buttonWidthEditText , new Utilities.FloatChangeListener(){
                        @Override
                        public boolean onFloatChange(float f){
                            if(f*Utilities.getScreenWidth(KeyCodeButton.this.getContext())>=Utilities.getMinSizeByRatio(KeyCodeButton.this.getContext(),Utilities.MiniButtonSizeScreenRatio)){//判断是否过小
                            /*不能太小，不然不会显示，此处为大小合适*/
                            relativeLayout.updateViewLayout(KeyCodeButton.this , new RelativeLayout.LayoutParams((int)(f*Utilities.getScreenWidth(KeyCodeButton.this.getContext())),KeyCodeButton.this.getHeight()));
                            return true;}
                            return false;
                        }
                    }));
			
            (buttonHeightEditText = dialogView.findViewById(R.id.dialog_editor_editText_buttonHeight)).addTextChangedListener(
                new sizeEditTextWatcher(buttonHeightEditText , new Utilities.FloatChangeListener(){
                        @Override
                        public boolean onFloatChange(float f){
                            if(f*Utilities.getScreenHeight(KeyCodeButton.this.getContext())>=Utilities.getMinSizeByRatio(KeyCodeButton.this.getContext(),Utilities.MiniButtonSizeScreenRatio)){//判断是否过小
                            /*不能太小，此时大小合适*/
                            relativeLayout.updateViewLayout(KeyCodeButton.this , new RelativeLayout.LayoutParams(KeyCodeButton.this.getWidth(),(int)(f*Utilities.getScreenHeight(KeyCodeButton.this.getContext()))));
                            return true;}
                            return false;
                        }
                    }));
                    
            (buttonNameEditText = dialogView.findViewById(R.id.dialog_editor_editText_buttonName)).addTextChangedListener(new nameEditTextWatcher(buttonNameEditText,this));
			
            (buttonARGBEditText = dialogView.findViewById(R.id.dialog_editor_editText_buttonARGB)).addTextChangedListener(
                new argbEditTextWatcher(buttonARGBEditText, new ColorPickerView.OnColorChangedListener(){
                        @Override
                        public void onColorChanged(int color){
                            KeyCodeButton.this.setButtonColor(color);
                        }
                    }));
			
            (stringARGBEditText = dialogView.findViewById(R.id.dialog_editor_editText_textARGB)).addTextChangedListener(
                new argbEditTextWatcher(stringARGBEditText, new ColorPickerView.OnColorChangedListener(){
                        @Override
                        public void onColorChanged(int color){
                            KeyCodeButton.this.setTextColor(color);
                        }
                    }));
			
            titleTextView = dialogView.findViewById(R.id.dialog_editor_textView_title);
			/*
			 dialog 编辑按钮
			 */
			AlertDialog.Builder buttonEditorBuilder = new AlertDialog.Builder(context);
            ScrollView scrollView = new ScrollView(context);//使其可以上下滚动
            scrollView.addView(dialogView);
            buttonEditorBuilder.setView(scrollView);
            buttonEditorBuilder.setPositiveButton(Utilities.确定删除[0],//确定
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface p1, int p2){
                        KeyCodeButton.this.buttonEditorDialog.dismiss();
                    }
                });
            buttonEditorBuilder.setNegativeButton(Utilities.确定删除[1],//删除
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface p1, int p2){
                        relativeLayout.removeView(ModelEditorActivity.KeyCodeButton.this);
                    }
                });
			buttonEditorDialog = buttonEditorBuilder.create();
        }
		
		KeyCodeButton(ViewGroup viewGroup, ModelManager.KeyCodeButtonProperties prop){
			this(viewGroup.getContext());
			this.keyCodeIndex = prop.getKeyCodeIndex();
			this.setTextColor(prop.getTextColor());
			stringARGBEditText.setText(ColorUtil.convertToARGB(prop.getTextColor()));
			this.setX(prop.getX(getContext()));
			this.setY(prop.getY(getContext()));
			if(prop.getButtonColorString()!=null){
                this.setButtonColor(ColorUtil.convertToColorInt(prop.getButtonColorString()));
				buttonARGBEditText.setText(prop.getButtonColorString());
            }
            if(prop.getButtonName()!=null){
                this.setText(prop.getButtonName());
				buttonNameEditText.setText(prop.getButtonName());
            }
            viewGroup.addView(this , prop.getWidth(getContext()) , prop.getHeight(getContext()));
		}
        
        private float lastX;
        private float lastY;
        private float onDownX;
        private float onDownY;
        @Override
        public boolean onTouchEvent(MotionEvent event)//处理按钮移动
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
                    if((  Math.abs(onDownY-event.getRawY()) + Math.abs(onDownX-event.getRawX()) )< Utilities.OFFSET){//判断是否点击按钮
                        this.editThis();//call edit dialog
                        return true;
                    }
                default:
                    return false;
            }
        }
    

        private void setButtonColor(int color)
        {
			buttonColorString = ColorUtil.convertToARGB(color);
            this.getBackground().setAlpha(0);//按钮阴影便会消除
            //   this.setBackgroundColor(color);//不好看
            this.getBackground().setColorFilter(color , android.graphics.PorterDuff.Mode.SRC);
            //   this.getBackground().setTint(color); 会出现所有按钮颜色都改变的问题
            /**
             SRC SRC_IN  OK
             ADD 按钮不会透明
             SRC_OVER 同上
             SRC_ATOP 同上
             SCREEN 同上
             LIGHTEN 同上
             CLEAR 按钮只是透明
             SRC_OUT 同上
             DARKEN 是真的黑，没法用
             DST 同上
             OVERLAY 同上
             MULTIPLY 有透明，无颜色
             XOR 同上
            */
        }
		
		private int getButtonColorInt(){
			try
			{
				return ColorUtil.convertToColorInt(getButtonColorString());
			}
			catch (Exception e)
			{
				return Utilities.DefaultButtonColor;
			}
		}
		
        public String getButtonColorString(){
			return buttonColorString;//null为未修改
		}
		
		public int getKeyCode(){
            if(keyCodeIndex==-1){
                return -1;
            }
			return KeyCode.getAllKeyCode()[keyCodeIndex];
		}
		
		public int getKeyCodeIndex(){
			return keyCodeIndex;
		}
		
		public float getHeightScreenRatio(){
			return ((float)getHeight()==0 ? Utilities.getMinSizeByRatio(ModelEditorActivity.this , Utilities.DefaultButtonSizeScreenRatio) : getHeight())/(float)Utilities.getScreenHeight(getContext());
		}
		
		public float getWidthScreenRatio(){
			return (float)(getWidth()==0 ? Utilities.getMinSizeByRatio(ModelEditorActivity.this , Utilities.DefaultButtonSizeScreenRatio) : getWidth())/(float)Utilities.getScreenWidth(getContext());
		}
		
		public float getXScreenRatio(){
            return (float)getX()/(float)Utilities.getScreenHeight(getContext());
        }

        public float getYScreenRatio(){
            return (float)getY()/(float)Utilities.getScreenWidth(getContext());
		}
		
        public void editThis(){
            if(buttonHeightEditText.getText()==null || buttonHeightEditText.getText().length()==0){
                buttonHeightEditText.setText(String.valueOf(getHeightScreenRatio()));
            }
            if(buttonWidthEditText.getText()==null || buttonWidthEditText.getText().length()==0){
                buttonWidthEditText.setText(String.valueOf(getWidthScreenRatio()));
            }
			int size = Utilities.getMinSizeByRatio(this.getContext(),Utilities.DialogScreenRatio);
            buttonEditorDialog.show();
			buttonEditorDialog.getWindow().setDimAmount(0f);//去除黑色遮罩;
			buttonEditorDialog.getWindow().setLayout(size , size);
        }
    }
    
	
	/**
	 针对editText的事件处理，使不正确的文本更改为红色
	 */

	private class argbEditTextWatcher implements android.text.TextWatcher{
		EditText editText;
		int oriTextColor;
		ColorPickerView.OnColorChangedListener onColorChangedListener;

		argbEditTextWatcher(EditText editText , ColorPickerView.OnColorChangedListener onColorChangedListener){
			this.editText = editText;
			this.oriTextColor = editText.getCurrentTextColor();
			this.onColorChangedListener = onColorChangedListener;
		}

		@Override
		public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){
		}

		@Override
		public void onTextChanged(CharSequence p1, int p2, int p3, int p4){
		}

		@Override
		public void afterTextChanged(Editable p1)
		{
			try
			{
				onColorChangedListener.onColorChanged(ColorUtil.convertToColorInt(p1.toString()));
				editText.setTextColor(oriTextColor);//小心白色，导致白色主题无色
			}
			catch (Exception e)
			{
				editText.setTextColor(Utilities.ErrorTextColor);
			}
		}
	}

	
	private class sizeEditTextWatcher implements android.text.TextWatcher{
		EditText editText;
		int oriTextColor;
		Utilities.FloatChangeListener floatChangeListener;
		sizeEditTextWatcher(EditText editText , Utilities.FloatChangeListener floatChangeListener){
			this.editText = editText;
			this.oriTextColor = editText.getCurrentTextColor();
			this.floatChangeListener = floatChangeListener;
		}

		@Override
		public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){
		}

		@Override
		public void onTextChanged(CharSequence p1, int p2, int p3, int p4){
		}

		@Override
		public void afterTextChanged(Editable p1)
		{
			try{
				float f = Float.valueOf(p1.toString());
				if(f >= 1.2f || f < 0 || !floatChangeListener.onFloatChange(f)){//注意此时是短路或
					editText.setTextColor(Utilities.ErrorTextColor);
					return;
				}
				editText.setTextColor(oriTextColor);
			}catch(Exception ignored){
				editText.setTextColor(Utilities.ErrorTextColor);
			}
		}
	}

	
	private class nameEditTextWatcher implements android.text.TextWatcher{
		EditText editText;
		KeyCodeButton button;
		nameEditTextWatcher(EditText editText , KeyCodeButton button){
			this.editText = editText;
			this.button = button;
		}

		@Override
		public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){
		}

		@Override
		public void onTextChanged(CharSequence p1, int p2, int p3, int p4){
		}

		@Override
		public void afterTextChanged(Editable p1)
		{
			button.setText(p1);
		}
	}
	
    
    public class ToolButton extends Button implements android.widget.PopupMenu.OnDismissListener ,android.widget.PopupMenu.OnMenuItemClickListener , ModelManager.ToolButtonPropInterface
    {
        PopupMenu popuMenu;
        ToolButton(Context context , RelativeLayout relativeLayout){//创建button对象 & 为后来的悬浮按钮做准备
            super(context);
            RelativeLayout.LayoutParams rLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rLP.addRule(RelativeLayout.CENTER_IN_PARENT);
            rLP.height = rLP.width = Utilities.getMinSizeByRatio(context , Utilities.DefaultButtonSizeScreenRatio);
            relativeLayout.addView(this, rLP);
            popuMenu = new PopupMenu(getContext() , this);//按钮点击时的菜单
            for(String i :Utilities.添加界面的按键文字){
                popuMenu.getMenu().add(i);
            }
            popuMenu.setOnDismissListener(this);
            popuMenu.setOnMenuItemClickListener(this);
            
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
                    if((  Math.abs(onDownY-event.getRawY()) + Math.abs(onDownX-event.getRawX()) )< Utilities.OFFSET){//判断是否点击按钮
                        this.popuMenu.show();
                        return true;
                    }
                default:
                    return false;
            }
        }  
        
        @Override
        public void onDismiss(PopupMenu p1){
            p1.dismiss();
        }
        
        @Override
        public boolean onMenuItemClick(MenuItem p1)
        {
            if(p1.getTitle().toString()==Utilities.添加界面的按键文字[0]){//完成
                if(onAddTextView.getText().length()!=0){
                    addFinished();//处理背景变化和 onTouchEvent
                }else{
					ModelManager model = new ModelManager(relativeLayout , getContext() , getIntent().getStringExtra("modelName"));
                    Object o = model.saveModelToFile(getContext());
                    if(o!=null){
                        showError(o);
                    }
                    ModelEditorActivity.this.finish();//此时应该保存模板
                }
            }else if(p1.getTitle().toString()==Utilities.添加界面的按键文字[1]){//添加按钮
                addButtonStarted();
            }else{//添加触摸板
                
            }
            return false;
        }
        
        @Override
		public float getXScreenRatio(){
            return (float)getX()/(float)Utilities.getScreenHeight(getContext());
        }

        @Override
        public float getYScreenRatio(){
            return (float)getY()/(float)Utilities.getScreenWidth(getContext());
		}
        
    }
}
