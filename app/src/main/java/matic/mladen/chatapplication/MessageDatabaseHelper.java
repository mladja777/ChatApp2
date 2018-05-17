package matic.mladen.chatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mladen on 4/23/2018.
 */

public class MessageDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "messages.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "message";
    public static final String COLUMN_MESSAGE_ID = "MessageId";
    public static final String COLUMN_SENDER_ID = "SenderId";
    public static final String COLUMN_RECEIVER_ID = "ReceiverId";
    public static final String COLUMN_MESSAGE = "Message";

    private SQLiteDatabase mDatabase = null;

    public MessageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SENDER_ID + " INT, " +
                COLUMN_RECEIVER_ID + " INT, " + COLUMN_MESSAGE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertMessage(Message message) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COLUMN_MESSAGE_ID, message.getMessageId());
        /*
        values.put(COLUMN_SENDER_ID, message.getSenderId());
        values.put(COLUMN_RECEIVER_ID, message.getReceiverId());
        values.put(COLUMN_MESSAGE, message.getMessage());
        */
        database.insert(TABLE_NAME, null, values);
        close();
    }

    public Message[] readMessages(int me, int friend) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, "(" + COLUMN_RECEIVER_ID + "=? and " + COLUMN_SENDER_ID + "=?) or " +
                "(" + COLUMN_SENDER_ID + "=? and " + COLUMN_RECEIVER_ID + "=?)",
                new String[] {String.valueOf(friend), String.valueOf(me), String.valueOf(friend), String.valueOf(me)},
                null, null, null);

        if(cursor.getCount() <= 0) {
            return null;
        }

        Message[] messages = new Message[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        close();
        return messages;
    }

    public Message readMessage(int id) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_MESSAGE_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        Message message = createMessage(cursor);

        close();
        return message;
    }

    public void deleteMessage(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_MESSAGE_ID + "=?", new String[] {String.valueOf(id)});
        close();
    }

    private Message createMessage(Cursor cursor) {
        int messageId = cursor.getInt(cursor.getColumnIndex(COLUMN_MESSAGE_ID));
        int senderId = cursor.getInt(cursor.getColumnIndex(COLUMN_SENDER_ID));
        int receiverId = cursor.getInt(cursor.getColumnIndex(COLUMN_RECEIVER_ID));
        String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));

        return /*new Message(message, messageId, senderId, receiverId)*/null;
    }
}
