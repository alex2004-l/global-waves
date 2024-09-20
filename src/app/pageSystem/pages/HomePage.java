package app.pageSystem.pages;

import app.user.User;

import java.util.ArrayList;

public class HomePage implements Page {
    private final User user;
    private ArrayList<String> songs;
    private ArrayList<String> playlists;
    private ArrayList<String> songRecommendations;
    private ArrayList<String> playlistRecommendations;

    public HomePage(final User user) {
        this.user = user;
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        songRecommendations = new ArrayList<>();
        playlistRecommendations = new ArrayList<>();
    }

    /**
     * refresh page
     */
    public void refresh() {
        this.songs = user.getTop5Songs();
        this.playlists = user.getTop5Playlists();
    }

    /**
     * Adds a song recommendation on the home page
     * @param string -> the name of the song
     */
    public void addSongRec(final String string) {
        if (!songRecommendations.stream().anyMatch(s -> s.equals(string))) {
            songRecommendations.add(string);
        }
    }

    /**
     * Adds a playlist recommendation on the home page
     * @param string -> the name of the playlist
     */
    public void addPlaylistRec(final String string) {
        if (!playlistRecommendations.stream().anyMatch(s -> s.equals(string))) {
            playlistRecommendations.add(string);
        }
    }

    /**
     * display page
     * @return String
     */
    public String display() {
        this.refresh();
        StringBuilder message = new StringBuilder("Liked songs:\n\t[");
        for (int i = 0; i < songs.size(); ++i) {
            message.append(songs.get(i));
            if (i == songs.size() - 1) {
                break;
            }
            message.append(", ");
        }
        message.append("]\n");
        message.append("\nFollowed playlists:\n\t[");
        for (int i = 0; i < playlists.size(); ++i) {
            message.append(playlists.get(i));
            if (i == playlists.size() - 1) {
                break;
            }
            message.append(", ");
        }

        message.append("]\n");
        message.append("\nSong recommendations:\n\t[");
        for (int i = 0; i < songRecommendations.size(); ++i) {
            message.append(songRecommendations.get(i));
            if (i == songRecommendations.size() - 1) {
                break;
            }
            message.append(", ");
        }

        message.append("]\n");
        message.append("\nPlaylists recommendations:\n\t[");
        for (int i = 0; i < playlistRecommendations.size(); ++i) {
            message.append(playlistRecommendations.get(i));
            if (i == playlistRecommendations.size() - 1) {
                break;
            }
            message.append(", ");
        }

        message.append("]");

        return message.toString();
    }

}
