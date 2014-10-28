package org.solarex.volleydemo;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    
    private RequestQueue mRequestQueue;
    private static App mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    
    public static synchronized App getInstance(){
        return mInstance;
    }
    
    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    
    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(request);
    }
    
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
    
    public void cancelPendingRequests(Object tag){
        if (null != mRequestQueue) {
            mRequestQueue.cancelAll(tag);
        }
    }
    
}
