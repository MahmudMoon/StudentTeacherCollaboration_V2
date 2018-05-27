package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Update extends AppCompatActivity {


    DatabaseReference databaseReference;
    EditText name_,email_,phone_,id_,password_;
    Button update;
    String Key;
    Spinner role_;
    Spinner session_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        name_ = (EditText)findViewById(R.id.editText4);
        email_ = (EditText)findViewById(R.id.editText5);
        phone_ = (EditText)findViewById(R.id.editText6);
        update = (Button)findViewById(R.id.button2);
        id_ =(EditText)findViewById(R.id.editText7);
        password_ = (EditText)findViewById(R.id.editText8);
        role_ = (Spinner)findViewById(R.id.spinner);
        session_ = (Spinner)findViewById(R.id.spinner_ses);

        Intent intent = getIntent();
        Key = intent.getStringExtra("Key");

        databaseReference = FirebaseDatabase.getInstance().getReference("User_details").child(Key);
        final DatabaseReference ref = databaseReference.child("name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Students_detail detail = dataSnapshot.getValue(Students_detail.class);
                String name = detail.getName();
                String email = detail.getEmail_id_number();
                String phone = detail.getPhone_number();
                String id = detail.getID();
                String password = detail.getPassword();
                String role = detail.getRole();
                String Session = detail.getSession();

                name_.setText(name);
                email_.setText(email);
                phone_.setText(phone);
                id_.setText(id);
                password_.setText(password);
                String[] positions = getResources().getStringArray(R.array.role);
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(positions));

                int position = arrayList.indexOf(role);
                role_.setSelection(position);

                if(!Session.equals("Teacher")) {
                    String[] positions_1 = getResources().getStringArray(R.array.session);
                    ArrayList<String> arrayList_1 = new ArrayList<>(Arrays.asList(positions_1));

                    int position1 = arrayList_1.indexOf(Session);
                    session_.setSelection(position1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot data : dataSnapshot.getChildren()){
//                    String name = data.child("name").getValue(String.class);
//                    Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
//                    name_.setText(data.child("name").getValue(String.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name  = name_.getText().toString();
                String email = email_.getText().toString();
                String phone = phone_.getText().toString();
                String Id = id_.getText().toString();
                String Password = password_.getText().toString();
                String Role = (String) role_.getSelectedItem();
                String Session = "";

                if(!Session.equals("Teacher")) {
                    Session = (String) session_.getSelectedItem();
                }

                Students_detail students_detail = new Students_detail(Key,email,name,phone,Id,Password,Role,Session);
                databaseReference.setValue(students_detail);
                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();


                name_.setText("");
                email_.setText("");
                phone_.setText("");
                id_.setText("");
                password_.setText("");

                Intent intent1 = new Intent(Update.this,Admin.class);
                startActivity(intent1);
            }
        });

    }
}
