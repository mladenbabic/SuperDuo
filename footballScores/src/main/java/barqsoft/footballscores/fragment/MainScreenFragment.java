package barqsoft.footballscores.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.adapter.ScoresAdapter;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.sync.FootballScoresSyncAdapter;
import barqsoft.footballscores.util.Constants;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    @Bind(R.id.scores_list)
    RecyclerView mScoreList;

    public ScoresAdapter mScoreAdapter;
    private RecyclerViewMaterialAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private int last_selected_item = -1;
    private int mPosition;
    private String mFormattedDate;

    public MainScreenFragment() {
    }

    private void update_scores() {
        FootballScoresSyncAdapter.syncImmediately(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        update_scores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        if(getArguments() != null && getArguments().containsKey(Constants.POSITION_KEY)){
            mPosition = getArguments().getInt(Constants.POSITION_KEY);
            Date fragmentDate = new Date(System.currentTimeMillis() + ((mPosition - 2) * 86400000));
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            mFormattedDate = mformat.format(fragmentDate);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mScoreList.setLayoutManager(layoutManager);

        mScoreAdapter = new ScoresAdapter(getActivity(), null);
        mAdapter = new RecyclerViewMaterialAdapter(mScoreAdapter, 1);

        mScoreList.setAdapter(mAdapter);

        getLoaderManager().initLoader(SCORES_LOADER, null, this);

//        mScoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ViewHolder selected = (ViewHolder) view.getTag();
//                mAdapter.detail_match_id = selected.match_id;
//                MainActivity.selected_match_id = (int) selected.match_id;
//                mAdapter.notifyDataSetChanged();
//            }
//        });

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mScoreList, null);

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
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
        //Log.v(FetchScoreTask.TAG,"Loader query: " + String.valueOf(i));
        mScoreAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mScoreAdapter.swapCursor(null);
    }


    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment =  new MainScreenFragment();
        fragment.setArguments(bundle);
        return  fragment;
    }
}
