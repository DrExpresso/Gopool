package firebase.gopool.Pickup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import firebase.gopool.Adapter.BookingAdapter;
import firebase.gopool.Home.HomeActivity;
import firebase.gopool.R;
import firebase.gopool.Utils.BottomNavigationViewHelper;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.dialogs.LeaveRideDialog;
import firebase.gopool.dialogs.ParticipantsDialog;
import firebase.gopool.models.BookingResults;

public class PickupActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "PickupActivity";

    private Context mContext = PickupActivity.this;

    //Firebase variables
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseMethods mFirebaseMethods;

    //Google map variables
    private GoogleMap mMap;
    private static int DEFAULT_ZOOM = 18;

    private String pickLocation, rideID, licencePlate, pickupTime;

    //Widgets
    private ImageView mBack;
    private TextView mLicencePlate, mPickupTime;
    private FloatingActionButton mUnjoinBtn, mParticipantsBtn;

    private String userID, currentUserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        setContentView(R.layout.activity_pickup);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);

        getActivityData();
        initMap();
        setupWidgets();
        mLicencePlate.setText(licencePlate);
        mPickupTime.setText(pickupTime);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Setup firebase object
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null){
            currentUserID = mAuth.getCurrentUser().getUid();
            Log.i(TAG, "onCreate: "+ currentUserID);
        }

        mParticipantsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogParticpants();
            }
        });

        mUnjoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLeave();
            }
        });
    }

    private void setupWidgets() {
        mBack = (ImageView) findViewById(R.id.backArrowPickupRide);
        mLicencePlate = (TextView) findViewById(R.id.licencePlate);
        mPickupTime = (TextView) findViewById(R.id.pickupTimeText);
        mParticipantsBtn = (FloatingActionButton) findViewById(R.id.paticipantsRideBtn);
        mUnjoinBtn = (FloatingActionButton) findViewById(R.id.unjoinBtn);
    }

    private void getActivityData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pickLocation = getIntent().getStringExtra("pickupLocation");
            pickupTime = getIntent().getStringExtra("pickupTime");
            rideID = getIntent().getStringExtra("rideID");
            userID = getIntent().getStringExtra("userID");
            licencePlate = getIntent().getStringExtra("licencePlate");
        }
    }

    private void showDialogParticpants() {
        //Confirmation to delete the ride dialog
        ParticipantsDialog dialog = new ParticipantsDialog(mContext, userID, rideID);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showDialogLeave() {
        //Confirmation to delete the ride dialog
        LeaveRideDialog dialog = new LeaveRideDialog(mContext, currentUserID ,rideID);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     * sets up map from the view
     */
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pickupMap);
        mapFragment.getMapAsync(PickupActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        LatLng pickupLocation = geoLocationFromAddress(pickLocation);
        mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickupLocation, DEFAULT_ZOOM));
    }

    private LatLng geoLocationFromAddress(String streetAddress){
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        LatLng latLng = null;

        try {
            addresses = geocoder.getFromLocationName(streetAddress, 5);
            if (addresses == null) {
                return null;
            }

            Address location = addresses.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return latLng;
    }


    /***
     *  Setup the firebase object
     */
    @Override
    public void onStart() {
        super.onStart();
    }




}
