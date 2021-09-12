package com.romelidme.androidbigbox.vistas.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.adaptadores.SliderAdapter;

public class Slider extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private SliderAdapter sliderAdapter;
    private Button mNextBtn;
    private Button mBackBtn;

    private int mCurrentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        castView();
        mNextBtn.setOnClickListener(v -> {
            if(mNextBtn.getText()==getString(R.string.finalizar)){
                    Intent intent = new Intent(this,Principal.class);
                    startActivity(intent);
                    this.finish();
            }
            else{
                mSlideViewPager.setCurrentItem(mCurrentPage +1);
            }
        });
        mBackBtn.setOnClickListener(view -> mSlideViewPager.setCurrentItem(mCurrentPage -1));
    }

    private void castView() {
        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout =findViewById(R.id.dotsLayout);

        mNextBtn =findViewById(R.id.nextBtn);
        mBackBtn =findViewById(R.id.prevBtn);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for(int i =0; i< mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.blancotransparente));

            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.blanco));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            mCurrentPage= i;

            if(i == 0){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText(getString(R.string.siguiente));
                mBackBtn.setText("");
            }else if(i ==mDots.length - 1){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText(getString(R.string.finalizar));
                mBackBtn.setText(getString(R.string.atras));
            }else{
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText(getString(R.string.siguiente));
                mBackBtn.setText(getString(R.string.atras));
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


}