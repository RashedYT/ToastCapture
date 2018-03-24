package com.example.rashed.toastcapture;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Rashed on 16-03-2018.
 */

public class CaptureService extends AccessibilityService {

    private final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    private static final String TAG = "CaptureService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

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
                    if (toastMsg.endsWith("Changed profile pic")){
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= 26){
                            vibrator.vibrate(VibrationEffect.createOneShot(300,VibrationEffect.DEFAULT_AMPLITUDE));
                        }
                        else{
                            vibrator.vibrate(300);
                        }
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
