package matic.mladen.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.prefs.Preferences;

public class ContactsActivity extends AppCompatActivity {
    private Button   contacts_activity_log_out;
    private Button   contacts_activity_refresh;

    private HttpHelper mHttpHelper;

    private FriendCharacterAdapter adapter;
    private ContactDatabaseHelper mContactDatabaseHelper;
    private String mUsername;
    private String mSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mHttpHelper = new HttpHelper();

        contacts_activity_log_out   = findViewById(R.id.contacts_activity_log_out);
        contacts_activity_refresh   = findViewById(R.id.contacts_activity_refresh);

        mContactDatabaseHelper = new ContactDatabaseHelper(this);
        adapter = new FriendCharacterAdapter(this);

        Contact[] contacts = mContactDatabaseHelper.readContacts("");

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString("users", "ERROR");
        mSessionId = sharedPreferences.getString("sessionId", "ERROR");

        if(mSessionId == null) {
            Toast.makeText(getApplicationContext(), "FATAL ERROR!", Toast.LENGTH_LONG).show();
            Intent log_out_intent = new Intent(ContactsActivity.this, MainActivity.class);
            log_out_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(log_out_intent);
        }

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final HttpHelper.RetrunClass response = mHttpHelper.postJSONObjectFromURL("http://18.205.194.168:80/logout", new JSONObject(), mSessionId);
                            if(response.mResponseCode == 200) {
                                Toast.makeText(getApplicationContext(), "USER LOGGED OUT!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "FATAL ERROR!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        contacts_activity_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
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

    private void refresh() {
        adapter.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = mHttpHelper.getJSONArrayFromURL("http://18.205.194.168:80/contacts", mSessionId);
                    if(jsonArray == null) {
                        Toast.makeText(getApplicationContext(), "FATAL ERROR!", Toast.LENGTH_LONG).show();
                        Intent refresh_intent = new Intent(getApplicationContext(), MainActivity.class);
                        refresh_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(refresh_intent);
                    } else {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String friends = jsonObject.getString("username");
                            Contact contact = new Contact(friends);
                            if(!friends.equals(mUsername)) {
                                adapter.addCharacter(contact);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    adapter.notifyDataSetChanged();
                }
            }
        }).start();
    }
}

/*          V03 LOGOUT LISTENER - WORKING STATE
                 Intent log_out_intent = new Intent(ContactsActivity.this, MainActivity.class);
                log_out_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log_out_intent);
* */