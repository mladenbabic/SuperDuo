package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class TeamLinks {

    @Expose
    private Href self;

    public Href getSelf() {
        return self;
    }

    public void setSelf(Href self) {
        this.self = self;
    }
}
