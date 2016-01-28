package barqsoft.footballscores.http;

import android.util.Log;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class CallResponse<T> {

    private static final boolean DEBUG = false;
    private static final String TAG = "CallResponse";

    public T execute(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if(DEBUG){
                Log.d(TAG, "execute: " + response.isSuccess());
            }
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
