package test.example.com.studentteachercollaboration;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import test.example.com.studentteachercollaboration.Models.AcademicClass;

public class RoutineAddOrUpdate extends AppCompatActivity {

    Spinner spn_session, spn_day;
    EditText et_subject, et_course;
    EditText et_room;
    TextView startTimeTextView;
    TextView endTimeTextView;
    Button btn_add_clicked;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String session_, day, key;
    boolean isStartTimeClicked = false;
    boolean isEndTimeClicked = false;
    int currentHour, currentMinute;
    final int TIME_DIALOUGE_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_add);
        Intent intent = getIntent();
        session_ = intent.getStringExtra("session");
        day = intent.getStringExtra("day");
        key = intent.getStringExtra("key");

        initializeViews();
        initializeVariables();
        initializeButtonOnClickListener();

        startTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOUGE_ID); // Time Picker Will be Shown
                isStartTimeClicked = true;
                isEndTimeClicked = false;
            }
        });
        endTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOUGE_ID); // Time Picker Will be Shown
                isStartTimeClicked = false;
                isEndTimeClicked = true;
            }
        });

        // If edit option is clicked
        if (checkIfEditable()) {
            btn_add_clicked.setText("UserUpdate");
            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                    .getReference("Routine").child(session_).child(day).child(key);

            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (checkIfEditable()) {
                        AcademicClass academicClass = dataSnapshot.getValue(AcademicClass.class);
                        et_subject.setText(academicClass.getSubject());
                        et_course.setText(academicClass.getCourse_code());
                        startTimeTextView.setText(academicClass.getStart());
                        endTimeTextView.setText(academicClass.getEnd());
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
        startTimeTextView = (TextView) findViewById(R.id.editText11);
        endTimeTextView = (TextView) findViewById(R.id.editText12);
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
                String start = startTimeTextView.getText().toString();
                String end = endTimeTextView.getText().toString();
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

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new TimePickerDialog(this, mTimeSetListener, currentHour,
                        currentMinute, false);

            default:
                break;
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    currentHour = hourOfDay;
                    currentMinute = minute;

                    updateText(hourOfDay, minute);
                }
            };

    private void updateText(int hours, int mins) {

        String timeSet = "", hourString = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12) {
            hours = 12;
            timeSet = "PM";
        } else
            timeSet = "AM";

        if (hours > 0 && hours < 10) {
            hourString = "0" + String.valueOf(hours);
        } else
            hourString = String.valueOf(hours);

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hourString).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        if (isStartTimeClicked == true) {
            startTimeTextView.setText(aTime);
        } else if (isEndTimeClicked == true) {
            endTimeTextView.setText(aTime);
        }
    }
}
