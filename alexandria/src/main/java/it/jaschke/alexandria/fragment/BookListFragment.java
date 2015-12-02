package it.jaschke.alexandria.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.api.BookListAdapter;
import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.util.SpacesItemDecoration;


public class BookListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    private static final String TAG = "BookListFragment";
    private final int LOADER_ID = 10;
    private int mPosition = RecyclerView.NO_POSITION;

    @Bind(R.id.books_list)
    RecyclerView mBookList;

    @Bind(R.id.no_empty_view)
    TextView mEmptyBooksView;

    @BindString(R.string.key_search_text)
    String mKeySearchText;
    private BookListAdapter mBookListAdapter;
    private String mTextSearch;

    public static Fragment getInstance() {
        return new BookListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: " + mKeySearchText + ":" + mTextSearch);
        super.onSaveInstanceState(outState);
        outState.putString(mKeySearchText, mTextSearch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        ButterKnife.bind(this, rootView);

        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        mBookListAdapter = new BookListAdapter(getActivity(), null, (Callback) getActivity());
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), gridColumns);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);

        mBookList.setLayoutManager(mLayoutManager);
        mBookList.setHasFixedSize(true);
        mBookList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        mBookList.setAdapter(mBookListAdapter);

        if (savedInstanceState != null) {
            mTextSearch = savedInstanceState.getString(mKeySearchText);
            Log.d(TAG, "mTextSearch: " + mTextSearch);
        }

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        boolean nonEmptyText = (mTextSearch != null && mTextSearch.length() > 0);
        if (nonEmptyText) {
            final String selection = AlexandriaContract.BookEntry.TITLE + " LIKE ? OR " + AlexandriaContract.BookEntry.SUBTITLE + " LIKE ? ";
            String search = "%" + mTextSearch + "%";
            return new CursorLoader(
                    getActivity(),
                    AlexandriaContract.BookEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{search, search},
                    null
            );
        } else {
            return new CursorLoader(
                    getActivity(),
                    AlexandriaContract.BookEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mBookListAdapter.swapCursor(data);
        setEmptyView(data.getCount() == 0);
        if (mPosition != RecyclerView.NO_POSITION) {
            mBookList.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBookListAdapter.swapCursor(null);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu() returned: ");
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(this);
            if (mTextSearch != null) {
                Log.d(TAG, "onCreateOptionsMenu() called with: " + "mTextSearch = [" + mTextSearch + "]");
                searchView.setQuery(mTextSearch, false);
            }
            searchView.setIconified(mTextSearch == null);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit() called with: " + "query = [" + query + "]");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange() called with: " + "newText = [" + newText + "]");
        mTextSearch = newText;
        restartLoader();
        return false;
    }

    private void setEmptyView(boolean showEmptyView) {
        mEmptyBooksView.setVisibility(showEmptyView ? View.VISIBLE : View.GONE);
    }

}
