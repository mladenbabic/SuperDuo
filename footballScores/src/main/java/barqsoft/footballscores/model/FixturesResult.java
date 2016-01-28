package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 1/27/2016.
 */
public class FixturesResult {

    @Expose
    private List<Fixtures> fixtures = new ArrayList<>();

    public List<Fixtures> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<Fixtures> fixtures) {
        this.fixtures = fixtures;
    }
}
