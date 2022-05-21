package com.rentalmotor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.rentalmotor.HelperClasses.SeeAllFragment.AllFragment;
import com.rentalmotor.HelperClasses.SeeAllFragment.Manual;
import com.rentalmotor.HelperClasses.SeeAllFragment.Matic;
import com.rentalmotor.HelperClasses.SeeAllFragment.Sport;

import java.util.ArrayList;
import java.util.Objects;

public class SeeAll extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    MainAdapter adapter;
    public String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        this.idUser = extras.getString("id_user");

        tabLayout = findViewById(R.id.tab_seeall);
        viewPager = findViewById(R.id.view_pager_seeall);
        adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllFragment(),"All");
        adapter.addFragment(new Manual(),"Manual");
        adapter.addFragment(new Matic(),"Matic");
        adapter.addFragment(new Sport(),"Sport");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private static class MainAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        public void addFragment(Fragment fragment,String s){
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position){
            return stringArrayList.get(position);
        }
    }
}