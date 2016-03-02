package mckenna.colin.mckennacolinhw2;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Based on ToDoProvider Class created by Scott Stanchfield
 */
public class ContactProvider extends ContentProvider {
   //DB Constants

    public static final String CONTACT_TABLE = "contact";
    public static final String ID = "_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String BIRTHDAY = "birthday";
    public static final String HOME_PHONE = "home_phone";
    public static final String MOBILE_PHONE = "mobile_phone";
    public static final String WORK_PHONE = "work_phone";
    public static final String EMAIL = "email";
    public static final int DB_VERSION = 1;

    // URI Constants
    public static final int CONTACTS = 1;
    public static final int CONTACT_ITEM = 2;
    public static final String AUTHORITY = "mckenna.colin.mckennacolinhw2";
    public static final String BASE_PATH = "contact";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.mckenna.colin.contact";
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.mckenna.colin.contact";
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, CONTACTS);

        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", CONTACT_ITEM);

    }

    private static class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context) {
            super(context, "CONTACT", null, DB_VERSION);
        }

        // create the database if it doesn't exist
        @Override public void onCreate(SQLiteDatabase db) {
            try {
                db.beginTransaction();
                // always keep version 1 creation
                String sql = String.format(
                        "create table %s (%s integer primary key autoincrement, %s text, %s text, %s text, %s text,%s text,%s text,%s text)",
                        CONTACT_TABLE, ID, FIRST_NAME, LAST_NAME, BIRTHDAY, HOME_PHONE, MOBILE_PHONE, WORK_PHONE, EMAIL);
                db.execSQL(sql);
                onUpgrade(db, 1, DB_VERSION);  // run the upgrades starting from version 1
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        // no db upgrades were done during the programming of HW2, so nothing to put here
        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        db = new OpenHelper(getContext()).getWritableDatabase();
        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch(URI_MATCHER.match(uri)){
            case CONTACTS: {
                Cursor c = db.query(CONTACT_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }

            case CONTACT_ITEM: {
                String id = uri.getLastPathSegment();
                Cursor c = db.query(CONTACT_TABLE,
                        projection,
                        ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }

            default:
                return null;

        }
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case CONTACTS:
                return CONTENT_DIR_TYPE;
            case CONTACT_ITEM:
                return CONTENT_ITEM_TYPE;
            default:
                return null; // unknown
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(CONTACT_TABLE, null, values);

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.withAppendedPath(CONTENT_URI, ""+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numDeleted = 0;
        switch (URI_MATCHER.match(uri)) {
            // if the URI looks like content://com.javadude.todo/todo (all todos)
            case CONTACTS: {
                numDeleted = db.delete(CONTACT_TABLE, selection, selectionArgs);
                break;
            }

            // if the uri looks like content://com.javadude.todo/todo/42 (single todo item w/id)
            case CONTACT_ITEM: {
                // get specific item
                String id = uri.getLastPathSegment(); // what's the id?
                numDeleted = db.delete(CONTACT_TABLE, ID + "=?", new String[] {id});
                break;
            }
        }

        // send a notification that the data has changed
        // this will notify cursors that were registered for the container URI
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int numUpdated = 0;
        switch (URI_MATCHER.match(uri)) {
            // if the URI looks like content://com.javadude.todo/todo (all todos)
            case CONTACTS: {
                numUpdated = db.update(CONTACT_TABLE, values, selection, selectionArgs);
                break;
            }

            // if the uri looks like content://com.javadude.todo/todo/42 (single todo item w/id)
            case CONTACT_ITEM: {
                // get specific item
                String id = uri.getLastPathSegment(); // what's the id?
                numUpdated = db.update(CONTACT_TABLE, values, ID + "=?", new String[]{id});
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
        }

        // send a notification that the data has changed
        // this will notify cursors that were registered for the container URI
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return numUpdated;
    }
}
