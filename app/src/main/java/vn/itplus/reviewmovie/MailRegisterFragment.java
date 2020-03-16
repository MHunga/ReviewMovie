package vn.itplus.reviewmovie;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vn.itplus.reviewmovie.utils.Utils;


public class MailRegisterFragment extends Fragment {

    private static final String TAG = "TAGGGGG";
    EditText txtRegisterName, txtRegisterEmail, txtRegisterPassword, txtRePassword;
    ImageView editShowRegisterHidePassword, editShowRegisterHideRePassword;
    CheckBox checkBox;
    TextView txtAgrees,txtLogin;
    Button btnMailRegister;

    FirebaseAuth firebaseAuth;

    boolean showPassword = true;
    boolean showRePassword = true;

    private  AlertDialog progressDialog;

    public MailRegisterFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mail_register, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        addControls(root);
        addEvents();
        return root;

    }

    private void addEvents() {
        editShowRegisterHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword){
                    editShowRegisterHidePassword.setImageResource(R.drawable.icon_visibility_on);
                showPassword = false;
                txtRegisterPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                txtRegisterPassword.setSelection(txtRegisterPassword.length());
                }
                else {
                    editShowRegisterHidePassword.setImageResource(R.drawable.icon_visibility_off);
                    showPassword = true;
                    txtRegisterPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtRegisterPassword.setSelection(txtRegisterPassword.length());
                }

            }
        });
        editShowRegisterHideRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showRePassword){
                    editShowRegisterHideRePassword.setImageResource(R.drawable.icon_visibility_on);
                    showRePassword = false;
                    txtRePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtRePassword.setSelection(txtRePassword.length());
                }
                else {
                    editShowRegisterHideRePassword.setImageResource(R.drawable.icon_visibility_off);
                    showRePassword = true;
                    txtRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtRePassword.setSelection(txtRePassword.length());
                }

            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });
        btnMailRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtRegisterName.getText().toString().trim();
                String email = txtRegisterEmail.getText().toString().trim();
                String password = txtRegisterPassword.getText().toString().trim();
                if (validate()) {
                    Log.d(TAG, name + "  " + email);
                    register(email, password, name);
                }
            }
        });
    }

    private void register(final String email, final String password, final String name) {
        try {
            showAlertDialog("Đăng ký");
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            unShowAlertDialog();
                                            Intent intent = new Intent(getActivity().getBaseContext(), EmailVerificationActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            intent.putExtra("name", name);
                                            getActivity().startActivity(intent);
                                        } else {
                                            unShowAlertDialog();
                                            Toast.makeText(getActivity().getBaseContext(), "Can not send verify mail", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    } else {
                        unShowAlertDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Can not create account", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
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

    private void addControls(View root) {
        txtAgrees = root.findViewById(R.id.txtAgrees);
        txtRegisterEmail = root.findViewById(R.id.txtRegisterEmail);
        txtRegisterName = root.findViewById(R.id.txtRegisterName);
        txtRegisterPassword = root.findViewById(R.id.txtRegisterPassword);
        txtRePassword = root.findViewById(R.id.txtRePassword);
        txtLogin = root.findViewById(R.id.txtLogin);

        checkBox = root.findViewById(R.id.checkBox);

        editShowRegisterHidePassword = root.findViewById(R.id.editShowRegisterHidePassword);
        editShowRegisterHideRePassword = root.findViewById(R.id.editShowRegisterHideRePassword);

        btnMailRegister = root.findViewById(R.id.btnMailRegister);

        String ag_str = "Tôi đồng ý với <b><a href='schemecwebview://1'>Điều khoản dịch vụ</a></b> &amp; <b><a href='schemecwebview://2'>Chính sách bảo mật</a></b>";
        txtAgrees.setText(Html.fromHtml(ag_str));
        txtAgrees.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private boolean validate() {
        boolean valid = true;
        String name = txtRegisterName.getText().toString();
        String email = txtRegisterEmail.getText().toString();
        String password = txtRegisterPassword.getText().toString();
        String rePassword = txtRePassword.getText().toString();

        if (name.isEmpty()) {
            txtRegisterName.setError("Nhập tên");
            valid = false;
        } else {
            txtRegisterName.setError(null);

        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtRegisterEmail.setError("Nhập Email");
            valid = false;
        } else {
            txtRegisterEmail.setError(null);

        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            txtRegisterPassword.setError("Mật khẩu từ 6 -> 12 kí tự");
            valid = false;
        } else {
            txtRegisterPassword.setError(null);

        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || rePassword.length() > 12 || !(rePassword.equals(password))) {
            txtRePassword.setError("Không khớp");
            valid = false;
        } else {
            txtRePassword.setError(null);

        }

        if (!checkBox.isChecked()){
            checkBox.setError("Vui lòng đồng ý điều khoản dịch vụ");
            valid = false;
        }
        else {
            checkBox.setError(null);
        }

        return valid;
    }
}
