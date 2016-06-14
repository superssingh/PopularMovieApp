package com.santossingh.popularmovieapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Stark on 23/05/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        boolean status=Utils.getConnectionStatus(context);
        if(!status){
            context.startActivity(intent);
            Toast.makeText(context,"Network Available !",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Network Not Available !",Toast.LENGTH_SHORT).show();
        }
    }

}