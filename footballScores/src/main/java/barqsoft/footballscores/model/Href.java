package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class Href {

    @Expose
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
