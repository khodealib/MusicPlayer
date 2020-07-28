package com.khodealib.musicplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Music {
    private int id;
    private String name;
    private String artist;
    private int coverResId;
    private int artistResId;
    private int musicResId;

    public int getMusicResId() {
        return musicResId;
    }

    public void setMusicResId(int musicResId) {
        this.musicResId = musicResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCoverResId() {
        return coverResId;
    }

    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }

    public int getArtistResId() {
        return artistResId;
    }

    public void setArtistResId(int artistResId) {
        this.artistResId = artistResId;
    }

    public static String convertMillisToString(long durationInMillis) {
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;

        return String.format(Locale.US, "%02d:%02d", minute, second);
    }

    public static List<Music> getList() {
        List<Music> musicList = new ArrayList<>();
        Music music1 = new Music();
        music1.setArtist("Evan Band");
        music1.setName("Chehel Gis");
        music1.setCoverResId(R.drawable.music_1_cover);
        music1.setArtistResId(R.drawable.music_1_artist);
        music1.setMusicResId(R.raw.music_1);

        Music music2 = new Music();
        music2.setArtist("Reza Sadeghi");
        music2.setName("Tanha tarin");
        music2.setCoverResId(R.drawable.music_2_cover);
        music2.setArtistResId(R.drawable.music_2_artist);
        music2.setMusicResId(R.raw.music_2);

        Music music3 = new Music();
        music3.setArtist("Reza Bahram");
        music3.setName("Hich");
        music3.setCoverResId(R.drawable.music_3_cover);
        music3.setArtistResId(R.drawable.music_3_artist);
        music3.setMusicResId(R.raw.music_3);

        musicList.add(music2);
        musicList.add(music3);
        musicList.add(music1);
        return musicList;
    }
}
