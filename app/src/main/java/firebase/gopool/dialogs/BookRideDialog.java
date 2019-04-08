package firebase.gopool.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import firebase.gopool.Account.ProfileActivity;
import firebase.gopool.Payment.PaymentActivity;
import firebase.gopool.R;
import firebase.gopool.Utils.SectionsStatePageAdapter;

public class BookRideDialog extends Dialog implements
        View.OnClickListener  {

    private static final String TAG = "ViewRideCreatedDialog";
    public Context c;
    public Dialog d;

    // variables
    private TextView mUsername, mRidesCompleted, mCost, mDepartureTime, mExtraTime, mFromStreet, mFromPostcode, mFromCity, mToStreet, mToPostcode, mToCity, mCancelDialogBtn, mDurationTextview, mPickupLocation;
    private RatingBar mRatingBar;
    private Button mEditRideBtn;
    private SectionsStatePageAdapter pageAdapter;
    private String rides, seats, from, to, date, cost, username, pickupTime, extraTime, rideID, duration, userID, profile_photo, completedRides, pickupLocation, dateOnly, licencePlate;
    private Float rating;
    private FloatingActionButton mViewProfileBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ride_confirm);

        setupWidgets();

        mCancelDialogBtn.setOnClickListener(this);
        mEditRideBtn.setOnClickListener(this);
        mViewProfileBtn.setOnClickListener(this);
    }

    public BookRideDialog(Context a, String rideID, String username, String licencePlate, String rides, String seats, String from, String to, String date, String dateOnly, String cost, Float rating, String pickupTime, String extraTime, String duration, String userID,
                          String profile_photo, String completedRides, String pickupLocation) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.rideID = rideID;
        this.username = username;
        this.rides = rides;
        this.seats = seats;
        this.from = from;
        this.to = to;
        this.date = date;
        this.dateOnly = dateOnly;
        this.cost = cost;
        this.rating = rating;
        this.extraTime = extraTime;
        this.pickupTime = pickupTime;
        this.duration = duration;
        this.userID = userID;
        this.profile_photo = profile_photo;
        this.completedRides = completedRides;
        this.pickupLocation = pickupLocation;
        this.licencePlate = licencePlate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogConfirm:
                showDialog();
                break;
            case R.id.dialogCancel:
                dismiss();
                break;
            case R.id.viewProfileBtn:
                showIntentProfile();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void showDialog(){
        Intent intent = new Intent(c, PaymentActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("currentLocation", from);
        intent.putExtra("destination", to);
        intent.putExtra("dateOfJourney", date);
        intent.putExtra("dateOnly", dateOnly);
        intent.putExtra("rideID", rideID);
        intent.putExtra("profile_photo", profile_photo);
        intent.putExtra("pickupLocation", pickupLocation);
        intent.putExtra("pickupTime", pickupTime);
        intent.putExtra("licencePlate", licencePlate);
        intent.putExtra("cost", cost);
        c.startActivity(intent);
    }

    private void showIntentProfile(){
        //Confirmation to delete the ride dialog
        Intent intent = new Intent(c, ProfileActivity.class);
        intent.putExtra("userID", userID);
        c.startActivity(intent);
    }


    private void setupWidgets(){
        //Setup widgets
        mUsername = (TextView) findViewById(R.id.usernameTxt);
        mRidesCompleted = (TextView) findViewById(R.id.completedRidesTxt);
        mCost = (TextView) findViewById(R.id.costTxt);
        mDepartureTime = (TextView) findViewById(R.id.timeTxt);
        mExtraTime = (TextView) findViewById(R.id.extraTimeTxt);
        mFromStreet = (TextView) findViewById(R.id.streetNameTxt);
        mToStreet = (TextView) findViewById(R.id.streetName2Txt);
        mPickupLocation = (TextView) findViewById(R.id.pickupLocationConfirm);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);


        mEditRideBtn = (Button) findViewById(R.id.dialogConfirm);
        mCancelDialogBtn = (TextView) findViewById(R.id.dialogCancel);
        mDurationTextview = (TextView) findViewById(R.id.durationConfirm);
        mViewProfileBtn = (FloatingActionButton) findViewById(R.id.viewProfileBtn);


        mCost.setText(cost);
        mUsername.setText(username);
        mRatingBar.setRating(rating);
        mDepartureTime.setText(pickupTime);
        mExtraTime.setText(extraTime);
        mFromStreet.setText(from);
        mToStreet.setText(to);
        mDurationTextview.setText("Duration: " + duration);
        mRidesCompleted.setText(completedRides + " Rides");
        mPickupLocation.setText("Pickup: " + pickupLocation);
    }

}
