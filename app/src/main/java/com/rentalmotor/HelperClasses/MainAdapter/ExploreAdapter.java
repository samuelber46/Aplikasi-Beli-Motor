package com.rentalmotor.HelperClasses.MainAdapter;

import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rentalmotor.ChooseMotor;
import com.rentalmotor.R;

import java.util.ArrayList;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.FeaturedViewHolder> {
    ArrayList<DetailMotorHelperClass> exploreMotor;
    int design;
    private String idUser;

    public ExploreAdapter(String idUser,ArrayList<DetailMotorHelperClass> exploreMotor,int design) {
        this.exploreMotor = exploreMotor;
        this.idUser = idUser;
        this.design = design;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.design,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        DetailMotorHelperClass detailMotorHelperClass = exploreMotor.get(position);
        Glide.with(holder.itemView.getContext()).load(detailMotorHelperClass.getImage()).into(holder.image);
        holder.namaMotor.setText(detailMotorHelperClass.getNamaMotor());
        holder.volume.setText(detailMotorHelperClass.getVolume());
        holder.harga.setText(detailMotorHelperClass.getHarga());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChooseMotor.class);
                intent.putExtra("id_motor",detailMotorHelperClass.getId());
                intent.putExtra("id_user",idUser);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exploreMotor.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView namaMotor,volume,harga;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.featured_image);
            namaMotor = itemView.findViewById(R.id.featured_nama_motor);
            volume = itemView.findViewById(R.id.featured_volume);
            harga = itemView.findViewById(R.id.featured_harga);
        }
    }
}
