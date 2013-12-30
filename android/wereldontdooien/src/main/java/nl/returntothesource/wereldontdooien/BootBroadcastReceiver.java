package nl.returntothesource.wereldontdooien;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmReceiver.setAlarm(context);
        }
    }
}