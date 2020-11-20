package N2LF.wirelesscontroller;
import android.widget.RelativeLayout;
import android.view.MotionEvent;
import android.view.View;

public class TouchRelativeLayout extends RelativeLayout
{
    public static float multiple = 1;
    public static int actionStatus = 0;
    public static boolean isOutOfCircle =false;
    public static boolean isLeftClicked = false;
    
    float actionDownX=0;
    float actionDownY=0;
    float actionMoveX=0;
    float actionMoveY=0;
    TouchRelativeLayout(android.content.Context context){
        super(context);
    }

    public void addView(View child , Attribute attr)
    {
        
        super.addView(child);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        actionStatus = event.getAction();
        System.out.println(event);
        switch (event.getAction()){
            case event.ACTION_DOWN:
                new Thread(new TouchThread()).start();
                actionDownX = event.getX();
                actionDownY = event.getY();
                break;
            case event.ACTION_MOVE :
                actionMoveX = event.getX();
                actionMoveY = event.getY();
                isOutOfCircle = Math.abs((actionMoveX-actionDownX)*(actionMoveY-actionDownX))>=10;
                if(isOutOfCircle){
                  int vectorX =(int) ((actionMoveX-actionDownX)*multiple);
                  int vectorY = (int) ((actionMoveY-actionDownY)*multiple);
            //      System.out.println(actionDownX+"<>"+actionDownY+"<>"+actionMoveX+"<>"+actionMoveY);
                  actionDownX = actionMoveX;
                  actionDownY = actionMoveY;
                  SocketClientService.eventQueue.addFirst(String.format("OMM%d,%d",vectorX,vectorY));
                }
                break;
            case event.ACTION_UP :
                isOutOfCircle = false;
                if(isLeftClicked){
                    System.out.println("omr");
                    SocketClientService.eventQueue.addFirst(String.format("OMR%d" , KeyCode.BUTTON1_DOWN_MASK));
                    isLeftClicked = false;
                }
                break;
        }
        
        return false;
    } 
}

class TouchThread implements Runnable
{
    
    @Override
    public void run(){
        try
        {
            Thread.sleep(200);
            if(!TouchRelativeLayout.isOutOfCircle){
                System.out.println("omp");
                SocketClientService.eventQueue.addFirst(String.format("OMP%d" , KeyCode.BUTTON1_DOWN_MASK));
                TouchRelativeLayout.isLeftClicked = true;
            }
        }
        catch (InterruptedException e)
        {System.out.println( e);
        }
    }  
}
