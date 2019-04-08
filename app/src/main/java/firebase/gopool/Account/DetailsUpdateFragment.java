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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.models.User;

public class DetailsUpdateFragment extends Fragment {

    private static final String TAG = "DetailsUpdateFragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    //Fragment view
    private View view;
    private CircleImageView mProfilePhoto;
    private EditText mUsername, mFullname, mMobileNumber, mDob;
    private TextView mChangePhoto;
    private Button mSnippetDetailsBtn;

    //vars
    private User mUserSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_details, container, false);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_change);
        mUsername = (EditText) view.findViewById(R.id.usernameEditText);
        mFullname = (EditText) view.findViewById(R.id.fullnameEditText);
        mMobileNumber = (EditText) view.findViewById(R.id.phoneEditText);
        mDob = (EditText) view.findViewById(R.id.dobEditText);
        mChangePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        mFirebaseMethods = new FirebaseMethods(getActivity());

        mSnippetDetailsBtn = (Button) view.findViewById(R.id.snippetDetailsBtn);
        mSnippetDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });

        setupFirebaseAuth();


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

        return view;
    }

    /**
     * Retrieves the data inside the widgets and saves it to the database.
     */
    private void saveProfileSettings(){
        final String username = mUsername.getText().toString();
        final String fullName = mFullname.getText().toString();
        final String mobileNumber = mMobileNumber.getText().toString();
        final Long dob = Long.parseLong(mDob.getText().toString());

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                //If the user is the current one then no changes have been made
                if (!mUserSettings.getUsername().equals(username)){

                    checkIfUsernameExists(username);
                }
                //user changed there username, checking for uniquness
                else {

                }

                //user did not change there username and email

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        UniversalImageLoader.setImage(user.getProfile_photo(), mProfilePhoto, null,"");

        mUsername.setText(user.getUsername());
        mFullname.setText(user.getFull_name());
        mMobileNumber.setText(String.valueOf(user.getMobile_number()));
        mDob.setText(String.valueOf(user.getDob()));

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
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
}
