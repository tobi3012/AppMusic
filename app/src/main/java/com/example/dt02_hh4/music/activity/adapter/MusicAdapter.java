package com.example.dt02_hh4.music.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dt02_hh4.music.R;
import com.example.dt02_hh4.music.model.Music;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private List<Music> musicList;
    private OnItemClickListener itemClickListener;
    private Context context;

    public MusicAdapter(List<Music> musicList, OnItemClickListener itemClickListener, Context context) {
        this.musicList = musicList;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        view = inflater.inflate(R.layout.item_list_music, viewGroup, false);
        final MusicViewHolder viewHolder = new MusicViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder musicViewHolder, int i) {
        musicViewHolder.tvNameMusic.setText(musicList.get(i).getName());
        musicViewHolder.tvNameSinger.setText(musicList.get(i).getSinger());
        Glide.with(context)
                .load(musicList.get(i).getImage())
                .into(musicViewHolder.ivImageAlbum);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameMusic;
        TextView tvNameSinger;
        ImageView ivImageAlbum;
        MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameMusic = itemView.findViewById(R.id.tv_name_music);
            tvNameSinger = itemView.findViewById(R.id.tv_name_singer);
            ivImageAlbum = itemView.findViewById(R.id.iv_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
