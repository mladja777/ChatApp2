package matic.mladen.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MessageActivity extends AppCompatActivity {
    private Button   message_activity_log_out;
    private Button   message_activity_refresh;
    private Button   message_activity_send_button;

    private TextView friend_to_send_to;

    private EditText message_activity_message_text;

    private MessageAdapter adapter;

    private ContactDatabaseHelper mContactDatabaseHelper;
    private MessageDatabaseHelper mMessageDatabaseHelper;

    private String mMe;
    private String mFriend;

    private HttpHelper mHttpHelper;
    private Handler mHandler;
    private String mSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mHttpHelper = new HttpHelper();
        mHandler = new Handler();

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
        message_activity_refresh      = findViewById(R.id.message_activity_refresh);
        message_activity_send_button  = findViewById(R.id.message_activity_send_button);

        message_activity_message_text = findViewById(R.id.message_activity_message_text);

        friend_to_send_to = findViewById(R.id.name_to_send_to);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        String connected_to = bundle.getString(String.valueOf(R.string.connected_to));
        mFriend = connected_to;
        /*
        mFriend = mContactDatabaseHelper.readContact(connected_to);
        */
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("users", "ERROR");
        mMe = username;
        /*
        Contact[] contacts = mContactDatabaseHelper.readContacts("");
        for (Contact tmp : contacts) {
            if(tmp.getUsername().equals(username)) {
                mMe = tmp;
            }
        }
        */
        //friend_to_send_to.setText(mFriend.getFirstName());

        ListView list = findViewById(R.id.list_of_messages);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                /*PASS*/
                final matic.mladen.chatapplication.Message message = (matic.mladen.chatapplication.Message) adapter.getItem(i);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            if(message.getSenderUname().equals(mMe)) {
                                jsonObject.put("receiver", mFriend);
                                jsonObject.put("sender", mMe);
                            } else {
                                jsonObject.put("receiver", mMe);
                                jsonObject.put("sender", mFriend);
                            }
                            jsonObject.put("data", JNIxor.encryption(message.getMessage()));
                            final HttpHelper.RetrunClass response = mHttpHelper.httpDelete("http://18.205.194.168:80/message", jsonObject, mSessionId);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.remove(i);
                                    if(response.mResponseCode == 200) {
                                        Toast.makeText(getApplicationContext(), "MESSAGE DELETED!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "MESSAGE DELETE PROCESS WENT WRONG!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();

                return false;
            }
        });

        message_activity_log_out.setOnClickListener(new View.OnClickListener() {
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
                                    Intent log_out_intent = new Intent(getApplicationContext(), MainActivity.class);
                                    log_out_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(log_out_intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        message_activity_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("receiver", mFriend);
                            jsonObject.put("data", JNIxor.encryption(message_activity_message_text.getText().toString()));
                            final HttpHelper.RetrunClass response = mHttpHelper.postJSONObjectFromURL("http://18.205.194.168:80/message", jsonObject, mSessionId);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.mResponseCode == 200) {
                                        adapter.addMessage(new matic.mladen.chatapplication.Message(message_activity_message_text.getText().toString(), false, mFriend));
                                        adapter.notifyDataSetChanged();
                                        message_activity_message_text.setText("");
                                        Toast.makeText(getApplicationContext(), "MESSAGE SENT!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "FATAL ERROR!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        adapter.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        mSessionId = sharedPreferences.getString("sessionId", "ERROR");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = mHttpHelper.getJSONArrayFromURL("http://18.205.194.168:80/message/" + mFriend, mSessionId);
                    if(jsonArray == null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "FATAL ERROR!", Toast.LENGTH_LONG).show();
                            }
                        });
                        Intent back_to_login = new Intent(getApplicationContext(), MainActivity.class);
                        back_to_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(back_to_login);
                    } else {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String sender = jsonObject.getString("sender");
                            String data = jsonObject.getString("data");
                            adapter.addMessage(new matic.mladen.chatapplication.Message(JNIxor.decryption(data), sender.equals(mMe), sender));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

}

/*              P03 SEND BUTTON LISTENER - WORKING STATE
Toast.makeText(MessageActivity.this, "Message has been sent.", Toast.LENGTH_LONG).show();

                matic.mladen.chatapplication.Message message = new matic.mladen.chatapplication.Message(message_activity_message_text.getText().toString(), 0, mMe.getContactId(), mFriend.getContactId());

                mMessageDatabaseHelper.insertMessage(message);
                adapter.addMessage(message);
                adapter.notifyDataSetChanged();

                message_activity_message_text.setText("");
 */

/*              P03 ITEM LONG CLICK - LAST WORKING STATE
adapter.remove(i);
                adapter.notifyDataSetChanged();
                return true;
 */