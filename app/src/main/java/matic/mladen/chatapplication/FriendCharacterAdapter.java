package matic.mladen.chatapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mladen on 3/30/2018.
 */

public class FriendCharacterAdapter extends BaseAdapter {
    private Context friend_context;
    private ArrayList<FriendCharacter> friend_characters;

    public FriendCharacterAdapter(Context context) {
        this.friend_context = context;
        friend_characters = new ArrayList<FriendCharacter>();
    }

    public void addCharacter(FriendCharacter friend_character) {
        friend_characters.add(friend_character);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friend_characters.size();
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        try {
            item = friend_characters.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convert_view, ViewGroup parent) {
        View view = convert_view;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) friend_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friend_item, null);
            ViewHolder holder = new ViewHolder();
            holder.symbol = view.findViewById(R.id.friend_symbol);
            holder.name = view.findViewById(R.id.friend_name);
            Random random = new Random();
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            holder.image = view.findViewById(R.id.friend_image);
            holder.image.setBackgroundColor(color);
            view.setTag(holder);
        }

        FriendCharacter friend_character = (FriendCharacter) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(friend_character.getFriend_name());
        String[] string_parts = friend_character.getFriend_name().split("");
        holder.image.setText(string_parts[1]);

        return view;
    }

    private class ViewHolder {
        public TextView image = null;
        public ImageView symbol = null;
        public TextView name = null;
    }
}
