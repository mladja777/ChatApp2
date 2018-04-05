package matic.mladen.chatapplication;

import android.content.Intent;
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

public class ContactsActivity extends AppCompatActivity {
    private Button   contacts_activity_log_out;

    private FriendCharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contacts_activity_log_out   = findViewById(R.id.contacts_activity_log_out);

        adapter = new FriendCharacterAdapter(this);

        adapter.addCharacter(new FriendCharacter("Filip Visnjic"));
        adapter.addCharacter(new FriendCharacter("Stanoje"));
        adapter.addCharacter(new FriendCharacter("Jevrosima"));
        adapter.addCharacter(new FriendCharacter("Teofil"));
        adapter.addCharacter(new FriendCharacter("Lavrentije"));
        adapter.addCharacter(new FriendCharacter("Jelisaveta"));
        adapter.addCharacter(new FriendCharacter("Grigorje"));

        ListView list = findViewById(R.id.friend_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnFriendClickListener());

        contacts_activity_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_out_intent = new Intent(ContactsActivity.this, MainActivity.class);
                startActivity(log_out_intent);
                finish();
            }
        });
    }

    private class OnFriendClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent contacts_activity_message_activity_intent = new Intent(ContactsActivity.this, MessageActivity.class);
            FriendCharacter fc = (FriendCharacter) adapter.getItem(position);
            contacts_activity_message_activity_intent.putExtra(String.valueOf(R.string.connected_to), fc.getFriend_name());
            startActivity(contacts_activity_message_activity_intent);
        }
    }
}
