package com.example.project1442.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project1442.Activity.DetailActivity;
import com.example.project1442.Domain.PopularDomain;
import com.example.project1442.R;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private ArrayList<PopularDomain> itemsList;
    private Context context;

    public SearchResultAdapter(ArrayList<PopularDomain> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void updateData(ArrayList<PopularDomain> searchResults) {
        this.itemsList = searchResults; // Mise à jour de la liste avec les nouveaux résultats
        notifyDataSetChanged(); // Notifie l'adaptateur que les données ont changé pour rafraîchir la vue
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, feeTxt, scoreTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeTxt = itemView.findViewById(R.id.feeTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final PopularDomain item = itemsList.get(position); // Faites 'item' final
        holder.titleTxt.setText(item.getTitle());
        holder.feeTxt.setText(String.format("$%.2f", item.getPrice()));
        holder.scoreTxt.setText(String.valueOf(item.getScore()));

        int drawableResourceId = context.getResources().getIdentifier(item.getPicUrl(), "drawable", context.getPackageName());
        Glide.with(context).load(drawableResourceId).into(holder.pic);

        // Utilisez 'item' directement dans le gestionnaire de clics
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", item); // Assurez-vous que la clé correspond à ce que vous utilisez dans DetailActivity
                context.startActivity(intent);
            }
        });
    }
}