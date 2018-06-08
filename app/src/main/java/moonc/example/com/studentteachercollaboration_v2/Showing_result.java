package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;

import moonc.example.com.studentteachercollaboration_v2.Adapters.RoutineAdapter;
import moonc.example.com.studentteachercollaboration_v2.Contstants.Constants;
import moonc.example.com.studentteachercollaboration_v2.Models.AcademicClass;

public class Showing_result extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_result);
        initializeViews();
        initializeVariables();
        Log.d(Constants.LOGTAG, "session from routine : " +session);
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
                Toast.makeText(Showing_result.this,
                        "Deleted!", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfDay = Constants.NAME_OF_DAY_IN_WEEK[today];
                String key = allRoutines.get(today).get(position).getKey();
                Intent intent = new Intent(Showing_result.this, RoutineAddOrUpdate.class);
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
        todayTextView = (TextView) findViewById(R.id.viewDayTBx);
        sessionSpinner = (Spinner) findViewById(R.id.session_spinner);
    }

    private void initializeVariables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Routine")
                .child(session);
        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_WEEK);
        //Adding empty arraylist
        for (int i = 0; i < Constants.MAX_DAY_OF_WEEK; i++) {
            allRoutines.add(new ArrayList<AcademicClass>());
        }

        Bundle bundle = getIntent().getExtras();
        try {
            isAdmin = bundle.getBoolean(Constants.IS_ADMIN);
            session = bundle.getString(Constants.SESSION);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

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
        routineAdapter = new RoutineAdapter(Showing_result.this,
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
}
