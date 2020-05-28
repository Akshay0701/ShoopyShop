package com.example.shoopyshop.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoopyshop.Adapter.TableAcessAdapter;
import com.example.shoopyshop.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tableLayout;
    TableAcessAdapter tableAcessAdapter;
    private int[] tabIcons = {
            R.drawable.add_cart_icon,
            R.drawable.cerditicon,
            R.drawable.profileicon2,
    };
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        viewPager=view.findViewById(R.id.viewpager);
        tableLayout=view.findViewById(R.id.main_tabs);
        tableAcessAdapter=new TableAcessAdapter(getChildFragmentManager());
        viewPager.setAdapter(tableAcessAdapter);
        tableLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return view;
    }
    private void setupTabIcons() {
        tableLayout.getTabAt(0).setIcon(tabIcons[0]);
        tableLayout.getTabAt(1).setIcon(tabIcons[1]);
        tableLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
}
