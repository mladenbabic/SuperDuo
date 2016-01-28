package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class Result {

    @Expose
    private int goalsHomeTeam;
    @Expose
    private int goalsAwayTeam;

    public int getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    public void setGoalsHomeTeam(int goalsHomeTeam) {
        this.goalsHomeTeam = goalsHomeTeam;
    }

    public int getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    public void setGoalsAwayTeam(int goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }
}
