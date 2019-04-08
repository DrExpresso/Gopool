package firebase.gopool.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import firebase.gopool.R;
import firebase.gopool.Register.RegisterStepFourFragment;
import firebase.gopool.Register.RegisterStepOneFragment;
import firebase.gopool.Register.RegisterStepThreeFragment;
import firebase.gopool.Register.RegisterStepTwoFragment;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.NonSwipeableViewPager;
import firebase.gopool.Utils.SectionsStatePageAdapter;
import firebase.gopool.models.User;

public class RegisterActivity  extends AppCompatActivity implements RegisterStepOneFragment.OnButtonClickListener,
                                                                    RegisterStepTwoFragment.OnButtonClickListener,
                                                                    RegisterStepThreeFragment.OnButtonClickListener,
                                                                    RegisterStepFourFragment.OnButtonClickListener  {

    private static final String TAG = "RegisterActivity";


    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    private String append = "";
    private String username, password, email, fullName, dob, gender, profile_photo, licence_number, registration_plate, car, car_photo, education, work, bio;
    private Long mobileNumber;
    private int seats;
    private Boolean carOwner;

    //Fragment variables
    private SectionsStatePageAdapter pageAdapter;
    private NonSwipeableViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    //Fragments
    private RegisterStepOneFragment mRegisterStepOneFragment;
    private RegisterStepTwoFragment mRegisterStepTwoFragment;
    private RegisterStepThreeFragment mRegisterStepThreeFragment;
    private RegisterStepFourFragment mRegisterStepFourFragment;


    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started.");

        mContext = RegisterActivity.this;
        mFirebaseMethods = new FirebaseMethods(mContext);

        setupFragments();

        //instantiate objects
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.removeableLayout);

        setViewPager(0);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        setupFirebaseAuth();
    }

    private void setupFragments() {
        mRegisterStepOneFragment = new RegisterStepOneFragment();
        mRegisterStepTwoFragment = new RegisterStepTwoFragment();
        mRegisterStepThreeFragment = new RegisterStepThreeFragment();
        mRegisterStepFourFragment = new RegisterStepFourFragment();
        pageAdapter = new SectionsStatePageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(mRegisterStepOneFragment, getString(R.string.fragment)); //fragment 0
        pageAdapter.addFragment(mRegisterStepTwoFragment, getString(R.string.fragment)); //fragment 1
        pageAdapter.addFragment(mRegisterStepThreeFragment, getString(R.string.fragment)); //fragment 2
        pageAdapter.addFragment(mRegisterStepFourFragment, getString(R.string.fragment)); //fragment 3
    }

    private void setViewPager(int fragmentNumber) {
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: navigating to fragment #: " + fragmentNumber);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public void onButtonClicked(View view) {
        int currPos = mViewPager.getCurrentItem();

        switch (view.getId()) {

            case R.id.loginBackArrow:
                //handle currPos is zero
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;

            case R.id.nextBtn1:
                //handle currPos is reached last item
                mViewPager.setCurrentItem(currPos + 1);
                break;

            case R.id.nextBtn2:
                //handle currPos is reached last item
                mViewPager.setCurrentItem(currPos + 1);
                break;

            case R.id.nextBtn3:
                //handle currPos is reached last item
                mViewPager.setCurrentItem(currPos + 1);
                break;

            case R.id.nextBtn4:
                //handle currPos is reached last item
                mViewPager.setCurrentItem(currPos + 1);
                gatherData(); //To create the account on the last step of fragment
                break;
            case R.id.restartRegistrationBtn:
                //handle currPos is zero
                mViewPager.setCurrentItem(0);
                break;

            case R.id.loginBackArrowStep:
                mViewPager.setCurrentItem(currPos - 1);
                break;
        }
    }


    private void gatherData() {

        //Fragment 1 data (main details create account for firebase)
        this.email = mRegisterStepOneFragment.getEmail();
        this.password = mRegisterStepOneFragment.getPassword();

        //Fragment 2 data (username check)
        this.username = mRegisterStepTwoFragment.getUsername();

        //Fragment 3 data (personal info for database)
        this.fullName = mRegisterStepThreeFragment.getFullname();
        this.dob = mRegisterStepThreeFragment.getDob();
        this.mobileNumber = mRegisterStepThreeFragment.getMobileNumber();
        this.gender = mRegisterStepThreeFragment.getGender();
        this.profile_photo = mRegisterStepThreeFragment.getRegistrationPicture();
        this.education = mRegisterStepThreeFragment.getEducation();
        this.work = mRegisterStepThreeFragment.getWork();
        this.bio = mRegisterStepThreeFragment.getBio();

        //Fragment 4 car data (car info for firebase)
        this.licence_number = mRegisterStepFourFragment.getLicence();
        this.car = mRegisterStepFourFragment.getCar();
        this.registration_plate = mRegisterStepFourFragment.getRegistration();
        this.seats = mRegisterStepFourFragment.getSeats();
        this.carOwner = mRegisterStepFourFragment.getCarToggle();
        this.car_photo = mRegisterStepFourFragment.getCarPhoto();

        //Main registration once all data is gathered
        mFirebaseMethods.createAccount(this.email, this.password);
    }

   /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Check is @param username already exists in teh database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("user")
                .orderByChild("username")
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        append = myRef.push().getKey().substring(3,10);
                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                    }
                }

                String mUsername = "";
                mUsername = username + append;

                //add new user to the database
                mFirebaseMethods.addNewUser(email, fullName, mUsername, profile_photo, mobileNumber, dob, licence_number, car, registration_plate, seats, education, work, bio, carOwner, gender, car_photo);

                Toast.makeText(mContext, "Signup successful. You may login now!.", Toast.LENGTH_SHORT).show();

                mAuth.signOut();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
