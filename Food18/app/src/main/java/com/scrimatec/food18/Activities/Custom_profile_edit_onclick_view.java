package com.scrimatec.food18.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.makeramen.roundedimageview.RoundedImageView;
import com.scrimatec.food18.Interface.APIInterface;
import com.scrimatec.food18.R;
import com.scrimatec.food18.models.ProfileResponceManager;
import com.scrimatec.food18.models.SharedPreferanceManager;
import com.scrimatec.food18.models.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Custom_profile_edit_onclick_view extends Activity {

    //Basic inicialization of network library
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final APIInterface apiInterface = retrofit.create(APIInterface.class);
    String userId = SharedPreferanceManager.getInstance(this).getUsers().getUser_id();

    EditText profile_name, profile_mobile_number,
             profile_email, profile_date_of_birth, profile_address;

    RadioButton buttonMale, buttonFemale, buttonOther;
    RadioGroup profile_gender_group;

    RoundedImageView roundedImageView, roundImageeditpage;

    Button profile_save_button, select_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_profile_edit_onclick_view);
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                finish();
                startActivity(intent);
                return;
            }

            profile_gender_group = findViewById(R.id.title_profile_gender);
            buttonMale = findViewById(R.id.profile_gender_male);
            buttonFemale = findViewById(R.id.profile_gender_female);
            buttonOther = findViewById(R.id.profile_gender_other);

            profile_name = findViewById(R.id.profile_Full_Name);
            profile_email = findViewById(R.id.profile_Email);
            profile_address = findViewById(R.id.profile_address);
            profile_mobile_number = findViewById(R.id.Mobile_Number);
            profile_date_of_birth = findViewById(R.id.profile_Date_of_birth);

            roundImageeditpage = findViewById(R.id.profile_image_view_edit);
            profile_save_button = findViewById(R.id.fragment_profile_user_save_button);
        }
        profile_gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint({"NewApi", "NonConstantResourceId"})
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.profile_gender_male:
                    setRadioSelected(buttonMale);
                    setRadioUnselected(buttonFemale);
                    setRadioUnselected(buttonOther);
                    break;

                case R.id.profile_gender_female:
                    setRadioSelected(buttonFemale);
                    setRadioUnselected(buttonMale);
                    setRadioUnselected(buttonOther);
                    break;

                case R.id.profile_gender_other:
                    setRadioSelected(buttonOther);
                    setRadioUnselected(buttonMale);
                    setRadioUnselected(buttonFemale);
                    break;
            }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setRadioSelected(RadioButton button) {
        button.setChecked(true);
        button.setBackground(getResources().getDrawable(R.drawable.radio_selected));
        button.setTextColor(getResources().getColor(R.color.white));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setRadioUnselected(RadioButton button) {
        button.setBackground(getResources().getDrawable(R.drawable.radio_background));
        button.setTextColor(getResources().getColor(R.color.white));
        button.setChecked(false);
    }

    public void Update_Profile(View view) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("u_id", userId);
            jsonObject.put("name", profile_name.getText().toString());
            jsonObject.put("email", profile_email.getText().toString());
            jsonObject.put("dob", profile_date_of_birth.getText().toString());
            jsonObject.put("mob", profile_mobile_number.getText().toString());
            jsonObject.put("gen", profile_gender_group);

            final Call<ProfileResponceManager> profileResponceManagerCall = apiInterface.PROFILE_RESPONCE_MANAGER_CALL(jsonObject.toString());

            profileResponceManagerCall.enqueue(new Callback<ProfileResponceManager>() {
                @Override
                public void onResponse(@NonNull Call<ProfileResponceManager> call, @NonNull Response<ProfileResponceManager> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Custom_profile_edit_onclick_view.this, "Responce is successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Custom_profile_edit_onclick_view.this,HomeActivity.class);
                        intent.putExtra("Profile", 1);
                        startActivity(intent);
                    }
                }

                 @Override
                public void onFailure(@NonNull Call<ProfileResponceManager> call, @NonNull Throwable t) {
                    Toast.makeText(Custom_profile_edit_onclick_view.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingInflatedId")
    public void upload_registration_image(View view) {

        ViewGroup viewGroup = findViewById(R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.upload_image, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        roundedImageView = dialogView.findViewById(R.id.profile_image_view_edit);
        select_button = dialogView.findViewById(R.id.selectimagebutton);

        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent_image = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(intent_image,100);
            }
        });
    }
}
