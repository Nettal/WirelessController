package n2lf.wirelesscontroller.utilities;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.EditText;
import android.view.Gravity;

public class SearchableDialog extends AlertDialog.Builder
{
	public static void showDialog(Context context , String[] stringList , int keyCodeIndex){
		int dialogSize = Utilities.getMinSizeByRatio(context,Utilities.DialogScreenRatio);
		new SearchableDialog(context , stringList , keyCodeIndex).show().getWindow().setLayout(dialogSize ,dialogSize);
	}
	
	
    String[] stringList;
    int keyCodeIndex;

    SearchableDialog(Context context , String[] stringList , int keyCodeIndex){
        super(context);
        this.stringList = stringList;
        this.keyCodeIndex = keyCodeIndex;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);//垂直方向
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        EditText editText = new EditText(context);
        editText.setHint("搜索");
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextParams);
        linearLayout.addView(editText);
        setView(linearLayout);
    }
    
    
    interface KeyCodeIndexChangeListener{
        void onKeyCodeIndexChange(int keyCodeIndex);
    }
}
