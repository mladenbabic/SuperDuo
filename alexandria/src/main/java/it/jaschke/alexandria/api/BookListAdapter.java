package it.jaschke.alexandria.api;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;

/**
 * Created by saj on 11/01/15.
 */
public class BookListAdapter extends CursorRecyclerViewAdapter<BookListAdapter.ViewHolder> {

    final private Callback callback;

    public BookListAdapter(Context context, Cursor c, Callback callback) {
        super(context, c);
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        BookModel bookModel = new BookModel().fromCursor(cursor);
        viewHolder.bookTitle.setText(bookModel.getTitle());

        Glide.with(viewHolder.bookCover.getContext())
                .load(bookModel.getImageUrl())
                .placeholder(R.drawable.ic_launcher)
                .crossFade()
                .into(viewHolder.bookCover);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.fullBookCover)
        ImageView bookCover;
        @Bind(R.id.listBookTitle)
        TextView bookTitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            bookCover.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            getCursor().moveToPosition(adapterPosition);
            String ean = getCursor().getString(getCursor().getColumnIndex(AlexandriaContract.BookEntry._ID));
            callback.onItemSelected(ean);
        }
    }


}
