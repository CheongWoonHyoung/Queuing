package com.example.owner.queuing;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by cheongwh on 2015. 8. 3..
 */
public class ProgressDialog extends Dialog{
    public ProgressDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_progress_dialog);
    }
}
