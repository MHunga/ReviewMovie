package vn.itplus.reviewmovie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.itplus.reviewmovie.R;

public class FragmentChangeName extends Fragment {
    TextView txtOldName;
    EditText txtNewName;
    Button btnExitDiaLog,btnChangeName;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String UID,name;
    public static FragmentChangeName newInstance(String id, String name) {
        FragmentChangeName fragmentChangeName = new FragmentChangeName();
        Bundle bundle = new Bundle();
        bundle.putString("getID",id);
        bundle.putString("getName",name);
        fragmentChangeName.setArguments(bundle);
        return fragmentChangeName;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diaglog_edit_name, null);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UID = getArguments().getString("getID");
        name = getArguments().getString("getName");
        txtOldName = view.findViewById(R.id.txtOldName);
        txtOldName.setText(name);
        txtNewName = view.findViewById(R.id.txtNewName);
        btnExitDiaLog = view.findViewById(R.id.btnExitDialog);
        btnChangeName = view.findViewById(R.id.btnChangeName);
        btnExitDiaLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.user.setName(txtNewName.getText().toString().trim());
                databaseReference.child("User").child(UID).setValue(ProfileFragment.user);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
