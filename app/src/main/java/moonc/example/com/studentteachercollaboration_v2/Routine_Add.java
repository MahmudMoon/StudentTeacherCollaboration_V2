package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import moonc.example.com.studentteachercollaboration_v2.Models.AcademicClass;

public class Routine_Add extends AppCompatActivity {

    Spinner spn_session,spn_day;
    EditText et_subject,et_course;
    EditText et_start,et_end;
    EditText et_room;
    Button btn_add_clicked;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String session_,day_,period_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine__add);


        Intent intent = getIntent();
        session_ = intent.getStringExtra("session");
        day_ = intent.getStringExtra("day");
        period_ = intent.getStringExtra("period");

        init_views();
        init_variables();
        init_functions();

        if(!TextUtils.isEmpty(session_) && !TextUtils.isEmpty(day_) && !TextUtils.isEmpty(period_)){
             btn_add_clicked.setText("Update");
             final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Routine").child(session_).child(day_).child(period_);
             databaseReference1.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {

                     if(!TextUtils.isEmpty(session_) && !TextUtils.isEmpty(day_) && !TextUtils.isEmpty(period_)) {

                         AcademicClass academicClass = dataSnapshot.getValue(AcademicClass.class);
                         Toast.makeText(getApplicationContext(), "on Routine", Toast.LENGTH_SHORT).show();
                         et_subject.setText(academicClass.getSubject());
                         et_course.setText(academicClass.getCourse_code());
                         et_start.setText(academicClass.getStart());
                         et_end.setText(academicClass.getEnd());
                         et_room.setText(academicClass.getRoom());
                         String ses = session_;
                         session_ = null;

                         String[] positions = getResources().getStringArray(R.array.session);
                         ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(positions));

                         int position = arrayList.indexOf(ses);
                         spn_session.setSelection(position);

                         String[] positions1 = getResources().getStringArray(R.array.day);
                         ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(positions1));

                         int position1 = arrayList1.indexOf(day_);
                         spn_day.setSelection(position1);


//                         String[] positions2 = getResources().getStringArray(R.array.period);
//                         ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(positions2));
//
//                         int position2 = arrayList2.indexOf(period_);
//                         spn_period.setSelection(position2);
                     }


                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });
        }
    }

    private void init_views() {
        spn_session = (Spinner)findViewById(R.id.spinner2);
        spn_day = (Spinner)findViewById(R.id.spinner3);
        //spn_period = (Spinner)findViewById(R.id.spinner4);
        et_subject = (EditText)findViewById(R.id.editText9);
        et_course = (EditText)findViewById(R.id.editText10);
        et_start = (EditText)findViewById(R.id.editText11);
        et_end = (EditText)findViewById(R.id.editText12);
        btn_add_clicked = (Button)findViewById(R.id.btn_Add);
        et_room = (EditText)findViewById(R.id.editText13);

    }

    private void init_variables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Routine");

    }

    private void init_functions() {

        btn_add_clicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String session = (String) spn_session.getSelectedItem();
                String day = (String) spn_day.getSelectedItem();
               // String period = (String) spn_period.getSelectedItem();

                String subject = et_subject.getText().toString();
                String course_code = et_course.getText().toString();
                String start = et_start.getText().toString();
                String end = et_end.getText().toString();
                String room = et_room.getText().toString();
                String Key  = period_;


                if( !TextUtils.isEmpty(subject) && !TextUtils.isEmpty(course_code)  && !TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)  && !TextUtils.isEmpty(room)){
                    if( ! btn_add_clicked.getText().equals("Update")) {
                        Key = databaseReference.child(session).child(day).push().getKey();
                    }
                    AcademicClass academicClass = new AcademicClass(subject,course_code,start,end,room,Key);
                    databaseReference.child(session).child(day).child(Key).setValue(academicClass);
                    Toast.makeText(getApplicationContext(),"New Routine Added",Toast.LENGTH_SHORT).show();
                    et_subject.setText("");
                    et_course.setText("");
                    et_start.setText("");
                    et_end.setText("");
                    et_room.setText("");


                }else
                {
                    Toast.makeText(getApplicationContext(),"can't leave the fields blank",Toast.LENGTH_SHORT).show();
                }


                if(btn_add_clicked.getText().toString().equals("Update")){
                    Intent intent = new Intent(Routine_Add.this,Showing_result.class);
                    intent.putExtra("session",session);
                    intent.putExtra("day",day);
                   // intent.putExtra("period",period);
                    startActivity(intent);
                }

            }
        });
    }
}
