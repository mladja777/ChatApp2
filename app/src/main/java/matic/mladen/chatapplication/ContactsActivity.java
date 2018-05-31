package matic.mladen.chatapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

public class ContactsActivity extends AppCompatActivity implements ServiceConnection {
    private Button   contacts_activity_log_out;
    private Button   contacts_activity_refresh;

    private HttpHelper mHttpHelper;
    private Handler mHandler;

    private FriendCharacterAdapter adapter;
    private ContactDatabaseHelper mContactDatabaseHelper;
    private String mUsername;
    private String mSessionId;

    private NotificationBinder mNotificationBinder = null;
    protected NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mHttpHelper = new HttpHelper();
        mHandler = new Handler();

        contacts_activity_log_out   = findViewById(R.id.contacts_activity_log_out);
        contacts_activity_refresh   = findViewById(R.id.contacts_activity_refresh);

        mContactDatabaseHelper = new ContactDatabaseHelper(this);
        adapter = new FriendCharacterAdapter(this);
        /*
        Contact[] contacts = mContactDatabaseHelper.readContacts("");
        */
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString("users", "ERROR");
        mSessionId = sharedPreferences.getString("sessionId", "ERROR");

        if(mSessionId == null) {
            Toast.makeText(getApplicationContext(), "FATAL ERROR!", Toast.LENGTH_LONG).show();
            Intent log_out_intent = new Intent(ContactsActivity.this, MainActivity.class);
            log_out_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(log_out_intent);
        }
        /*
        for(Contact contact : contacts) {
            if(!contact.getUsername().equals(mUsername)) {
                adapter.addCharacter(contact);
            }
        }
        */

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
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.mResponseCode == 200) {
                                        Toast.makeText(getApplicationContext(), "USER LOGGED OUT!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "FATAL ERROR!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Intent log_out_intent = new Intent(ContactsActivity.this, MainActivity.class);
                            log_out_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(log_out_intent);
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

        Intent intent = new Intent(this, NotificationService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mNotificationBinder = (NotificationBinder) NotificationBinder.Stub.asInterface(iBinder);

        mNotificationBinder.setCallback(new YetAnotherCallback());
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mNotificationBinder = null;
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
                }
                Log.i("MSG", "Dosao do kraja treda.");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
        Log.i("MSG", "Dosao do notify.");
    }

    private class YetAnotherCallback extends ICallbackExample.Stub {
        private boolean mResponse;
        private NotificationManagerCompat mNotificationManagerCompat = NotificationManagerCompat.from(ContactsActivity.this);

        @Override
        public void onCallbackCall() throws RemoteException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mResponse = mHttpHelper.getServiceFromURL("http://18.205.194.168:80/getfromservice", mSessionId);
                        if (mResponse) {
                            Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                            mBuilder = new NotificationCompat.Builder(getApplicationContext());
                            mBuilder.setSmallIcon(R.drawable.pepe);
                            mBuilder.setContentTitle("Alert: ");
                            mBuilder.setContentText("New message!");
                            mBuilder.setContentIntent(pendingIntent);
                            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            mBuilder.setAutoCancel(true);
                            mNotificationManagerCompat.notify(777, mBuilder.build());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

/*          V03 LOGOUT LISTENER - WORKING STATE
                 Intent log_out_intent = new Intent(ContactsActivity.this, MainActivity.class);
                log_out_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log_out_intent);
* */