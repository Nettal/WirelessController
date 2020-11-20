package N2LF.wirelesscontroller;
import android.view.WindowManager;
import android.widget.TableLayout.LayoutParams;
import java.io.Serializable;

public class Attribute implements Serializable{//属性   默认为横屏
    private static final long serialVersionUID = -8486594296615491973L;
 //   ExtraButton extraButton;
 //   WindowManager.LayoutParams layoutParam;
    private float alpha = 0.5F;
    private String text;
    private int keyCode;
    private int color;//not set color
    private float x;
    private float y;
    //    int z;
    private float width = MainActivity.DefaultHW;
    private float height = MainActivity.DefaultHW;
    boolean isMouseButton = false;
    boolean isExitButton = false;
    
    Attribute(float x ,float y ,int keyCode){
        this.x = x;
        this.y = y;
        this.keyCode = keyCode;
        isMouseButton = isMouseKeyCode(keyCode);
        this.text = KeyCode.getAllKeyName(keyCode);
    }
    Attribute(float x ,float y ,int keyCode ,String text ,float alpha ,int color ,float height ,float width){
        this.x = x;
        this.y = y;
        isMouseButton = isMouseKeyCode(keyCode);
        this.keyCode = keyCode;
        this.text = text;
        this.alpha = alpha;
        this.color = color;
        this.height = height;
        this.width = width;
    }
  /*  /////////
    public void setExtarbutton(ExtraButton exButton){
        this.extraButton = exButton;
    }
    public ExtraButton getExtraButton(){
        return extraButton;
    }
    public void setLayoutParam(WindowManager.LayoutParams lp){
        this.layoutParam = lp;
    }
    public WindowManager.LayoutParams getLayoutParam(){
        return this.layoutParam;
    }
    ////////  */
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public int getKeyCode(){
        return keyCode;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public float getAlpha(){
        return alpha;
    }
    public int getColor(){
        return color;
    }
    public String getText(){
        if(text != null){
            return text;
        }else{
            return KeyCode.getAllKeyName(keyCode);
        }
    }

    @Override
    public String toString()
    {
        // TODO: Implement this method
        return "alpha:"+alpha+",text:"+text+",keycode:"+keyCode+",color:"+color+",x:"+x+",y:"+y+",width:"+width+",height:"+height;
    }
    
    boolean isMouseKeyCode(int keycode){
        if(
            (keycode == 1<<6) ||
            (keycode == 1<<7) ||
            (keycode == 1<<8) ||
            (keycode == 1<<9) ||
            (keycode == 1<<10) || 
            (keycode == 1<<11) ||
            (keycode == 1<<12) ||
            (keycode == 1<<13) ||
            (keycode == 1<<14) ||
            (keycode == 1<<15) ||
            (keycode == 1<<16) ||
            (keycode == 1<<17) ||
            (keycode == 1<<18) ||
            (keycode == 1<<19) ||
            (keycode == 1<<20) ||
            (keycode == 1<<21) ||
            (keycode == 1<<22) ||
            (keycode == 1<<23) ||
            (keycode == 1<<24) ||
            (keycode == 1<<25) ||
            (keycode == 1<<26) ||
            (keycode == 1<<27) ||
            (keycode == 1<<28) ||
            (keycode == 1<<29) ||
            (keycode == 1<<30))
        {
            return true;
        }
        return false;
    }
}
