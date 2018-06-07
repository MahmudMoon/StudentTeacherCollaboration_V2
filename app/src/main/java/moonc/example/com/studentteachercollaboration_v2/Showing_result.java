package moonc.example.com.studentteachercollaboration_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

    ListView listView;
    String Session_, Day_;
    int From;
    RoutineAdapter routineAdapter;
    ArrayList<AcademicClass> mArrayList;
    ArrayList<ArrayList<AcademicClass>> allRoutines = new ArrayList<>();
    int today = Constants.MIN_DAY_OF_WEEK; //day of week
    ImageButton nextDayButton;
    ImageButton previousDayButton;
    TextView todayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_result);
        initializeVariables();
        initializeViews();
        getAllRoutinesFromServer("2013-14");
        showCurrentDayRoutine();

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
        // TODO: remove unnecessary codes

       /* Intent intent = getIntent();
        Session_ = intent.getStringExtra("session");
        Day_ = intent.getStringExtra("day");
        From = intent.getIntExtra("from", 0);

        init_variables();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArrayList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AcademicClass academicClass = data.getValue(AcademicClass.class);
                    mArrayList.add(academicClass);
                }
                routineAdapter = new RoutineAdapter(getApplicationContext(), mArrayList);
                listView.setAdapter(routineAdapter);
               *//* Toast.makeText(getApplicationContext(),
                        Integer.toString(mArrayList.size()), Toast.LENGTH_SHORT).show();*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (From == 0) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    updateOrDelete(position);
                    return false;
                }
            });
        }
*/
    }


   /* private void updateOrDelete(final int position) {
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
                String Key = mArrayList.get(position).getKey();
                databaseReference.child(Key).removeValue();
                Toast.makeText(Showing_result.this,
                        "Deleted!", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Key = mArrayList.get(position).getKey();
                Intent intent = new Intent(Showing_result.this, Routine_Add.class);
                intent.putExtra("session", Session_);
                intent.putExtra("day", Day_);
                intent.putExtra("period", Key);
                startActivity(intent);
            }
        });
    }

    private void init_variables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Routine").child(Session_).child(Day_);
        mArrayList = new ArrayList<>();

    }*/

    private void initializeViews() {
        nextDayButton = (ImageButton) findViewById(R.id.viewNextDayButton);
        previousDayButton = (ImageButton) findViewById(R.id.viewPreviousDayButton);
        listView = (ListView) findViewById(R.id.lv_);
        todayTextView = (TextView) findViewById(R.id.viewDayTBx);
    }

    private void initializeVariables() {
        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_WEEK);
        //Adding empty arraylist
        for (int i = 0; i < Constants.MAX_DAY_OF_WEEK; i++) {
            allRoutines.add(new ArrayList<AcademicClass>());
        }
    }

    private void getAllRoutinesFromServer(String session) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Routine")
                .child("2013-14");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int index;
                for (DataSnapshot day : dataSnapshot.getChildren()) {
                    index = getIndexForKey(day.getKey());
                    for (DataSnapshot routine : day.getChildren()) {
                        AcademicClass academicClass = routine.getValue(AcademicClass.class);
                        allRoutines.get(index).add(academicClass);
                    }
                }
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
}
