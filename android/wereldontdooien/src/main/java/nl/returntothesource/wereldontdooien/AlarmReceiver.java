package nl.returntothesource.wereldontdooien;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by jolandaverhoef on 30-12-13.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm received");
        List<String> currentFonkels = FonkelIO.readFonkelsFromDisk(context);
        List<String> newFonkels = FonkelIO.readFonkelsFromApi();
        FonkelIO.writeFonkelsToDisk(context, newFonkels);
        // Only send notification when there are new fonkels
        if (newFonkels != null && newFonkels.size() > 0) {
            // Send notification if there are no current fonkels or the most recent current
            // fonkel differs from the most recent new fonkel
            if (currentFonkels == null || currentFonkels.size() == 0 ||
                    ! newFonkels.get(newFonkels.size()-1).
                            equals(currentFonkels.get(currentFonkels.size()-1))) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(
                                R.drawable.ic_notification_snowflake)).getBitmap())
                        .setSmallIcon(R.drawable.ic_notification_snowflake)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_text))
                        .setAutoCancel(true);
                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                builder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                int notifyID = 1;
                mNotificationManager.notify(notifyID, builder.build());
            }
        }
    }

    public static void setAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, newIntent, 0);

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
