package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public final class Podcast extends AudioCollection {
    private final List<Episode> episodes;

    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        super(name, owner);
        this.episodes = episodes;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return episodes.get(index);
    }

    /**
     * Check if podcast can be deleted
     * @return true -> can be; false -> can't be
     */
    public boolean checkIfDelete() {
        ArrayList<String> onlineUsers = new ArrayList<>(Admin.getOnlineUsers());
        for (String u : onlineUsers) {
            User user = (User) Admin.getUser(u);
            if (user != null && user.playerInteractionPodcast() == this) {
                return false;
            }
        }
        return true;
    }
}
