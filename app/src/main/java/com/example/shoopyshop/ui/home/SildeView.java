package com.example.shoopyshop.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.shoopyshop.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

class SlideView extends PagerAdapter {

    private Context context;
    LayoutInflater inflater;



    //getContext.setS(toolbar);
    int images[]={R.drawable.banner4,R.drawable.banner2,R.drawable.banner3,R.drawable.banner1,R.drawable.banner};

    public SlideView(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    public SlideView(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.slideview,container,false);
        ImageView img=(ImageView)view.findViewById(R.id.imageviewtxt);
        img.setImageResource(images[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }
}
