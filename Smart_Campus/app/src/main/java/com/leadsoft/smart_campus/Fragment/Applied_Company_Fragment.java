package com.leadsoft.smart_campus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leadsoft.smart_campus.R;

import java.util.ArrayList;
import java.util.List;

public class Applied_Company_Fragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public Applied_Company_Fragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_applied_comapany,container,false);
        tabLayout = rootView.findViewById(R.id.tab_layout);
        viewPager = rootView.findViewById(R.id.view_pagers);
        addTabs(viewPager);
        return rootView;
    }

    public void addTabs(ViewPager viewPager) {
        ViewPagerAdaptor adaptor = new ViewPagerAdaptor(getChildFragmentManager());
        adaptor.addFrag(new Applied_Status_company(),"Applied");
        adaptor.addFrag(new Saved_Company(),"Applied");
        viewPager.setAdapter(adaptor);
    }

    public class ViewPagerAdaptor extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdaptor(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
