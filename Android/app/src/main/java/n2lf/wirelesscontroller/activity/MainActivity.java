package n2lf.wirelesscontroller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.view.View;
import n2lf.wirelesscontroller.utilities.Utilities;
import android.content.Context;
import android.content.Intent;
import n2lf.wirelesscontroller.R;
import android.text.TextWatcher;
import android.text.Editable;
import android.content.SharedPreferences;
import n2lf.wirelesscontroller.service.SocketClientService;
import n2lf.wirelesscontroller.utilities.ModelManager;
import android.text.InputFilter;

public class MainActivity extends Activity {
    Button modelManagerButton;
    Button startButton;
    EditText ipEditText;
	EditText portEditText;
    Spinner modelListSelector;
    
	SharedPreferences sharedPreferences;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        ipEditText = findViewById(R.id.activity_main_editText_ip);
		ipEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);//单行输入的前提
		ipEditText.setMaxLines(1);
		//ipEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(256)});//最长长度
		ipEditText.setText(sharedPreferences.getString("ipEditText_String","127.0.0.1"));
		ipEditText.addTextChangedListener(new TextWatcher(){
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void afterTextChanged(Editable p1){
					sharedPreferences.edit().putString("ipEditText_String" , (p1!=null && p1.length()!=0) ? p1.toString() : "127.0.0.1").commit();
				}
			});
		portEditText = findViewById(R.id.activity_main_editText_port);
		portEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);//单行输入的前提
		portEditText.setMaxLines(1);
		portEditText.setText(String.valueOf(sharedPreferences.getInt("portEditText_Tnt" , 37385)));
		portEditText.addTextChangedListener(new TextWatcher(){
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void afterTextChanged(Editable p1){
					sharedPreferences.edit().putInt("portEditText_Tnt" , isLegalText(p1) ? Integer.parseInt(p1.toString()) : 37385 ).commit();
				}
			});
        startButton = findViewById(R.id.activity_main_button_start);
        startButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1){
                    if(Utilities.checkAndAllowOverlayPermission(MainActivity.this)){
						Intent intent = new Intent(MainActivity.this , SocketClientService.class);
						intent.putExtra("port" , sharedPreferences.getInt("portEditText_Tnt" , 37385));
						intent.putExtra("ip" , sharedPreferences.getString("ipEditText_String","127.0.0.1"));
						intent.putExtra("modelName" , Utilities.DefaultModelName);
						startService(intent);
					}
                }
            });
		modelManagerButton = findViewById(R.id.activity_main_button_modelManager);
        modelManagerButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1){
					if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O && !Utilities.checkAndAllowOverlayPermission(MainActivity.this)){//安卓7-的待遇
                        return;}
                    Intent intent = new Intent(MainActivity.this,ModelEditorActivity.class);
                    intent.putExtra("modelName" , Utilities.DefaultModelName);
                    startActivity(intent);
                }
            });
    }
	
	private boolean isLegalText(Editable e){
		if(e==null || e.length()==0){
			return false;}
		try{
			Integer.parseInt(e.toString());}
		catch(RuntimeException re){
			System.out.println(re);
			return false;}
		return true;
	}
}
