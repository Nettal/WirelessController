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
        modelManagerButton = (Button) findViewById(R.id.activity_mainButton_modelManager);     
        ip_port_EditText = (EditText) findViewById(R.id.activity_mainEditText_ip_port);  
        startButton = (Button) findViewById(R.id.activity_mainButton_start);
        modelListSelector = (Spinner) findViewById(R.id.activity_mainSpinner_selector);
        
        
        startButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    Utilities.checkAndAllowOverlayPermission(MainActivity.this);
                }
            });
        modelManagerButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    startActivity(new Intent(MainActivity.this,ModelCreatorActivity.class));
                }
            });
    }
    
    class mOnClickListener implements OnClickListener
    {
        @Override
        public void onClick(View p1)
        {
            
        } 
    }
}
