package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class Team {

    @Expose
    private String name;
    @Expose
    private String code;
    @Expose
    private String shortName;
    @Expose
    private String crestUrl;

    @Expose
    @SerializedName("_links")
    private TeamLinks links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

    public TeamLinks getLinks() {
        return links;
    }

    public void setLinks(TeamLinks links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Team{" +
                "crestUrl='" + crestUrl + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
