package firebase.gopool.Reminder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import firebase.gopool.Adapter.ReminderAdapter;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.models.Reminder;
import firebase.gopool.models.User;

/**
 * Created by P16174003 on 13/02/2019.
 */

public class ReminderActivity extends AppCompatActivity {
    private static final String TAG = "ReminderActivity";


    private Context mContext = ReminderActivity.this;
    private Activity mActivity = this;

    //Recycler view setup
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecycleAdapter;
    private ReminderAdapter myAdapter;
    private ArrayList<Reminder> reminders;

    //widgets
    private TextView mUsername;
    private ImageView mBack;

    //Firebase variables
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseMethods mFirebaseMethods;

    private String userID;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Log.d(TAG, "onCreate: starting.");

        setupFirebase();
        setupWidgets();
        setupRecyclerView();

        mRef = FirebaseDatabase.getInstance().getReference().child("Reminder").child(userID);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.i(TAG, "onDataChange: " + dataSnapshot1);
                            Reminder r = dataSnapshot1.getValue(Reminder.class);
                            reminders.add(r);
                    }
                }
                myAdapter = new ReminderAdapter(ReminderActivity.this, reminders, mActivity);
                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupWidgets(){
        mUsername = (TextView) findViewById(R.id.reminderUsername);
        mBack = (ImageView) findViewById(R.id.notificationBack);
    }

    private void setupRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecycleAdapter);
        reminders = new ArrayList<Reminder>();
    }

    private void setupFirebase(){

        mFirebaseMethods = new FirebaseMethods(mContext);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

}
