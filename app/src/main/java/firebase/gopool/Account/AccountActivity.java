package firebase.gopool.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import firebase.gopool.Leaderboard.LeaderboardActivity;
import firebase.gopool.Login.LoginActivity;
import firebase.gopool.R;
import firebase.gopool.Settings.SettingsActivity;
import firebase.gopool.Utils.BottomNavigationViewHelper;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.NonSwipeableViewPager;
import firebase.gopool.Utils.SectionsStatePageAdapter;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.models.User;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "AccountActivity";
    private static final int ACTIVITY_NUMBER = 3;

    private Context mContext = AccountActivity.this;

    private SectionsStatePageAdapter pageAdapter;
    private NonSwipeableViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private BottomNavigationView bottomNavigationView;

    //activity widgets
    private Button mEmailUpdateButton, mPasswordUpdateButton, mDetailsUpdateButton, mCarUpdateButton, mSignoutButton, mSettingsBtn, mHelpBtn, mAddPaymentInformationBtn;
    private ImageView profilePhoto, leaderboards;
    private TextView mDisplayUsername, mCompleteRides, mEmail;
    private RatingBar mRatingBar;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Log.d(TAG, "onCreate: started.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);

        if (mAuth.getCurrentUser() != null){
            //Gets userID of current user signed in
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        checkNotifications();
        setupFirebaseAuth();
        setupFragments();
        setupBottomNavigationView();
        setupAtivityWidgets();

        // OnClick Listener to navigate to the fragments
        mEmailUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPager(0);
            }
        });

        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        mSignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseMessaging.getInstance().unsubscribeFromTopic(userID);

                mAuth.signOut();
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, HelpFragment.class);
                startActivity(intent);
            }
        });


        leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        mPasswordUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPager(1);
            }
        });

        mDetailsUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPager(2);
            }
        });

        mAddPaymentInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        mCarUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPager(3);
            }
        });
    }

    private void setupFragments() {
        pageAdapter = new SectionsStatePageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new EmailUpdateFragment(), getString(R.string.edit_email)); //fragment 0
        pageAdapter.addFragment(new PasswordUpdateFragment(), getString(R.string.edit_password));  //fragment 1
        pageAdapter.addFragment(new DetailsUpdateFragment(),  getString(R.string.edit_details));  //fragment 2
        pageAdapter.addFragment(new CarUpdateFragment(),  getString(R.string.car_information));  //fragment 3
    }

    private void setViewPager(int fragmentNumber) {
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: navigating to fragment #: " + fragmentNumber);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setCurrentItem(fragmentNumber);
    }

    private void setupAtivityWidgets(){
        Log.d(TAG, "setupAtivityWidgets: setting up widgets");
        //instantiate objects
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayout1);
        mEmailUpdateButton = (Button) findViewById(R.id.updateEmailButton);
        mPasswordUpdateButton = (Button) findViewById(R.id.updatePasswordButton);
        mDetailsUpdateButton = (Button) findViewById(R.id.updateDetailsButton);
        mAddPaymentInformationBtn = (Button) findViewById(R.id.addPaymentInformationBtn);
        mCarUpdateButton = (Button) findViewById(R.id.updateCarDetailsButton);
        profilePhoto = (ImageView) findViewById(R.id.profile_image);
        mSignoutButton = (Button) findViewById(R.id.signoutButton);
        mDisplayUsername = (TextView) findViewById(R.id.displayUsername);
        mEmail = (TextView) findViewById(R.id.email_textview);
        mCompleteRides = (TextView) findViewById(R.id.rides_textview);
        mSettingsBtn = (Button) findViewById(R.id.settingsBtn);
        mHelpBtn = (Button) findViewById(R.id.helpBtn);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        leaderboards = (ImageView) findViewById(R.id.leaderboards);
    }


    private void setProfileWidgets(User userSettings){
        Log.d(TAG, "setProfileWidgets: setting user widgets from firebase data");

        User user = userSettings;

        UniversalImageLoader.setImage(user.getProfile_photo(), profilePhoto, null,"");

        mDisplayUsername.setText(user.getUsername());
        mCompleteRides.setText(user.getCompletedRides() + " rides");
        mEmail.setText(user.getEmail());
        mRatingBar.setRating(user.getUserRating());
    }

    private void setupBadge(int reminderLength){
        if (reminderLength > 0){
            //Adds badge and notification number to the BottomViewNavigation
            BottomNavigationViewHelper.addBadge(mContext, bottomNavigationView, reminderLength);
        }
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

    /** --------------------------- Firebase ---------------------------- **/

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Checks if there are notifications available for the current logged in user.
     */
    private void checkNotifications(){
        mRef.child("Reminder").child(userID).addValueEventListener(new ValueEventListener() {
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
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}
