package n2lf.wirelesscontroller.utilities;
import android.content.Context;
import android.view.ViewGroup;
import n2lf.wirelesscontroller.activity.ModelEditorActivity;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ModelManager implements java.io.Serializable
{
    private static final long serialVersionUID = -8485794206615001973L;
    public static ArrayList getAllModelFileName(Context context , boolean deleteIllegalFile){
        ArrayList list = new ArrayList<String>();
        File file = context.getExternalFilesDir("model");
        for(int i= 0;i< file.list().length ; i++){
            File childFile = new File(file , file.list()[i]);
            if(childFile.getName().length()>3 && childFile.getName().substring(childFile.getName().length()-4).equals(".obj")){
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
        throws FileNotFoundException, ClassNotFoundException, IOException
        {
            File file = new File(context.getExternalFilesDir("model") , fileName);
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
            ModelManager manager = (ModelManager)ois.readObject();
            return manager;
	}
	
    
	private ArrayList buttonList;
	private String modelName;
    int screenWidth;
    int screenHeight;
	public ModelManager(ViewGroup viewGroup , int screenWidth , int screenHeight ,  String modelName){
		this.modelName = modelName;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
		buttonList = new ArrayList<ButtonProperties>();
		for(int i = 0; i < viewGroup.getChildCount(); i++){
			if(viewGroup.getChildAt(i) instanceof ModelEditorActivity.OverviewButton){
				buttonList.add(new ButtonProperties((ModelEditorActivity.OverviewButton)viewGroup.getChildAt(i)));
			}else if(viewGroup.getChildAt(i) instanceof ModelEditorActivity.TopButton){
               TopbuttonXScreenRatio = ((ModelEditorActivity.TopButton)viewGroup.getChildAt(i)).getXScreenRatio();
                TopbuttonYScreenRatio = ((ModelEditorActivity.TopButton)viewGroup.getChildAt(i)).getYScreenRatio();
            }
		}
	}
    
    public ModelManager(ViewGroup viewGroup , Context context , String modelName){
        this(viewGroup , Utilities.getScreenWidth(context) , Utilities.getScreenHeight(context) , modelName);
    }
	
	public void loadModelForViewGroup(ViewGroup viewGroup){
        
		
	}
	
    public String getModelName(){
        return modelName;
    }
	
    public boolean isHorizontal(){
        if(screenHeight < screenWidth){
            return true;
        }
        return false;
    }
    
	public Exception saveModelToFile(Context context){
        File file = new File(context.getExternalFilesDir("model") , modelName);
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
    
    float TopbuttonXScreenRatio;
    float TopbuttonYScreenRatio;

    @Override
    public String toString(){
        return new String("Contain "+buttonList.size()+"Button Properties");
    }
    
	class ButtonProperties implements java.io.Serializable{
		String buttonName;
		float buttonWidthScreenRatio;
		float buttonHeightScreenRatio;
		float buttonXScreenRatio;
		float buttonYScreenRatio;
		int buttonTextColor;
		String buttonColorString;//null为未修改
		int keyCode;
		ButtonProperties(ModelEditorActivity.OverviewButton overviewButton){
			buttonName = overviewButton.getText().toString();
			buttonWidthScreenRatio = overviewButton.getWidthScreenRatio();
			buttonHeightScreenRatio = overviewButton.getHeightScreenRatio();
			buttonXScreenRatio = overviewButton.getXScreenRatio();
			buttonYScreenRatio = overviewButton.getYScreenRatio();
			buttonTextColor = overviewButton.getTextColors().getDefaultColor();
			buttonColorString = overviewButton.getButtonColorString();
			keyCode = overviewButton.getKeyCode();
		}
	}
}
