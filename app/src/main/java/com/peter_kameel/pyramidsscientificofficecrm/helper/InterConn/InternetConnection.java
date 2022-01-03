package com.peter_kameel.pyramidsscientificofficecrm.helper.InterConn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {
    private final Context context;

    public InternetConnection(Context context) {
        this.context = context;
    }

    public Boolean isConnectToInternet() {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (con != null) {
            NetworkInfo info = con.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }
}
