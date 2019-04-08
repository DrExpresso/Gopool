package firebase.gopool.Home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.gopool.Common.ApplicationContext;
import firebase.gopool.Pickup.PickupLocationActivity;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.Utils.UniversalImageLoader;
import firebase.gopool.dialogs.OfferRideCreatedDialog;
import firebase.gopool.models.User;

public class EditRideActivity extends AppCompatActivity {

    private static final String TAG = "EditRideActivity";
    private EditRideActivity mContext = EditRideActivity.this;;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    //Widgets
    private EditText mDateOfJourneyEditText, mCostEditText, mPickupEditText, mExtraTimeEditText, mLuggageEditText, mPickupLocationEditText;
    private MaterialAnimatedSwitch mSameGender;
    private Button mSnippetOfferRideButton;
    private Boolean sameGenderBoolean = false;
    private Calendar mCalandar;
    private DatePickerDialog.OnDateSetListener date;
    private CircleImageView mCarPhoto;
    private TextView mLicencePlateEditText, mCarEditText, mSeatsEditText, mDestinationEditText, mFromEditText, mUsername, durationTxt;


    //vars
    private User mUserSettings;
    private String destinationId, locationId, profile_photo, username, pickupTimeID, costID, dateOfJourneyID, lengthOfJourneyID,
            extraTimeID, licencePlateID, carID, luggageID, destinationId2, locationId2, duration, pickupLocation;
    private int userRating, seatsID;
    private int completeRides;
    private double currentLatitude, currentLongtitude;
    private LatLng currentLocation;


    //GeoFire
    private DatabaseReference mRef;
    private GeoFire mGeoFire;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_ride);
        Log.d(TAG, "onCreate: starting.");

        //Disables focused keyboard on view startup
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        getActivityData();
        setupFirebase();
        setupFirebaseAuth();

        mUsername = (TextView) findViewById(R.id.usernameTxt);
        mDestinationEditText = (TextView) findViewById(R.id.destinationEditText);
        mFromEditText = (TextView) findViewById(R.id.fromEditText);
        mCostEditText = (EditText) findViewById(R.id.costEditText);
        mLicencePlateEditText = (TextView) findViewById(R.id.licencePlateEditText);
        mExtraTimeEditText = (EditText) findViewById(R.id.extraTimeEditText);
        mSeatsEditText = (TextView) findViewById(R.id.seatsEditText);
        mCarEditText = (TextView) findViewById(R.id.carEditText);
        mLuggageEditText = (EditText) findViewById(R.id.luggageEditText);
        mDateOfJourneyEditText = (EditText) findViewById(R.id.DateOfJourneyEditText);
        mPickupEditText = (EditText) findViewById(R.id.pickupEditText);
        mPickupLocationEditText = (EditText) findViewById(R.id.pickupLocationEditText);
        mCarPhoto = (CircleImageView) findViewById(R.id.car_image);
        durationTxt = (TextView) findViewById(R.id.durationTxt);
        mDateOfJourneyEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditRideActivity.this, date, mCalandar
                        .get(Calendar.YEAR), mCalandar.get(Calendar.MONTH),
                        mCalandar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mFromEditText.setText(locationId2);
        mDestinationEditText.setText(destinationId2);
        mCostEditText.setText(costID);
        mExtraTimeEditText.setText(extraTimeID);
//        mSeatsEditText.setText(seatsID);
        mDateOfJourneyEditText.setText(dateOfJourneyID);
        //mPickupLocationEditText.setText(pick);
        mPickupEditText.setText(pickupTimeID);
        durationTxt.setText(duration);
        mPickupLocationEditText.setText(pickupLocation);

        mSameGender = (MaterialAnimatedSwitch) findViewById(R.id.genderSwitch);
