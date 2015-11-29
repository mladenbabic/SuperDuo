package it.jaschke.alexandria.activity;

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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.util.ServiceResponse;


public class AddBookActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchView.OnQueryTextListener {

    private static final String TAG = "AddBookActivity";
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
    @Bind(R.id.delete_button)
    Button mDeleteBtn;
    @Bind(R.id.save_button)
    Button mSaveBtn;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Nullable
    @Bind(R.id.bookDetailContainer)
    CardView mBookDetailContainer;
    @Bind(R.id.coordinator)
    CoordinatorLayout mBookCoordinatorLayout;

    @BindString(R.string.key_search_text)
    String mKeySearchText;

    @BindString(R.string.input_hint)
    String mSearchTextHint;

    private SearchView searchView = null;
    private String mTextSearch;

    private BroadcastReceiver messageReceiver;
    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);
        setToolbar(true, true);
        getSupportActionBar().setTitle(getTitle());

        if (savedInstanceState != null) {
            mTextSearch = savedInstanceState.getString(mKeySearchText);
            Log.d(TAG, "mTextSearch: " + mTextSearch);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(mKeySearchText, mTextSearch);
    }

    @Override
    public void onStart() {
        super.onStart();
        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, filter);
    }


    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
    }

    @OnClick(R.id.save_button)
    public void onSaveBook(View view) {
        if (searchView != null) {
            searchView.setQuery("", false);
        }
    }

    @OnClick(R.id.save_button)
    public void onDeleteBook(View view) {
        Intent bookIntent = new Intent(this, BookService.class);
        bookIntent.putExtra(BookService.EAN, mTextSearch);
        bookIntent.setAction(BookService.DELETE_BOOK);
        this.startService(bookIntent);
        clearFields();
    }

    @NonNull
    private String checkEan(String eanStr) {
        if (eanStr.length() == 10 && !eanStr.startsWith("978")) {
            eanStr = "978" + eanStr;
        }
        return eanStr;
    }

    private void restartLoader() {
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mTextSearch == null || mTextSearch.length() == 0) {
            return null;
        }
        String eanStr = mTextSearch.toString();
        eanStr = checkEan(eanStr);

        return new CursorLoader(
                this,
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(eanStr)),
                null,
                null,
                null,
                null
        );
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

        if (TextUtils.isEmpty(authorsString)) {
            String[] authorsArr = authorsString.split(",");
            mAuthors.setLines(authorsArr.length);
            mAuthors.setText(authorsString.replace(",", "\n"));
        }

        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));

        mBookCover.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .crossFade()
                .into(mBookCover);

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        mCategories.setText(categories);
    }

    private void showDetailView(boolean showView) {
        mBookDetailContainer.setVisibility(showView ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu() returned: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_book, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            searchView.setOnQueryTextListener(this);

            if (mTextSearch != null) {
                Log.d(TAG, "onCreateOptionsMenu() called with: " + "mTextSearch = [" + mTextSearch + "]");
                searchView.setQuery(mTextSearch, false);
            }
            searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
            searchView.setQueryHint(mSearchTextHint);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called with: " + "item = [" + item + "]");
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_scan_book:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
                break;
        }
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], intent = [" + intent + "]");
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult: scanResult:" + scanResult);
        if (scanResult != null) {
            mTextSearch = scanResult.getContents();
            startSearchService(mTextSearch);
            Log.d(TAG, "onActivityResult() called with: EAN found: " + mTextSearch);
        }
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
        if (TextUtils.isEmpty(query)) {
            return false;
        }

        String ean = query.toString();

        if (ean.length() > TRASH_HOLD_INPUT) return false;

        //catch isbn10 numbers
        ean = checkEan(ean);
        if (ean.length() < TRASH_HOLD_INPUT) {
//            clearFields();
            return true;
        }
        //Once we have an ISBN, start a book intent
        Intent bookIntent = new Intent(this, BookService.class);
        bookIntent.putExtra(BookService.EAN, ean);
        bookIntent.setAction(BookService.FETCH_BOOK);
        this.startService(bookIntent);
        mProgressBar.setVisibility(View.VISIBLE);
        restartLoader();
        return false;
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive() called with: " + "context = [" + context + "], intent = [" + intent + "]");
            if (intent.hasExtra(MESSAGE_KEY)) {
                @ServiceResponse int code = intent.getIntExtra(MESSAGE_KEY, BookService.NO_BOOK);
                String message = null;
                switch (code) {
                    case BookService.FOUND_OK:
                        restartLoader();
                        message = getString(R.string.book_found);
                        break;
                    case BookService.NO_CONNECTION:
                        message = getString(R.string.no_internet_connection);
                        break;
                    case BookService.NO_BOOK:
                        message = getString(R.string.not_found);
                        break;
                }
                Toast.makeText(AddBookActivity.this, message, Toast.LENGTH_SHORT).show();
                Snackbar.make(mBookCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private void clearFields() {
        mTextSearch = "";
        if (searchView != null) {
            searchView.setQuery("", true);
        }
    }
}
