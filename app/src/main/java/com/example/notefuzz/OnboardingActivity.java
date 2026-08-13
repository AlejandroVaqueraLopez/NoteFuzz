package com.example.notefuzz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.notefuzz.adapter.OnboardingAdapter;
import com.example.notefuzz.model.OnboardingPage;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "app_prefs";
    public static final String KEY_FIRST_RUN = "isFirstRun";

    private ViewPager2 viewPager;
    private Button btnNext;
    private LinearLayout dotsIndicator;
    private List<OnboardingPage> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //en caso de no ser la primera vez, saltar directo a la vista principal
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (!prefs.getBoolean(KEY_FIRST_RUN, true)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        //captura de elementos de la vista
        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        dotsIndicator = findViewById(R.id.dotsIndicator);

        //listado de paginas del tutorial
        pages = new ArrayList<>();
        pages.add(new OnboardingPage(R.drawable.ic_onboarding_list, R.string.onboarding_title_1, R.string.onboarding_desc_1));
        pages.add(new OnboardingPage(R.drawable.ic_onboarding_add, R.string.onboarding_title_2, R.string.onboarding_desc_2));
        pages.add(new OnboardingPage(R.drawable.ic_onboarding_delete, R.string.onboarding_title_3, R.string.onboarding_desc_3));

        //instancia de OnboardingAdapter
        OnboardingAdapter adapter = new OnboardingAdapter(pages);
        viewPager.setAdapter(adapter);

        //dibuja los puntos indicadores
        setupDots(0);

        //boton para avanzar de pagina o finalizar el tutorial
        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < pages.size() - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        //cambia el texto del boton y el punto activo al cambiar de pagina
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                btnNext.setText(position == pages.size() - 1
                        ? getString(R.string.start)
                        : getString(R.string.next));
                setupDots(position);
            }
        });
    }

    //dibuja los puntos indicadores y marca cual esta seleccionado
    private void setupDots(int selectedPosition) {
        dotsIndicator.removeAllViews();
        for (int i = 0; i < pages.size(); i++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(i == selectedPosition ? R.drawable.dot_selected : R.drawable.dot_unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            dotsIndicator.addView(dot, params);
        }
    }

    //marca el tutorial como visto y navega a la vista principal
    private void finishOnboarding() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
