package firebase.gopool.Payment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import firebase.gopool.Common.Common;
import firebase.gopool.R;
import firebase.gopool.Remote.IFCMService;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.models.FCMResponse;
import firebase.gopool.models.Notification;
import firebase.gopool.models.Sender;
import firebase.gopool.models.Token;
import retrofit2.Call;
import retrofit2.Callback;

import static firebase.gopool.Common.ApplicationContext.getContext;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private static final int REQUEST_CODE = 1234;

    private Context mContext = PaymentActivity.this;

    //Payment variables
    private String API_GET_TOKEN = "ENTER WEB APP URL HERE (SEE README FOR SETUP GUIDE)";
    private String API_CHECK_OUT = "ENTER WEB APP URL HERE (SEE README FOR SETUP GUIDE)";

    private String token, amount;
    private HashMap<String, String> paramsHash;

    //widgets
    private Button mPaymentBtn;
    private TextView mEditAmount, mCancelBtn;
    private LinearLayout mGroupWaiting, mGroupPayment;

    //Activity data
    private String userID, currentLocation, destination, rideID, profile_photo, profile_photo2, pickupLocation, currentUserID, username, cost, pickupTime, dateOnly, licencePlate;
    private int seatsAvailable = 0;


    //Firebase
    private IFCMService mService;
    private FirebaseMethods mFirebaseMethods;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getActivityData();

        mService = Common.getFCMService();
        mFirebaseMethods = new FirebaseMethods(getContext());

        init();
        getUserInformation();
        getsSeatsRemaining();


        new getToken().execute();

        mPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(mContext), REQUEST_CODE);
    }

    private void init(){
        mPaymentBtn = (Button) findViewById(R.id.paymentBtn);
        mCancelBtn = (TextView) findViewById(R.id.cancelBtn);
        mEditAmount = (TextView) findViewById(R.id.moneyTextview);
        mGroupWaiting = (LinearLayout) findViewById(R.id.waiting_group);
        mGroupPayment = (LinearLayout) findViewById(R.id.payment_group);

        mEditAmount.setText(cost);
    }

    private void getActivityData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = getIntent().getStringExtra("userID");
            currentLocation = getIntent().getStringExtra("currentLocation");
            destination = getIntent().getStringExtra("destination");
            rideID = getIntent().getStringExtra("rideID");
            profile_photo2 = getIntent().getStringExtra("profile_photo");
            pickupLocation = getIntent().getStringExtra("pickupLocation");
            cost = getIntent().getStringExtra("cost");
            licencePlate = getIntent().getStringExtra("licencePlate");
            dateOnly = getIntent().getStringExtra("dateOnly");
            pickupTime = getIntent().getStringExtra("pickupTime");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                DropInResult  result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (!mEditAmount.getText().toString().isEmpty()){
                    amount = getCurrentAmount();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount);
                    paramsHash.put("nonce", strNonce);

                    sendPayments();
                } else {
                    Toast.makeText(mContext, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(mContext, "User cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d(TAG, "onActivityResult: " + exception.getMessage());
            }
       }
    }

    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().contains("Successful")) {
                            Toast.makeText(mContext, "Transaction successful!", Toast.LENGTH_SHORT).show();
                            requestPickupHere();
                        } else {
                            Toast.makeText(mContext, "Transaction failed!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        Log.d(TAG, "onResponse: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: " + error.getMessage());
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (paramsHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for (String key : paramsHash.keySet()) {
                    params.put(key, paramsHash.get(key));
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private class getToken extends AsyncTask{

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mContext, android.R.style.Theme_DeviceDefault);
            mDialog.setCancelable(false);
            mDialog.setMessage("Please wait");
            mDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGroupWaiting.setVisibility(View.GONE);
                            mGroupPayment.setVisibility(View.VISIBLE);

                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d(TAG, "failure: " + exception.getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            mDialog.dismiss();
        }
    }

    private String getCurrentAmount(){
        return mEditAmount.getText().toString().split("£")[1];
    }

    /*------------------------------- Request booking after payment confirmation ---------------------------------- */

    private void requestPickupHere() {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");


        tokens.orderByKey().equalTo(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Token token = dataSnapshot1.getValue(Token.class);

                            String extraData = username + "," + profile_photo + "," + currentLocation;
                            Notification data = new Notification(currentUserID, userID, rideID, extraData, destination);
                            Sender content = new Sender(data, token.getToken());

                            mService.sendMessage(content)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, retrofit2.Response<FCMResponse> response) {
                                            Log.i(TAG, "onResponse: " + response.toString());
                                            if (response.body().success == 1 || response.code() == 200){
                                                Toast.makeText(mContext, "Booking request sent!", Toast.LENGTH_SHORT).show();
                                                updateSeatsRemaining();
                                                mFirebaseMethods.addPoints(userID, 200);
                                                finish();
                                            } else {
                                                Toast.makeText(mContext, "Booking request failed!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                                            Log.e(TAG, "onFailure: "+ t.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        firebase.gopool.models.Request request = new firebase.gopool.models.Request(userID, profile_photo2, profile_photo, username, 1, destination, currentLocation, 1, false, rideID, dateOnly, pickupTime,   Float.parseFloat(cost.substring(2)), pickupLocation, licencePlate);

        myRef.child("requestRide")
                .child(rideID)
                .child(currentUserID)
                .setValue(request);
    }

    public void getUserInformation(){
        myRef.child("user").child(currentUserID).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.child("user").child(currentUserID).child("profile_photo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile_photo = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getsSeatsRemaining(){
        myRef.child("availableRide").child(rideID).child("seatsAvailable").addValueEventListener(new ValueEventListener() {
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
        myRef.child("availableRide").child(rideID).child("seatsAvailable").setValue(seatsAvailable - 1);
    }



}
