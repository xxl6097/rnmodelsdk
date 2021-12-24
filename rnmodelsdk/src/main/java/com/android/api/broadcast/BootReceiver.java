package com.android.api.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.api.util.Logc;

public class BootReceiver extends BroadcastReceiver {
    //com.mtscene.app.MainActivity
    //com.awesomeproject.MainActivity

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            try {
                Logc.e("自启动了 ！！！！！");
                Class<?> clasz = null;
                try {
                    clasz = Class.forName("com.awesomeproject.MainActivity");
                    System.out.println("1===>"+clasz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("2===>"+e.getMessage());
                    clasz = Class.forName("com.mtscene.app.MainActivity");
                    System.out.println("1===>"+clasz);
                }
                Intent newIntent = new Intent(context, clasz);  // 要启动的Activity
                //1.如果自启动APP，参数为需要自动启动的应用包名
                //Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                //这句话必须加上才能开机自动运行app的界面
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //2.如果自启动Activity
                context.startActivity(newIntent);
                //3.如果自启动服务
                //context.startService(newIntent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}