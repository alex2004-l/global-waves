package app.user.wrapped;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class ArtistWrapped extends WrappedInterface {
    private HashMap<String, Integer> topSongs;
    private HashMap<String, Integer> topAlbums;
    private HashMap<String, Integer> topFans;
    @Getter
    private Integer listeners;

    public ArtistWrapped() {
        this.topAlbums = new HashMap<>();
        this.topSongs = new HashMap<>();
        this.topFans = new HashMap<>();
        this.listeners = 0;
    }

    /**
     * Increase the album plays count
     * @param album -> the name of the album
     */
    public void incrementAlbum(final String album) {
       increment(album, topAlbums);
    }

    /**
     * Increase the song plays count
     * @param song -> the name of the song
     */
    public void incrementSong(final String song) {
        increment(song, topSongs);
    }

    /**
     * Increase user plays count
     * @param user -> the name of the user
     */
    public void incrementFans(final String user) {
        increment(user, topFans);
    }

    /**
     * Checks if wrapped is valid
     * @return true, if it is, false otherwise
     */
    public boolean validWrapped() {
        return !(topAlbums.isEmpty() && topSongs.isEmpty() && topFans.isEmpty());
    }

    /**
     * Gets at most top 5 songs after the play count
     * @return -> top songs
     */
    public TreeMap<String, Integer> topSongs() {
       return top(topSongs);
    }

    /**
     * Gets at most top 5 albums after the play count
     * @return -> top albums
     */
    public TreeMap<String, Integer> topAlbums() {
        return top(topAlbums);
    }

    /**
     * Gets at most top 5 fans after the play count
     * @return -> top fans
     */
    public ArrayList<String> topFans() {
        TreeMap<String, Integer> sortedMap = top(topFans);
        return new ArrayList<>(new ArrayList<>(sortedMap.keySet()));
    }

    /**
     * Update the total number of listeners
     */
    public void updateListeners() {
        listeners = topFans.size();
    }
}
