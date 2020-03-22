package vn.itplus.reviewmovie.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.List;

import vn.itplus.reviewmovie.LoginActivity;
import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.model.comment.Comment;
import vn.itplus.reviewmovie.model.user.User;
import vn.itplus.reviewmovie.utils.ImagePickerActivity;
import vn.itplus.reviewmovie.utils.Utils;

public class ProfileFragment extends Fragment {

    CircularImageView imgUser,addPic;
    ImageButton imgEditName;
    TextView txtUserName;
    Button btnSeen,btnLiked,btnLocation,btnHotLine,btnInfo,btnLogout, btnExitDiaLog, btnChangeName ;

   static User user;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    String UID;
    private static final String TAG = ProfileFragment.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,null);
        addControls(view);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("PhotoUser");
        getProfile();
        addEvents();


       // loadProfileDefault();
        return view;
    }
    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(getActivity()).load(url)
                .into(imgUser);
    }

    private void loadProfileDefault() {
        Glide.with(getActivity()).load(R.drawable.profile)
                .into(imgUser);
    }

    private void getProfile() {
    UID = firebaseUser.getUid();
       try {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("User").getChildren()){
                    user = ds.getValue(User.class);

                   if (user.getId().equalsIgnoreCase(UID)){
                        Log.d("ABCXXX",user.getPhotoURL());

                        txtUserName.setText(user.getName());
                        txtUserName.setTextSize(20);

                        if (user.getPhotoURL().equalsIgnoreCase("no photo")){
                            loadProfileDefault();
                        }else {
                            Glide.with(getActivity()).load(user.getPhotoURL() ).into(imgUser);

                        }

                        break;}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }catch (Exception e){
           e.printStackTrace();
       }
    }

    private void addEvents() {
        imgEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentChangeName fragmentChangeName = new FragmentChangeName();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.begin_bottom_to_top,R.anim.exit_bottom_to_top
                        ,R.anim.begin_top_to_bottom,R.anim.exit_top_to_bottom)
                        .replace(R.id.frame_container, fragmentChangeName.newInstance(UID, txtUserName.getText().toString()))
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
addPic.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onProfileImageClick();
    }
});
imgUser.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showImagePickerOptions();
    }
});
    }
    void onProfileImageClick() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    if (uri != null){
                        Utils.SetProgressDialogIndeterminate(getActivity(),"Uploading..");
                        StorageReference storage = storageReference.child(System.currentTimeMillis()
                        +"."+ getFileExtention(uri));
                       storage.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                           @Override
                           public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                               if (!task.isSuccessful()) {
                                   throw task.getException();
                               }
                               return storage.getDownloadUrl();
                           }
                       }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                           @Override
                           public void onComplete(@NonNull Task<Uri> task) {
                               if (task.isSuccessful()) {

                                   Uri downloadUri = task.getResult();
                                  user.setPhotoURL(downloadUri.toString());
                                  databaseReference.child("User").child(UID).setValue(user);
                                  Utils.UnSetProgressDialogIndeterminate();
                               } else {
                                   Utils.UnSetProgressDialogIndeterminate();
                                   Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       });

                    }else {
                        Utils.UnSetProgressDialogIndeterminate();
                        Toast.makeText(getActivity(),"Upload fail",Toast.LENGTH_LONG).show();
                    }
                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String getFileExtention(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void addControls(View view) {
        imgEditName = view.findViewById(R.id.imgEditName);
        addPic = view.findViewById(R.id.addPic);
        imgUser = view.findViewById(R.id.imgUser);
        txtUserName = view.findViewById(R.id.txtUserName);
        btnSeen = view.findViewById(R.id.btnSeen);
        btnLiked = view.findViewById(R.id.btnLiked);
        btnLocation = view.findViewById(R.id.btnLocation);
        btnHotLine = view.findViewById(R.id.btnHotLine);
        btnInfo = view.findViewById(R.id.btnInfo);
        btnLogout = view.findViewById(R.id.btnLogout);
        user = new User();
    }
}
