package com.example.shoopyshop.Adapter;

import android.widget.Switch;

import com.example.shoopyshop.ui.Cart.CartFragment;
import com.example.shoopyshop.ui.Profile.EditProfileFragment;
import com.example.shoopyshop.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TableAcessAdapter extends FragmentPagerAdapter {
    public TableAcessAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                HomeFragment homeFragment=new HomeFragment();
                return homeFragment;
            case 1:
                CartFragment cartFragment=new CartFragment();
                return cartFragment;
            case 2:
                EditProfileFragment profileFragment=new EditProfileFragment();
                return profileFragment;
            default:
                HomeFragment homeFragment1=new HomeFragment();
                return homeFragment1;

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i) {
        switch(i){
            case 0:
                return "Home";
            case 1:
                return "Cart";
            case 2:
                return "Profile";
            default:
                return "Home";

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
