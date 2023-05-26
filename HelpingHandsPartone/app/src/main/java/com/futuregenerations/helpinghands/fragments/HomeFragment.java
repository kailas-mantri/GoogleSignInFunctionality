package com.futuregenerations.helpinghands.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.activities.users.FavouritesActivity;
import com.futuregenerations.helpinghands.activities.users.HistoryActivity;
import com.futuregenerations.helpinghands.activities.users.HomeActivity;
import com.futuregenerations.helpinghands.activities.users.OrganizationTypesActivity;
import com.futuregenerations.helpinghands.models.FavoritesRecyclerViewAdapter;
import com.futuregenerations.helpinghands.models.GetOrganizationsDataModel;
import com.futuregenerations.helpinghands.models.PaymentsRecyclerViewAdapter;
import com.futuregenerations.helpinghands.models.UserDataModel;
import com.futuregenerations.helpinghands.models.UserPaymentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    TextView textViewName, textViewEmail, textViewUserName, textViewUserEmail, textViewUserContact, textViewUserAddress
            , textViewDonationCount, textViewDonationAmount, textViewLastDonationTo, textViewLastDonationAmount
            , textViewLastDonationDate, textViewButtonViewHistory, textViewViewPayments
            , textViewButtonFavorites, textViewDonateMoney, textViewNoData, textViewNoData2;

    RecyclerView recyclerViewFavorites, recyclerViewPayments;

    RecyclerView.LayoutManager RecyclerViewLayoutManagerPayments;
    RecyclerView.LayoutManager RecyclerViewLayoutManagerFavorites;
    PaymentsRecyclerViewAdapter RecyclerViewHorizontalAdapterPayments;
    FavoritesRecyclerViewAdapter RecyclerViewHorizontalAdapterFavorites;
    LinearLayoutManager HorizontalLayoutPayments;
    LinearLayoutManager HorizontalLayoutFavorites;

    RelativeLayout layout;
    MKLoader loader;

    DatabaseReference userReference, paymentReference, favoritesReference;

    String userID, userName, userEmail, userContact, userAddress, userImage, userDate, userCity, userState;

    UserDataModel userModel;
    UserPaymentModel paymentModel;
    GetOrganizationsDataModel organizationsDataModel;

    RoundedImageView imageView;

    List<UserPaymentModel> paymentModelList;
    List<GetOrganizationsDataModel> organizationsDataModelList;

    int amount = 0, count, listSize;
    String lastDate, lastOrg, lastAmount, totalAmount, totalCount;

    Context context;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        setRecyclerLayouts(rootView);
        getViewsByIDs(rootView);

        setAllReferences();
        getUserDetails();
        setAllOnClicks();

        return rootView;
    }

    private void setAllOnClicks() {

        textViewButtonViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        textViewViewPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),HistoryActivity.class);
                startActivity(intent);
            }
        });

        textViewButtonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FavouritesActivity.class);
                startActivity(intent);
            }
        });

        textViewDonateMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrganizationTypesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAllReferences() {
        paymentModelList = new ArrayList<>();
        organizationsDataModelList = new ArrayList<>();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        paymentReference = FirebaseDatabase.getInstance().getReference("Payments");
        favoritesReference = FirebaseDatabase.getInstance().getReference("UserFavorites");
    }

    private void setRecyclerLayouts(View rootView) {
        recyclerViewPayments = rootView.findViewById(R.id.recyclerViewPayments);
        RecyclerViewLayoutManagerPayments = new LinearLayoutManager(getContext());
        recyclerViewPayments.setLayoutManager(RecyclerViewLayoutManagerPayments);
        HorizontalLayoutPayments = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPayments.setLayoutManager(HorizontalLayoutPayments);
        recyclerViewPayments.setAdapter(RecyclerViewHorizontalAdapterPayments);

        recyclerViewFavorites = rootView.findViewById(R.id.recyclerViewFavorites);
        RecyclerViewLayoutManagerFavorites = new LinearLayoutManager(getContext());
        recyclerViewFavorites.setLayoutManager(RecyclerViewLayoutManagerFavorites);
        HorizontalLayoutFavorites = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewFavorites.setLayoutManager(HorizontalLayoutFavorites);
        recyclerViewFavorites.setAdapter(RecyclerViewHorizontalAdapterFavorites);
    }

    private void getViewsByIDs(View rootView) {
        textViewName = rootView.findViewById(R.id.text_name);
        textViewEmail = rootView.findViewById(R.id.text_email);
        textViewDonateMoney = rootView.findViewById(R.id.donate_money);

        textViewUserName = rootView.findViewById(R.id.user_name);
        textViewUserEmail = rootView.findViewById(R.id.user_email);
        textViewUserContact = rootView.findViewById(R.id.user_contact);
        textViewUserAddress = rootView.findViewById(R.id.user_address);
        imageView = rootView.findViewById(R.id.user_image);

        textViewDonationCount = rootView.findViewById(R.id.donation_count);
        textViewDonationAmount = rootView.findViewById(R.id.donation_amount);
        textViewLastDonationTo = rootView.findViewById(R.id.last_donated_to);
        textViewLastDonationAmount = rootView.findViewById(R.id.last_donated_amount);
        textViewLastDonationDate = rootView.findViewById(R.id.last_donated_date);
        textViewButtonViewHistory = rootView.findViewById(R.id.view_history);

        textViewViewPayments = rootView.findViewById(R.id.view_payments);
        textViewButtonFavorites = rootView.findViewById(R.id.view_favorites);
        layout = rootView.findViewById(R.id.home_main_layout);
        loader = rootView.findViewById(R.id.loader);
        textViewNoData = rootView.findViewById(R.id.text_no_data);
        textViewNoData2 = rootView.findViewById(R.id.text_no_data_2);

        layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    private void getUserDetails() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser!=null) {
            userID = firebaseUser.getUid();
        }
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userModel = dataSnapshot.getValue(UserDataModel.class);
                            if (userModel!=null) {
                                userName = userModel.getUserName();
                                userEmail = userModel.getUserEmail();
                                userContact = userModel.getUserMobile();
                                userImage = userModel.getUserImage();
                                userState = userModel.getUserState();
                                userCity = userModel.getUserCity();
                                userAddress = userModel.getUserAddress();
                                userDate = userModel.getUserDate();
                            }

                            textViewName.setText(userName);
                            textViewUserName.setText(userName);
                            textViewEmail.setText(userEmail);
                            textViewUserEmail.setText(userEmail);
                            textViewUserContact.setText(userContact);
                            textViewUserAddress.setText(userAddress);
                            Glide.with(context)
                                    .load(userImage)
                                    .into(imageView);

                            getDonationDetails();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    getDonationDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDonationDetails() {
        paymentModelList.clear();
        paymentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    paymentReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    paymentModel = snapshot.getValue(UserPaymentModel.class);
                                    paymentModelList.add(paymentModel);
                                }
                                RecyclerViewHorizontalAdapterPayments = new PaymentsRecyclerViewAdapter(getContext(),paymentModelList);
                                recyclerViewPayments.setAdapter(RecyclerViewHorizontalAdapterPayments);
                                getDataForDonationView();
                                getFavoritesDetails();
                            }
                            else {
                                textViewNoData.setVisibility(View.VISIBLE);
                                getFavoritesDetails();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            textViewNoData.setVisibility(View.VISIBLE);
                            getFavoritesDetails();
                        }
                    });
                }
                else {
                    textViewNoData.setVisibility(View.VISIBLE);
                    getFavoritesDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textViewNoData.setVisibility(View.VISIBLE);
                getFavoritesDetails();
            }
        });
    }

    private void getDataForDonationView() {
        listSize = paymentModelList.size();
        int position = listSize - 1;
        int i;
        lastAmount = paymentModelList.get(position).getPaymentDetails().getDonationAmount();
        lastDate = paymentModelList.get(position).getPaymentDetails().getDonationDate();
        lastOrg = paymentModelList.get(position).getOrganizationDetails().getOrganizationTitle();
        count = listSize;
        for (i=0;i<listSize;i++) {
            String money = paymentModelList.get(i).getPaymentDetails().getDonationAmount();
            int donationMoney = Integer.parseInt(money);
            amount = amount+donationMoney;
        }
        totalCount = String.valueOf(count);
        totalAmount = String.valueOf(amount);

        textViewLastDonationDate.setText(lastDate);
        textViewLastDonationAmount.setText(lastAmount);
        textViewLastDonationTo.setText(lastOrg);
        textViewDonationCount.setText(totalCount);
        textViewDonationAmount.setText(totalAmount);
    }

    private void getFavoritesDetails() {
        organizationsDataModelList.clear();
        favoritesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    favoritesReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    organizationsDataModel = snapshot.getValue(GetOrganizationsDataModel.class);
                                    organizationsDataModelList.add(organizationsDataModel);
                                }
                                RecyclerViewHorizontalAdapterFavorites = new FavoritesRecyclerViewAdapter(getContext(),organizationsDataModelList);
                                recyclerViewFavorites.setAdapter(RecyclerViewHorizontalAdapterFavorites);
                                layout.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                            }
                            else {
                                layout.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                                textViewNoData2.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            layout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);
                            textViewNoData2.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else {
                    layout.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    textViewNoData2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                textViewNoData2.setVisibility(View.VISIBLE);
            }
        });
    }
}
