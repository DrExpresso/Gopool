package firebase.gopool.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import firebase.gopool.Account.HelpFragment;
import firebase.gopool.Booked.BookedActivity;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.SectionsStatePageAdapter;

public class LeaveRideDialog extends Dialog implements
        View.OnClickListener  {

    private static final String TAG = "LeaveRideDialog";
    public Context c;
    public Dialog d;
    private TextView cancelDialog;
    private Button confirmDialog;
    private String currentUserID, rideID;

    //Firebase
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;

    private int seatsAvailable = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_leave_ride);
        cancelDialog = (TextView) findViewById(R.id.dialogCancel);
        confirmDialog = (Button) findViewById(R.id.dialogConfirm);
        cancelDialog.setOnClickListener(this);
        confirmDialog.setOnClickListener(this);

        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();

        getsSeatsRemaining();
    }

    public LeaveRideDialog(Context a, String currentUserID, String rideID) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.currentUserID = currentUserID;
        this.rideID = rideID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogConfirm:
                leaveRide();
                dismiss();
                updateSeatsRemaining();
                ((Activity) c).finish();
                Intent intent = new Intent(c, BookedActivity.class);
                ((Activity) c).startActivity(intent);
                break;
            case R.id.dialogCancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void leaveRide(){
        mRef.child("requestRide").child(rideID).child(currentUserID).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(c, "Left ride successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getsSeatsRemaining(){
        mRef.child("availableRide").child(rideID).child("seatsAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                seatsAvailable = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateSeatsRemaining(){
        mRef.child("availableRide").child(rideID).child("seatsAvailable").setValue(seatsAvailable + 1);
    }

}
