package N2LF.wirelesscontroller;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.view.View.OnLongClickListener;

public class EditorActivity extends Activity implements OnTouchListener , OnLongClickListener
{

	@Override
	public boolean onLongClick(View p1)
	{
		
		return false;
	}
	

	@Override
	public boolean onTouch(View p1, MotionEvent p2)
	{
		
		return false;
	}
	

    public static ArrayList attributeList = new ArrayList<Attribute>();
    
	//屏幕长宽
    int height ;
    int width;
    
	TextView back_textview = null;
    boolean isSelected = false;
    boolean isToasted = false;
    float selectedX ;
    float selectedY;

   

    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		//请求不要标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        height= this.getWindowManager().getDefaultDisplay().getHeight();
        width = this.getWindowManager().getDefaultDisplay().getWidth();
        setContentView(R.layout.edit);
		back_textview = findViewById(R.id.back_textview);
        back_textview.setOnTouchListener(this);
		back_textview.setOnLongClickListener(this);
    }

    
    public boolean onTouchEven7t(MotionEvent event)
    {
        if(isSelected){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
               selectedX= event.getX();
               selectedY = event.getY();
          //     Toast.makeText(this ,"You selected: X:"+ selectedX + ",Y:"+selectedY , Toast.LENGTH_LONG).show();
                break;
            case MotionEvent.ACTION_UP :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //    builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("选择一个按钮");
                //    指定下拉列表的显示数据
                //    设置一个下拉的列表选择项
                builder.setItems(KeyCode.getAllStringList(), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Toast.makeText(EditorActivity.this ,"\""+KeyCode.getAllStringList()[which]+"\" is at: X:"+ selectedX + ",Y:"+selectedY , Toast.LENGTH_SHORT).show();
                            Button button = new Button(EditorActivity.this);
                            button.setX(selectedX);
                            button.setY(selectedY);
                            button.setText(KeyCode.getAllStringList()[which]);
                            EditorActivity.this.addContentView( button,new ViewGroup.LayoutParams(150 ,150));
                            attributeList.add(new Attribute(selectedX/width,selectedY/height,KeyCode.getAllCodeList()[which]));
                            back_textview.setText("已添加"+attributeList.size()+"个按钮");
                        }
                    });
                builder.show();
                isSelected = false;
                break;
            }
         }
        return true;
    }

	
	
	
    @Override
    protected void onStop()
    {
        super.onStop();
		//退出时保存
		
   //     String stringAttributeList = (new Gson()).toJson(attributeList);
        File file = this.getExternalFilesDir(null);
        File jsonfile = new File(file.toString()+"/model.obj");
        try
        {
            FileOutputStream fis = new FileOutputStream(jsonfile , false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fis);
            objectOutputStream.writeObject(attributeList);
            objectOutputStream.flush();
			objectOutputStream.close();
            fis.flush();
            fis.close();
        }
        catch (FileNotFoundException | IOException e)
        {
            System.out.println(e);
        }
    }  

}
