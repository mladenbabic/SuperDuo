package barqsoft.footballscores.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.adapter.ScoresAdapter;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.event.OnRefreshEndEvent;
import barqsoft.footballscores.sync.FootballScoresSyncAdapter;
import barqsoft.footballscores.util.Constants;
import barqsoft.footballscores.util.DeviceUtil;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a match data for particular day
 */
public class MainScreenFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "MainScreenFragment";
    @Bind(R.id.match_scores_list)
    RecyclerView mScoreList;

    @Bind(R.id.no_matches_container)
    FrameLayout mNoMatchesContainer;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.match_scores_container)
    CoordinatorLayout mCoordinatorLayout;

    public ScoresAdapter mScoreAdapter;
    private RecyclerViewMaterialAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private int mPosition;
    private String mFormattedDate;

    public MainScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null && getArguments().containsKey(Constants.POSITION_KEY)) {
            mPosition = getArguments().getInt(Constants.POSITION_KEY);
            Date fragmentDate = new Date(System.currentTimeMillis() + ((mPosition - 2) * 86400000));
            SimpleDateFormat mformat = new SimpleDateFormat(Constants.DATE_FORMAT);
            mFormattedDate = mformat.format(fragmentDate);
        }

        int gridColumns = getActivity().getResources().getInteger(R.integer.grid_columns);

        RecyclerView.LayoutManager layoutManager = null;

        if (!DeviceUtil.isLandscape(getContext())) {
            layoutManager = new LinearLayoutManager(getActivity());
        } else {
            layoutManager = new GridLayoutManager(getActivity(), gridColumns);
        }

        mScoreList.setLayoutManager(layoutManager);

        mScoreAdapter = new ScoresAdapter(getActivity(), null);
        mAdapter = new RecyclerViewMaterialAdapter(mScoreAdapter, gridColumns);
        mScoreList.setAdapter(mAdapter);

        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mScoreList, null);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //Workaround to disable log
        MaterialViewPagerAnimator animator = MaterialViewPagerHelper.getAnimator(this.getContext());
        animator.ENABLE_LOG = false;
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, new String[]{mFormattedDate}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.getCount() == 0) {
            mNoMatchesContainer.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        } else {
            cursor.moveToFirst();
            mNoMatchesContainer.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);

            while (!cursor.isAfterLast()) {
                cursor.moveToNext();
            }
            mScoreAdapter.swapCursor(cursor);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mScoreAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void refreshMatches() {
        Log.d(TAG, "refreshMatches: ");
        FootballScoresSyncAdapter.syncImmediately(getContext());
    }

    @Override
    public void onRefresh() {
        if(DeviceUtil.isOnline(getContext())){
            mSwipeRefreshLayout.setRefreshing(true);
            refreshMatches();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Snackbar.make(mCoordinatorLayout, R.string.match_detail_no_internet, Snackbar.LENGTH_LONG).show();
        }
    }

    public void onEvent(OnRefreshEndEvent event){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new MainScreenFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
