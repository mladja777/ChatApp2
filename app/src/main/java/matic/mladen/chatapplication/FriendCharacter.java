package matic.mladen.chatapplication;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by Mladen on 3/30/2018.
 */

public class FriendCharacter {
    private String friend_name;

    public FriendCharacter(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String name) {
        this.friend_name = name;
    }
}
