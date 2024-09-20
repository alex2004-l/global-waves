package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends AudioCollection {
    private ArrayList<Song> songs;
    private String description;
    private int timestamp;
    private Integer releaseYear;

    public Album(final String name, final String owner, final String description,
                                                            final Integer releaseYear) {
        this(name, owner, description, releaseYear, 0);
    }

    public Album(final String name, final String owner, final String description,
                 final Integer releaseYear, final int timestamp) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.description = description;
        this.releaseYear = releaseYear;
        this.timestamp = timestamp;
    }

    public Album(final String name, final String owner, final String description,
                 final Integer releaseYear, final ArrayList<Song> songs, final int timestamp) {
        this(name, owner, description, releaseYear, timestamp);
        this.songs = songs;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean matchesDescription(final String descript) {
        return this.getDescription().startsWith(descript);
    }

    /**
     * checks if album can be deleted
     * @return true -> can be; false -> can't be
     */
    public boolean checkIfDelete() {
        ArrayList<String> onlineUsers = new ArrayList<>(Admin.getOnlineUsers());
        for (String u : onlineUsers) {
            User user = (User) Admin.getUser(u);
            if (user != null && user.playerInteractionAlbum() == this) {
                return false;
            }
            if (user != null) {
                Playlist playlist = user.playerInteractionPlaylist();
                if (playlist != null) {
                    for (Song song : playlist.getSongs()) {
                        if (songs.stream()
                                .anyMatch(s -> s.getName().equals(song.getName())
                                        && s.getArtist().equals(song.getArtist()))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Gets an album's likes count
     * @return likes count
     */
    public int getLikesCount() {
        int result = 0;
        for (Song song : songs) {
            result += song.getLikes();
        }
        return result;
    }

}
