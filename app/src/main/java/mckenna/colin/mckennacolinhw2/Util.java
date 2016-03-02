package mckenna.colin.mckennacolinhw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Calendar;

/**
 * Based on Util Class created by Scott Stanchfield
 */
public class Util {

    public static Contact findContact(Context context, long id) {
        // set up a URI that represents the specific item
        Uri uri = Uri.withAppendedPath(ContactProvider.CONTENT_URI, "" + id);

        // set up a projection to show which columns we want to retrieve
        String[] projection = {
                ContactProvider.ID,
                ContactProvider.FIRST_NAME,
                ContactProvider.LAST_NAME,
                ContactProvider.BIRTHDAY,
                ContactProvider.HOME_PHONE,
                ContactProvider.MOBILE_PHONE,
                ContactProvider.WORK_PHONE,
                ContactProvider.EMAIL,
        };

        // declare a cursor outside the try so we can close it in a finally
        Cursor cursor = null;
        try {
            // ask the content resolver to find the data for the URI
            cursor = context.getContentResolver().
                    query(uri, projection, null, null, null);

            // if nothing found, return null
            if (cursor == null || !cursor.moveToFirst())
                return null;

            // otherwise return the located item
            Calendar bday = Calendar.getInstance();
            bday.setTimeInMillis(cursor.getLong(3));

            return new Contact(cursor.getLong(0), cursor.getString(1), cursor.getString(2), bday, cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        } finally {
            // BE SURE TO CLOSE THE CURSOR!!!
            if (cursor != null)
                cursor.close();
        }
    }

    // helper method to update or insert an item
    public static void updateContact(Context context, Contact contact) {
        // create a URI that represents the item
        Uri uri = Uri.withAppendedPath(ContactProvider.CONTENT_URI, "" + contact.getId());

        // set up the data to store or update
        ContentValues values = new ContentValues();
        values.put(ContactProvider.FIRST_NAME, contact.getFirstName());
        values.put(ContactProvider.LAST_NAME, contact.getLastName());
        values.put(ContactProvider.BIRTHDAY, contact.getBirthday().getTimeInMillis());
        values.put(ContactProvider.HOME_PHONE, contact.getHomePhone());
        values.put(ContactProvider.MOBILE_PHONE, contact.getMobilePhone());
        values.put(ContactProvider.WORK_PHONE, contact.getWorkPhone());
        values.put(ContactProvider.EMAIL, contact.getEmail());

        // if the item didn't yet have an id, insert it and set the id on the object
        if (contact.getId() == -1) {
            Uri insertedUri = context.getContentResolver().insert(uri, values);
            String idString = insertedUri.getLastPathSegment();
            long id = Long.parseLong(idString);
            contact.setId(id);

            // otherwise, update the item with that id
        } else {
            context.getContentResolver().update(uri, values, ContactProvider.ID + "=" + contact.getId(), null);
        }
    }
}


