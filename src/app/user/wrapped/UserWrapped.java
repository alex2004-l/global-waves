package app.user.wrapped;

import java.util.*;

public class UserWrapped  extends WrappedInterface {
    private HashMap<String, Integer> topArtists;
    private HashMap<String, Integer> topGenre;
    private HashMap<String, Integer> topSongs;
    private HashMap<String, Integer> topAlbums;
    private HashMap<String, Integer> topEpisodes;

    public UserWrapped() {
        topArtists = new HashMap<>();
        topGenre = new HashMap<>();
        topSongs = new HashMap<>();
        topAlbums = new HashMap<>();
        topEpisodes = new HashMap<>();
    }

    /**
     * Increase the artist plays count
     * @param artist -> the name of the artist
     */
    public void incrementArtist(final String artist) {
        increment(artist, topArtists);
    }

    /**
     * Increase the genre plays count
     * @param genre -> the genre
     */
    public void incrementGenre(final String genre) {
        increment(genre, topGenre);
    }

    /**
     * Increase the song plays count
     * @param song -> the name of the song
     */
    public void incrementSong(final String song) {
        increment(song, topSongs);
    }

    /**
     * Increase the album plays count
     * @param album -> the name of the album
     */
    public void incrementAlbums(final String album) {
        increment(album, topAlbums);
    }

    /**
     * Increase the episode plays count
     * @param episode -> the name of the episode
     */
    public void incrementEpisodes(final String episode) {
        increment(episode, topEpisodes);
    }

    /**
     * Checks if wrapped is valid
     * @return true, if it is, false otherwise
     */
    @Override
    public boolean validWrapped() {
        return !(topArtists.isEmpty() && topAlbums.isEmpty()
                && topSongs.isEmpty() && topGenre.isEmpty()
                    && topEpisodes.isEmpty());
    }

    /**
     * Gets at most top 5 artists after the play count
     * @return -> top artists
     */
    public TreeMap<String, Integer> topArtists() {
        return top(topArtists);
    }

    /**
     * Gets at most top 5 genres after the play count
     * @return -> top genres
     */
    public TreeMap<String, Integer> topGenres() {
        return top(topGenre);
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
     * Gets at most top 5 episodes after the play count
     * @return -> top episodes
     */
    public TreeMap<String, Integer> topEpisodes() {
        return top(topEpisodes);
    }

}
