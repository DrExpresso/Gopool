package firebase.gopool.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import firebase.gopool.R;
import firebase.gopool.Rides.RidesActivity;
import firebase.gopool.Utils.FirebaseMethods;

public class DeleteConfirmationDialog extends Dialog implements
        View.OnClickListener  {

    private static final String TAG = "ViewRideCreatedDialog";
    public Context c;
    public Dialog d;

    //Firebase
    private FirebaseMethods mFirebaseMethods;

    // variables
    private TextView mCancelDialogBtn;
    private Button mDeleteRideBtn;

    private String rideID;


    public interface onConfirmPasswordListener{
        public void onConfirmPassword(String password);
    }

    onConfirmPasswordListener mOnConfirmPassowrdListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_confirmation);

        setupWidgets();
        setupFirebase();

        mCancelDialogBtn.setOnClickListener(this);
        mDeleteRideBtn.setOnClickListener(this);
    }

    public DeleteConfirmationDialog(Context a, String rideID) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.rideID = rideID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogConfirm:
                mFirebaseMethods.deleteRide(rideID);
                Intent intent1 = new Intent(c, RidesActivity.class);
                c.startActivity(intent1);
                dismiss();
                break;
            case R.id.dialogCancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void setupWidgets(){
        //Setup widgets
        mDeleteRideBtn = (Button) findViewById(R.id.dialogConfirm);
        mCancelDialogBtn = (TextView) findViewById(R.id.dialogCancel);
    }

    private void setupFirebase() {

        mFirebaseMethods = new FirebaseMethods(c);
    }

}
