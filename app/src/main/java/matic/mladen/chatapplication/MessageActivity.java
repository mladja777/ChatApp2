package matic.mladen.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {
    private Button   message_activity_log_out;
    private Button   message_activity_send_button;

    private TextView friend_to_send_to;

    private EditText message_activity_message_text;

    private MessageAdapter adapter;

    private ContactDatabaseHelper mContactDatabaseHelper;
    private MessageDatabaseHelper mMessageDatabaseHelper;

    private Contact mMe;
    private Contact mFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        adapter = new MessageAdapter(this);
        mContactDatabaseHelper = new ContactDatabaseHelper(this);
        mMessageDatabaseHelper = new MessageDatabaseHelper(this);

        /*adapter.addMessage(new matic.mladen.chatapplication.Message("Hello!"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Bonjour!"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("How are You?"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Ca va bien, et vous?"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Perfect! Bye!"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Au revoir!"));*/

        message_activity_log_out      = findViewById(R.id.message_activity_log_out);
        message_activity_send_button  = findViewById(R.id.message_activity_send_button);

        message_activity_message_text = findViewById(R.id.message_activity_message_text);

        friend_to_send_to = findViewById(R.id.name_to_send_to);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        String connected_to = bundle.getString(String.valueOf(R.string.connected_to));

        mFriend = mContactDatabaseHelper.readContact(connected_to);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("users", "ERROR");

        Contact[] contacts = mContactDatabaseHelper.readContacts("");
        for (Contact tmp : contacts) {
            if(tmp.getUsername().equals(username)) {
                mMe = tmp;
            }
        }

        friend_to_send_to.setText(mFriend.getFirstName());

        ListView list = findViewById(R.id.list_of_messages);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        message_activity_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_out_intent = new Intent(MessageActivity.this, MainActivity.class);
                log_out_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log_out_intent);
            }
        });

        message_activity_message_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(message_activity_message_text.getText().length() < 1))
                {
                    message_activity_send_button.setEnabled(true);
                }
                else
                {
                    message_activity_send_button.setEnabled(false);
                }
            }
        });

        message_activity_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MessageActivity.this, "Message has been sent.", Toast.LENGTH_LONG).show();

                matic.mladen.chatapplication.Message message = new matic.mladen.chatapplication.Message(message_activity_message_text.getText().toString(), 0, mMe.getContactId(), mFriend.getContactId());

                mMessageDatabaseHelper.insertMessage(message);
                adapter.addMessage(message);
                adapter.notifyDataSetChanged();

                message_activity_message_text.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        matic.mladen.chatapplication.Message[] messages = mMessageDatabaseHelper.readMessages(mMe.getContactId(), mFriend.getContactId());
        adapter.update(messages);
    }
}
