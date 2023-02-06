package com.example.moneyio.reviewfrags;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class RVViewPagerAdapter extends FragmentStateAdapter {

    public RVViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GFragment();
            case 1:
                return new BFragment();
            case 2:
                return new NSFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
