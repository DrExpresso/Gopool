package firebase.gopool.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import firebase.gopool.Adapter.SearchAdapter;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.models.Ride;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultsActivity";

    //Recycle View variables
    private Context mContext = SearchResultsActivity.this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecycleAdapter;
    private SearchAdapter myAdapter;
    private ArrayList<Ride> rides;

    //Firebase variables
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;

    //Variables
    private String user_id, location, destination, date, genderCurrentUser, genderDriverUser, user_id_driver;
    private RelativeLayout mNoResultsFoundLayout;
    private Boolean sameGender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        setContentView(R.layout.fragment_search_results);
        getActivityData();

        //Setup firebase object
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        if (mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
        }

        //setup widgets
        mNoResultsFoundLayout = (RelativeLayout) findViewById(R.id.noResultsFoundLayout);


        //Setup recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecycleAdapter);
        rides = new ArrayList<Ride>();


        mRef.child("availableRide").orderByChild("destination").equalTo(destination).limitToFirst(20)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Ride r = dataSnapshot1.getValue(Ride.class);
                                    if (r.getSeatsAvailable() > 0) {
                                        Date aParsed = null;
                                        Date bParsed = null;
                                        try {
                                            aParsed = parseDate(r.getDateOfJourney());
                                            bParsed = parseDate(date);
                                            Log.i(TAG, "onDataChange: " + aParsed + bParsed);
                                            if (aParsed.after(bParsed) || aParsed.equals(bParsed)) {
                                                if (!(r.getUser_id().contains(user_id))) {
                                                    rides.add(r);
                                                    mNoResultsFoundLayout.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                }
                            }
                            myAdapter = new SearchAdapter(SearchResultsActivity.this, rides);
                            mRecyclerView.setAdapter(myAdapter);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SearchResultsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

        //Setup back arrow for navigating back to 'ProfileActivity'
        ImageView backArrow = (ImageView) findViewById(R.id.backArrowSearchRide);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ImageView exitSearchRide = (ImageView) findViewById(R.id.exitSearchRide);
        exitSearchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getActivityData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            location = getIntent().getStringExtra("DESTINATION");
            destination = getIntent().getStringExtra("LOCATION");
            sameGender = getIntent().getExtras().getBoolean("sameGender");
            date = getIntent().getStringExtra("DATE");
        }
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter.parse(date);
    }


    /***
     *  Setup the firebase object
     */
    @Override
    public void onStart() {
        super.onStart();
    }




}
