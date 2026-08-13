package com.example.notefuzz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notefuzz.R;
import com.example.notefuzz.model.OnboardingPage;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.PageViewHolder> {

    private final List<OnboardingPage> pages;

    //constructor
    public OnboardingAdapter(List<OnboardingPage> pages) {
        this.pages = pages;
    }

    //sobreescritura para inflar el viewpager
    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding_page, parent, false);
        return new PageViewHolder(view);
    }

    //llenado de cada pagina del tutorial
    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        OnboardingPage page = pages.get(position);
        holder.image.setImageResource(page.getImageRes());
        holder.title.setText(page.getTitleRes());
        holder.description.setText(page.getDescriptionRes());
    }

    //obtencion de cantidad de paginas
    @Override
    public int getItemCount() {
        return pages.size();
    }

    //guarda referencias de los items de cada pagina
    static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        PageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgOnboarding);
            title = itemView.findViewById(R.id.tvOnboardingTitle);
            description = itemView.findViewById(R.id.tvOnboardingDesc);
        }
    }
}
