package vn.itplus.reviewmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.itplus.reviewmovie.model.user.User;
import vn.itplus.reviewmovie.utils.Utils;

public class EmailVerificationActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    String email;
    String password,name;
    FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");
firebaseAuth =FirebaseAuth.getInstance();
firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void OnVerified_Click(View view) {
        User user = new User(firebaseUser.getUid(),name,email,password,"","","","");
        FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid()).setValue(user);

        startActivity(new Intent(this,LoginActivity.class));
    }

    public void OnResendEmail_Click(View view) {
        reSendEmail(email,password);
    }

    private void reSendEmail(String email, String password) {
       Utils.SetProgressDialogIndeterminate(this,"Sending...");
        AuthCredential authCredential =EmailAuthProvider.getCredential(email,password);
FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   Utils.UnSetProgressDialogIndeterminate();
                   Toast.makeText(getApplicationContext(),"check email",Toast.LENGTH_LONG).show();
               }
                }
            });
        }
    }
});

    }
}
