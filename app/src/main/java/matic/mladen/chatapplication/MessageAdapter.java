package matic.mladen.chatapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mladen on 4/5/2018.
 */

public class MessageAdapter extends BaseAdapter {
    private Context message_context;
    private ArrayList<Message> messages;

    public int counter = 0;

    public MessageAdapter(Context context) {
        this.message_context = context;
        messages = new ArrayList<Message>();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    public void remove(int position) {
        messages.remove(position);
    }

    @Override
    public Object getItem(int i) {
        Object object = null;
        try {
            object = messages.get(i);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"RtlHardcoded", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View message_view = view;

        if(message_view == null) {
            LayoutInflater inflater = (LayoutInflater) message_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            message_view = inflater.inflate(R.layout.message_item, null);
            MessageHolder messageHolder = new MessageHolder();
            messageHolder.message = view.findViewById(R.id.message_text);

            view.setTag(messageHolder);
        }

        Message message = (Message) getItem(i);
        MessageHolder messageHolder = (MessageHolder) view.getTag();
        messageHolder.message.setText(message.getMessage());

        if(counter%2 == 0) {
            messageHolder.message.setBackgroundColor(Color.WHITE);
            messageHolder.message.setGravity(Gravity.RIGHT);
            counter++;
        }
        else {
            messageHolder.message.setBackgroundColor(Color.GRAY);
            messageHolder.message.setGravity(Gravity.LEFT);
            counter++;
        }

        return message_view;
    }

    public class MessageHolder {
        public TextView message = null;
    }
}
