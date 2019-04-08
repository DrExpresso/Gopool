package firebase.gopool.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import firebase.gopool.Login.LoginActivity;
import firebase.gopool.R;
import firebase.gopool.Settings.SettingsActivity;
import firebase.gopool.Utils.BottomNavigationViewHelper;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.NonSwipeableViewPager;
import firebase.gopool.Utils.SectionsStatePageAdapter;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.models.User;
import firebase.gopool.models.UserReview;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private Context mContext = ProfileActivity.this;

    private ImageView profilePhoto, mBackBtn;
    private TextView mDisplayUsername, mPersonalBio, mEducationTextview, mWorkTextview, mReview1, mReview2, mReview3;
    private RatingBar mRatingBar;
    private LinearLayout mReviewLayout;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;
    private HashMap<Integer, TextView> textViewHashMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

//        TextView textView1 =  new TextView(this);
//        textViewHashMap.put(1, textView1);
//        TextView textView2 =  new TextView(this);
//        textViewHashMap.put(2, textView2);
//        TextView textView3 =  new TextView(this);
//        textViewHashMap.put(2, textView3);

        setupAtivityWidgets();

//        mReviewLayout.addView(textView1);
//        mReviewLayout.addView(textView2);
//        mReviewLayout.addView(textView3);
        getActivityData();
        //fetchUserReviews();
        setupFirebaseAuth();

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getActivityData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = getIntent().getStringExtra("userID");
        }
    }


    private void setupAtivityWidgets(){
        Log.d(TAG, "setupAtivityWidgets: setting up widgets");
        //instantiate objects
        mBackBtn = (ImageView) findViewById(R.id.backBtn);
        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
        mDisplayUsername = (TextView) findViewById(R.id.nameTextview);
        mPersonalBio = (TextView) findViewById(R.id.personalBio);
        mEducationTextview = (TextView) findViewById(R.id.educationTextview);
        mPersonalBio = (TextView) findViewById(R.id.personaBioText);
        mWorkTextview = (TextView) findViewById(R.id.workTextview);
        //mReview1 = (TextView) findViewById(R.id.review1);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        //mReviewLayout = (LinearLayout) findViewById(R.id.reviewLayout);
        //mReview1.setVisibility(View.INVISIBLE);
    }


    private void setProfileWidgets(User userSettings){
        Log.d(TAG, "setProfileWidgets: setting user widgets from firebase data");

        User user = userSettings;

        UniversalImageLoader.setImage(user.getProfile_photo(), profilePhoto, null,"");

        mDisplayUsername.setText(user.getUsername());
        mRatingBar.setRating(user.getUserRating());
        mPersonalBio.setText(user.getBio());
        mEducationTextview.setText(user.getEducation());
        mWorkTextview.setText(user.getWork());
    }

    /**
     * Fetches the first 3 user reviews if there are any available
     */
    private void fetchUserReviews(){

        final UserReview userReview = new UserReview();

        mRef.child("user").child(userID).child("userReviews").limitToFirst(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot1 : ds.getChildren()){
                            userReview.setComment(dataSnapshot1.getValue().toString());
                            userReview.setRating(Float.parseFloat(dataSnapshot1.getKey()));

                            int textViewCount = 1;


                            textViewHashMap.get(textViewCount).setText(userReview.getComment());
                            Log.i(TAG, "onDataChange: " + userReview.toString());

                            textViewCount++;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /** --------------------------- Firebase ---------------------------- **/

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getSpeficUserSettings(dataSnapshot, userID));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
