package com.example.ihc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ihc.data.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    private EditText setBio, setAge, setCountry;
    private Button btnPhoto, updateBtn, cancelBtn;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    String storagepath = "photos/";
    ImageView set;
    ProgressDialog pd;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String[] cameraPermission;
    String[] storagePermission;
    Uri imageuri;
    String profileOrCoverPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editprofile);

        setBio = findViewById(R.id.bio);
        setAge = findViewById(R.id.age);
        setCountry = findViewById(R.id.country);
        set = findViewById(R.id.profile_image);
        btnPhoto = findViewById(R.id.setPhoto);
        updateBtn = findViewById(R.id.update_profile_btn);
        cancelBtn = findViewById(R.id.cancel_update);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE};
            storagePermission = new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE};
        } else {
            cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        firestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        firestore.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            assert user != null;

            Picasso.get().load(user.getPhotoUri()).into(set);
        });

        btnPhoto.setOnClickListener(view -> {
            Toast.makeText(EditProfile.this, "Updating profile photo!", Toast.LENGTH_SHORT).show();
            profileOrCoverPhoto = "profile_photo";
            showImagePicDialog();
        });

        cancelBtn.setOnClickListener(v -> {
            Toast.makeText(EditProfile.this, "Update canceled!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditProfile.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        updateBtn.setOnClickListener(view -> {
            if (!setBio.getText().toString().equals(""))
                firestore.collection("users").document(firebaseUser.getUid()).update("bio", setBio.getText().toString());
            if (!setAge.getText().toString().equals(""))
                firestore.collection("users").document(firebaseUser.getUid()).update("age", setAge.getText().toString());
            if (!setCountry.getText().toString().equals(""))
                firestore.collection("users").document(firebaseUser.getUid()).update("country", setCountry.getText().toString());
            Intent intent = new Intent(EditProfile.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // checking storage permission, if given then we can add something in our storage
    private Boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    // requesting for storage permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permission, if given then we can click image using our camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        Log.e("------------------------", String.valueOf(result));
        boolean result1;
        result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        Log.e("...........................", String.valueOf(result1));
        return result && result1;
    }

    // requesting for camera permission if not given
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // Here we are showing image pic dialog where we will select
    // an image either from camera or gallery
    private void showImagePicDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Image From");
        builder.setItems(options, (dialog, which) -> {
            // if access is not given then we will request for permission
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromCamera();
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(",,,,,,,,,,,,,,,,,,,,,,,,,,,", String.valueOf(resultCode));
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
                assert data != null;
                imageuri = data.getData();
                uploadProfileCoverPhoto(imageuri);
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                uploadProfileCoverPhoto(imageuri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // Here we will click a photo and then go to startactivityforresult for updating data
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(cameraIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    // We will select an image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

    // We will upload the image from here.
    private void uploadProfileCoverPhoto(final Uri uri) {
        pd.show();
        // We are taking the filepath as storagepath + firebaseauth.getUid()+".png"
        String filepathname = storagepath + firebaseUser.getUid() + "/" + profileOrCoverPhoto;
        StorageReference storageReference1 = storageReference.child(filepathname);
        storageReference1.putFile(uri).addOnSuccessListener((OnSuccessListener<UploadTask.TaskSnapshot>) taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;

            // We will get the url of our image using uritask
            final Uri downloadUri = uriTask.getResult();
            if (uriTask.isSuccessful()) {
                // updating our image url into the database
                firestore.collection("users").document(firebaseUser.getUid()).update("photoUri", downloadUri.toString()).addOnSuccessListener(aVoid -> {
                    pd.dismiss();
                    Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditProfile.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(EditProfile.this, "Error Updating ", Toast.LENGTH_LONG).show();
                });
            } else {
                pd.dismiss();
                Toast.makeText(EditProfile.this, "Error", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(EditProfile.this, "Error", Toast.LENGTH_LONG).show();
        });
    }
}