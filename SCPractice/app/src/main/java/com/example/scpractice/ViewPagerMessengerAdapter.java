package com.example.scpractice;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.scpractice.tabs.KnowMoreFragment;
import com.example.scpractice.tabs.QuizFragment;
import com.example.scpractice.tabs.SquareCube;
import com.example.scpractice.tabs.Tables;

public class ViewPagerMessengerAdapter extends FragmentStateAdapter {

    private final int NUMBER_OF_TABS = 3;

    public ViewPagerMessengerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SquareCube();
            case 1:
                return new Tables();
            case 2:
                return new QuizFragment();

            default:
                return new KnowMoreFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_TABS;
    }
}
