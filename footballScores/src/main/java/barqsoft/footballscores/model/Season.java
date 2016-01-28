package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class Season {

    @Expose
    @SerializedName("_links")
    private SeasonLinks links;
    @Expose
    private String caption;
    @Expose
    private String league;
    @Expose
    private String year;

    public SeasonLinks getLinks() {
        return links;
    }

    public void setLinks(SeasonLinks links) {
        this.links = links;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
