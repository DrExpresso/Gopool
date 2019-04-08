package firebase.gopool.Home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.models.User;

public class SearchRideActivity extends AppCompatActivity {

    private static final String TAG = "SearchRideActivity";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    private Context mContext;

    //Fragment view
    private View view;

    private EditText mDestinationEditText, mFromEditText, mDateOfJourneyEditText;
    private Button mSnippetSeachARideBtn;
    private Switch mSameGenderSearchSwitch;
    private Boolean sameGender;

    //vars
    private User mUserSettings;
    private Calendar mCalandar;
    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_ride);

        mContext = SearchRideActivity.this;

        setupWidgets();
        setupFirebaseAuth();
        getActivityData();

        //Setup back arrow for navigating back to 'ProfileActivity'
        ImageView backArrow = (ImageView) findViewById(R.id.backArrowFindRide);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSameGenderSearchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sameGender = true;
                } else {
                    sameGender = false;
                }
            }
        });

        mDateOfJourneyEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchRideActivity.this, date, mCalandar
                        .get(Calendar.YEAR), mCalandar.get(Calendar.MONTH),
                        mCalandar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        mSnippetSeachARideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDateOfJourneyEditText.getText().length() > 0 &&  mFromEditText.getText().length() > 0 && mDestinationEditText.getText().length() > 0) {
                    Intent intent = new Intent(mContext, SearchResultsActivity.class);
                    intent.putExtra("LOCATION", mDestinationEditText.getText().toString());
                    intent.putExtra("DESTINATION", mFromEditText.getText().toString());
                    intent.putExtra("sameGender", sameGender);
                    intent.putExtra("DATE", mDateOfJourneyEditText.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "All fields must be filled in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getActivityData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mFromEditText.setText(getIntent().getStringExtra("DESTINATION"));
            mDestinationEditText.setText(getIntent().getStringExtra("LOCATION"));
        }
    }

    private void setupWidgets(){
        //Firebase setup
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        mFirebaseMethods = new FirebaseMethods(this);

        //Widget setup
        mDestinationEditText = (EditText) findViewById(R.id.destinationEditText);
        mFromEditText = (EditText) findViewById(R.id.locationEditText);
        mDateOfJourneyEditText = (EditText) findViewById(R.id.dateEditText);
        mSameGenderSearchSwitch = (Switch) findViewById(R.id.sameGenderSearchSwitch);
        mSnippetSeachARideBtn = (Button) findViewById(R.id.snippetSeachARideBtn);

        //Calander setup
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

    private void updateLabel() {
        String dateFormat = "dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.UK);

        mDateOfJourneyEditText.setText(simpleDateFormat.format(mCalandar.getTime()));
    }




    /** --------------------------- Firebase ---------------------------- **/

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
