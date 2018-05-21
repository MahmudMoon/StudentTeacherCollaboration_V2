package moonc.example.com.studentteachercollaboration_v2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Students_detail> mArrayList;
    TextView name_,email_id,phone_num_;


    public Adapter(Context mContext, ArrayList<Students_detail> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        view = layoutInflater.inflate(R.layout.adapter,null);
        name_ = (TextView)view.findViewById(R.id.tv_name);
        email_id = (TextView)view.findViewById(R.id.tv_email);
        phone_num_ = (TextView)view.findViewById(R.id.tv_phone);

        name_.setText(mArrayList.get(position).getName());
        email_id.setText(mArrayList.get(position).getEmail_id_number());
        phone_num_.setText(mArrayList.get(position).getPhone_number());

        return view;
    }
}
