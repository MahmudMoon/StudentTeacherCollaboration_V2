package moonc.example.com.studentteachercollaboration_v2;

import android.media.effect.Effect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add extends AppCompatActivity {

    EditText name_,email_,phone_;
    Button button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        name_= (EditText)findViewById(R.id.editText);
        email_ =(EditText)findViewById(R.id.editText2);
        phone_ = (EditText)findViewById(R.id.editText3);
        button = (Button)findViewById(R.id.button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Student_detail");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Key =  databaseReference.push().getKey();
               String Name = name_.getText().toString();
               String Email = email_.getText().toString();
               String Phone = phone_.getText().toString();
               Students_detail students_detail = new Students_detail(Key,Name,Email,Phone);

               databaseReference.child(Key).setValue(students_detail);

                Toast.makeText(getApplicationContext(),"New Student Added",Toast.LENGTH_SHORT).show();

                name_.setText("");
                email_.setText("");
                phone_.setText("");
            }
        });
    }
}
