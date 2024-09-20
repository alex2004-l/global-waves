package app.user.wrapped;

import lombok.Getter;

import java.util.HashMap;
import java.util.TreeMap;

public class HostWrapped extends WrappedInterface {
    private HashMap<String, Integer> topEpisodes;
    private HashMap<String, Integer> fans;
    @Getter
    private Integer listeners;

    public HostWrapped() {
        this.topEpisodes = new HashMap<>();
        this.fans = new HashMap<>();
        this.listeners = 0;
    }

    /**
     * Increase the episode plays count
     * @param episode -> the name of the episode
     */
    public void incrementEpisode(final String episode) {
        increment(episode, topEpisodes);
    }

    /**
     * Increase the listener plays count
     * @param listener -> the username of the listener
     */
    public void incrementListener(final String listener) {
        increment(listener, fans);
    }

    /**
     * Checks if wrapped is valid
     * @return true, if it is, false otherwise
     */
    public boolean validWrapped() {
        return !(topEpisodes.isEmpty() && fans.isEmpty());
    }

    /**
     * Gets at most top 5 episodes after the play count
     * @return -> top episodes
     */
    public TreeMap<String, Integer> topEpisodes() {
        return top(topEpisodes);
    }

    /**
     * Update the total number of listeners
     */
    public void updateListeners() {
        this.listeners = fans.size();
    }
}
