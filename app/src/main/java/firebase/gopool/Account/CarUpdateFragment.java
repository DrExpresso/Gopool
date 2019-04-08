package firebase.gopool.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.models.User;

public class CarUpdateFragment extends Fragment {

    private static final String TAG = "CarUpdateFragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    //Fragment view
    private View view;
    private CircleImageView mCarPhoto;
    private EditText mCar, mRegistration, mLicence, mSeats;
    private Button mSnippetCarBtn;
    private RadioGroup mCarOwnerRadioGroup;
    private RadioButton ownerYesButton, ownerNoButton;
    private RelativeLayout mCarDetailsLayout;

    //vars
    private User mUserSettings;
    private Boolean carOwner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_car, container, false);
        mCarPhoto = (CircleImageView) view.findViewById(R.id.uploadCarPicture);
        mCar = (EditText) view.findViewById(R.id.carEditText);
        mRegistration = (EditText) view.findViewById(R.id.registrationEditText);
        mLicence = (EditText) view.findViewById(R.id.licenceEditText);
        mSeats = (EditText) view.findViewById(R.id.seatsEditText);
        mCarOwnerRadioGroup = (RadioGroup) view.findViewById(R.id.carToggle);
        ownerYesButton = (RadioButton) view.findViewById(R.id.yesCarButton);
        ownerNoButton = (RadioButton) view.findViewById(R.id.noCarButton);
        mCarDetailsLayout = (RelativeLayout) view.findViewById(R.id.carDetailsLayout);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        mFirebaseMethods = new FirebaseMethods(getActivity());


        mSnippetCarBtn = (Button) view.findViewById(R.id.snippetCarDetailsBtn);
        mSnippetCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });

        setupFirebaseAuth();

        getUserInformation();


        //Setup back arrow for navigating back to 'ProfileActivity'
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                Intent intent = new Intent(view.getContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        mCarOwnerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.yesCarButton:
                        ownerYesButton.setChecked(true);
                        mCarDetailsLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.noCarButton:
                        ownerNoButton.setChecked(true);
                        mCarDetailsLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        return view;
    }

    /**
     * Retrieves the data inside the widgets and saves it to the database.
     */
    private void saveProfileSettings(){
        final String car = mCar.getText().toString();
        final String registration = mRegistration.getText().toString();
        final String licence = mLicence.getText().toString();
        final int seats = Integer.parseInt(mSeats.getText().toString());

//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//
//
//                //If the user is the current one then no changes have been made
//                if (!mUserSettings.getUsername().equals(username)){
//
//                    checkIfUsernameExists(username);
//                }
//                //user changed there username, checking for uniquness
//                else {
//
//                }
//
//                //user did not change there username and email
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    /***
     * checks if @param username already exisits in the database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if:  " + username + "already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("user")
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    //add the username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "Username changed", Toast.LENGTH_SHORT).show();

                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: found a match "+ singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "Username already exists", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(User userSettings){
        Log.d(TAG, "setProfileWidgets: setting user widgets from firebase data");

        User user = userSettings;

        mUserSettings = userSettings;

        UniversalImageLoader.setImage(user.getCar_photo(), mCarPhoto, null,"");

        mCar.setText(user.getCar());
        mRegistration.setText(user.getRegistration_plate());
        mLicence.setText(user.getLicence_number());
        mSeats.setText(String.valueOf(user.getSeats()));

        mCarPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: changing profile photo");
//                Intent intent = new Intent(getActivity(), ShareActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //268435456
//                getActivity().startActivity(intent);
//                getActivity().finish();
            }
        });
    }


    /** --------------------------- Firebase ---------------------------- **/

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        userID = mAuth.getCurrentUser().getUid();

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

    /***
     *  Setup the firebase object
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void getUserInformation() {
        mRef.child("user").child(userID).child("carOwner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carOwner = dataSnapshot.getValue(Boolean.class);
                if (carOwner == false) {
                    ownerNoButton.setChecked(true);
                    mCarDetailsLayout.setVisibility(View.GONE);
                    mCar.setText("");
                    mRegistration.setText("");
                    mLicence.setText("");
                    mSeats.setText("");
                    mCarPhoto.setImageDrawable(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
