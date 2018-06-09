package test.example.com.studentteachercollaboration.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import test.example.com.studentteachercollaboration.Models.CustomMessage;
import test.example.com.studentteachercollaboration.R;

public class ViewMessageAdapter extends BaseAdapter {
    private TextView fromTextView;
    private TextView messageTextView;
    private TextView timeTextView;
    private Context context;
    private ArrayList<CustomMessage> messageList;
    private LayoutInflater inflater;

    public ViewMessageAdapter(Context context, ArrayList<CustomMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = inflater.inflate(R.layout.view_message_list_view, null);
        fromTextView = (TextView) view.findViewById(R.id.from_text_view);
        timeTextView = (TextView) view.findViewById(R.id.time_text_view);
        messageTextView = (TextView) view.findViewById(R.id.message_text_view);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm a", Locale.ENGLISH);
        String time = df.format(messageList.get(position).getTime());

        fromTextView.setText(messageList.get(position).getFrom());
        timeTextView.setText(time);
        messageTextView.setText(messageList.get(position).getMessageBody());
        return view;
    }
}
