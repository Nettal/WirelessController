import java.awt.*;

public class Controller_wrapper implements Runnable{

    Robot robot;
    int port;
    BytesQueue bytesQueue;
    Controller_wrapper(int port,BytesQueue bytesQueue,Robot robot) {
        this.port = port;
        this.bytesQueue = bytesQueue;
        this.robot=robot;
    }

    @Override
    public void run() {
        try {
            System.out.println("Controller_wrapper: Created Controller_wrapper thread!");
    while (true){

        if (bytesQueue.isEmpty()){
            Thread.sleep(10);
    //        System.out.println("DEBUG:Controller_wrapper: bytesQueue isEmpty");
            continue;
        }

        try {
            exeCommand(bytesQueue.get());
        }catch(Throwable e){
            e.printStackTrace();
       //     System.err.println(e.toString());
        }
    }


        } catch (Exception e){
            System.err.println("Controller_wrapper: unKnow Error!");
            e.printStackTrace();
            try {
                new Thread(new Controller_wrapper(port,bytesQueue,new Robot())).start();
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        }
    }
    private void exeCommand(byte[] b){
        if (b[0] == 0)return;//no data
        int true_length;
        //计算真实长度(不包含前面的操作标记符:OMM,OKP,etc)
        for (true_length = 0; true_length < b.length; true_length++) {
            if (b[true_length + 3] == 0 || b[true_length + 3] == 79) break;
            // 0 就是数据末尾 ， 79 是下一个数据的开头
        }
   //     System.out.println(" DEBUG:Controller_wrapper: TrueLength:"+true_length);


        if (b[2]==77){//move or press

        /*
         *   移动
         * */

            int length_x;
            //计算X位移的真实长度
            for (length_x = 0; length_x < b.length; length_x++) {
                if (b[length_x + 3] == 44) break;// , 为 44
            }
       //     System.out.println("DEBUG:Controller_wrapper: MouseMove:"+ new String(b, 3, true_length));
            try {
             //   int X = Integer.parseInt(new String(b, 3, length_x));
             //   int Y = Integer.parseInt(new String(b, length_x+4, true_length-length_x-1));
            //    System.out.println(X);
            //    System.out.println(Y);
                 robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x+
                         Integer.parseInt(new String(b, 3, length_x)),
                                 MouseInfo.getPointerInfo().getLocation().y+
                         Integer.parseInt(new String(b, length_x+4, true_length-length_x-1)));
            }catch (Exception e){
                e.printStackTrace();
                System.err.println(e.toString());
            }
        }else {


            /*
             *   按键
             * */

            //偏移量：剔除OKP三个字符，读取真实长度
            int keycode = Integer.parseInt(new String(b, 3, true_length));


            if (b[1] == 75) {//键盘按键事件
                if (b[2] == 80) {//pressed
                    //          System.out.println("DEBUG:Controller_wrapper: Pressed:"+keycode);
                    robot.keyPress(keycode);
                }
                if (b[2] == 82) {//released
                    //          System.out.println("DEBUG:Controller_wrapper: Released:" + keycode);
                    robot.keyRelease(keycode);

                }
            } else {//鼠标按键事件
                if (b[2] == 80) {//pressed
                    robot.mousePress(keycode);
                }

                if (b[2] == 82) {//released
                    robot.mouseRelease(keycode);
                }
            }//mouse or keyboard
        }//move or press
            //检查是否有多余
        if (b[true_length + 3+1] != 0){
            System.out.println("DEBUG:Controller_wrapper: Too many data!");
             byte[] next = new byte[1024-true_length-3];
            for (int i = 0; i < next.length; i++) {
                /*
                     3 : OMM,OKP,etc
                     i : loop form 0
                 */
                next[i] = b[true_length + 3 + i];
                if (b[true_length + 3 + i] == 0)break;//end of extra data
            }
       //     System.out.println("DEBUG:Controller_wrapper: next data:"+new String(next, 0, next.length));
            exeCommand(next);
        }
    }
}
