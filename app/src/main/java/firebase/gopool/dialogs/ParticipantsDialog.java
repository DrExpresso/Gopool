package firebase.gopool.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import firebase.gopool.Adapter.BookingAdapter;
import firebase.gopool.Adapter.ParticipantsAdapter;
import firebase.gopool.Booked.BookedActivity;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.models.BookingResults;
import firebase.gopool.models.Participants;
import firebase.gopool.models.User;
import firebase.gopool.models.UserReview;

public class ParticipantsDialog extends Dialog implements
        View.OnClickListener  {

    private static final String TAG = "ParticipantsDialog";
    public Context c;
    public Dialog d;

    //View variables
    private RelativeLayout mUserBooked[];
    private ImageView mProfilePicture[], user_id_1;
    private TextView mUsernames[], username1;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecycleAdapter;
    private ParticipantsAdapter myAdapter;
    private ArrayList<Participants> participants;

    // variables
    private TextView mCancelDialogBtn;
    private String userID, rideID;

    //Firebase
    private FirebaseMethods mFirebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_participants);

        mFirebaseMethods = new FirebaseMethods(c);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();

        setupWidgets();

        findDriverInformation();

        findParticipantDetails();

        mCancelDialogBtn = (TextView) findViewById(R.id.dialogCancel);
        mCancelDialogBtn.setOnClickListener(this);

    }

    public ParticipantsDialog(Context a, String userID, String rideID) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.userID = userID;
        this.rideID = rideID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogCancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void setupWidgets(){
        username1 = (TextView) findViewById(R.id.username1);
        user_id_1 = (ImageView) findViewById(R.id.user_id_1);

        //Setup recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(c);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(c));
        mRecyclerView.setAdapter(mRecycleAdapter);
        participants = new ArrayList<Participants>();
    }

    private void findDriverInformation(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setDriverWidgets(mFirebaseMethods.getSpeficUserSettings(dataSnapshot, userID));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDriverWidgets(User userSettings){
        Log.d(TAG, "setProfileWidgets: setting user widgets from firebase data");

        User user = userSettings;

        UniversalImageLoader.setImage(user.getProfile_photo(), user_id_1, null,"");

        username1.setText(user.getUsername());
    }

    /**
     * This method will find all the participants information including username and profile picture
     * and populate the recycler view with the information. The condition of accepted must be true
     * otherwise they have not be included by the user.
     */
    private void findParticipantDetails(){
        mRef.child("requestRide").child(rideID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Participants r = dataSnapshot1.getValue(Participants.class);
                        if (r.getAccepted().equals(true)){
                            participants.add(r);
                        }
                    }
                }

                myAdapter = new ParticipantsAdapter(c, participants);
                mRecyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}
