package it.jaschke.alexandria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.fragment.BookDetailFragment;
import it.jaschke.alexandria.fragment.BookListFragment;


public class MainActivity extends BaseActivity implements  Callback {

    private static final String TAG = "MainActivity";

    private boolean mTwoPane = true;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setToolbar(false, true);
        getSupportActionBar().setTitle(getTitle());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BookListFragment.getInstance())
                    .commit();
        }
        if (findViewById(R.id.containerDetail) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        Intent addBookIntent = new Intent(this, AddBookActivity.class);
        ActivityCompat.startActivity(this, addBookIntent, null);
    }

    @Override
    public void onItemSelected(String ean) {
        Log.d(TAG, "onItemSelected() called with: " + "ean = [" + ean + "]");
        Bundle bundle = new Bundle();
        bundle.putString(BookDetailFragment.EAN_KEY, ean);
        bundle.putBoolean(BookDetailFragment.TWO_PANE_KEY, mTwoPane);
        if(mTwoPane){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerDetail, BookDetailFragment.getInstance(bundle))
                    .addToBackStack("detail")
                    .commit();

        } else {
            Intent callDetailActivity = new Intent(this, BookDetailActivity.class);
            callDetailActivity.putExtras(bundle);
            ActivityCompat.startActivity(this,callDetailActivity, null);
        }
    }

}