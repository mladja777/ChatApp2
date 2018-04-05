package matic.mladen.chatapplication;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements TextWatcher{

    private Button       register_activity_button;

    private EditText     register_activity_username;
    private EditText     register_activity_password;
    private EditText     register_activity_first_name;
    private EditText     register_activity_last_name;
    private EditText     register_activity_email;

    private Spinner      register_activity_spinner;

    private CheckBox     register_activity_checkbox;

    private CalendarView register_activity_calendar;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_activity_button     = findViewById(R.id.register_activity_register_button);

        register_activity_username   = findViewById(R.id.register_activity_username);
        register_activity_password   = findViewById(R.id.register_activity_password);
        register_activity_first_name = findViewById(R.id.register_activity_first_name);
        register_activity_last_name  = findViewById(R.id.register_activity_last_name);
        register_activity_email      = findViewById(R.id.register_activity_email);

        register_activity_spinner    = findViewById(R.id.register_activity_spinner);

        register_activity_checkbox   = findViewById(R.id.register_activity_checkbox);

        register_activity_calendar   = findViewById(R.id.register_activity_calendar);

        Calendar support_calendar = Calendar.getInstance();
        register_activity_calendar.setMaxDate(support_calendar.getTimeInMillis());

        register_activity_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(register_activity_username.getText().length() < 1 || register_activity_email.getText().length() < 1 || register_activity_password.getText().length() < 1)) {
                    register_activity_button.setEnabled(true);
                } else {
                    register_activity_button.setEnabled(false);
                }
            }
        });

        register_activity_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!(register_activity_username.getText().length() < 1 || register_activity_email.getText().length() < 1 || register_activity_password.getText().length() < 1))
                {
                    register_activity_button.setEnabled(true);
                }
                else
                {
                    register_activity_button.setEnabled(false);
                }

                String ePattern = getString(R.string.email_pattern);
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                java.util.regex.Matcher m = p.matcher(register_activity_email.getText());
                if(m.matches())
                {
                    register_activity_button.setEnabled(true);
                }
                else
                {
                    register_activity_button.setEnabled(false);
                }
            }
        });

        register_activity_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!(register_activity_username.getText().length() < 1 || register_activity_email.getText().length() < 1 || register_activity_password.getText().length() < 6))
                {
                    register_activity_button.setEnabled(true);
                }
                else
                {
                    register_activity_button.setEnabled(false);
                }
            }
        });

        register_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_activity_contacts_activity_intent = new Intent(RegisterActivity.this, ContactsActivity.class);
                startActivity(register_activity_contacts_activity_intent);
                finish();
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
