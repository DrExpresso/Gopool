package firebase.gopool.Account;

import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.dialogs.ConfirmPasswordDialog;
import firebase.gopool.models.User;

public class PasswordUpdateFragment extends Fragment {

    private static final String TAG = "PasswordUpdateFragment";

    //Fragment view
    private View view;

    //widgets
    private Button mSnippetPasswordBtn;
    private EditText mPassword;

    //vars
    private User mUserSettings;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;
    private PasswordUpdateFragment mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_password, container, false);

        mContext = PasswordUpdateFragment.this;

        //Firebase setup
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        mFirebaseMethods = new FirebaseMethods(getActivity());

        mPassword = (EditText) view.findViewById(R.id.passwordEditTextSnippet);



        mSnippetPasswordBtn = (Button) view.findViewById(R.id.snippetPasswordBtn);
        mSnippetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePasswordSettings();
            }
        });


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

        setupFirebaseAuth();

        return view;
    }

    /**
     * Retrieves the data inside the widgets and saves it to the database.
     */
    private void savePasswordSettings(){
        final String password = mPassword.getText().toString();

        //Check if the email exists in the database
        if (!mUserSettings.getEmail().equals(password)){

        }
        else {

        }
    }

    private void setProfileWidgets(User userSettings){
        Log.d(TAG, "setProfileWidgets: setting user widgets from firebase data");

        User user = userSettings;

        mUserSettings = userSettings;
    }


    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        userID = mAuth.getCurrentUser().getUid();
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
