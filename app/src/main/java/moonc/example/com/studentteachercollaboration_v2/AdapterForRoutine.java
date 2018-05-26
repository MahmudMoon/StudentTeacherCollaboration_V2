package moonc.example.com.studentteachercollaboration_v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterForRoutine extends BaseAdapter {

    Context mContext;
    ArrayList<Object_Created_for_class> mArrayList;
    LayoutInflater inflater;
    TextView sub,course,starttime,endtime,room;

    public AdapterForRoutine(Context mContext, ArrayList<Object_Created_for_class> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        view = inflater.inflate(R.layout.adapter_for_routine,null);
        sub = (TextView)view.findViewById(R.id.subject);
        course = (TextView)view.findViewById(R.id.courseCode);
        starttime = (TextView)view.findViewById(R.id.startTime);
        endtime = (TextView)view.findViewById(R.id.endTime);
        room = (TextView)view.findViewById(R.id.room);


        sub.setText(mArrayList.get(position).getSubject());
        course.setText(mArrayList.get(position).getCourse_code());
        starttime.setText(mArrayList.get(position).getStart());
        endtime.setText(mArrayList.get(position).getEnd());
        room.setText(mArrayList.get(position).getRoom());

        return view;
    }
}
