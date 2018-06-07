package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import moonc.example.com.studentteachercollaboration_v2.Adapters.RoutineAdapter;
import moonc.example.com.studentteachercollaboration_v2.Contstants.Constants;
import moonc.example.com.studentteachercollaboration_v2.Models.AcademicClass;

public class Showing_result extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listView;
    String Session_, Day_;
    int From;
    RoutineAdapter routineAdapter;
    ArrayList<AcademicClass> mArrayList;
    ArrayList<ArrayList<AcademicClass>> allRoutines = new ArrayList<>();
    ArrayList<AcademicClass> sunday = new ArrayList<>();
    ArrayList<AcademicClass> monday = new ArrayList<>();
    ArrayList<AcademicClass> tuesday = new ArrayList<>();
    ArrayList<AcademicClass> wednesday = new ArrayList<>();
    ArrayList<AcademicClass> thursday = new ArrayList<>();
    ArrayList<AcademicClass> friday = new ArrayList<>();
    ArrayList<AcademicClass> saturday = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_result);
        initializeArrayLists();
        getAllRoutinesFromServer("2013-14");

        // TODO: remove unnecessary codes

       /* Intent intent = getIntent();
        Session_ = intent.getStringExtra("session");
        Day_ = intent.getStringExtra("day");
        From = intent.getIntExtra("from", 0);
        init_view();
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

    private void init_view() {
        listView = (ListView) findViewById(R.id.lv_);
    }

    private void init_variables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Routine").child(Session_).child(Day_);
        mArrayList = new ArrayList<>();

    }

    private void initializeArrayLists() {
        //Adding empty arraylist
        int numberOfDayInWeek = 7;
        for (int i = 0; i < numberOfDayInWeek; i++) {
            allRoutines.add(new ArrayList<AcademicClass>());
        }
    }

    // TODO  : get all data from server and store to a arraylist
    private void getAllRoutinesFromServer(String session) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Routine")
                .child("2013-14");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Log.d(Constants.LOGTAG, dataSnapshot.toString());
                int count = 0;
                for (DataSnapshot day : dataSnapshot.getChildren()) {
                    for (DataSnapshot routine : day.getChildren()) {
                        AcademicClass academicClass = routine.getValue(AcademicClass.class);
                        allRoutines.get(count).add(academicClass);
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
