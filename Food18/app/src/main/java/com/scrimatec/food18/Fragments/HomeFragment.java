package com.scrimatec.food18.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.scrimatec.food18.Interface.APIInterface;
import com.scrimatec.food18.R;
import com.scrimatec.food18.models.SliderAdapterExample;
import com.scrimatec.food18.models.SliderImageModel;
import com.scrimatec.food18.models.Urls;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragment extends Fragment {

    SliderView sliderView;
    private SliderAdapterExample adapter;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final APIInterface apiInterface = retrofit.create(APIInterface.class);

    TabLayout tabLayout;
    ViewPager viewPager;

    public HomeFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        tabLayout = rootView.findViewById(R.id.tablayout);
        viewPager = rootView.findViewById(R.id.viewpagers);
        addTabs(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setupTAbIcon();
        
        sliderView = rootView.findViewById(R.id.imageSlider);

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        addNewItem();

        return rootView;
    }

    private void setupTAbIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_regular_order);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_extra_order);
    }

    public void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new Regular_order(),"REGULAR ORDER");
        adapter.addFrag(new Extra_order(),"EXTRA ORDER");
        viewPager.setAdapter(adapter);
    }

    public void addNewItem() {
        try {
            final Call<SliderImageModel> sliderImageModelCall =apiInterface.SLIDER_IMAGE_MODEL_CALL();
            sliderImageModelCall.enqueue(new Callback<SliderImageModel>() {
                @Override
                public void onResponse(Call<SliderImageModel> call, Response<SliderImageModel> response) {
                    if (response.isSuccessful()){
                        SliderImageModel imageModel =response.body();
                        adapter=new SliderAdapterExample(getContext(),imageModel);
                        sliderView.setSliderAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<SliderImageModel> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) { return mFragmentList.get(position); }

        @Override
        public int getCount() { return mFragmentList.size(); }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }
    }
}
