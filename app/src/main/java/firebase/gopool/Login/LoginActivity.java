package firebase.gopool.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import firebase.gopool.Home.HomeActivity;
import firebase.gopool.R;
import firebase.gopool.Register.RegisterStepFourFragment;
import firebase.gopool.Register.RegisterStepOneFragment;
import firebase.gopool.Register.RegisterStepThreeFragment;
import firebase.gopool.Register.RegisterStepTwoFragment;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.NonSwipeableViewPager;
import firebase.gopool.Utils.SectionsStatePageAdapter;
import firebase.gopool.models.User;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private CardView mLoadingCardView;
    private EditText mEmail, mPassword;
    private TextView mPleaseWait, mBtn_signup;
    private String append = "";
    private String username;
    private String email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: started.");

        mContext = LoginActivity.this;

        setupFirebaseAuth();


        mAuth = FirebaseAuth.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.loginRequestProgressBar);
        mPleaseWait = (TextView) findViewById(R.id.loadingPleaseWait);
        mLoadingCardView = (CardView) findViewById(R.id.card_view_loading);
        mEmail = (EditText) findViewById(R.id.emailEditText);
        mPassword = (EditText) findViewById(R.id.passwordEditText);
        mBtn_signup = (TextView) findViewById(R.id.btn_signup);


        mProgressBar.setVisibility(GONE);
        mLoadingCardView.setVisibility(GONE);
        mPleaseWait.setVisibility(GONE);

        init();
    }

    private boolean isStringNull(String string) {
        Log.d(TAG, "isStringNull: checking string if null");
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }



    /**
     * --------------------------- Firebase ----------------------------
     **/

    private void init() {
        //Initialize the button for logging in
        Button mBtn_login = (Button) findViewById(R.id.btn_login);
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to login in.");

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (isStringNull(email) && isStringNull(password)) {
                    Toast.makeText(mContext, "You must fill in empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(email, password);
                }
            }
        });

        mBtn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to fragment 0");
               Intent intent = new Intent(mContext, RegisterActivity.class);
               startActivity(intent);
            }
        });

//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
    }

    /**
     * Sign in method for firebase auth
     *
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {
        mProgressBar.setVisibility(View.VISIBLE);
        mPleaseWait.setVisibility(View.VISIBLE);
        mLoadingCardView.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            try {
                                boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                                Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                                Log.d(TAG, "onComplete: success. email is verified");
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);

                            } catch (NullPointerException e) {
                                Log.e(TAG, "instance initializer: NullPointerException " + e.getMessage());
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                            mProgressBar.setVisibility(GONE);
                            mPleaseWait.setVisibility(GONE);
                            mLoadingCardView.setVisibility(GONE);

                        }
                    }
                });
    }

    /***
     *  If the user is logged in then navigate to 'HomeActivity' and call finish()
     * @param user
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }



    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
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