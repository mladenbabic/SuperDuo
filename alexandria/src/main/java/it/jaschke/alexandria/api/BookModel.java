package it.jaschke.alexandria.api;

import android.database.Cursor;

import it.jaschke.alexandria.data.AlexandriaContract;

public class BookModel {

    private String id;
    private String imageUrl;
    private String title;
    private String description;

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BookModel fromCursor(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID));
        imageUrl = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        title = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        description = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        return this;
    }


}