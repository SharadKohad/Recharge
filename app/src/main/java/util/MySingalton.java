package util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingalton
{
    private static MySingalton mySingalton;
    private RequestQueue requestQueue;
    private static Context context;

    public MySingalton(Context context)
    {
        this.context=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public  static synchronized MySingalton getInstance(Context context){
        if (mySingalton==null)
        {
            mySingalton=new MySingalton(context);
        }
        return mySingalton;
    }
    public <T> void addRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
