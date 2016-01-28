package barqsoft.footballscores.http;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class CallResponse<T> {

    public T execute(Call<T> call) {
        try {
            Response<T> response = call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
