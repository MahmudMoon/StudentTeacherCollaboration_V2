package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import moonc.example.com.studentteachercollaboration_v2.Adapters.RoutineAdapter;
import moonc.example.com.studentteachercollaboration_v2.Contstants.Constants;
import moonc.example.com.studentteachercollaboration_v2.Models.AcademicClass;

public class ShowRoutine extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listView;
    String session = "";
    RoutineAdapter routineAdapter;
    ArrayList<ArrayList<AcademicClass>> allRoutines = new ArrayList<>();
    int today = Constants.MIN_DAY_OF_WEEK;
    ImageButton nextDayButton;
    ImageButton previousDayButton;
    TextView todayTextView;
    Spinner sessionSpinner;
    boolean isAdmin;
    FloatingActionButton addRoutineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_result);
        initializeViews();
        initializeVariables();
        getAllRoutinesFromServer(session);

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today++;
                if (today >= Constants.MAX_DAY_OF_WEEK)
                    today = Constants.MIN_DAY_OF_WEEK;
                showCurrentDayRoutine();
            }
        });
        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today--;
                if (today < Constants.MIN_DAY_OF_WEEK)
                    today = Constants.MAX_DAY_OF_WEEK - 1;
                showCurrentDayRoutine();
            }
        });

        // Only admin has these privileges
        if (isAdmin) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    updateOrDelete(position);
                    return false;
                }
            });

            LinearLayout linearLayout = (LinearLayout) findViewById(
                    R.id.routine_spinner_linear_layout);
            linearLayout.setVisibility(View.VISIBLE);

            sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    session = (String) sessionSpinner.getSelectedItem();
                    getAllRoutinesFromServer(session);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            addRoutineButton.setVisibility(View.VISIBLE);

            addRoutineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowRoutine.this, RoutineAddOrUpdate.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void updateOrDelete(final int position) {
        ImageButton delete, update;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        delete = (ImageButton) view.findViewById(R.id.delete);
        update = (ImageButton) view.findViewById(R.id.update);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfDay = Constants.NAME_OF_DAY_IN_WEEK[today];
                String key = allRoutines.get(today).get(position).getKey();
                databaseReference.child(nameOfDay).child(key).removeValue();
                Toast.makeText(ShowRoutine.this,
                        "Deleted!", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfDay = Constants.NAME_OF_DAY_IN_WEEK[today];
                String key = allRoutines.get(today).get(position).getKey();
                Intent intent = new Intent(ShowRoutine.this, RoutineAddOrUpdate.class);
                intent.putExtra("session", session);
                intent.putExtra("day", nameOfDay);
                intent.putExtra("key", key);
                startActivity(intent);
                alertDialog.cancel();
            }
        });
    }

    private void initializeViews() {
        nextDayButton = (ImageButton) findViewById(R.id.viewNextDayButton);
        previousDayButton = (ImageButton) findViewById(R.id.viewPreviousDayButton);
        listView = (ListView) findViewById(R.id.lv_);
        listView.setDivider(null);
        todayTextView = (TextView) findViewById(R.id.viewDayTBx);
        sessionSpinner = (Spinner) findViewById(R.id.session_spinner);
        addRoutineButton = (FloatingActionButton) findViewById(R.id.routine_add_floating_button);
    }

    private void initializeVariables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Routine")
                .child(session);
        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_WEEK);
        if (today == Constants.MAX_DAY_OF_WEEK)
            today = Constants.MIN_DAY_OF_WEEK;

        //Adding empty arraylist
        for (int i = 0; i < Constants.MAX_DAY_OF_WEEK; i++) {
            allRoutines.add(new ArrayList<AcademicClass>());
        }
        Intent intent = getIntent();
        isAdmin = intent.getBooleanExtra(Constants.IS_ADMIN, false);
        session = intent.getStringExtra(Constants.SESSION);

        if (isAdmin)
            session = (String) sessionSpinner.getSelectedItem();
    }

    private void getAllRoutinesFromServer(String session) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Routine")
                .child(session);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearAllRoutines();
                int index;
                for (DataSnapshot day : dataSnapshot.getChildren()) {
                    index = getIndexForKey(day.getKey());
                    for (DataSnapshot routine : day.getChildren()) {
                        AcademicClass academicClass = routine.getValue(AcademicClass.class);
                        allRoutines.get(index).add(academicClass);
                    }
                }
                showCurrentDayRoutine();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showCurrentDayRoutine() {
        String nameOfToday = Constants.NAME_OF_DAY_IN_WEEK[today];
        todayTextView.setText(nameOfToday);

        // Sorting arraylist
        sortAccordingToStartTime(allRoutines.get(today));
        routineAdapter = new RoutineAdapter(ShowRoutine.this,
                allRoutines.get(today));
        listView.setAdapter(routineAdapter);
    }

    private int getIndexForKey(String key) {
        if (key.equalsIgnoreCase(Constants.NAME_OF_DAY_IN_WEEK[0]))
            return 0;
        else if (key.equalsIgnoreCase(Constants.NAME_OF_DAY_IN_WEEK[1]))
            return 1;
        else if (key.equalsIgnoreCase(Constants.NAME_OF_DAY_IN_WEEK[2]))
            return 2;
        else if (key.equalsIgnoreCase(Constants.NAME_OF_DAY_IN_WEEK[3]))
            return 3;
        else if (key.equalsIgnoreCase(Constants.NAME_OF_DAY_IN_WEEK[4]))
            return 4;
        else if (key.equalsIgnoreCase(Constants.NAME_OF_DAY_IN_WEEK[5]))
            return 5;
        else if (key.equalsIgnoreCase(Constants.NAME_OF_DAY_IN_WEEK[6]))
            return 6;
        return 0;
    }

    private void clearAllRoutines() {
        for (int i = 0; i < Constants.MAX_DAY_OF_WEEK; i++) {
            allRoutines.get(i).clear();
        }
    }

    private void sortAccordingToStartTime(ArrayList<AcademicClass> currentDayData) {
        Collections.sort(currentDayData, new Comparator<AcademicClass>() {
            @Override
            public int compare(AcademicClass lhs, AcademicClass rhs) {
                SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                String timeLHS = lhs.getStart();
                String timeRHS = rhs.getEnd();
                Calendar calendarRHD = Calendar.getInstance();
                Calendar calendarLHS = Calendar.getInstance();

                try {
                    calendarLHS.setTime(format.parse(timeLHS));
                    calendarRHD.setTime(format.parse(timeRHS));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return calendarLHS.compareTo(calendarRHD);
            }
        });
    }
}
