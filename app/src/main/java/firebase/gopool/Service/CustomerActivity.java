package firebase.gopool.Service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.gopool.R;
import firebase.gopool.Remote.IFCMService;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.models.User;

public class CustomerActivity extends AppCompatActivity {
    private static final String TAG = "CustomerActivity";

    private TextView txtUsername, txtTo, txtFrom;
    private String title, body, username, profile_photo, to, from, userID, rideID;
    private Boolean rideAccepted;

    //Widgets
    private FloatingActionButton acceptBtn, declineBtn;
    private CircleImageView mRequestProfilePhoto;

    //Firebase
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseMethods mFirebaseMethods;

    private Context mContext = CustomerActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        final CustomerActivity c = this;

        getActivityData();
        setupDialog();

        mFirebaseMethods = new FirebaseMethods(mContext);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        acceptBtn = (FloatingActionButton) findViewById(R.id.confirmRideBtn);
        declineBtn = (FloatingActionButton) findViewById(R.id.declineRideBtn);
        mRequestProfilePhoto = (CircleImageView)findViewById(R.id.requestProfilePhoto);
        txtTo = (TextView) findViewById(R.id.to);
        txtFrom = (TextView) findViewById(R.id.from);
        txtUsername = (TextView) findViewById(R.id.message);
        txtUsername.setText("Hi, i'm " + username + " and would like to request a seat on your journey!");


        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRide();
            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineRide();
            }
        });
//     txtAddress = (TextView) findViewById(R.id.txtAddress);
//     txtTime.setText(title);
//     txtDistance.setText(body);
       txtTo.setText("To: " + to);
       txtFrom.setText("From: " + from);

        UniversalImageLoader.setImage(profile_photo, mRequestProfilePhoto, null,"");
    }

    private void getActivityData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = getIntent().getStringExtra("title");
            body = getIntent().getStringExtra("userID");
            username = getIntent().getStringExtra("username");
            profile_photo = getIntent().getStringExtra("profile_photo");
            to = getIntent().getStringExtra("to").replaceAll("\n", ", ");
            from = getIntent().getStringExtra("from").replaceAll("\n", ", ");
            rideID = getIntent().getStringExtra("rideID");
        }
    }

    private void setupDialog(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void acceptRide(){
        myRef.child("requestRide")
                .child(rideID)
                .child(userID)
                .child("accepted")
                .setValue(true);

        //Will close the intent when the ride is accepted
        finish();
    }

    private void declineRide(){

        myRef.child("requestRide")
                .child(rideID)
                .child(userID)
                .removeValue();

        //Will close the intent when the ride is accepted
        finish();
    }


}
