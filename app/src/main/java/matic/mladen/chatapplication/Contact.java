package matic.mladen.chatapplication;

/**
 * Created by Mladen on 4/22/2018.
 */

public class Contact {
    //private int mContactId;
    private String mUsername;
    private String mPassword;
    private String mEmail;
    //private String mFirstName;
    //private String mLastName;

    public Contact(/*int contactId, */String username, String password, String email/*String firstName, String lastName*/) {
        //this.mContactId = contactId;
        this.mUsername = username;
        this.mPassword = password;
        this.mEmail = email;
        //this.mFirstName = firstName;
        //this.mLastName = lastName;
    }

    public Contact(String username) {
        this.mUsername = username;
    }

    /*
    public String getFirstName() {
        return this.mFirstName;
    }

    public String getLastName() {
        return this.mLastName;
    }
    */
    public String getUsername() {
        return this.mUsername;
    }
    /*
    public int getContactId() {
        return this.mContactId;
    }
    */
    public String getPassword() {
        return this.mPassword;
    }

    public String getEmail() {
        return this.mEmail;
    }
}
