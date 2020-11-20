package N2LF.wirelesscontroller;
import android.widget.Button;
import android.content.Context;
import android.view.MotionEvent;
import N2LF.wirelesscontroller.KeyCode;

public class ExtraButton extends Button
{//button
    boolean isMouseButton =false;
    int keyCode;
  ExtraButton(Context Context,Attribute attribute){
      super (Context);
      isMouseButton = attribute.isMouseButton;
      this.keyCode = attribute.getKeyCode();
      this.setText(attribute.getText());
      this.setBackgroundColor(attribute.getColor());
  }
  ExtraButton(Context context ,int keyCode){
      super(context);
      this.keyCode = keyCode;
      isMouseButton = KeyCode.isMouseKeyCode(keyCode);
      this.setText(KeyCode.getAllKeyName(keyCode));
  }

  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
      System.out.println(keyCode+"*******"+event.toString());
      switch (event.getAction()){
          case MotionEvent.ACTION_DOWN:
              if(isMouseButton){
                  SocketClientService.eventQueue.addFirst(String.format("OMP%d",keyCode));
              }else{
                  SocketClientService.eventQueue.addFirst(String.format("OKP%d",keyCode));
              }
              break;
          case MotionEvent.ACTION_UP:
              if(isMouseButton){
                  SocketClientService.eventQueue.addFirst(String.format("OMR%d",keyCode));
              }else{
                  SocketClientService.eventQueue.addFirst(String.format("OKR%d",keyCode));
              }
              break;
      }
      return true;
  }
}
