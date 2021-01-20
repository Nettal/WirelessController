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
		sharedPreferences = getSharedPreferences("MainActivity_ipEditText_String" , Context.MODE_PRIVATE);
        modelManagerButton = findViewById(R.id.activity_main_button_modelManager);
        ipEditText = findViewById(R.id.activity_main_editText_ip);
		ipEditText.addTextChangedListener(new TextWatcher(){
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void afterTextChanged(Editable p1){
					//TODO ip and port
				}
			});
        startButton = findViewById(R.id.activity_main_button_start);
        modelListSelector = findViewById(R.id.activity_main_spinner_selector);
        
        
        startButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1){
                    if(Utilities.checkAndAllowOverlayPermission(MainActivity.this)){
						Intent intent = new Intent(MainActivity.this , SocketClientService.class);
						intent.putExtra("port" , 37385);
						intent.putExtra("ip" , "127.0.0.1");
						intent.putExtra("modelName" , Utilities.DefaultModelName);
						startService(intent);
					}
                }
            });
        modelManagerButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1){
                    startActivity(new Intent(MainActivity.this,ModelEditorActivity.class));
                }
            });
    }
}
