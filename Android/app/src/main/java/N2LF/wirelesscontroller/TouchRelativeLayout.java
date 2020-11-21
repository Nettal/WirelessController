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
		boolean isMoved = false;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        
        //  System.out.println(event);
        switch (event.getAction()){
            case event.ACTION_DOWN:
				isMoved = false;
			//	System.out.println("isMoved = false");
                //    SocketClientService.eventQueue.addFirst(String.format("OMP%d" , KeyCode.BUTTON1_DOWN_MASK));
                lastX=event.getX();
                lastY=event.getY();
                break;
            case event.ACTION_MOVE :
                if(
				!((event.getX()-lastX
					+event.getY()-lastY)<2)
					){
					isMoved = true;
			//		 System.out.println("isMoved = true");
				}
				
                SocketClientService.eventQueue.addFirst(String.format("OMM%d,%d",(int)(event.getX()-lastX),(int)(event.getY()-lastY)));
                lastX=event.getX();

                lastY=event.getY();
                break;
            case event.ACTION_UP :
                if(!isMoved){
                    SocketClientService.eventQueue.addFirst(String.format("OMP%d" , KeyCode.BUTTON1_DOWN_MASK));
                    SocketClientService.eventQueue.addFirst(String.format("OMR%d" , KeyCode.BUTTON1_DOWN_MASK));
                }
				isMoved = false;
                break;
        }

        return true;
    } 
}
