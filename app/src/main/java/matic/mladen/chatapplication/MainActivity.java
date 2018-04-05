package matic.mladen.chatapplication;

import android.content.Intent;
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

import static matic.mladen.chatapplication.R.color.black;
import static matic.mladen.chatapplication.R.drawable.branch;

public class MainActivity extends AppCompatActivity {

    private EditText username_edittext;
    private EditText password_edittext;

    private Button   login_button;
    private Button   register_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_edittext = (EditText) findViewById(R.id.edit_text_username);
        password_edittext = (EditText) findViewById(R.id.edit_text_password);

        login_button      = (Button)   findViewById(R.id.login_button);
        register_button   = (Button)   findViewById(R.id.register_button);

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
                Intent contacts_activity_intent = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(contacts_activity_intent);
                finish();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_activity_intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register_activity_intent);
                finish();
            }
        });
    }
}
