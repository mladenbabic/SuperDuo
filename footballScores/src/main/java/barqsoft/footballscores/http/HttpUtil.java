package barqsoft.footballscores.http;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/16/2015.
 */
public class HttpUtil {

    private static FootballDBService service;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.football-data.org/alpha/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FootballDBService.class);
    }

    public static FootballDBService getService() {
        return service;
    }
}
