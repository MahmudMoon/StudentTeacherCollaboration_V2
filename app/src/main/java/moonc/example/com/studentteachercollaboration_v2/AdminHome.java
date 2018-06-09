package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import moonc.example.com.studentteachercollaboration_v2.Adapters.StudentAdapter;
import moonc.example.com.studentteachercollaboration_v2.Contstants.Constants;
import moonc.example.com.studentteachercollaboration_v2.Models.Student;

public class AdminHome extends AppCompatActivity {

    ArrayList<Student> employees_detail = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listView;
    FloatingActionButton addButton, routine;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initializeViews();
        initializeVariables();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employees_detail.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Student value = data.getValue(Student.class);
                    employees_detail.add(value);
                }
                progressBar.setVisibility(View.INVISIBLE);
                StudentAdapter studentAdapter = new StudentAdapter(getApplicationContext(),
                        employees_detail);
                listView.setAdapter(studentAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  alertDialog.show();
                Intent intent = new Intent(AdminHome.this, UserAdd.class);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                updateOrDelete(position);
                return false;
            }
        });

        routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, ShowRoutine.class);
                intent.putExtra(Constants.IS_ADMIN,true);
                startActivity(intent);
            }
        });
    }

    private void updateOrDelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ImageButton delete = (ImageButton) view.findViewById(R.id.delete);
        ImageButton update = (ImageButton) view.findViewById(R.id.update);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Key = employees_detail.get(position).getKey();
                databaseReference.child(Key).removeValue();
                alertDialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Key = employees_detail.get(position).getKey();
                Intent intent = new Intent(AdminHome.this, UserUpdate.class);
                intent.putExtra("Key", Key);
                startActivity(intent);
            }
        });
    }

    private void initializeVariables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User_details");
    }

    private void initializeViews() {
        listView = (ListView) findViewById(R.id.list_view);
        listView.setDivider(null);
        addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        routine = (FloatingActionButton) findViewById(R.id.routine_);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
