package matic.mladen.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;
import java.util.prefs.Preferences;

public class ContactsActivity extends AppCompatActivity {
    private Button   contacts_activity_log_out;

    private FriendCharacterAdapter adapter;
    private ContactDatabaseHelper mContactDatabaseHelper;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contacts_activity_log_out   = findViewById(R.id.contacts_activity_log_out);

        mContactDatabaseHelper = new ContactDatabaseHelper(this);
        adapter = new FriendCharacterAdapter(this);

        Contact[] contacts = mContactDatabaseHelper.readContacts("");

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString("users", "ERROR");

        for(Contact contact : contacts) {
            if(!contact.getUsername().equals(mUsername)) {
                adapter.addCharacter(contact);
            }
        }

        ListView list = findViewById(R.id.friend_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnFriendClickListener());

        contacts_activity_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_out_intent = new Intent(ContactsActivity.this, MainActivity.class);
                log_out_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log_out_intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Contact[] contacts = mContactDatabaseHelper.readContacts(mUsername);
        adapter.update(contacts);
    }

    private class OnFriendClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent contacts_activity_message_activity_intent = new Intent(ContactsActivity.this, MessageActivity.class);
            Contact fc = (Contact) adapter.getItem(position);
            contacts_activity_message_activity_intent.putExtra(String.valueOf(R.string.connected_to), fc.getUsername());
            startActivity(contacts_activity_message_activity_intent);
        }
    }
}
