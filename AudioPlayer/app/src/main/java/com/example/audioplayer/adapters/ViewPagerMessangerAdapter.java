package com.example.audioplayer.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.audioplayer.Player;
import com.example.audioplayer.SongList;

public class ViewPagerMessangerAdapter extends FragmentPagerAdapter {


    public ViewPagerMessangerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                return  new Player();
            }
            case 1:{
                return  new SongList();
            }

            default:{
                return null;
            }
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "player";

            case 1:return "All Songs";

            default: return "Empty";
        }

    }
}
