package firebase.gopool.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import firebase.gopool.Common.Common;
import firebase.gopool.R;
import firebase.gopool.Remote.IFCMService;
import firebase.gopool.models.FCMResponse;
import firebase.gopool.models.Notification;
import firebase.gopool.models.Sender;
import firebase.gopool.models.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingReceivedDialog extends Dialog implements
        View.OnClickListener  {

    private static final String TAG = "BookingReceivedDialog";
    public Context c;
    public Dialog d;

    // variables
    private FloatingActionButton mConfirmBtn, declineRideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_booking_received);

        mConfirmBtn = (FloatingActionButton) findViewById(R.id.confirmRideBtn);
        declineRideBtn = (FloatingActionButton) findViewById(R.id.declineRideBtn);

        declineRideBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

    }

    public BookingReceivedDialog(Context a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmRideBtn:
                dismiss();
                Toast.makeText(c, "Ride confirmed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.declineRideBtn:
                Toast.makeText(c, "You cancelled the ride", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }




}