//        mSameGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    sameGenderBoolean = true;
//                } else {
//                    sameGenderBoolean = false;
//                }
//            }
//        });


        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to HomeActivity");
                finish();
            }
        });

        mSnippetOfferRideButton = (Button) findViewById(R.id.snippetOfferRideButton);
        mSnippetOfferRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cost = Integer.parseInt(mCostEditText.getText().toString());
                String dateOfJourney = mDateOfJourneyEditText.getText().toString();
                int extraTime = Integer.parseInt(mExtraTimeEditText.getText().toString());
                int seatsAvailable = seatsID;
                int luggageAllowance = Integer.parseInt(mLuggageEditText.getText().toString());

                String licencePlate = mLicencePlateEditText.getText().toString();
                String pickupLocation = mPickupLocationEditText.getText().toString();
                String pickupTime = mPickupEditText.getText().toString();
                String car = mCarEditText.getText().toString();
                String destination = mDestinationEditText.getText().toString();
                String from = mFromEditText.getText().toString();
                String duration = durationTxt.getText().toString().replaceAll("Duration: " , "");

                if(!isStringNull(pickupTime) && pickupTime != null &&
                        !isIntNull(cost) && cost != 0 &&
                        !isStringNull(dateOfJourney) && dateOfJourney != null
                        && !isIntNull(extraTime)){

                    //Creates the ride information and adds it to the database
                    mFirebaseMethods.offerRide(userID , username, from, destination, dateOfJourney, seatsAvailable, licencePlate,  currentLongtitude, currentLatitude,
                            sameGenderBoolean, luggageAllowance, car, pickupTime, extraTime, profile_photo, cost, completeRides, userRating, duration, pickupLocation);

                    //Adds a notification to firebase
                    mFirebaseMethods.checkNotifications(getCurrentDate(), "You have created a ride!");

                    //Shows the ride has been created successfully
                    OfferRideCreatedDialog dialog = new OfferRideCreatedDialog(mContext);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    finish();

                } else {
                    Toast.makeText(EditRideActivity.this, "You must fill in empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isStringNull(String string){
        Toast.makeText(this, "All fields must be filled in!", Toast.LENGTH_LONG).show();
        if (string.equals("")){
            return true;
        } else {
            return false;
        }
    }

    private boolean isIntNull(int integer){
        Toast.makeText(this, "All fields must be filled in!", Toast.LENGTH_LONG).show();
        if (integer < 0 || integer == 0){
            return true;
        } else {
            return false;
        }
    }

    private String getCurrentDate(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = formatter.format(todayDate);

        return todayString;
    }


    private void updateLabel() {
        String dateFormat = "dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.UK);

        mDateOfJourneyEditText.setText(simpleDateFormat.format(mCalandar.getTime()));
    }

    private void getActivityData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
                locationId = getIntent().getStringExtra("DESTINATION");
                destinationId = getIntent().getStringExtra("LOCATION");
                pickupTimeID = getIntent().getStringExtra("PICKUPTIME");
                costID = getIntent().getStringExtra("COST");
                dateOfJourneyID = getIntent().getStringExtra("DATE");
                lengthOfJourneyID = getIntent().getStringExtra("LENGTH");
                extraTimeID = getIntent().getStringExtra("EXTRATIME");
               // seatsID = getIntent().getStringExtra("SEATS");
                licencePlateID = getIntent().getStringExtra("LICENCE");
                carID = getIntent().getStringExtra("CAR");
                luggageID = getIntent().getStringExtra("LUGGAGE");
                destinationId2 = getIntent().getStringExtra("DESTINATION2");
                locationId2 = getIntent().getStringExtra("FROM2");
                duration = getIntent().getStringExtra("LENGTH");
                pickupLocation = getIntent().getStringExtra("PICKUPLOCATION");
        }
    }

    private void setProfileWidgets(User userSettings){
        Log.d(TAG, "setProfileWidgets: setting user widgets from firebase data");

        User user = userSettings;

        mUserSettings = userSettings;

        UniversalImageLoader.setImage(user.getCar_photo(), mCarPhoto, null,"");

        username = user.getUsername();
        userRating = user.getUserRating();
        completeRides = user.getCompletedRides();
        profile_photo = user.getProfile_photo();
        carID = user.getCar();
        seatsID = user.getSeats() - 1;
        licencePlateID = user.getRegistration_plate();

        mUsername.setText(username);
        mLicencePlateEditText.setText(licencePlateID);
        mDestinationEditText.setText(destinationId);
        mFromEditText.setText(locationId);
        mCarEditText.setText(carID);
        mSeatsEditText.setText(String.valueOf(seatsID) + " Seats left!");
        durationTxt.setText("Duration: "+ ApplicationContext.getDuration());

        mCalandar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalandar.set(Calendar.YEAR, year);
                mCalandar.set(Calendar.MONTH, month);
                mCalandar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();
        mPickupLocationEditText.setText(firebase.gopool.Common.Common.getClassName());
    }

    /*----------------------------- SETUP FIREBASE -----------------------------------*/

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

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(this);

//        mGeoFire = new GeoFire(mRef);
//
//        mGeoFire.setLocation("availableRides", new GeoLocation(37.7853889, -122.4056973), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                if (error != null) {
//                    System.err.println("There was an error saving the location to GeoFire: " + error);
//                } else {
//                    System.out.println("Location saved on server successfully!");
//                }
//            }
//        });

    }
}
