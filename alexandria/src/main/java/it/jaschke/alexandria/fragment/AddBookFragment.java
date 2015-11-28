package it.jaschke.alexandria.fragment;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.util.ServiceResponse;


public class AddBookFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchView.OnQueryTextListener {


    private static final String TAG = "AddBookFragment";
    private static final int TRASH_HOLD_INPUT = 13;
    private final int LOADER_ID = 1;

    @Bind(R.id.bookTitle)
    TextView mBookTitle;
    @Bind(R.id.bookSubTitle)
    TextView mBookSubTitle;
    @Bind(R.id.authors)
    TextView mAuthors;
    @Bind(R.id.categories)
    TextView mCategories;
    @Bind(R.id.bookCover)
    ImageView mBookCover;

    @Nullable
    @Bind(R.id.bookDetailContainer)
    CardView mBookDetailContainer;
    @Bind(R.id.coordinator)
    CoordinatorLayout mBookCoordinatorLayout;

    @BindString(R.string.key_search_text)
    String mKeySearchText;

    @BindString(R.string.input_hint)
    String mSearchTextHint;

    SearchView searchView = null;
    private String mTextSearch;
    private View rootView;

    private BroadcastReceiver messageReceiever;
    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    private static final String SCAN_FORMAT = "scanFormat";
    private static final String SCAN_CONTENTS = "scanContents";
    private String mScanFormat = "Format:";
    private String mScanContents = "Contents:";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        messageReceiever = new MessageReceiver();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(messageReceiever, filter);
    }


    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiever);
        super.onPause();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        ButterKnife.bind(this, rootView);

//        rootView.findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // This is the callback method that the system will invoke when your button is
//                // clicked. You might do this by launching another app or by including the
//                //functionality directly in this app.
//                // Hint: Use a Try/Catch block to handle the Intent dispatch gracefully, if you
//                // are using an external app.
//                //when you're done, remove the toast below.
//                Context context = getActivity();
//                CharSequence text = "This button should let you scan a book for its ic_barcode!";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
//
//            }
//        });

        rootView.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchView != null){
                    searchView.setQuery("", false);
                }
            }
        });

        rootView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent bookIntent = new Intent(getActivity(), BookService.class);
//                bookIntent.putExtra(BookService.EAN, ean.getText().toString());
//                bookIntent.setAction(BookService.DELETE_BOOK);
//                getActivity().startService(bookIntent);
            }
        });


        if (savedInstanceState != null) {
            mTextSearch = savedInstanceState.getString(mKeySearchText);
            Log.d(TAG, "mTextSearch: " + mTextSearch);
        }

        return rootView;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mTextSearch == null || mTextSearch.length() == 0) {
            return null;
        }
        String eanStr = mTextSearch.toString();
        eanStr = checkEan(eanStr);

        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(eanStr)),
                null,
                null,
                null,
                null
        );
    }

    @NonNull
    private String checkEan(String eanStr) {
        if (eanStr.length() == 10 && !eanStr.startsWith("978")) {
            eanStr = "978" + eanStr;
        }
        return eanStr;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            showDetailView(false);
            return;
        }

        showDetailView(true);

        String title = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        mBookTitle.setText(title);

        String subTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        mBookSubTitle.setText(subTitle);

        String authorsString = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        String[] authorsArr = authorsString.split(",");
        mAuthors.setLines(authorsArr.length);
        mAuthors.setText(authorsString.replace(",", "\n"));

        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));

        mBookCover.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(imgUrl)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .crossFade()
                .into(mBookCover);

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        mCategories.setText(categories);

        rootView.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
    }

    private void showDetailView(boolean showView) {
        mBookDetailContainer.setVisibility(showView ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {}


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu() returned: ");
        inflater.inflate(R.menu.add_book, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

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
            searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
            searchView.setQueryHint(mSearchTextHint);
        }
    }

    private void clearFields() {

        mBookTitle.setText("");
        mBookSubTitle.setText("");
        mAuthors.setText("");
        mCategories.setText("");

        rootView.findViewById(R.id.bookCover).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit() called with: " + "query = [" + query + "]");
        mTextSearch = query;
        return !startSearchService(mTextSearch);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange() called with: " + "newText = [" + newText + "]");
        mTextSearch = newText;
        return !startSearchService(mTextSearch);
    }

    private boolean startSearchService(String query) {
        String ean = query.toString();

        if(ean.length() > TRASH_HOLD_INPUT) return false;

        //catch isbn10 numbers
        ean = checkEan(ean);
        if (ean.length() < TRASH_HOLD_INPUT) {
//            clearFields();
            return true;
        }
        //Once we have an ISBN, start a book intent
        Intent bookIntent = new Intent(getActivity(), BookService.class);
        bookIntent.putExtra(BookService.EAN, ean);
        bookIntent.setAction(BookService.FETCH_BOOK);
        getActivity().startService(bookIntent);
        AddBookFragment.this.restartLoader();
        return false;
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(MESSAGE_KEY)) {
                @ServiceResponse int code = intent.getIntExtra(MESSAGE_KEY, BookService.NO_BOOK);
                String message = null;
                switch (code){
                    case  BookService.FOUND_OK:
                        restartLoader();
                        break;
                    case  BookService.NO_CONNECTION:
                        message = getString(R.string.no_internet_connection);
                        break;
                    case  BookService.NO_BOOK:
                        message = getString(R.string.not_found);
                        break;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                Snackbar.make(mBookCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
