package matic.mladen.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import static matic.mladen.chatapplication.R.color.black;
import static matic.mladen.chatapplication.R.drawable.branch;

public class MainActivity extends AppCompatActivity {

    private EditText username_edittext;
    private EditText password_edittext;

    private Button   login_button;
    private Button   register_button;

    private ContactDatabaseHelper mContactDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_edittext = (EditText) findViewById(R.id.edit_text_username);
        password_edittext = (EditText) findViewById(R.id.edit_text_password);

        login_button      = (Button)   findViewById(R.id.login_button);
        register_button   = (Button)   findViewById(R.id.register_button);

        mContactDatabaseHelper = new ContactDatabaseHelper(this);

        if(!(login_button.isEnabled())) {
            login_button.setVisibility(View.INVISIBLE);
        }

        username_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!(username_edittext.getText().length() < 1 || password_edittext.getText().length() < 1))
                {
                    login_button.setEnabled(true);
                }
                else
                {
                    login_button.setEnabled(false);
                }
            }
        });

        password_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!(username_edittext.getText().length() < 1 || password_edittext.getText().length() < 6))
                {
                    login_button.setEnabled(true);
                    login_button.setVisibility(View.VISIBLE);
                }
                else
                {
                    login_button.setEnabled(false);
                    login_button.setVisibility(View.INVISIBLE);
                }
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = mContactDatabaseHelper.readContact(username_edittext.getText().toString());
                if(contact != null) {
                    Intent contacts_activity_intent = new Intent(MainActivity.this, ContactsActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("users", contact.getUsername());
                    editor.apply();
                    username_edittext.setText("");
                    username_edittext.setHint("username");
                    password_edittext.setText("");
                    password_edittext.setHint("password");
                    startActivity(contacts_activity_intent);
                } else {
                    Toast.makeText(getApplicationContext(), "User is not registered!", Toast.LENGTH_LONG).show();
                }
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username_edittext.setText("");
                username_edittext.setHint("username");
                password_edittext.setText("");
                password_edittext.setHint("password");
                Intent register_activity_intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register_activity_intent);
            }
        });
    }
}
