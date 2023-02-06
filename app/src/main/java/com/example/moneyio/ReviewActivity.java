package com.example.moneyio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.moneyio.reviewfrags.RVViewPagerAdapter;
import com.example.moneyio.databinding.ActivityReviewBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReviewActivity extends AppCompatActivity {

    private ActivityReviewBinding binding;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RVViewPagerAdapter rvViewPagerAdapter = new RVViewPagerAdapter(this);
        binding.viewpager.setAdapter(rvViewPagerAdapter);
        viewPager = binding.viewpager;
        new TabLayoutMediator(binding.tabLayout, viewPager, (TabLayoutMediator.TabConfigurationStrategy) (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Good");
                    break;
                case 1:
                    tab.setText("Bad");
                    break;
                case 2:
                    tab.setText("Not sure");
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}