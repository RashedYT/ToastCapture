package com.example.rashed.toastcapture;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

/**
 * Created by Rashed on 16-03-2018.
 */

public class CaptureService extends AccessibilityService {

    private final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    private static final String TAG = "CaptureService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Notifications";
            String description = "Toast Notifications";
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notify", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            String packageName = (String) event.getPackageName();
            Parcelable parcelableData = event.getParcelableData();

            if(parcelableData instanceof Notification){

            }
            else{
                List<CharSequence> messages = event.getText();
                if (messages.size()>0){
                    String toastMsg = (String) messages.get(0);
                    if (toastMsg.endsWith("Changed profile pic") || toastMsg.endsWith("online")){
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= 26){
                            vibrator.vibrate(VibrationEffect.createOneShot(300,VibrationEffect.DEFAULT_AMPLITUDE));
                        }
                        else{
                            vibrator.vibrate(300);
                        }
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Notify")
                                .setContentText(toastMsg)
                                .setContentTitle("Whatsapp Notifier")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setSmallIcon(R.mipmap.ic_launcher);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                        notificationManagerCompat.notify((new Random()).nextInt(),mBuilder.build());
                    }
                    else{
                        Log.v(TAG,toastMsg);
                    }
                }
                else{
                    Log.v(TAG,"No message detected");
                }
            }
        }
        else{
            Log.v(TAG,"Got un-handled event");
        }

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {

        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        } else {
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        }

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }
}
