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

public class EditorActivity extends Activity implements OnClickListener
{
    public static ArrayList attributeList = new ArrayList<Attribute>();
    
    int height ;
    int width;
    
    boolean isSelected = false;
    boolean isToasted = false;
    float selectedX ;
    float selectedY;

    @Override
    public void onClick(View p1)
    {
        if(!isToasted){
            Toast.makeText(this ,"点击以选取位置" , Toast.LENGTH_SHORT).show();
            isToasted = true;}
        isSelected = true;
    }

    Button editButtton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        height= this.getWindowManager().getDefaultDisplay().getHeight();
        width = this.getWindowManager().getDefaultDisplay().getWidth();
        setContentView(R.layout.edit);
        editButtton = (Button) findViewById(R.id.editButton);
        editButtton.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
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
                            editButtton.setText("已添加"+attributeList.size()+"个");
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
