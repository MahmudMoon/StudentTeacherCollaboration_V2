package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Update extends AppCompatActivity {


    DatabaseReference databaseReference;
    EditText name_,email_,phone_;
    Button update;
    String Key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        name_ = (EditText)findViewById(R.id.editText4);
        email_ = (EditText)findViewById(R.id.editText5);
        phone_ = (EditText)findViewById(R.id.editText6);
        update = (Button)findViewById(R.id.button2);

        Intent intent = getIntent();
        Key = intent.getStringExtra("Key");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name  = name_.getText().toString();
                String email = email_.getText().toString();
                String phone = phone_.getText().toString();
                databaseReference = FirebaseDatabase.getInstance().getReference("Student_detail").child(Key);
                Students_detail students_detail = new Students_detail(Key,name,email,phone);
                databaseReference.setValue(students_detail);

                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
            }
        });





    }
}
