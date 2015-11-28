package it.jaschke.alexandria.activity;

import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.fragment.BookDetailFragment;


public class BookDetailActivity extends BaseActivity {

    private static final String TAG = "BookDetailActivity";

    private CharSequence title;
    public static boolean IS_TABLET = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setToolbar(false, true);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BookDetailFragment.getInstance(new Bundle()))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}