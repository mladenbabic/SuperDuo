package barqsoft.footballscores.http;

import java.util.List;

import barqsoft.footballscores.model.FixturesResult;
import barqsoft.footballscores.model.Season;
import barqsoft.footballscores.model.TeamResult;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email>  on 11/1/2015.
 */
public interface FootballDBService {

    @GET("/soccerseasons")
    Call<List<Season>> getSoccerSeasons(@Header("X-Auth-Token") String API);

    @GET("/fixtures")
    Call<FixturesResult> getFixtures(@Header("X-Auth-Token") String API, @Query("timeFrame") String timeFrame);

    @GET("/soccerseasons/{seasonId}/teams")
    Call<TeamResult> getTeams(@Header("X-Auth-Token") String API, @Path("seasonId") String seasonId);

}
