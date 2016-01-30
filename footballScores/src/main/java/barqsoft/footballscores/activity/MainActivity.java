package barqsoft.footballscores.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import barqsoft.footballscores.R;
import barqsoft.footballscores.fragment.MainScreenFragment;
import barqsoft.footballscores.sync.FootballScoresSyncAdapter;
import barqsoft.footballscores.util.Constants;
import barqsoft.footballscores.util.Utilities;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * The main activity responsible for showing tabs with match scores for 5 days.
 */

public class MainActivity extends AppCompatActivity {

    public static int current_fragment = 2;
    public static final int NUM_PAGES = 5;
    public static String TAG = "MainActivity";
    private Toolbar toolbar;

    @Bind(R.id.materialViewPager)
    MaterialViewPager mViewPager;


    /**
     * Initialize views in main acitivity for all 5 fragments. Fragment represents each of 5 days in soccer seasons.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Reached MainActivity onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(false);
            }
        }

        mViewPager.getViewPager().setCurrentItem(current_fragment);
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.POSITION_KEY, position);
                return MainScreenFragment.newInstance(bundle);
            }

            @Override
            public int getCount() {
                return NUM_PAGES;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return Utilities.getDayName(MainActivity.this, System.currentTimeMillis() + ((position - 2) * 86400000));
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorResAndUrl(
                        R.color.colorPrimaryDark,
                        "http://wallpaperswide.com/download/sport_2-wallpaper-800x480.jpg");
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());//TODO Mladen check it
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        FootballScoresSyncAdapter.initializeSyncAdapter(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
