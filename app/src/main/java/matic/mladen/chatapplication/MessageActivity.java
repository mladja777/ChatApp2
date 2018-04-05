package matic.mladen.chatapplication;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        adapter = new MessageAdapter(this);
/*
        adapter.addMessage(new matic.mladen.chatapplication.Message("Hello!"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Bonjour!"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("How are You?"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Ca va bien, et vous?"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Perfect! Bye!"));
        adapter.addMessage(new matic.mladen.chatapplication.Message("Au revoir!"));
*/
        message_activity_log_out      = findViewById(R.id.message_activity_log_out);
        message_activity_send_button  = findViewById(R.id.message_activity_send_button);

        message_activity_message_text = findViewById(R.id.message_activity_message_text);

        friend_to_send_to = findViewById(R.id.name_to_send_to);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        String connected_to = bundle.getString(String.valueOf(R.string.connected_to));
        friend_to_send_to.setText(connected_to);

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
                startActivity(log_out_intent);
                finish();
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
                Toast.makeText(MessageActivity.this, "Message is sent.", Toast.LENGTH_LONG).show();

                message_activity_message_text.setText("");
            }
        });
    }
}
