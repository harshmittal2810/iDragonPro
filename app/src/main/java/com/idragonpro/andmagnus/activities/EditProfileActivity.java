package com.idragonpro.andmagnus.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.responseModels.NewRegisterResponseModel;
import com.idragonpro.andmagnus.responseModels.ProfileResponseModel;
import com.idragonpro.andmagnus.responseModels.RegisterResponseModel;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewBackArrow;
    private EditText editTextFirstName, editTextLasttName, editTextMobile, editEmail;
    private Button updateButton;
    private Dialog dialog;
    private Button uploadProfileButton;
    private CircleImageView circleImageView;
    private String base64String;
    private Bitmap resizedBitmap;
    private TextView textViewMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        setClickListener();

        setData();
    }

    private void setData() {
        editTextFirstName.setText(SaveSharedPreference.getFirstName(EditProfileActivity.this));
        editTextLasttName.setText(SaveSharedPreference.getLastName(EditProfileActivity.this));
        editEmail.setText(SaveSharedPreference.getEmail(EditProfileActivity.this));
        if (SaveSharedPreference.getLoginFromGoogle(EditProfileActivity.this)) {
            editTextMobile.setVisibility(View.GONE);
            textViewMobile.setVisibility(View.GONE);
        } else {
            editTextMobile.setVisibility(View.VISIBLE);
            textViewMobile.setVisibility(View.VISIBLE);
            editTextMobile.setText(SaveSharedPreference.getMobileNumber(EditProfileActivity.this));
        }

        if (SaveSharedPreference.getProfilePic(EditProfileActivity.this) != null && !SaveSharedPreference.getProfilePic(
            EditProfileActivity.this).isEmpty()) {
            Glide.with(circleImageView.getContext())
                .load(SaveSharedPreference.getProfilePic(EditProfileActivity.this))
                .centerCrop()
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(circleImageView);
        }
    }

    private void setClickListener() {
        imageViewBackArrow.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        uploadProfileButton.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
    }

    private void initViews() {
        imageViewBackArrow = findViewById(R.id.imageViewBackArrow);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLasttName = findViewById(R.id.editTextLasttName);
        editEmail = findViewById(R.id.editEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        textViewMobile = findViewById(R.id.textViewMobile);
        updateButton = findViewById(R.id.updateButton);
        uploadProfileButton = findViewById(R.id.uploadProfileButton);
        circleImageView = findViewById(R.id.circleImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewBackArrow:
                onBackPressed();
                break;
            case R.id.updateButton:
                checkValidation();
                break;
            case R.id.uploadProfileButton:
                updateProfileImage();
                break;
            case R.id.circleImageView:
                requestPermission();
                break;
        }
    }

    private void checkValidation() {
        if (editTextFirstName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter Valid First Name", Toast.LENGTH_SHORT).show();
        } else if (editTextLasttName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter Valid Last Name", Toast.LENGTH_SHORT).show();
        } else {
            dialog = GlobalModule.showProgressDialog("Updating...", this);
            updateProfile();
        }
    }

    private void updateProfile() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<NewRegisterResponseModel> call = webApi.updateProfile(API.USER_ROLE,
            editTextFirstName.getText().toString(),
            editTextLasttName.getText().toString(),
            SaveSharedPreference.getUserId(EditProfileActivity.this));
        call.enqueue(new Callback<NewRegisterResponseModel>() {
            @Override
            public void onResponse(Call<NewRegisterResponseModel> call, Response<NewRegisterResponseModel> response) {
                if (response.body() != null && response.body().getRegisterResponseModel() != null) {
                    setLoginDetails(response.body().getRegisterResponseModel());
                    Toast.makeText(getApplicationContext(), "Your Profile is Updated Successfully", Toast.LENGTH_LONG)
                        .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Profile Updated Failed", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<NewRegisterResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Profile Updated Failed", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void updateProfileImage() {
        if (base64String == null) {
            Toast.makeText(this, "please select a image!", Toast.LENGTH_SHORT).show();
            return;
        }
        Dialog dialog = GlobalModule.showProgressDialog("Processing...", this);
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<ProfileResponseModel> call = webApi.updateProfileImage(SaveSharedPreference.getUserId(EditProfileActivity.this),
            base64String);
        call.enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getUser() != null) SaveSharedPreference.setProfilePic(EditProfileActivity.this,
                        response.body().getUser().getProfilePic());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Profile Image Updated Failed", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void setLoginDetails(RegisterResponseModel registerResponseModel) {
        SaveSharedPreference.setUserId(this, registerResponseModel.getUserId());
        SaveSharedPreference.setFirstName(this, registerResponseModel.getSFirstName());
        SaveSharedPreference.setLastName(this, registerResponseModel.getSLastName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    Uri uri = FileProvider.getUriForFile(
                            getApplicationContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 1);*/
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Dialog dialog = GlobalModule.showProgressDialog("Processing...", this);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bmp = (Bitmap) data.getExtras().get("data");
                        encodeToBase64(bmp, false);
                        dialog.dismiss();
                    }
                });
            } else if (requestCode == 2) {
                Dialog dialog = GlobalModule.showProgressDialog("Processing...", this);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Uri selectedImage = data.getData();
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);
                        c.close();
                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                        try {
                            ExifInterface exifInterface = new ExifInterface(picturePath);
                            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);

                            if (orientation == 6) {
                                encodeToBase64(thumbnail, true);
                            } else {
                                encodeToBase64(thumbnail, false);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    private void encodeToBase64(Bitmap bitmapOld, boolean isImageSelectedFromGallery) {
        resizedBitmap = resizedBitmap(bitmapOld);
        if (isImageSelectedFromGallery) resizedBitmap = rotate(resizedBitmap, 90f);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                circleImageView.setImageBitmap(resizedBitmap);
            }
        });
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        //        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        //        Log.d("TAG","base 64: 11:"+decoded.getWidth()+"___"+ bitmap[0].getWidth());
        base64String = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap resizedBitmap(Bitmap image) {
        int maxSize = 200;
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            selectImage();
        } else {
            // You can directly ask for the permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            selectImage();
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}