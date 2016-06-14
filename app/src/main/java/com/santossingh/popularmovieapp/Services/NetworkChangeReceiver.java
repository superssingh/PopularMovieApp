package com.santossingh.popularmovieapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.santossingh.popularmovieapp.Activities.MainActivity;

/**
 * Created by Stark on 23/05/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean status=Utils.getConnectionStatus(context);
        if (status) {
            intent = new Intent(context, MainActivity.class);
            context.startService(intent);
            Toast.makeText(context,"Network Available !",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Network Not Available !",Toast.LENGTH_SHORT).show();
        }
    }

}