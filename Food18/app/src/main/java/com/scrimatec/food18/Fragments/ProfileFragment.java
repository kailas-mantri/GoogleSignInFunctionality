package com.scrimatec.food18.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.makeramen.roundedimageview.RoundedImageView;
import com.scrimatec.food18.Activities.Custom_profile_edit_onclick_view;
import com.scrimatec.food18.Interface.APIInterface;
import com.scrimatec.food18.R;
import com.scrimatec.food18.models.SharedPreferanceManager;
import com.scrimatec.food18.models.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileFragment extends Fragment {

    ImageView imageView;
    TextView textname, textemail, textmobile, textaddress, textgender, textdateob;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final APIInterface apiInterface = retrofit.create(APIInterface.class);

    RoundedImageView roundImageView;
    RelativeLayout relativeLayout;

    public ProfileFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile,container,false);

        textname = rootView.findViewById(R.id.textValueName);
        textemail = rootView.findViewById(R.id.textValueEmail);
        textmobile = rootView.findViewById(R.id.textValueMobile);
        textaddress = rootView.findViewById(R.id.textValueAddress);
        textgender = rootView.findViewById(R.id.textValueGender);
        textdateob = rootView.findViewById(R.id.textValueDate);

        relativeLayout = rootView.findViewById(R.id.shared_data);
        roundImageView = rootView.findViewById(R.id.user_image_profile);

        final AppCompatActivity activity=(AppCompatActivity)getActivity();
        if (activity.getSupportActionBar()!=null){
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            imageView = toolbar.findViewById(R.id.edit_image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), Custom_profile_edit_onclick_view.class));
                }
            });
        }

        String u_id = SharedPreferanceManager.getInstance(getContext()).getUsers().getUser_id();

        JSONObject object = new JSONObject();
        try {
            object.put("u_id",u_id);
            final Call<String> stringCall = apiInterface.PROFILE_DETAILS_OF_USER_CALL(object.toString());

            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String dataresponce = response.body();

                        try {

                            JSONObject jsonObject = new JSONObject(dataresponce);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject profiledetails = jsonArray.getJSONObject(0);

                            String user_id = profiledetails.getString("u_id");
                            String name = profiledetails.getString("u_name");
                            String email = profiledetails.getString("u_email");
                            String gen = profiledetails.getString("u_gen");

//                            String mobile = profiledetails.getString("u_mob");
                            String mobile = SharedPreferanceManager.getInstance(getContext()).getUsers().getMobile_number();

//                            String address = profiledetails.getString("u_address");

                            String dateob = profiledetails.getString("u_dobl");

                            textname.setText(name);
                            textemail.setText(email);
                            textmobile.setText(mobile);
                            textdateob.setText(dateob);
                            textgender.setText(gen);

//                            textaddress.setText(address);

                        }
                        catch (JSONException e) { e.printStackTrace(); }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) { }
            });
        }
        catch (JSONException e) { e.printStackTrace(); }
        return rootView;
    }
}