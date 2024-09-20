package app.pageSystem.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;

public class LikedContentPage implements Page {
    private final User user;
    private ArrayList<Song> songs;
    private ArrayList<Playlist> playlists;
    public LikedContentPage(final User user) {
        this.user = user;
        this.songs = user.getLikedSongs();
        this.playlists = user.getFollowedPlaylists();
    }

    /**
     * refresh page
     */
    public void refresh() {
        this.songs = user.getLikedSongs();
        this.playlists = user.getFollowedPlaylists();
    }

    /**
     * display page
     * @return page display as String
     */
    public String display() {
        this.refresh();
        StringBuilder message = new StringBuilder("Liked songs:\n\t[");
        for (int i = 0; i < songs.size(); ++i) {
            message.append(songs.get(i).getName()).append(" - ")
                    .append(songs.get(i).getArtist());
            if (i == songs.size() - 1) {
                break;
            }
            message.append(", ");
        }

        message.append("]\n");
        message.append("\nFollowed playlists:\n\t[");
        for (int i = 0; i < playlists.size(); ++i) {
            message.append(playlists.get(i).getName()).append(" - ")
                    .append(playlists.get(i).getOwner());
            if (i == playlists.size() - 1) {
                break;
            }
            message.append(", ");
        }

        message.append("]");
        return message.toString();
    }

}
