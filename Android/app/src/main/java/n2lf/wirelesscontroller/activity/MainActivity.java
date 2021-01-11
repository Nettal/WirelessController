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

public class MainActivity extends Activity {
    Button modelManagerButton;
    Button startButton;
    EditText ip_port_EditText;
    Spinner modelListSelector;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modelManagerButton = findViewById(R.id.activity_main_button_modelManager);     
        ip_port_EditText = findViewById(R.id.activity_main_editText_ip_port);  
        startButton = findViewById(R.id.activity_main_button_start);
        modelListSelector = findViewById(R.id.activity_main_spinner_selector);
        
        
        startButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1){
                    Utilities.checkAndAllowOverlayPermission(MainActivity.this);
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
