package firebase.gopool.Leaderboard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import firebase.gopool.Adapter.LeaderboardsAdapter;
import firebase.gopool.Adapter.SearchAdapter;
import firebase.gopool.Home.SearchResultsActivity;
import firebase.gopool.R;
import firebase.gopool.models.Leaderboards;
import firebase.gopool.models.Ride;

public class LeaderboardActivity extends AppCompatActivity {
    private static final String TAG = "LeaderboardActivity";
    private Context mContext = LeaderboardActivity.this;

    //Widgets
    private ImageView backBtn;

    //Recycle View variables
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecycleAdapter;
    private LeaderboardsAdapter myAdapter;
    private ArrayList<Leaderboards> leaderboards;

    //Firebase variables
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        setupWidgets();
        setupFirebase();

        //Setup back arrow for navigating back to 'AccountActivity'
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final List<Leaderboards> model = new ArrayList<>();


        mRef = FirebaseDatabase.getInstance().getReference().child("user");
        mRef.orderByChild("points").limitToFirst(20)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Leaderboards l = dataSnapshot1.getValue(Leaderboards.class);
                                leaderboards.add(l);
                            }
                            Collections.sort(leaderboards, new Comparator<Leaderboards>() {
                                @Override
                                public int compare(Leaderboards o1, Leaderboards o2) {
                                    return o1.getPoints() > o2.getPoints() ? -1 : (o1.points < o2.points ) ? 1 : 0;
                                }

                            });

                            myAdapter = new LeaderboardsAdapter(mContext, leaderboards);
                            mRecyclerView.setAdapter(myAdapter);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupWidgets(){
        backBtn = (ImageView) findViewById(R.id.leaderboardsBackBtn);

        //Setup recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecycleAdapter);
        leaderboards = new ArrayList<Leaderboards>();
    }

    private void setupFirebase(){
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
        }

    }

}
