package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Routine extends AppCompatActivity {

    FloatingActionButton btn_routine_add;
    TextView session1,session2,session3,session4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        init_view();
        init_variables();
        init_functions();


    }

    private void init_view() {
        btn_routine_add = (FloatingActionButton) findViewById(R.id.routine_Add);
        session1 = (TextView)findViewById(R.id.textView2);
        session2 = (TextView)findViewById(R.id.textView3);
        session3 = (TextView)findViewById(R.id.textView9);
        session4 = (TextView)findViewById(R.id.textView10);
    }

    private void init_variables() {

    }

    private void init_functions() {
        btn_routine_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Routine.this,Routine_Add.class);
                startActivity(intent);
            }
        });

        session1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Routine.this,Day.class);
                intent.putExtra("session","2013-14");
                startActivity(intent);
            }
        });

        session2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Routine.this,Day.class);
                intent.putExtra("session","2014-15");
                startActivity(intent);
            }
        });

        session3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Routine.this,Day.class);
                intent.putExtra("session","2015-16");
                startActivity(intent);
            }
        });

        session4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Routine.this,Day.class);
                intent.putExtra("session","2016-17");
                startActivity(intent);
            }
        });
    }
}
