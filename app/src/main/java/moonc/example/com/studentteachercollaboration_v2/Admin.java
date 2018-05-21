package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    Intent intent;
    ArrayList<Students_detail> employees_detail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init_views();
        init_variables();
        init_functions();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Students_detail value = data.getValue(Students_detail.class);
                    employees_detail.add(value);
                    Toast.makeText(getApplicationContext(),Integer.toString(employees_detail.size()),Toast.LENGTH_SHORT).show();
                }
                Adapter adapter = new Adapter(getApplicationContext(),employees_detail);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"LOng Click",Toast.LENGTH_LONG).show();

                return false;
            }
        });



    }

    private void init_functions() {



    }


    private void init_variables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Student_detail");

    }

    private void init_views() {
        listView = (ListView)findViewById(R.id.list_view);
        employees_detail = new ArrayList<>();
    }
}
