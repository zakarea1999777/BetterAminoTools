package com.better.amino.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class IntentManager {

    /* Start Activity From URL */

    public static void goToUrl(Activity context, String url){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
