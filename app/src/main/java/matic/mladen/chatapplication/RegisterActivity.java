package matic.mladen.chatapplication;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private ContactDatabaseHelper mContactDatabaseHelper;

    private HttpHelper mHttpHelper;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mHttpHelper = new HttpHelper();

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
                    register_activity_button.setVisibility(View.VISIBLE);
                }
                else
                {
                    register_activity_button.setEnabled(false);
                    register_activity_button.setVisibility(View.INVISIBLE);
                }

                String ePattern = getString(R.string.email_pattern);
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                java.util.regex.Matcher m = p.matcher(register_activity_email.getText());
                if(m.matches())
                {
                    register_activity_button.setEnabled(true);
                    register_activity_button.setVisibility(View.VISIBLE);
                }
                else
                {
                    register_activity_button.setEnabled(false);
                    register_activity_button.setVisibility(View.INVISIBLE);
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
                    register_activity_button.setVisibility(View.VISIBLE);
                }
                else
                {
                    register_activity_button.setEnabled(false);
                    register_activity_button.setVisibility(View.INVISIBLE);
                }
            }
        });

        register_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", register_activity_username.getText().toString());
                            jsonObject.put("password", register_activity_password.getText().toString());
                            jsonObject.put("email", register_activity_email.getText().toString());

                            final HttpHelper.RetrunClass response = mHttpHelper.postJSONObjectFromURL("http://18.205.194.168:80/register", jsonObject);

                            if(response.mResponseCode == 200) {
                                Toast.makeText(getApplicationContext(), "REGISTERED!", Toast.LENGTH_LONG).show();
                                Intent intent_to_login = new Intent(getApplicationContext(), MainActivity.class);
                                intent_to_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent_to_login);
                            }  else if(response.mResponseCode == 409) {
                                Toast.makeText(getApplicationContext(), "USER EXISTS!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "INTERNAL ERROR!", Toast.LENGTH_LONG).show();
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

/*          P03 REGISTER BUTTON LISTENER - WORKING STATE
   if(mContactDatabaseHelper.readContact(register_activity_username.getText().toString()) == null) {
                    Contact[] tmp = mContactDatabaseHelper.readContacts("");
                    int id = 0;
                    if(tmp != null) {
                        for(Contact it : tmp) {
                            id++;
                        }
                    }
                    Contact contact = new Contact(id, register_activity_username.getText().toString(), register_activity_first_name.getText().toString(), register_activity_last_name.getText().toString());
                    mContactDatabaseHelper.insertContact(contact);
                    Intent register_activity_contacts_activity_intent = new Intent(RegisterActivity.this, MainActivity.class);
                    register_activity_contacts_activity_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(register_activity_contacts_activity_intent);
                } else {
                    Toast.makeText(getApplicationContext(), "User is not registered!", Toast.LENGTH_LONG).show();
                }
 */