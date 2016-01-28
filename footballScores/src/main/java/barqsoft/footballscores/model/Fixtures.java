package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class Fixtures {

    @Expose
    private String date;
    @Expose
    private String status;
    @Expose
    private int matchday;
    @Expose
    private Result result;
    @Expose
    @SerializedName("_links")
    private FixturesLinks links;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMatchday() {
        return matchday;
    }

    public void setMatchday(int matchday) {
        this.matchday = matchday;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public FixturesLinks getLinks() {
        return links;
    }

    public void setLinks(FixturesLinks links) {
        this.links = links;
    }
}
