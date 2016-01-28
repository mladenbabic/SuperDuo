package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class FixturesLinks {

    @Expose
    private Href self;
    @Expose
    private Href soccerseason;
    @Expose
    private Href homeTeam;
    @Expose
    private Href awayTeam;

    public Href getSelf() {
        return self;
    }

    public void setSelf(Href self) {
        this.self = self;
    }

    public Href getSoccerseason() {
        return soccerseason;
    }

    public void setSoccerseason(Href soccerseason) {
        this.soccerseason = soccerseason;
    }

    public Href getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Href homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Href getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Href awayTeam) {
        this.awayTeam = awayTeam;
    }
}
