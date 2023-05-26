package com.futuregenerations.helpinghands.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.futuregenerations.helpinghands.R;
import com.futuregenerations.helpinghands.models.NotificationDataModel;
import com.futuregenerations.helpinghands.models.NotificationListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private ListView listView;
    private MKLoader loader;
    private DatabaseReference reference;
    private String userID;
    private List<NotificationDataModel> dataModelList;
    private TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public NotificationsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications,container,false);

        listView = rootView.findViewById(R.id.notifications_list);
        textView = rootView.findViewById(R.id.text_no_notifications);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        loader = rootView.findViewById(R.id.loader);
        dataModelList = new ArrayList<>();
        loader.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("UserNotifications");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser!=null) {
            userID = firebaseUser.getUid();
        }

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                child = listView;
                return child.canScrollVertically(-1);
            }
        });

        onSwipeRefreshed();

        getUserNotifications();

        return rootView;
    }

    private void onSwipeRefreshed() {
        if (swipeRefreshLayout.canChildScrollUp()) {
            swipeRefreshLayout.setEnabled(false);
        }
        else {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    getUserNotifications();
                }
            });
        }
    }

    private void getUserNotifications() {
        dataModelList.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    NotificationDataModel dataModel = snapshot.getValue(NotificationDataModel.class);
                                    dataModelList.add(dataModel);
                                }
                                NotificationListAdapter adapter = new NotificationListAdapter(getContext(),dataModelList);
                                listView.setAdapter(adapter);
                                loader.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            else {
                                loader.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    loader.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
