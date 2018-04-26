package matic.mladen.chatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mladen on 4/23/2018.
 */

public class ContactDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "students.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "contact";
    public static final String COLUMN_CONTACT_ID = "ContactId";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";

    private SQLiteDatabase mDatabase = null;

    public ContactDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertContact(Contact contact) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COLUMN_CONTACT_ID, contact.getContactId());
        values.put(COLUMN_USERNAME, contact.getUsername());
        values.put(COLUMN_FIRST_NAME, contact.getFirstName());
        values.put(COLUMN_LAST_NAME, contact.getLastName());

        database.insert(TABLE_NAME, null, values);
        close();
    }

    public Contact[] readContacts(String uname) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, "not " + COLUMN_USERNAME + "=?", new String[] {uname}, null, null, null);

        if(cursor.getCount() <= 0) {
            return null;
        }

        Contact[] contacts = new Contact[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContact(cursor);
        }

        close();
        return contacts;
    }

    public Contact readContact(String username) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_USERNAME + "=?",
                new String[] {username}, null, null, null);
        cursor.moveToFirst();
        Contact contact = createContact(cursor);

        close();
        return contact;
    }

    public void deleteContact(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_CONTACT_ID + "=?", new String[] {String.valueOf(id)});
        close();
    }

    private Contact createContact(Cursor cursor) {
        try {
            int contactId = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_ID));
            String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
            String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));

            return new Contact(contactId, username, firstName, lastName);
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
