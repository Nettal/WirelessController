package n2lf.wirelesscontroller.utilities;
import android.content.Context;
import android.view.ViewGroup;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import n2lf.wirelesscontroller.activity.ModelEditorActivity;
import n2lf.wirelesscontroller.service.OverlayService;


public class ModelManager implements java.io.Serializable
{
    private static final long serialVersionUID = 16384L;
    public static ArrayList getAllModelFileName(Context context , boolean deleteIllegalFile){
        ArrayList list = new ArrayList<String>();
        File file = context.getExternalFilesDir("model");
        for(int i= 0;i< file.list().length ; i++){
            File childFile = new File(file , file.list()[i]);
            if(childFile.getName().length()>5 && childFile.getName().endsWith(".model")){
                list.add(childFile.getName());
            }else{
                if(deleteIllegalFile){
                    childFile.delete();
                    i--; //file.list()像arraylist
                }
            }
        }
        return list;
    }
    public static ModelManager getModelFromFile(Context context , String fileName) 
    throws ClassNotFoundException, IOException
    {
        File file = new File(context.getExternalFilesDir("model") , fileName+".model");
        java.io.FileInputStream fis = new java.io.FileInputStream(file);
        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
        ModelManager manager = (ModelManager)ois.readObject();
        fis.close();
        return manager;
    }


    private final String modelName;
    private final int screenWidth;
    private final int screenHeight;
    private ToolButtonProperties toolButtinProp;
    private ArrayList keyCodeButtonPropList;
    public ModelManager(ViewGroup viewGroup , int screenWidth , int screenHeight ,  String modelName){
        this.modelName = modelName;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        keyCodeButtonPropList = new ArrayList<KeyCodeButtonProperties>();
        for(int i = 0; i < viewGroup.getChildCount(); i++){
            if(viewGroup.getChildAt(i) instanceof ModelEditorActivity.KeyCodeButton){
                keyCodeButtonPropList.add(new KeyCodeButtonProperties((ModelEditorActivity.KeyCodeButton)viewGroup.getChildAt(i)));
            }else if(viewGroup.getChildAt(i) instanceof ToolButtonPropInterface){
                toolButtinProp = new ToolButtonProperties((ToolButtonPropInterface)viewGroup.getChildAt(i));
            }
        }
    }

    public ModelManager(ViewGroup viewGroup , Context context , String modelName){
        this(viewGroup , Utilities.getScreenWidth(context) , Utilities.getScreenHeight(context) , modelName);
    }

    public String getModelName(){
        return modelName;
    }

    public boolean isHorizontal(){
        return screenHeight < screenWidth;
    }

    public Exception saveModelToFile(Context context){
        File file = new File(context.getExternalFilesDir("model") , modelName+".model");
        try{
            if(file.exists()){
                file.delete();
                //throw new Exception("There is already a file with the same name");
            }
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            fos.close();
        }
        catch (IOException e){
            file.delete();
            return e;
        }
        return null;
    }

    public void setToolButtonProp(ToolButtonPropInterface button){
        toolButtinProp = new ToolButtonProperties(button);
    }

    public ToolButtonProperties getToolButtonProp(){
        return toolButtinProp;
    }

    public void addKeyCodeButtonProp(ModelEditorActivity.KeyCodeButton button){
        keyCodeButtonPropList.add(new KeyCodeButtonProperties(button));
    }

    public KeyCodeButtonProperties[] getKeyCodeButtonPropList(){
        KeyCodeButtonProperties[] propList = new KeyCodeButtonProperties[keyCodeButtonPropList.size()];
        return (KeyCodeButtonProperties[])keyCodeButtonPropList.toArray(propList);
    }

    public class KeyCodeButtonProperties implements java.io.Serializable
    {
        private static final long serialVersionUID = 32768L;
        private final String buttonName;//nullable
        private final float widthScreenRatio;
        private final float heightScreenRatio;
        private final float XScreenRatio;
        private final float YScreenRatio;
        private final int textColor;
        private final String buttonColorString;//null为未修改
        private final int keyCode;
        private final int KeyCodeIndex;
        KeyCodeButtonProperties(ModelEditorActivity.KeyCodeButton keyCodeButton){
            buttonName = keyCodeButton.getText().toString();
            widthScreenRatio = keyCodeButton.getWidthScreenRatio();
            heightScreenRatio = keyCodeButton.getHeightScreenRatio();
            XScreenRatio = keyCodeButton.getXScreenRatio();
            YScreenRatio = keyCodeButton.getYScreenRatio();
            textColor = keyCodeButton.getTextColors().getDefaultColor();
            buttonColorString = keyCodeButton.getButtonColorString();
            keyCode = keyCodeButton.getKeyCode();
            KeyCodeIndex = keyCodeButton.getKeyCodeIndex();
        }

        public int getKeyCode(){
            return keyCode;
        }

        public int getKeyCodeIndex(){
            return KeyCodeIndex;
        }

        public int getTextColor(){
            return textColor;
        }

        public String getButtonName(){
            return buttonName;//nullable
        }

        public String getButtonColorString(){
            return buttonColorString;
        }

        public int getWidth(Context context){
            return (int)(Utilities.getScreenWidth(context)*widthScreenRatio);
        }

        public int getHeight(Context context){
            return (int)(Utilities.getScreenHeight(context)*heightScreenRatio);
        }

        public float getX(Context context){
            return Utilities.getScreenHeight(context)*XScreenRatio;
        }

        public float getY(Context context){
            return Utilities.getScreenWidth(context)*YScreenRatio;
        }

        public boolean isMouseKeyCode(){
            return KeyCode.isMouseKeyCode(keyCode);
        }
    }

    public class ToolButtonProperties implements java.io.Serializable{
        private static final long serialVersionUID = 65536L;
        private final float ToolbuttonXScreenRatio;
        private final float ToolbuttonYScreenRatio;
        ToolButtonProperties(ToolButtonPropInterface button){
            ToolbuttonXScreenRatio = button.getXScreenRatio();
            ToolbuttonYScreenRatio = button.getYScreenRatio();
        }

        public float getX(Context context){
            return Utilities.getScreenHeight(context)*ToolbuttonXScreenRatio;
        }

        public float getY(Context context){
            return Utilities.getScreenWidth(context)*ToolbuttonYScreenRatio;
        }
    }

    public interface ToolButtonPropInterface{
        float getXScreenRatio();
        float getYScreenRatio();
    }
}
