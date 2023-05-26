package com.scrimatec.food18.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.scrimatec.food18.Interface.APIInterface;
import com.scrimatec.food18.R;
import com.scrimatec.food18.models.OtpVerification;
import com.scrimatec.food18.models.SharedPreferanceData;
import com.scrimatec.food18.models.SharedPreferanceManager;
import com.scrimatec.food18.models.SignUpResponse;
import com.scrimatec.food18.models.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText number, otptext, loginmobile, password;
    Button buttonsignup, loginbutton;
    Button varify, otpbutton;
    SharedPreferanceData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (SharedPreferanceManager.getInstance(this).isLoggedIn()){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        imageView = findViewById(R.id.image);
        buttonsignup = findViewById(R.id.signup);

        loginmobile = findViewById(R.id.loginmobile);
        password = findViewById(R.id.password);
        loginbutton = findViewById(R.id.login_Successful);
    }

    public void AlertDialoguebox(View view) {

        ViewGroup viewGroup = findViewById(R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_registration_signup, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        number = dialogView.findViewById(R.id.registernumber);
        varify = dialogView.findViewById(R.id.submitregister);

        number.addTextChangedListener(new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int num = number.length();
            if (num == 10) {
                varify.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners));
                varify.setClickable(true);
                varify.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (varify.isClickable()) {
                        alertDialog.hide();

                        //Basic inicialization of network library
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Urls.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final APIInterface apiInterface = retrofit.create(APIInterface.class);

                        try {
                            JSONObject jsonobject = new JSONObject();
                            jsonobject.put("mob", number.getText().toString());
                            jsonobject.put("token", "c1850000");

                            //Calling the retrofit library
                            final Call<SignUpResponse> responseCall = apiInterface.SIGN_UP_RESPONSE_CALL(jsonobject.toString());

                            // Server-responce : how to get the responce data through server
                            responseCall.enqueue(new Callback<SignUpResponse>() {

                            @Override
                            public void onResponse(@NonNull Call<SignUpResponse> call, @NonNull Response<SignUpResponse> response) {
                                if (response.isSuccessful()) {
                                    otpDialogBox();
                                    SignUpResponse signUpResponse = response.body();
                                    assert signUpResponse != null;
                                    Toast.makeText(MainActivity.this, signUpResponse.getId(), Toast.LENGTH_SHORT).show();

                                    // save data to the current page
                                    data=new SharedPreferanceData(number.getText().toString(),signUpResponse.getId());

                                    try {
                                        JSONObject optjsonobject = new JSONObject();
                                        optjsonobject.put("mobile", number.getText().toString());
                                        Call<OtpVerification> verificationCall = apiInterface.OTP_VERIFICATION_CALL(optjsonobject.toString());
                                        verificationCall.enqueue(new Callback<OtpVerification>() {
                                            @Override
                                            public void onResponse(@NonNull Call<OtpVerification> call, @NonNull Response<OtpVerification> response) {
                                                if (response.isSuccessful()) {
                                                    OtpVerification otpVerification = response.body();
                                                    assert otpVerification != null;
                                                    Toast.makeText(MainActivity.this, otpVerification.getOtp(), Toast.LENGTH_SHORT).show();
                                                } }
                                                @Override
                                                public void onFailure(@NonNull Call<OtpVerification> call, @NonNull Throwable t) {
                                                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (JSONException e) { e.printStackTrace(); }
                                    }
                                }
                                @Override
                                public void onFailure(@NonNull Call<SignUpResponse> call, @NonNull Throwable t) {
                                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                }
                });
            } else {
                varify.setClickable(false);
                varify.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_off));
            }
        }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void otpDialogBox() {
        ViewGroup viewGroup = findViewById(R.id.content);

        final View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_register, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        otptext = dialogView.findViewById(R.id.otp);
        otpbutton = dialogView.findViewById(R.id.submit);

        otptext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = otptext.length();
                if (length == 4)
                {
                    otpbutton.setClickable(true);
                    otpbutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners));
                    otpbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        if (otpbutton.isClickable()) {
                            clear();
                            alertDialog.hide();
//                                Toast.makeText(MainActivity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                            SharedPreferanceManager.getInstance(getApplicationContext()).userLogin(data);
                            startActivity(new Intent(MainActivity.this, Change_password.class));
                            finish();
                        }
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void clear() { number.setText(""); }
}