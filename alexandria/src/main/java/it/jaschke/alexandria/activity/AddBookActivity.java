package it.jaschke.alexandria.activity;

import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import it.jaschke.alexandria.R;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/26/2015.
 */
public class AddBookActivity extends BaseActivity {

    private static final String TAG = "AddBookActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);
        setToolbar(true, true);
        getSupportActionBar().setTitle(getTitle());
    }

}
