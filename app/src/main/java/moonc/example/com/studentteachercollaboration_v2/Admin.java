package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
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
    FloatingActionButton btn_add;
    ProgressBar progressBar;

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
                employees_detail.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Students_detail value = data.getValue(Students_detail.class);
                    employees_detail.add(value);
                    Toast.makeText(getApplicationContext(),Integer.toString(employees_detail.size()),Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
                Adapter adapter = new Adapter(getApplicationContext(),employees_detail);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this,Add.class);
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


    }

    private void updateOrDelete(final int position) {
        ImageButton delete,update;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog,null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        delete = (ImageButton)view.findViewById(R.id.delete);
        update = (ImageButton)view.findViewById(R.id.update);

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
               Intent intent = new Intent(Admin.this,Update.class);
               intent.putExtra("Key",Key);
               startActivity(intent);
            }
        });
    }



    private void init_functions() {

          progressBar.setVisibility(View.VISIBLE);

    }


    private void init_variables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Student_details");
        employees_detail = new ArrayList<>();

    }

    private void init_views() {
        listView = (ListView)findViewById(R.id.list_view);
        btn_add = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
