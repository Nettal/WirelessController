package n2lf.wirelesscontroller;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class MessageHandler implements SocketServerService.IMessageHandler {
    Robot robot;

    MessageHandler() throws AWTException {
        robot = new Robot();
    }

    @Override
    public void handleMouseMove(String onMouseMove) {
        robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x+
                        Integer.parseInt(onMouseMove.substring(0,onMouseMove.indexOf(";"))),
                        MouseInfo.getPointerInfo().getLocation().y+
                        Integer.parseInt(onMouseMove.substring(onMouseMove.indexOf(";")+1)));
    }

    @Override
    public void handleKeyPress(String onKeyPress) {
        robot.keyPress(Integer.parseInt(onKeyPress));
    }

    @Override
    public void handleKeyRelease(String onKeyRelease) {
        robot.keyRelease(Integer.parseInt(onKeyRelease));
    }

    @Override
    public void handleMousePress(String onMousePress) {
        robot.mousePress(Integer.parseInt(onMousePress));
    }

    @Override
    public void handleMouseRelease(String onMouseRelease) {
        robot.mouseRelease(Integer.parseInt(onMouseRelease));
    }

    @Override
    public void handleMouseWheel(String onMouseWheel) {
        robot.mouseWheel(Integer.parseInt(onMouseWheel));
    }

    @Override
    public void handleSetClipboard(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length-1; i++) {
            stringBuilder.append(strings[i]).append(System.lineSeparator());
        }
        stringBuilder.append(strings[strings.length-1]);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(stringBuilder.toString()) , null);
    }
}