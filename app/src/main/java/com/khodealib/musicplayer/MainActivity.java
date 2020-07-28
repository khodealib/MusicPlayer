package com.khodealib.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.slider.Slider;
import com.khodealib.musicplayer.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MusicAdapter.OnMusicClickListener {
    private ActivityMainBinding binding;
    private List<Music> musics = Music.getList();
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private MusicState musicState = MusicState.STOPPED;
    private boolean isDragging;
    private int cursor;
    private MusicAdapter adapter;


    enum MusicState {
        PLAYING, PAUSED, STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        RecyclerView recyclerView = binding.rvMain;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new MusicAdapter(musics, this);
        recyclerView.setAdapter(adapter);

        onMusicChange(musics.get(cursor));
        binding.ivMainPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (musicState) {
                    case PLAYING:
                        mediaPlayer.pause();
                        musicState = MusicState.PAUSED;
                        binding.ivMainPlay.setImageResource(R.drawable.ic_play_32dp);
                        break;
                    case PAUSED:
                    case STOPPED:
                        mediaPlayer.start();
                        musicState = MusicState.PLAYING;
                        binding.ivMainPlay.setImageResource(R.drawable.ic_pause_24dp);
                }
            }
        });

        binding.sliderMain.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                binding.tvMainPosition.setText(Music.convertMillisToString((long) value));
            }
        });

        binding.sliderMain.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                isDragging = true;
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                isDragging = false;
                mediaPlayer.seekTo((int) slider.getValue());
            }
        });
    }

    public void onMusicChange(Music music) {
        adapter.notifyMusicOnChange(music);
        binding.sliderMain.setValue(0);
        mediaPlayer = MediaPlayer.create(this, music.getMusicResId());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                mediaPlayer.start();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isDragging) {
                                    binding.sliderMain.setValue(mediaPlayer.getCurrentPosition());
                                }
                            }
                        });
                    }
                }, 1000, 1000);
                binding.tvMainPosition.setText(Music.convertMillisToString(mediaPlayer.getCurrentPosition()));
                binding.tvMainDuration.setText(Music.convertMillisToString(mediaPlayer.getDuration()));
                binding.sliderMain.setValueTo(mediaPlayer.getDuration());
                musicState = MusicState.PLAYING;
                binding.ivMainPlay.setImageResource(R.drawable.ic_pause_24dp);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        goNext();
                    }
                });
            }
        });

        binding.ivMainArtist.setActualImageResource(music.getArtistResId());
        binding.ivMainCover.setActualImageResource(music.getCoverResId());
        binding.tvMainArtistName.setText(music.getArtist());
        binding.tvMainSongName.setText(music.getName());

        binding.ivMainSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrev();
            }
        });

        binding.ivMainForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    private void goPrev() {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        if (cursor == 0) {
            cursor = musics.size() - 1;
        } else
            cursor--;

        onMusicChange(musics.get(cursor));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void goNext() {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        if (cursor < musics.size() - 1) {
            cursor++;
        } else
            cursor = 0;
        onMusicChange(musics.get(cursor));
    }

    @Override
    public void onClick(Music music, int position) {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        cursor = position;
        onMusicChange(musics.get(cursor));
    }

}