package firebase.gopool.Register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import firebase.gopool.Account.AccountActivity;
import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;

import static android.app.Activity.RESULT_OK;

public class RegisterStepFourFragment extends Fragment {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    //Widgets
    private View mView;
    private Button mNextButton4;
    private ImageView mbackButton4, mCarPhoto, mRestartRegistration;
    private EditText mLicence, mCar, mSeats, mRegistration;
    private TextInputLayout mLicenceLayout, mCarLayout, mRegistrationLayout, mSeatsLayout;
    private RadioButton mCarToggleTrue, mCarToggleFalse;
    private RadioGroup mCarToggle;
    private Boolean ownVehicle;

    //Profile picture vars
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String imgURL;

    //Interface variables
    private RegisterStepFourFragment.OnButtonClickListener mOnButtonClickListener;

    private RegisterStepOneFragment.OnButtonClickListener mOnButtonClickListenerMain;

    public interface OnButtonClickListener{
        void onButtonClicked(View view);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register_four, container, false);

        //Firebase setup
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference();
        mFirebaseMethods = new FirebaseMethods(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //instantiate objects
        mNextButton4 = (Button) mView.findViewById(R.id.nextBtn4);
        mLicence = (EditText) mView.findViewById(R.id.licenceStepFourEditText);
        mCar = (EditText) mView.findViewById(R.id.carStepFourEditText);
        mSeats = (EditText) mView.findViewById(R.id.seatsStepFourEditText);
        mRegistration = (EditText) mView.findViewById(R.id.registrationStepFourEditText);
        mCarToggle = (RadioGroup) mView.findViewById(R.id.carToggle);
        mCarToggleFalse = (RadioButton) mView.findViewById(R.id.noCarButton);
        mCarToggleTrue = (RadioButton) mView.findViewById(R.id.yesCarButton);
        mLicenceLayout = (TextInputLayout) mView.findViewById(R.id.licenceLayout);
        mCarLayout = (TextInputLayout) mView.findViewById(R.id.carLayout);
        mRegistrationLayout = (TextInputLayout) mView.findViewById(R.id.registrationLayout);
        mSeatsLayout = (TextInputLayout) mView.findViewById(R.id.seatsLayout);
        mCarPhoto = (ImageView) mView.findViewById(R.id.uploadCarPicture);

        mCarPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("STEP4", "onClick: adding profile photo");
                chooseImage();
            }
        });



        mCarToggle.check(mCarToggleFalse.getId());
        mCarToggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.yesCarButton:
                        mLicence.setVisibility(View.VISIBLE);
                        mCar.setVisibility(View.VISIBLE);
                        mSeats.setVisibility(View.VISIBLE);
                        mRegistration.setVisibility(View.VISIBLE);
                        mLicenceLayout.setVisibility(View.VISIBLE);
                        mCarLayout.setVisibility(View.VISIBLE);
                        mRegistrationLayout.setVisibility(View.VISIBLE);
                        mSeatsLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.noCarButton:
                        mLicence.setVisibility(View.INVISIBLE);
                        mCar.setVisibility(View.INVISIBLE);
                        mSeats.setVisibility(View.INVISIBLE);
                        mRegistration.setVisibility(View.INVISIBLE);
                        mLicenceLayout.setVisibility(View.INVISIBLE);
                        mCarLayout.setVisibility(View.INVISIBLE);
                        mRegistrationLayout.setVisibility(View.INVISIBLE);
                        mSeatsLayout.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        //Make edit fields invisible by default
        mLicence.setVisibility(View.INVISIBLE);
        mCar.setVisibility(View.INVISIBLE);
        mSeats.setVisibility(View.INVISIBLE);
        mRegistration.setVisibility(View.INVISIBLE);
        mLicenceLayout.setVisibility(View.INVISIBLE);
        mCarLayout.setVisibility(View.INVISIBLE);
        mRegistrationLayout.setVisibility(View.INVISIBLE);
        mSeatsLayout.setVisibility(View.INVISIBLE);


        mNextButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCarToggle.getCheckedRadioButtonId())
                {
                    case R.id.yesCarButton:
                        if (mLicence.getText().length() > 0 && mCar.getText().length() > 0
                                && mSeats.getText().length() > 0 && mRegistration.getText().length() > 0 ) {
                            mOnButtonClickListener.onButtonClicked(v);
                        }
                        break;

                    case R.id.noCarButton:
                        mOnButtonClickListener.onButtonClicked(v);
                        break;
                }
            }
        });

        mbackButton4 = (ImageView) mView.findViewById(R.id.loginBackArrowStep);
        mbackButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClicked(v);
            }
        });

        mRestartRegistration = (ImageView) mView.findViewById(R.id.restartRegistrationBtn);
        mRestartRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClicked(v);
            }
        });



        return mView;
    }

    /** ----------------------------------- UPLOADS IMAGE TO FIREBASE STORAGE -------------------------------------------- **/
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.i("STEP3", "onActivityResult: " + filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                mCarPhoto.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        if (filePath != null){
            final StorageReference ref = storageReference.child("carImages/"+UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i("STEP3", "onActivityResult: Image uploaded" + uri);
                                    imgURL = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to upload", Toast.LENGTH_SHORT).show();
                            Log.i("STEP3", "onActivityResult: failed to upload" + e.getMessage());
                        }
                    });
        }
    }

    /**
     * Interface attach on view show
     * @param context
     */
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            mOnButtonClickListener = (RegisterStepFourFragment.OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity) context).getLocalClassName()
                    + " must implement OnButtonClickListener");
        }
    }

    public String getLicence() {
        return mLicence.getText().toString();
    }

    public String getCar() {
        return mCar.getText().toString();
    }

    public int getSeats() {
        if (mSeats.getText().length() > 0){
            return Integer.parseInt(mSeats.getText().toString());
        }
        return 0;
    }

    public String getRegistration() {
        return mRegistration.getText().toString();
    }

    public Boolean getCarToggle() {
        int whichIndex = mCarToggle.getCheckedRadioButtonId();
        if (whichIndex == R.id.yesCarButton) {
            return true;
        } else if (whichIndex == R.id.noCarButton) {
            return false;
        }
        return false;
    }

    public String getCarPhoto() {
        return imgURL;
    }

    /** --------------------------- Firebase ---------------------------- **/
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
