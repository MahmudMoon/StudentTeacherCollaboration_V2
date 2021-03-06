package test.example.com.studentteachercollaboration.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import test.example.com.studentteachercollaboration.Models.Student;
import test.example.com.studentteachercollaboration.R;

public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Student> mArrayList;
    private TextView name_, email_id, phone_num_;
    private TextView roleTextView;

    public UserAdapter(Context mContext, ArrayList<Student> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = layoutInflater.inflate(R.layout.show_user_list_view, null);
        name_ = (TextView) view.findViewById(R.id.tv_name);
        email_id = (TextView) view.findViewById(R.id.tv_email);
        phone_num_ = (TextView) view.findViewById(R.id.tv_phone);
        roleTextView = (TextView) view.findViewById(R.id.role_text_view);

        name_.setText(mArrayList.get(position).getName());
        email_id.setText(mArrayList.get(position).getEmail_id_number());
        phone_num_.setText(mArrayList.get(position).getPhone_number());
        roleTextView.setText(mArrayList.get(position).getRole());
        return view;
    }
}
