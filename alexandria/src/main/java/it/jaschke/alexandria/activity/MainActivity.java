package it.jaschke.alexandria.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.fragment.BookListFragment;


public class MainActivity extends BaseDetailActivity implements  Callback {

    private static final String TAG = "MainActivity";
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private CharSequence title;
    public static boolean IS_TABLET = false;
    private BroadcastReceiver messageReceiever;
    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setToolbar(false, true);

        messageReceiever = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiever, filter);

        title = getTitle();
        getSupportActionBar().setTitle(title);

        if(savedInstanceState == null){
            //TODO Mladen check the selected first fragment and add two pane
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BookListFragment.getInstance())
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiever);
        super.onDestroy();
    }

    @Override
    public void onItemSelected(String ean) {

        //TODO Mladen call AddBookActivity for book detail with transition animation

//        Bundle args = new Bundle();
//        args.putString(BookDetailFragment.EAN_KEY, ean);
//
//        BookDetailFragment fragment = new BookDetailFragment();
//        fragment.setArguments(args);
//
//        int id = R.id.container;
//        if (findViewById(R.id.right_container) != null) {
//            id = R.id.right_container;
//        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(id, fragment)
//                .addToBackStack("Book Detail")
//                .commit();

    }

    private class MessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(MESSAGE_KEY) != null) {
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

}