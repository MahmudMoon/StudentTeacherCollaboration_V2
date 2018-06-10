package test.example.com.studentteachercollaboration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import test.example.com.studentteachercollaboration.Contstants.Constants;
import test.example.com.studentteachercollaboration.Models.CustomMessage;

public class SendMessage extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText messageEditText;
    Button sendButton;
    String senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        initializeViews();
        initializeVariables();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidated = checkValidation();
                if (isValidated) {
                    String key = databaseReference.push().getKey();
                    String message = messageEditText.getText().toString();
                    CustomMessage message1 = new CustomMessage();
                    message1.setFrom(senderName);
                    message1.setMessageBody(message);
                    message1.setTime(new Date());
                    message1.setKey(key);
                    databaseReference.child(key).setValue(message1);
                    Toast.makeText(SendMessage.this, "Message sent!",
                            Toast.LENGTH_SHORT).show();
                    messageEditText.setText("");
                    finish();
                } else {
                    messageEditText.setError("This field can not be empty");
                }
            }
        });
    }

    private boolean checkValidation() {
        return !TextUtils.isEmpty(messageEditText.getText().toString());
    }

    private void initializeVariables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.MESSAGES);
        senderName = getIntent().getStringExtra(Constants.NAME);
    }

    private void initializeViews() {
        messageEditText = (EditText) findViewById(R.id.message_edit_text);
        sendButton = (Button) findViewById(R.id.send_button);
    }
}
