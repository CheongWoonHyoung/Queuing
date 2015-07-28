package com.example.owner.queuing;

/**
 * Created by mark_mac on 2015. 7. 23..
 */
import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            programShutdown();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                R.string.back_twice_confirm, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void programShutdown() {
        activity .moveTaskToBack(true);
        activity .finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}