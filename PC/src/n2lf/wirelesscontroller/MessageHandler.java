package n2lf.wirelesscontroller;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class MessageHandler implements SocketServerService.IMessageHandler {
    Robot robot;

    MessageHandler() throws AWTException {
        robot = new Robot();
    }

    @Override
    public void handleMouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    @Override
    public void handleKeyPress(int keycode) {
        robot.keyPress(keycode);
    }

    @Override
    public void handleKeyRelease(int keycode) {
        robot.keyRelease(keycode);
    }

    @Override
    public void handleMousePress(int buttons) {
        robot.mousePress(buttons);
    }

    @Override
    public void handleMouseRelease(int buttons) {
        robot.mouseRelease(buttons);
    }

    @Override
    public void handleMouseWheel(int wheelAmt) {
        robot.mouseWheel(wheelAmt);
    }

    @Override
    public void handleSetClipboard(String setClipboard) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(setClipboard) , null);
    }
}