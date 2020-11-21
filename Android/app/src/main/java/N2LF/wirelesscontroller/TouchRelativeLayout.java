package N2LF.wirelesscontroller;
import android.widget.RelativeLayout;
import android.view.MotionEvent;
import android.view.View;

public class TouchRelativeLayout extends RelativeLayout
{
    TouchRelativeLayout(android.content.Context context){
        super(context);
    }

    public void addView(View child , Attribute attr)
    {
        
        super.addView(child);
    }

        float lastX ,lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean isMoved = false;

        //  System.out.println(event);
        switch (event.getAction()){
            case event.ACTION_DOWN:
                //    SocketClientService.eventQueue.addFirst(String.format("OMP%d" , KeyCode.BUTTON1_DOWN_MASK));
                lastX=event.getX();
                lastY=event.getY();
                break;
            case event.ACTION_MOVE :
                isMoved=true;
                SocketClientService.eventQueue.addFirst(String.format("OMM%d,%d",(int)(event.getX()-lastX),(int)(event.getY()-lastY)));
                lastX=event.getX();

                lastY=event.getY();
                break;
            case event.ACTION_UP :
                if(!isMoved){
                    SocketClientService.eventQueue.addFirst(String.format("OMP%d" , KeyCode.BUTTON1_DOWN_MASK));
                    SocketClientService.eventQueue.addFirst(String.format("OMR%d" , KeyCode.BUTTON1_DOWN_MASK));
                }
                break;
        }

        return true;
    } 
}
