package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Day extends AppCompatActivity {

    TextView d1,d2,d3,d4,d5,tv_session_;
    String session;
    String day_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Intent intent = getIntent();
        session = intent.getStringExtra("session");


        init_view();
        init_variables();
        init_functions();


    }

    private void init_view() {
        d1 = (TextView)findViewById(R.id.textView12);
        d2 = (TextView)findViewById(R.id.textView13);
        d3 = (TextView)findViewById(R.id.textView14);
        d4 = (TextView)findViewById(R.id.textView15);
        d5 = (TextView)findViewById(R.id.textView16);
        tv_session_ = (TextView)findViewById(R.id.tv_session);

    }

    private void init_variables() {
       tv_session_.setText(session);
    }

    private void init_functions() {
        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_ = "Sunday";
                go();
            }
        });

        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_ = "Monday";
                go();
            }
        });

        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_ = "Tuesday";
                go();
            }
        });

        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_ = "Wednesday";
                go();
            }
        });

        d5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_ = "Thursday";
                go();
            }
        });
    }

    private void go() {
        Intent intent = new Intent(Day.this,Showing_result.class);
        intent.putExtra("session",session);
        intent.putExtra("day",day_);
        startActivity(intent);
    }
}
