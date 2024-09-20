package fileio.output;

import app.audio.Collections.outputFiles.AlbumOutput;
import app.audio.Collections.outputFiles.PlaylistOutput;
import app.audio.Collections.outputFiles.PodcastOutput;
import app.player.PlayerStats;
import app.user.notification.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public final class CommandOutput {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private String command;
    private Integer timestamp;
    private String user;
    private String pageUser;
    private String message;
    private ArrayList<String> results;
    private ArrayList<String> result;
    private ArrayList<PlaylistOutput> playlists;
    private ArrayList<AlbumOutput> albums;
    private ArrayList<PodcastOutput> podcasts;
    private PlayerStats stats;
    private String preferredGenre;
    private List<String> top5;

    private TreeMap<String, Integer> topArtists;
    private TreeMap<String, Integer> topGenres;
    private TreeMap<String, Integer> topSongs;
    private TreeMap<String, Integer> topAlbums;
    private TreeMap<String, Integer> topEpisodes;
    private ArrayList<String> topFans;
    private Integer listeners;
    private ArrayList<Notification> notifications;

    private CommandOutput(final CommandOutputBuilder builder) {
        this.command = builder.command;
        this.timestamp = builder.timestamp;
        this.user = builder.user;
        this.pageUser = builder.pageUser;
        this.message = builder.message;
        this.results = builder.results;
        this.result = builder.result;
        this.playlists = builder.playlists;
        this.albums = builder.albums;
        this.podcasts = builder.podcasts;
        this.stats = builder.stats;
        this.preferredGenre = builder.preferredGenre;
        this.top5 = builder.top5;
        this.topArtists = builder.topArtists;
        this.topGenres = builder.topGenres;
        this.topSongs = builder.topSongs;
        this.topAlbums = builder.topAlbums;
        this.topEpisodes = builder.topEpisodes;
        this.topFans = builder.topFans;
        this.listeners = builder.listeners;
        this.notifications = builder.notifications;
    }

    /**
     * Creates an object node depending on the initialized fields
     * @return -> the object node
     */
    public ObjectNode asObjectNode() {
        ObjectNode objectNode = objectMapper.createObjectNode();

        if (pageUser != null) {
            objectNode.put("user", pageUser);
        }
        objectNode.put("command", command);
        if (user != null) {
            objectNode.put("user", user);
        }
        objectNode.put("timestamp", timestamp);
        if (message != null) {
            objectNode.put("message", message);
        }
        if (results != null) {
            objectNode.put("results", objectMapper.valueToTree(results));
        }
        if (result != null) {
            objectNode.put("result", objectMapper.valueToTree(result));
        }
        if (playlists != null) {
            objectNode.put("result", objectMapper.valueToTree(playlists));
        }
        if (albums != null) {
            objectNode.put("result", objectMapper.valueToTree(albums));
        }
        if (podcasts != null) {
            objectNode.put("result", objectMapper.valueToTree(podcasts));
        }
        if (stats != null) {
            objectNode.put("stats", objectMapper.valueToTree(stats));
        }
        if (preferredGenre != null) {
            objectNode.put("result", objectMapper.valueToTree(preferredGenre));
        }
        if (top5 != null) {
            objectNode.put("result", objectMapper.valueToTree(top5));
        }
        if (topArtists != null && topSongs != null && topAlbums != null
                && topGenres != null && topEpisodes != null) {
            ObjectNode objectNode2 = objectMapper.createObjectNode();

            objectNode2.put("topArtists", objectMapper.valueToTree(topArtists));
            objectNode2.put("topGenres", objectMapper.valueToTree(topGenres));
            objectNode2.put("topSongs", objectMapper.valueToTree(topSongs));
            objectNode2.put("topAlbums", objectMapper.valueToTree(topAlbums));
            objectNode2.put("topEpisodes", objectMapper.valueToTree(topEpisodes));

            objectNode.put("result", objectMapper.valueToTree(objectNode2));
        }
        if (topAlbums != null && topSongs != null && topFans != null && listeners != null) {
            ObjectNode objectNode2 = objectMapper.createObjectNode();

            objectNode2.put("topAlbums", objectMapper.valueToTree(topAlbums));
            objectNode2.put("topSongs", objectMapper.valueToTree(topSongs));
            objectNode2.put("topFans", objectMapper.valueToTree(topFans));
            objectNode2.put("listeners", objectMapper.valueToTree(listeners));

            objectNode.put("result", objectMapper.valueToTree(objectNode2));
        }
        if (topEpisodes != null && listeners != null) {
            ObjectNode objectNode2 = objectMapper.createObjectNode();

            objectNode2.put("topEpisodes", objectMapper.valueToTree(topEpisodes));
            objectNode2.put("listeners", objectMapper.valueToTree(listeners));

            objectNode.put("result", objectMapper.valueToTree(objectNode2));
        }
        if (notifications != null) {
            objectNode.put("notifications", objectMapper.valueToTree(notifications));
        }
        return objectNode;
    }


    public static class CommandOutputBuilder {
        // mandatory
        private String command;
        private Integer timestamp;
        // optional
        private String user = null;
        private String pageUser = null;
        private String message = null;
        private ArrayList<String> results = null;
        private ArrayList<String> result = null;
        private ArrayList<PlaylistOutput> playlists = null;
        private ArrayList<AlbumOutput> albums = null;
        private ArrayList<PodcastOutput> podcasts = null;
        private PlayerStats stats = null;
        private String preferredGenre = null;
        private List<String> top5 = null;
        private TreeMap<String, Integer> topArtists = null;
        private TreeMap<String, Integer> topGenres = null;
        private TreeMap<String, Integer> topSongs = null;
        private TreeMap<String, Integer> topAlbums = null;
        private TreeMap<String, Integer> topEpisodes = null;
        private ArrayList<String> topFans = null;
        private Integer listeners = null;
        private ArrayList<Notification> notifications;
        public CommandOutputBuilder(final String command) {
            this.command = command;
        }

        /**
         * Adds a value for the timestamp field
         * @param newTimestamp -> the timestamp
         * @return builder
         */
        public CommandOutputBuilder timestamp(final Integer newTimestamp) {
            this.timestamp = newTimestamp;
            return this;
        }

        /**
         * Adds a value for the user field
         * @param newUser -> the user
         * @return builder
         */
        public CommandOutputBuilder user(final String newUser) {
            this.user = newUser;
            return this;
        }

        /**
         * Adds a value for the pageUser field
         * @param newPageUser -> the pageUser
         * @return builder
         */
        public CommandOutputBuilder pageUser(final String newPageUser) {
            this.pageUser = newPageUser;
            return this;
        }

        /**
         * Adds a value for the message field
         * @param newMessage -> the message
         * @return builder
         */
        public CommandOutputBuilder message(final String newMessage) {
            this.message = newMessage;
            return this;
        }

        /**
         * Adds a value for the results field
         * @param newResults -> the results
         * @return builder
         */
        public CommandOutputBuilder results(final ArrayList<String> newResults) {
            this.results = newResults;
            return this;
        }

        /**
         * Adds a value for the result field
         * @param newResult -> the result
         * @return builder
         */
        public CommandOutputBuilder result(final ArrayList<String> newResult) {
            this.result = newResult;
            return this;
        }

        /**
         * Adds a value for the playlists field
         * @param newPlaylists -> the playlists
         * @return builder
         */
        public CommandOutputBuilder playlists(final ArrayList<PlaylistOutput> newPlaylists) {
            this.playlists = newPlaylists;
            return this;
        }

        /**
         * Adds a value for the albums field
         * @param newAlbums -> the albums
         * @return builder
         */
        public CommandOutputBuilder albums(final ArrayList<AlbumOutput> newAlbums) {
            this.albums = newAlbums;
            return this;
        }

        /**
         * Adds a value for the podcasts field
         * @param newPodcasts -> the podcasts
         * @return builder
         */
        public CommandOutputBuilder podcasts(final ArrayList<PodcastOutput> newPodcasts) {
            this.podcasts = newPodcasts;
            return this;
        }

        /**
         * Adds a value for the stats field
         * @param newStats -> the stats
         * @return builder
         */
        public CommandOutputBuilder stats(final PlayerStats newStats) {
            this.stats = newStats;
            return this;
        }

        /**
         * Adds a value for the preferred genre field
         * @param newPreferredGenre -> the preferred genre
         * @return builder
         */
        public CommandOutputBuilder preferredGenre(final String newPreferredGenre) {
            this.preferredGenre = newPreferredGenre;
            return this;
        }

        /**
         * Adds a value for the top5 field
         * @param newTop5 -> top 5 list
         * @return builder
         */
        public CommandOutputBuilder top5(final List<String> newTop5) {
            this.top5 = newTop5;
            return this;
        }

        /**
         * Adds a value for the top artists field
         * @param newTopArtists -> the top artists treemap
         * @return builder
         */
        public CommandOutputBuilder topArtists(final TreeMap<String, Integer> newTopArtists) {
            this.topArtists = newTopArtists;
            return this;
        }

        /**
         * Adds a value for the top genres field
         * @param newTopGenres -> the top genres treemap
         * @return builder
         */
        public CommandOutputBuilder topGenres(final TreeMap<String, Integer> newTopGenres) {
            this.topGenres = newTopGenres;
            return this;
        }

        /**
         * Adds a value for the top songs field
         * @param newTopSongs -> the top songs treemap
         * @return builder
         */
        public CommandOutputBuilder topSongs(final TreeMap<String, Integer> newTopSongs) {
            this.topSongs = newTopSongs;
            return this;
        }

        /**
         * Adds a value for the top albums field
         * @param newTopAlbums -> the top albums treemap
         * @return builder
         */
        public CommandOutputBuilder topAlbums(final TreeMap<String, Integer> newTopAlbums) {
            this.topAlbums = newTopAlbums;
            return this;
        }

        /**
         * Adds a value for the top episodes field
         * @param newTopEpisodes -> the top episodes treemap
         * @return builder
         */
        public CommandOutputBuilder topEpisodes(final TreeMap<String, Integer> newTopEpisodes) {
            this.topEpisodes = newTopEpisodes;
            return this;
        }

        /**
         * Adds a value for the top fans field
         * @param newTopFans -> the top fans treemap
         * @return builder
         */
        public CommandOutputBuilder topFans(final ArrayList<String> newTopFans) {
            this.topFans = newTopFans;
            return this;
        }

        /**
         * Adds a value for the listeners field
         * @param newListeners -> listeners
         * @return builder
         */
        public CommandOutputBuilder listeners(final int newListeners) {
            this.listeners = newListeners;
            return this;
        }

        /**
         * Adds a value for the notifications field
         * @param newNotifications _-> notifications
         * @return builder
         */
        public CommandOutputBuilder notifications(final ArrayList<Notification> newNotifications) {
            this.notifications = newNotifications;
            return this;
        }

        /**
         * Build
         * @return current builder instance
         */
        public CommandOutput build() {
            return new CommandOutput(this);
        }
    }
}
