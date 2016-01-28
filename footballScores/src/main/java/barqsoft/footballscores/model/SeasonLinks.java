package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class SeasonLinks {

    @Expose
    private Href self;
    @Expose
    private Href teams;
    @Expose
    private Href fixtures;

    public Href getSelf() {
        return self;
    }

    public void setSelf(Href self) {
        this.self = self;
    }

    public Href getTeams() {
        return teams;
    }

    public void setTeams(Href teams) {
        this.teams = teams;
    }

    public Href getFixtures() {
        return fixtures;
    }

    public void setFixtures(Href fixtures) {
        this.fixtures = fixtures;
    }

}
