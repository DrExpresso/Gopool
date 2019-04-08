package firebase.gopool.Rides;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import firebase.gopool.Adapter.ReminderAdapter;
import firebase.gopool.R;
import firebase.gopool.Reminder.ReminderActivity;
import firebase.gopool.Utils.BadgeView;
import firebase.gopool.Utils.BottomNavigationViewHelper;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Adapter.MyAdapter;
import firebase.gopool.models.Reminder;
import firebase.gopool.models.Ride;

public class RidesActivity extends AppCompatActivity {

    private static final String TAG = "RidesActivity";
    private static final int ACTIVITY_NUMBER = 1;

    //View variables
    private BottomNavigationView bottomNavigationView;
    private RelativeLayout mNoResultsFoundLayout;
    private ImageView mNotificationBtn;

    //Recycle View variables
    private Context mContext = RidesActivity.this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecycleAdapter;
    private MyAdapter myAdapter;
    private ArrayList<Ride> rides;

    //Firebase variables
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;

    private String user_id;
    private int reminderLength = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        setContentView(R.layout.activity_rides);
        setupBottomNavigationView();


        //Setup firebase object
        mAuth = FirebaseAuth.getInstance();
        mFirebaseMethods = new FirebaseMethods(mContext);
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        if (mAuth.getCurrentUser() != null) {
            user_id = mFirebaseMethods.getUserID();
        }

        checkNotifications();


        //Setup recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecycleAdapter);
        rides = new ArrayList<Ride>();

        mNoResultsFoundLayout = (RelativeLayout) findViewById(R.id.noResultsFoundLayout);
        mNotificationBtn = (ImageView) findViewById(R.id.notificationBtn) ;
        mNotificationBtn.setPadding(0, 0, 10, 0);
        mNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReminderActivity.class);
                startActivity(intent);
            }
        });

        mRef = FirebaseDatabase.getInstance().getReference().child("availableRide");

        mRef.orderByChild("user_id").equalTo(user_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    String rideID = dataSnapshot1.getKey();
                                    Log.i(TAG, "rideID: "+ rideID);
                                    Ride r = dataSnapshot1.getValue(Ride.class);
                                    rides.add(r);

                                    mNoResultsFoundLayout.setVisibility(View.INVISIBLE);
                            }
                            myAdapter = new MyAdapter(RidesActivity.this, rides);
                            mRecyclerView.setAdapter(myAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RidesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    /***
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);

        //Change current highlighted icon
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }


    private void setupBadge(int reminderLength){
        if (reminderLength > 0){
            //Adds badge to the notification imageview on the toolbar
            BadgeView badgeView = new BadgeView(mContext, mNotificationBtn);
            badgeView.setTextSize(10);
            badgeView.setTextColor(Color.parseColor("#ffffff"));
            badgeView.setBadgeBackgroundColor(Color.parseColor("#ff0000"));
            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badgeView.setText(String.valueOf(reminderLength));
            badgeView.setBadgeMargin(5, 0);
            badgeView.show();

            //Adds badge and notification number to the BottomViewNavigation
            BottomNavigationViewHelper.addBadge(mContext, bottomNavigationView, reminderLength);
        } else {

        }
    }

    /**
     * Checks if there are notifications available for the current logged in user.
     */
    private void checkNotifications(){
        mRef.child("Reminder").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int reminderLength = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        reminderLength++;
                    }
                }
                //Passes the number of notifications onto the setup badge method
                setupBadge(reminderLength);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /***
     *  Setup the firebase object
     */
    @Override
    public void onStart() {
        super.onStart();
    }


}
