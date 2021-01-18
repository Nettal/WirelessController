package n2lf.wirelesscontroller.utilities;
import android.content.Context;
import android.view.ViewGroup;
import n2lf.wirelesscontroller.activity.ModelEditorActivity;
import java.io.File;
import java.util.ArrayList;

public class ModelManager
{
	public static boolean saveModelToFile(Context context , ModelManager modelBuilder , String fileName){
		File file = new File(context.getExternalFilesDir("model") , fileName);

		return true;
	}
	
	public static boolean getModelFromFile(Context context , String fileName){
		File file = new File(context.getExternalFilesDir("model") , fileName);

		return true;
	}
	
	private Context context;
	private ArrayList buttonList;
	private String modelName;
	public ModelManager(Context context , ViewGroup viewGroup , String modelName){
		this.context = context;
		this.modelName = modelName;
		buttonList = new ArrayList<ButtonProperties>();
		for(int i = 0; i < viewGroup.getChildCount(); i++){
			if(viewGroup.getChildAt(i) instanceof ModelEditorActivity.OverviewButton){
				buttonList.add(new ButtonProperties((ModelEditorActivity.OverviewButton)viewGroup.getChildAt(i)));
			}
		}
	}
	
	public String getModelName(){
		return modelName;
	}
	
	public void loadModelForViewGroup(ViewGroup viewGroup){
		
	}
	
	public boolean saveModelToFile(){
		File file = new File(context.getExternalFilesDir("model") , modelName);

		return true;
	}
	
	class ButtonProperties{
		String buttonName;
		float buttonWidthScreenRatio;
		float buttonHeightScreenRatio;
		float buttonPositionX;
		float buttonPositionY;
		int buttonTextColor;
		String buttonColorString;//null为未修改
		int keyCode;
		ButtonProperties(ModelEditorActivity.OverviewButton overviewButton){
			buttonName = overviewButton.getText().toString();
			buttonWidthScreenRatio = overviewButton.getWidthScreenRatio();
			buttonHeightScreenRatio = overviewButton.getHeightScreenRatio();
			buttonPositionX = overviewButton.getX();
			buttonPositionY = overviewButton.getY();
			buttonTextColor = overviewButton.getTextColors().getDefaultColor();
			buttonColorString = overviewButton.getButtonColorString();
			keyCode = overviewButton.getKeyCode();
		}

		@Override
		public String toString()
		{
			return super.toString();
		}
	}
}
