package vn.itplus.reviewmovie;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import vn.itplus.reviewmovie.model.user.User;
import vn.itplus.reviewmovie.model.user.UserID;


public class PhoneRegisterFragment extends Fragment {

    private static final String TAG = "TAG";
    CountryCodePicker ccp;
    EditText txtPhoneNumner, txtPhonePassword, txtPhoneRePassword, txtOTPcode;
    ProgressBar progressBar;
    Button btnPhoneRegister, btnPhoneVerify;
    TextView txtSending;
    ImageView imgShowPhonePassword, imgShowPhoneRePassword;

    String verificationID;
    PhoneAuthProvider.ForceResendingToken token;

    FirebaseAuth firebaseAuth;

    AlertDialog progressDialog;

    boolean showPassword = true;
    boolean showRePassword = true;




    public PhoneRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_phone_register, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        addControls(root);
        addEvents();

        return root;

    }



    private void addEvents() {
        imgShowPhonePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword){
                    imgShowPhonePassword.setImageResource(R.drawable.icon_visibility_on);
                    showPassword = false;
                    txtPhonePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtPhonePassword.setSelection(txtPhonePassword.length());
                }
                else {
                    imgShowPhonePassword.setImageResource(R.drawable.icon_visibility_off);
                    showPassword = true;
                    txtPhonePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtPhonePassword.setSelection(txtPhonePassword.length());
                }
            }
        });
        imgShowPhoneRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showRePassword){
                    imgShowPhoneRePassword.setImageResource(R.drawable.icon_visibility_on);
                    showRePassword = false;
                    txtPhoneRePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtPhoneRePassword.setSelection(txtPhoneRePassword.length());
                }
                else {
                    imgShowPhoneRePassword.setImageResource(R.drawable.icon_visibility_off);
                    showRePassword = true;
                    txtPhoneRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtPhoneRePassword.setSelection(txtPhoneRePassword.length());
                }
            }
        });
        btnPhoneVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OTP = txtOTPcode.getText().toString();
                if (!OTP.isEmpty()&&OTP.length()==6){
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,OTP);
                    showAlertDialog("Đang xác thực");

                    verifyAuth(credential);
                }
                else {

                }
            }
        });
        btnPhoneRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String phone = "+" + ccp.getSelectedCountryCode() + txtPhoneNumner.getText().toString();
                    Log.d("TAG",phone);
                    progressBar.setVisibility(View.VISIBLE);
                    txtSending.setVisibility(View.VISIBLE);
                    requestOTP(phone);
                }
            }
        });

    }

    private void verifyAuth(PhoneAuthCredential credential) {
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        unShowAlertDialog();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String UID = firebaseUser.getUid();
                        User user = new User();
                        UserID userID = new UserID();
                        user.setPhoneNumber(txtPhoneNumner.getText().toString());
                        user.setPassword(txtPhonePassword.getText().toString());
                        user.setId(UID);
                        userID.setId(UID);

                        FirebaseDatabase.getInstance().getReference().child("User").child(UID).setValue(user);
                        FirebaseDatabase.getInstance().getReference().child("getIDByPhone").child(user.getPhoneNumber()).setValue(userID);
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else {
                        Toast.makeText(getActivity(),"Authentication is false",Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    private void requestOTP(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60L, TimeUnit.SECONDS,getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       // super.onCodeSent(s, forceResendingToken);
                        progressBar.setVisibility(View.GONE);
                        txtSending.setVisibility(View.GONE);
                        txtOTPcode.setVisibility(View.VISIBLE);
                        btnPhoneRegister.setVisibility(View.GONE);
                        btnPhoneVerify.setVisibility(View.VISIBLE);
                        verificationID = s;
                        token = forceResendingToken;

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getActivity(),"Can not Register.!",Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void addControls(View root) {
        ccp = root.findViewById(R.id.ccp);
        txtPhoneNumner = root.findViewById(R.id.txtPhoneNumner);
        txtPhonePassword = root.findViewById(R.id.txtPhonePassword);
        txtPhoneRePassword = root.findViewById(R.id.txtPhoneRePassword);
        txtOTPcode = root.findViewById(R.id.txtOTPcode);
        progressBar = root.findViewById(R.id.progressBar);
        btnPhoneRegister = root.findViewById(R.id.btnPhoneRegister);
        btnPhoneVerify = root.findViewById(R.id.btnPhoneVerify);
        txtSending = root.findViewById(R.id.txtSending);
        imgShowPhonePassword = root.findViewById(R.id.imgShowPhonePassword);
        imgShowPhoneRePassword = root.findViewById(R.id.imgShowPhoneRePassword);
    }

    private boolean validate() {
        boolean valid = true;
        String phone = "+" + ccp.getSelectedCountryCode() + txtPhoneNumner.getText().toString();

        String password = txtPhonePassword.getText().toString().trim();
        String rePassword = txtPhoneRePassword.getText().toString().trim();

        if (phone.isEmpty()) {
            txtPhoneNumner.setError("Nhập tên");
            valid = false;
        } else {
            txtPhoneNumner.setError(null);

        }


        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            txtPhonePassword.setError("Mật khẩu từ 6 -> 12 kí tự");
            valid = false;
        } else {
            txtPhonePassword.setError(null);

        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || rePassword.length() > 12 || !(rePassword.equals(password))) {
            txtPhoneRePassword.setError("Không khớp");
            valid = false;
        } else {
            txtPhoneRePassword.setError(null);

        }


        return valid;
    }
    public void showAlertDialog(String message) {
        try {
            LayoutInflater layoutInflater =  getActivity().getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.progress_dialog_layout, null);

            TextView title = view.findViewById(R.id.title);
            title.setText(message);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);
            builder.setCancelable(true);
            builder.setPositiveButton("Bỏ qua", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            progressDialog = builder.create();
            progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    progressDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.progressDialogPositiveTextColor));
                }
            });
            progressDialog.show();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }
    public  void unShowAlertDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
