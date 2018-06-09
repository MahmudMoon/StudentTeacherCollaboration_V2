package moonc.example.com.studentteachercollaboration_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import moonc.example.com.studentteachercollaboration_v2.Models.Student;

public class UserAdd extends AppCompatActivity {
    EditText name_, email_, phone_, id_, password_;
    Spinner role, Session;
    Button button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        name_ = (EditText) findViewById(R.id.editText);
        email_ = (EditText) findViewById(R.id.editText2);
        phone_ = (EditText) findViewById(R.id.editText3);
        button = (Button) findViewById(R.id.button);
        id_ = (EditText) findViewById(R.id.et_id);
        password_ = (EditText) findViewById(R.id.et_password);
        role = (Spinner) findViewById(R.id.spn_role);
        Session = (Spinner) findViewById(R.id.spinner5);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User_details");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Key = databaseReference.push().getKey();
                String Name = name_.getText().toString();
                String Email = email_.getText().toString();
                String Phone = phone_.getText().toString();
                String Id = id_.getText().toString();
                String Password = password_.getText().toString();
                String Role = (String) role.getSelectedItem();
                String Sess_ = "";
                if (!Role.equals("Teacher")) {
                    Sess_ = (String) Session.getSelectedItem();
                }
                Student student = new Student(Key, Email, Name, Phone, Id, Password, Role, Sess_);
                databaseReference.child(Key).setValue(student);
                Toast.makeText(getApplicationContext(),
                        "New item inserted", Toast.LENGTH_SHORT).show();
                name_.setText("");
                email_.setText("");
                phone_.setText("");
                id_.setText("");
                password_.setText("");
            }
        });

        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (role.getSelectedItem().toString().equalsIgnoreCase("teacher")) {
                    Session.setVisibility(View.GONE);
                } else
                    Session.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
