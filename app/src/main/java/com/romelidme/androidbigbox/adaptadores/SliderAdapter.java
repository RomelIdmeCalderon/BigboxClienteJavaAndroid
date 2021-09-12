package com.romelidme.androidbigbox.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import wholetec.cliente.bigbox.R;

public class SliderAdapter extends PagerAdapter  {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context= context;
    }
    //Arrays
    public String[] slide_descs={
            "LISTA DE COMPRAS, LISTA DE DESPENSA Y LISTA DE TAREAS PENDIENTES",
            "ORGANIZA TUS TAREAS PENDIENTES Y MANTENTE ORGANIZADO",
            "PERSONALIZA TODAS TUS LISTAS DE ACUERDO A TUS NECESIDADES"

    };
    public String[] slide_desc2={
            "TODO EN UNA APP FACIL DE USAR",
            "",
            ""
    };
    public int[] slide_images ={
            R.drawable.grafica_1_icon,
            R.drawable.grafica_2_icon,
            R.drawable.grafica_3_icon
    };
    public int[] slide_images2 ={
            R.drawable.logo_icon,
            R.drawable.logo_icon,
            R.drawable.logo_icon
    };


    @Override
    public int getCount() {
        return slide_descs.length;
    }

    @Override
    public boolean isViewFromObject( @NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    public Object instantiateItem( ViewGroup container, int position){
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        TextView slideDescription = view.findViewById(R.id.slide_desc);
        TextView slideDescription2 = view.findViewById(R.id.slide_desc2);
        ImageView slideImageView = view.findViewById(R.id.slide_image);
        ImageView slideImageView2 = view.findViewById(R.id.slide_icon);

        slideDescription.setText(slide_descs[position]);
        slideDescription2.setText(slide_desc2[position]);
        slideImageView.setImageResource(slide_images[position]);
        slideImageView2.setImageResource(slide_images2[position]);


        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }
}
