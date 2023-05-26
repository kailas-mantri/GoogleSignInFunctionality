package com.scrimatec.food18.Interface;

import com.scrimatec.food18.models.OtpVerification;
import com.scrimatec.food18.models.ProfileResponceManager;
import com.scrimatec.food18.models.SignUpResponse;
import com.scrimatec.food18.models.SliderImageModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("scrima_api.php?filter=signup")
    Call<SignUpResponse>SIGN_UP_RESPONSE_CALL(@Body String body);

    @POST("scrima_api.php?filter=otp")
    Call<OtpVerification>OTP_VERIFICATION_CALL(@Body String body);

    @POST("scrima_api.php?filter=slider_img")
    Call<SliderImageModel>SLIDER_IMAGE_MODEL_CALL();

    @POST("scrima_api.php?filter=profile")
    Call<ProfileResponceManager>PROFILE_RESPONCE_MANAGER_CALL(@Body String body);

    @POST("scrima_api.php?filter=profile")
    Call<String>PROFILE_DETAILS_OF_USER_CALL(@Body String body);

}
