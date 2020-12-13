package n2lf.wirelesscontroller.utilities;
import android.content.Context;
import android.provider.Settings;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Utilities
{
    public static float 偏移量 = 5;
    public static int 按钮默认大小的除数 = 20;
    public static float 按钮默认透明度 = 0.7f;
    public static boolean checkAndAllowOverlayPermission(Context context){
        if(Settings.canDrawOverlays(context)){
            return true;
        }else{
            context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName())));
            Toast.makeText(context,"请授予悬浮窗权限",Toast.LENGTH_LONG).show();
            return false;
        }
    }
    
    
}
