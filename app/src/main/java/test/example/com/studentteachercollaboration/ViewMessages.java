package test.example.com.studentteachercollaboration;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import test.example.com.studentteachercollaboration.Adapters.ViewMessageAdapter;
import test.example.com.studentteachercollaboration.Contstants.Constants;
import test.example.com.studentteachercollaboration.Models.CustomMessage;

public class ViewMessages extends AppCompatActivity {
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<CustomMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        listView = (ListView) findViewById(R.id.message_list_view);
        listView.setDivider(null);
        initializeVariables();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    CustomMessage tempMessage = data.getValue(CustomMessage.class);
                    messages.add(tempMessage);
                }
                ViewMessageAdapter adapter = new ViewMessageAdapter(ViewMessages.this,
                        messages);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                askPermissionToDelete(position);
                return false;
            }
        });
    }

    private void askPermissionToDelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this message?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String key = messages.get(position).getKey();
                databaseReference.child(key).removeValue();
                Toast.makeText(getApplicationContext(),
                        "Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void initializeVariables() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.MESSAGES);
    }
}
