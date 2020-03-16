package vn.itplus.reviewmovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.text.InputType;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;

import vn.itplus.reviewmovie.model.user.User;
import vn.itplus.reviewmovie.model.user.UserID;
import vn.itplus.reviewmovie.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private static  int Google_RC_SIGNIN = 999;
    private static int Facebook_RC_SIGNIN = -1;
    EditText txtLoginEmail, txtLoginPassword;
    ImageView imgShowHidePassword;
    TextView txtFailLogin, txtForgetPassword, txtCreatAccount, txtResendVerify;
    Button btnLogin;
    FloatingActionButton btnFacebookLogin, btnGoogleLogin, btnPhoneNumberLogin;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    boolean showPassword = true;

    CallbackManager facebookCallbackManager;
    GoogleSignInClient mGoogleSignInClient;

    DatabaseReference  mDatabase;
   public static ArrayList<String> idHotMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        addControls();
        if (firebaseUser != null){
           // getIdMovies();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }


        FacebookSdk.sdkInitialize(getApplicationContext());

        GoogleSignInOptions gos = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gos);


        addEvents();
    }
    public void getIdMovies() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("idHotMovie").getChildren()){
                    Log.d("TAG",ds.getValue().toString());
                    idHotMovies.add(ds.getValue().toString());

                }
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addEvents() {
        txtCreatAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        imgShowHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword) {
                    imgShowHidePassword.setImageResource(R.drawable.icon_visibility_on);
                    showPassword = false;
                    txtLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtLoginPassword.setSelection(txtLoginPassword.length());
                } else {
                    imgShowHidePassword.setImageResource(R.drawable.icon_visibility_off);
                    showPassword = true;
                    txtLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtLoginPassword.setSelection(txtLoginPassword.length());
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtLoginEmail.getText().toString().trim();
                String password = txtLoginPassword.getText().toString().trim();
                if (validate()) {
                    logInWithEmail(email, password);
                }

            }
        });
        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookLogin(v);
            }
        });
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoogleLogin();
            }
        });
        btnPhoneNumberLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhoneNumberLogin();
            }
        });
    }

    private void onPhoneNumberLogin() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.login_phone_dialog_layout,null);
        final EditText txtPhoneLogin = v.findViewById(R.id.txtPhoneLogin);
        final EditText txtPhonePasswordLogin = v.findViewById(R.id.txtPhonePasswordLogin);
        final CheckBox chkShowPassword = v.findViewById(R.id.chkShowPassword);

        chkShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txtPhonePasswordLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtPhonePasswordLogin.setSelection(txtPhonePasswordLogin.length());
                }
                else {
                    txtPhonePasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtPhonePasswordLogin.setSelection(txtPhonePasswordLogin.length());
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setCancelable(false);
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.SetProgressDialogIndeterminate(LoginActivity.this,"Đang đăng nhập");
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("getIDByPhone").child(txtPhoneLogin.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserID userID = dataSnapshot.getValue(UserID.class);
                       // Toast.makeText(getApplicationContext(),userID.getId(),Toast.LENGTH_LONG).show();

                        mDatabase.child("User").child(userID.getId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (txtPhoneLogin.getText().toString().equals(user.getPhoneNumber())
                                        && txtPhonePasswordLogin.getText().toString().equals(user.getPassword())){
                                    Utils.UnSetProgressDialogIndeterminate();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));

                                }else {
                                    Toast.makeText(getApplicationContext(),"Tài khoản hoặc mật khẩu không đúng",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.w(TAG, "Failed to read value.", databaseError.toException());
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                    }
                });
            }
        });
        builder.create();
        builder.show();

    }

    void onGoogleLogin(){
        Intent loginIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(loginIntent, Google_RC_SIGNIN);

    }

    private void onFacebookLogin(View v) {
        try {


        Utils.SetProgressDialogIndeterminate(LoginActivity.this, "Đang đăng nhập ...");
        facebookCallbackManager = CallbackManager.Factory.create();
        final LoginManager loginManager = LoginManager.getInstance();

        loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
        loginManager.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Utils.UnSetProgressDialogIndeterminate();
                            User user = new User(firebaseAuth.getCurrentUser().getUid(),firebaseAuth.getCurrentUser().getDisplayName(),firebaseAuth.getCurrentUser().getEmail(),"","","","",firebaseAuth.getCurrentUser().getPhotoUrl().toString());
                            FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                        else {
                            Utils.UnSetProgressDialogIndeterminate();
                            Toast.makeText(getApplicationContext(),"Login Faile",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utils.UnSetProgressDialogIndeterminate();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancel() {
                Utils.UnSetProgressDialogIndeterminate();
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
                Utils.UnSetProgressDialogIndeterminate();
                Log.e(TAG , error.toString());
            }
        });
    }
        catch (Exception ex){
            Utils.UnSetProgressDialogIndeterminate();
            Log.e(TAG, ex.toString());
        }

    }

    private void logInWithEmail(String email, String password) {
        Utils.SetProgressDialogIndeterminate(this, "Đăng nhập");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (firebaseUser.isEmailVerified()) {
                                Utils.UnSetProgressDialogIndeterminate();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                txtFailLogin.setText("Tài khoản của bạn chưa được xác minh");

                                Utils.UnSetProgressDialogIndeterminate();
                                txtResendVerify.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    private void addControls() {
        txtLoginEmail = findViewById(R.id.txtLoginEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
        imgShowHidePassword = findViewById(R.id.imgShowHidePassword);
        txtFailLogin = findViewById(R.id.txtFailLogin);
        txtForgetPassword = findViewById(R.id.txtForgetPassword);
        txtCreatAccount = findViewById(R.id.txtCreatAccount);
        txtResendVerify = findViewById(R.id.txtResendVerify);
        btnLogin = findViewById(R.id.btnLogin);
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnPhoneNumberLogin = findViewById(R.id.btnPhoneNumberLogin);
        idHotMovies = new ArrayList<>();
    }

    public void OnResendVerify_Click(View view) {
        String email = txtLoginEmail.getText().toString().trim();
        String password = txtLoginPassword.getText().toString().trim();
        reSendEmail(email, password);
    }

    private void reSendEmail(String email, String password) {
        Utils.SetProgressDialogIndeterminate(this, "Sending...");
        AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Utils.UnSetProgressDialogIndeterminate();
                                txtFailLogin.setText("Vui lòng kiểm tra email");
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean validate() {
        boolean valid = true;
        String email = txtLoginEmail.getText().toString();
        String password = txtLoginPassword.getText().toString();


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtLoginEmail.setError("Nhập Email");
            valid = false;
        } else {
            txtLoginEmail.setError(null);

        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            txtLoginPassword.setError("Mật khẩu từ 6 -> 12 kí tự");
            valid = false;
        } else {
            txtLoginPassword.setError(null);

        }


        return valid;
    }


    private void updateUI(FirebaseUser user) {
        if(user !=null) {
            //Get Display Name From Google
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());

            //Update Database For User

        }
        else{
            Log.e(TAG, "Không tìm thấy người dùng.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == Google_RC_SIGNIN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) firebaseAuthWithGoogle(googleSignInAccount);
            }
            catch (ApiException ex){
                Utils.UnSetProgressDialogIndeterminate();
                Log.e(TAG, ex.toString());
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }else/*(requestCode == Facebook_RC_SIGNIN)*/{
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG,"firebaseAuthWithGooggle: " + googleSignInAccount.getId());

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"Success");
                    if (firebaseAuth.getCurrentUser() != null){
                        User user = new User(firebaseAuth.getCurrentUser().getUid(),firebaseAuth.getCurrentUser().getDisplayName(),firebaseAuth.getCurrentUser().getEmail(),"","","","",firebaseAuth.getCurrentUser().getPhotoUrl().toString());
                        FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);

                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                    }

                }else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                    updateUI(null);
                }
            }
        });
    }

}
