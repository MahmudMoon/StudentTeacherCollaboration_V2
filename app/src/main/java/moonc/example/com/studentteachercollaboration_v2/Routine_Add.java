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

    Spinner spn_session, spn_day;
    EditText et_subject, et_course;
    EditText et_start, et_end;
    EditText et_room;
    Button btn_add_clicked;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String session_, day, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine__add);
        Intent intent = getIntent();
        session_ = intent.getStringExtra("session");
        day = intent.getStringExtra("day");
        key = intent.getStringExtra("key");

        initializeViews();
        initializeVariables();
        initializeButtonOnClickListener();

        if (checkIfEditable()) {
            btn_add_clicked.setText("Update");
            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                    .getReference("Routine").child(session_).child(day).child(key);

            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (checkIfEditable()) {
                        AcademicClass academicClass = dataSnapshot.getValue(AcademicClass.class);
                        et_subject.setText(academicClass.getSubject());
                        et_course.setText(academicClass.getCourse_code());
                        et_start.setText(academicClass.getStart());
                        et_end.setText(academicClass.getEnd());
                        et_room.setText(academicClass.getRoom());
                        String ses = session_;

                        String[] positions = getResources().getStringArray(R.array.session);
                        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(positions));

                        int position = arrayList.indexOf(ses);
                        spn_session.setSelection(position);

                        String[] positions1 = getResources().getStringArray(R.array.day);
                        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList(positions1));

                        int position1 = arrayList1.indexOf(day);
                        spn_day.setSelection(position1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private boolean checkIfEditable() {
        return !TextUtils.isEmpty(session_) && !TextUtils.isEmpty(day)
                && !TextUtils.isEmpty(key);
    }

    private void initializeViews() {
        spn_session = (Spinner) findViewById(R.id.spinner2);
        spn_day = (Spinner) findViewById(R.id.spinner3);
        et_subject = (EditText) findViewById(R.id.editText9);
        et_course = (EditText) findViewById(R.id.editText10);
        et_start = (EditText) findViewById(R.id.editText11);
        et_end = (EditText) findViewById(R.id.editText12);
        btn_add_clicked = (Button) findViewById(R.id.btn_Add);
        et_room = (EditText) findViewById(R.id.editText13);
    }

    private void initializeVariables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Routine");
    }

    private void initializeButtonOnClickListener() {
        btn_add_clicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String session = (String) spn_session.getSelectedItem();
                String day = (String) spn_day.getSelectedItem();
                String subject = et_subject.getText().toString();
                String course_code = et_course.getText().toString();
                String start = et_start.getText().toString();
                String end = et_end.getText().toString();
                String room = et_room.getText().toString();

                if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(course_code)
                        && !TextUtils.isEmpty(start) && !TextUtils.isEmpty(end) &&
                        !TextUtils.isEmpty(room)) {

                    if (!checkIfEditable()) {
                        key = databaseReference.child(session).child(day).push().getKey();
                    }
                    AcademicClass academicClass =
                            new AcademicClass(subject, course_code, start, end, room, key);
                     databaseReference.child(session).child(day).child(key).setValue(academicClass);
                    et_subject.setText("");
                    et_course.setText("");
                    et_start.setText("");
                    et_end.setText("");
                    et_room.setText("");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "can't leave the fields blank", Toast.LENGTH_SHORT).show();
                }

                if (checkIfEditable()) {
                    Toast.makeText(getApplicationContext(), "Routine updated!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(),
                            "New Routine Added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
