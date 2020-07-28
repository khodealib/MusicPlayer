package com.khodealib.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<Music> musics;
    private int playingMusicPos = -1;
    private OnMusicClickListener listener;

    public MusicAdapter(List<Music> musics, OnMusicClickListener listener) {
        this.musics = musics;
        this.listener = (OnMusicClickListener) listener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);

        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bindMusic(musics.get(position));
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView simpleDraweeView;
        private TextView artistTv;
        private TextView musicTv;
        private LottieAnimationView animationView;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.iv_musicItem_cover);
            artistTv = itemView.findViewById(R.id.tv_artist_name);
            musicTv = itemView.findViewById(R.id.tv_music_name);
            animationView = itemView.findViewById(R.id.animation_item);
        }

        public void bindMusic(final Music music) {
            simpleDraweeView.setActualImageResource(music.getCoverResId());
            artistTv.setText(music.getArtist());
            musicTv.setText(music.getName());

            if (getAdapterPosition() == playingMusicPos) {
                animationView.setVisibility(View.VISIBLE);
            } else
                animationView.setVisibility(View.GONE);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(music, getAdapterPosition());
                }
            });
        }

    }

    public void notifyMusicOnChange(Music music) {
        int index = musics.indexOf(music);

        if (index != -1) {
            if (index != playingMusicPos) {
                notifyItemChanged(playingMusicPos);
                playingMusicPos = index;
                notifyItemChanged(playingMusicPos);
            }
        }
    }

    public interface OnMusicClickListener {
        void onClick(Music music, int position);
    }
}
