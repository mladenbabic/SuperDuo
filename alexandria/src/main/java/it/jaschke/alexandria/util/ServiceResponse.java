package it.jaschke.alexandria.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import it.jaschke.alexandria.services.BookService;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/27/2015.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({BookService.FOUND_BOOK_OK, BookService.DELETE_BOOK_OK, BookService.ADD_BOOK_OK, BookService.NO_CONNECTION, BookService.NO_BOOK})
public @interface ServiceResponse {
}
