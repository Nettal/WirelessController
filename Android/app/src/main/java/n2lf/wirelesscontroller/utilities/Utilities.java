package n2lf.wirelesscontroller.utilities;
import android.content.Context;
import android.provider.Settings;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import android.view.WindowManager;
import android.graphics.Color;
import java.io.File;
import android.os.Build;

public class Utilities
{
    public static float OFFSET = 5;
    public static float DefaultButtonSizeScreenRatio = 0.12f;
    public static float MiniButtonSizeScreenRatio = 0.04f;
    public static float DefaultButtonAlpha = 0.7f;
    public static float DialogScreenRatio = 0.8f;
	public static String DefaultIp = "127.0.0.1";
	public static String DefaultModelName ="TempModel";
    public static String[] 添加界面的按键文字 = {"完成","添加按钮"};//逻辑实现在ModelEditorActivity
    public static String[] 悬浮界面的按键文字 = {"关闭","禁用/启用"};//逻辑实现在OverlayService
    public static String[] 点击选取位置 = {"点击以选取按钮位置","拖动以划定触摸区域"};
    public static String[] 确定删除 = {"确定","删除"};
    public static int DEFAULT_TEXT_SIZE = 25;
    public static int DefaultButtonColor = Color.argb(0xff,36,00,0xff);
    public static int EditorBackgroundColor = Color.argb(255,189,183,107);
    public static int ErrorTextColor = Color.RED;
	public static int DefaultPort = 37385;
	public static long ThreadSleepTime = 5;
    
	
    public static int getScreenWidth(Context context){
        return ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }
    public static int getScreenHeight(Context context){
        return ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }
	public static int getMaxSizeByRatio(Context context , float f){
        return (int) (Math.max(getScreenHeight(context),getScreenWidth(context))*f);
    }
    public static int getMinSizeByRatio(Context context , float f){
        return (int) (Math.min(getScreenHeight(context),getScreenWidth(context))*f);
    }
	
	public static int getLayoutParamsType(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
		}else{
			return WindowManager.LayoutParams.TYPE_PHONE;
		}
	}
		
    public static boolean checkAndAllowOverlayPermission(Context context){
        if(Settings.canDrawOverlays(context)){
            return true;
        }else{
            context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName())));
            Toast.makeText(context,"请授予悬浮窗权限",Toast.LENGTH_LONG).show();
            return false;
        }
    }
	
	public static String[] getModelNameList(){
		return null;
	}
	
    public interface FloatChangeListener{
        public boolean onFloatChange(float f)
    }
    
}
