package moonc.example.com.studentteachercollaboration_v2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import moonc.example.com.studentteachercollaboration_v2.Contstants.Constants;
import moonc.example.com.studentteachercollaboration_v2.Models.Student;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private FirebaseAuth firebaseAuth;
    private TextView register;
    private TextView lost_pass;
    private Button mEmailSignInButton;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        lost_pass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Working.class);
                startActivity(intent);
            }
        });


        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Working.class);
                startActivity(intent);
            }
        });

    }

    private void initializeViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        spinner = (Spinner) findViewById(R.id.spn_options);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mProgressView = findViewById(R.id.login_progress);
        lost_pass = (TextView) findViewById(R.id.tv_lostPass);
        register = (TextView) findViewById(R.id.tv_register);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.isEmpty()) {
            mEmailView.setError("Email Field Can't be empty");
            mEmailView.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError("Not a valid Email");
            mEmailView.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mPasswordView.setError("Password Field Can't be empty");
            mEmailView.requestFocus();
            return;
        }
        if (password.length() < 8) {
            mPasswordView.setError("Password can't be less than 8 character");
            mEmailView.requestFocus();
            return;
        }

        String selectedItem = (String) spinner.getSelectedItem();
        if (selectedItem.equalsIgnoreCase("Admin")) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to Login", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (selectedItem.equalsIgnoreCase("Student")) {
            LoginAsRole(email, password, selectedItem);
        } else {
            LoginAsRole(email, password, selectedItem);
        }
    }

    private void LoginAsRole(final String email, final String password, final String role) {
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance().getReference("User_details");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isAuthenticated = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Student detail = data.getValue(Student.class);
                    String Email = detail.getEmail_id_number();
                    String Password = detail.getPassword();
                    String Role = detail.getRole();

                    if (Email.equals(email) && Password.equals(password) && Role.equals(role)) {
                        isAuthenticated = true;
                        if (Role.equals("Student")) {
                            String Session = detail.getSession();
                            Intent intent = new Intent(LoginActivity.this,
                                    ShowRoutine.class);
                            intent.putExtra("session", Session);
                            intent.putExtra(Constants.IS_ADMIN, false);
                            startActivity(intent);
                            break;

                        } else {
                            Intent intent = new Intent(LoginActivity.this,
                                    Teacher_activity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                }
                if (!isAuthenticated)
                    Toast.makeText(getApplicationContext(), "Incorrect Email or Password!",
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

