package it.jaschke.alexandria.activity;

import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;

import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.fragment.BookDetailFragment;


public class BookDetailActivity extends BaseActivity {

    private static final String TAG = "BookDetailActivity";
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        ButterKnife.bind(this);
        setToolbar(true, true);
        getSupportActionBar().setTitle(null);

        if(savedInstanceState == null){
            Bundle bundle = new Bundle(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerDetail, BookDetailFragment.getInstance(bundle))
                    .commit();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}