package app.user;

import app.Admin;
import app.DatabaseEntry;
import app.audio.Collections.*;
import app.audio.Collections.outputFiles.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pageSystem.ChangePage;
import app.pageSystem.PageSystem;
import app.pageSystem.PageSystemInvoker;
import app.pageSystem.pages.HomePage;
import app.pageSystem.pages.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.user.notification.Observer;
import app.user.notification.Notification;
import app.user.wrapped.UserWrapped;
import app.utils.Enums;
import lombok.Getter;
import main.Main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * The type User.
 */
public class User extends UserEntry implements Observer {
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    private boolean onlineStatus;
    private static final Integer MAX_RECS = 5;
    private static final Double PREMIUM_VALUE = 1000000.0;
    private PageSystem pageSystem;
    private PageSystemInvoker pageSystemInvoker;
    @Getter
    private UserWrapped userWrapped;
    @Getter
    private boolean premium;
    private ArrayList<Song> monetizationPremium;
    private ArrayList<Song> monetizationFree;
    @Getter
    private ArrayList<String> merch;
    private Double adRevenue;
    private ArrayList<Notification> notifications;

    private LibraryEntry lastRecommendation = null;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        super(username, age, city, Enums.UserEntryType.USER);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player(this);
        searchBar = new SearchBar(username);
        lastSearched = false;
        onlineStatus = true;
        pageSystem = new PageSystem(this);
        pageSystemInvoker = new PageSystemInvoker();
        userWrapped = new UserWrapped();
        premium = false;
        monetizationPremium = new ArrayList<>();
        monetizationFree = new ArrayList<>();
        merch = new ArrayList<>();
        adRevenue = 0.0;
        notifications = new ArrayList<>();
    }

    /**
     * Gets special user with whom actual user interactionate throughout player
     * @return special user
     */
    public UserEntry playerInteraction() {
        return player.getUserInteraction();
    }

    /**
     * Gets album with whom actual user interactionate throughout player
     * @return album
     */
    public Album playerInteractionAlbum() {
        return player.getAlbumInteraction();
    }

    /**
     * Gets podcast with whom actual user interactionate throughout player
     * @return podcast
     */
    public Podcast playerInteractionPodcast() {
        return player.getPodcastInteraction();
    }

    /**
     * Gets player with whom actual user interactionate throughout player
     * @return playlist
     */
    public Playlist playerInteractionPlaylist() {
        return player.getPlaylistsInteraction();
    }
    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        if (!onlineStatus) {
            return null;
        }

        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<DatabaseEntry> databaseEntries = searchBar.search(filters, type);
        for (DatabaseEntry databaseEntry : databaseEntries) {
            results.add(databaseEntry.getName());
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!onlineStatus) {
            return null;
        }
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        DatabaseEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }
        if (selected.isUserEntry()) {
            if (((UserEntry) selected).isArtist()) {
                //currentPage = ((Artist) selected).getArtistPage();
                Artist artist = (Artist) selected;
                pageSystemInvoker.execute(new ChangePage(pageSystem, artist.getArtistPage()));
            } else {
                //currentPage = ((Host) selected).getHostPage();
                Host host = (Host) selected;
                pageSystemInvoker.execute(new ChangePage(pageSystem, host.getHostPage()));
            }
            return "Successfully selected " + selected.getName() + "'s page.";
        }
        return "Successfully selected %s.".formatted(selected.getName());
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (!onlineStatus) {
            return null;
        }
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }
        player.setSource((LibraryEntry) searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();
        player.resetAdBreak();

        player.pause();

        return "Playback loaded successfully.";
    }

    public String loadRecommendations() {
        if (lastRecommendation == null) {
            return "No recommendations available.";
        }

        if (!onlineStatus) {
            return "%s is offline.".formatted(this.getUsername());
        }
        String type;
        if (lastRecommendation.isSong()) {
            type = "song";
        } else {
            type = "playlist";
        }
        player.setSource(lastRecommendation, type);
        player.resetAdBreak();
        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!(player.getType().equals("playlist") || player.getType().equals("album"))) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
            && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (!onlineStatus) {
            return null;
        }
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, this.getUsername(), timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (!onlineStatus) {
            return null;
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (!onlineStatus) {
            return null;
        }
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        if (!onlineStatus) {
            return null;
        }
        DatabaseEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(this.getUsername())) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Get top 5 songs for user
     * @return top 5 songs
     */
    public ArrayList<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(this.getLikedSongs());
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        ArrayList<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= MAX_RECS) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Get top 5 playlists for user
     * @return top 5 playlists
     */
    public ArrayList<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(this.getFollowedPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getLikes).reversed());
        ArrayList<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= MAX_RECS) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (onlineStatus) {
            player.simulatePlayer(time);
            for (AudioFile audioFile: player.getPlayerHistory()) {
                if (audioFile.getType().equals("song")) {
                    addSongMonetization((Song) audioFile);
                }
                updateWrapped(audioFile);
            }
            player.clearPlayerHistory();
        }
    }

    /**
     * change page for user
     * @param nextPage next page
     * @return the message
     */
    public String changePage(final String nextPage) {
        return switch (nextPage) {
            case "Home" -> {
                pageSystemInvoker
                        .execute(new ChangePage(pageSystem, pageSystem.getHomePage()));
                yield this.getUsername() + " accessed Home successfully.";
            }
            case "LikedContent" -> {
                pageSystemInvoker
                        .execute(new ChangePage(pageSystem, pageSystem.getLikedContentPage()));
                yield this.getUsername() + " accessed LikedContent successfully.";
            }
            case "Artist" -> {
                if (player.getType().equals("album") || player.getType().equals("song")
                        || player.getType().equals("playlist")) {
                    Artist artist = (Artist) player.getUserInteraction();
                    pageSystemInvoker.execute(new ChangePage(pageSystem, artist.getArtistPage()));
                    yield "%s accessed Artist successfully.".formatted(this.getUsername());
                }
                yield this.getUsername() + " is trying to access a non-existent page.";
            }
            case "Host" -> {
                if (player.getType().equals("podcast")) {
                    Host host = (Host) player.getUserInteraction();
                    pageSystemInvoker.execute(new ChangePage(pageSystem, host.getHostPage()));
                    yield "%s accessed Host successfully.".formatted(this.getUsername());
                }
                yield this.getUsername() + " is trying to access a non-existent page.";
            }
            default -> this.getUsername() + " is trying to access a non-existent page.";
        };
    }

    /**
     * print page for user
     * @return page as string
     */
    public String printCurrentPage() {
        return pageSystem.displayPage();
    }

    /**
     * Gets current page
     * @return -> the page
     */
    public Page getCurrentPage() {
        return pageSystem.getCurrentPage();
    }

    /**
     * Sets next page
     * @return -> the message
     */
    public String nextPage() {
        if (!pageSystemInvoker.redo()) {
            return "There are no pages left to go forward.";
        }
        return "The user %s has navigated successfully to the next page."
                .formatted(this.getUsername());
    }

    /**
     * Sets prev page
     * @return -> the message
     */
    public String prevPage() {
        if (!pageSystemInvoker.undo()) {
            return "There are no pages left to go back.";
        }
        return "The user %s has navigated successfully to the previous page."
                .formatted(this.getUsername());
    }

    /**
     * Switch user online status.
     */
    public void switchOnlineStatus() {
        this.onlineStatus = !this.onlineStatus;
    }

    /**
     * Get user online status
     * @return online status
     */
    public boolean getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * Checks if user can be deleted safely
     * @return true, if it can, or false otherwise
     */
    @Override
    public boolean checkIfDelete() {
        ArrayList<String> onlineUsers = new ArrayList<>(Admin.getOnlineUsers());
        for (String u : onlineUsers) {
            User user = (User) Admin.getUser(u);
            for (Playlist playlist : playlists) {
                if (user.playerInteractionPlaylist() == playlist) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Update the wrapped for the user
     * @param audioFile -> the audiofile
     */
    private void updateWrapped(final AudioFile audioFile) {
        if (audioFile.getType().equals("song")) {
            Song song = (Song) audioFile;
            userWrapped.incrementSong(song.getName());
            userWrapped.incrementGenre(song.getGenre());
            userWrapped.incrementArtist(song.getArtist());
            userWrapped.incrementAlbums(song.getAlbum());

            Artist artist = (Artist) Admin.getUser(song.getArtist());
            if (!Admin.artistMonetizationExists(artist.getName())) {
                Admin.addArtistMonetization(artist);
            }
            artist.updateWrapped(this.getUsername(), song);
        } else {
            Episode episode = (Episode) audioFile;
            userWrapped.incrementEpisodes(episode.getName());

            Host host = Admin.getHostByEpisode(episode);
            if (host != null) {
                host.updateWrapped(this.getUsername(), episode);
            }
        }
    }

    /**
     * Switch from premium user to non-premium and vice-versa
     */
    public void switchPremium() {
        premium = !premium;
    }

    /**
     * Get if the player of the user is pause
     * @return -> true, if it is, false otherwise
     */
    public boolean getPlayerPaused() {
        return player.getPaused();
    }

    /**
     * Add ad break in the music queue
     */
    public void addAdBreak() {
        player.addAdBreak();
    }

    /**
     *
     * @return false
     */
    public boolean isHost() {
        return false;
    }

    /**
     *
     * @return false
     */
    public boolean isArtist() {
        return false;
    }

    /**
     * for checking instance
     * @return true
     */
    public boolean isUser() {
        return true;
    }

    /**
     * Get user premium status
     * @return -> true, if user is premium, false otherwise
     */
    public boolean getPremium() {
        return premium;
    }

    /**
     * Add listened songs in the premium or free list for monetization
     * Premium list -> the song monetization will be done before the end of the program
     * Free list -> the song monetization will be done after an ad was succesfully played
     * @param song -> the song
     */
    public void addSongMonetization(final Song song) {
        if (premium) {
            monetizationPremium.add(song);
        } else {
            monetizationFree.add(song);
        }
    }

    /**
     * Update the song revenue for the songs listened while the user account was premium
     */
    public void updatePremiumMonetization() {
        if (!monetizationPremium.isEmpty()) {
            double revenue = PREMIUM_VALUE / monetizationPremium.size();
            for (Song song : monetizationPremium) {
                song.addRevenue(revenue);
            }
        }
        monetizationPremium.clear();
    }

    /**
     * Update the song revenue after a valid ad break
     */
    public void updateFreeMonetization() {
        double songRevenue = adRevenue / (monetizationFree.size());
        for (Song song : monetizationFree) {
            song.addRevenue(songRevenue);
        }
        monetizationFree.clear();
    }

    /**
     * Sets ad revenue
     * @param revenue -> the revenue
     */
    public void setAdRevenue(final Double revenue) {
        adRevenue = revenue;
    }

    /**
     * Adds merch in list
     * @param merchName -> the merch
     */
    public void addMerch(final String merchName) {
        merch.add(merchName);
    }

    /**
     * Adds notifications in list.
     * @param notification -> the notification
     */
    @Override
    public void update(final Notification notification) {
        notifications.add(notification);
    }

    /**
     * Resets notifications
     */
    public void resetNotifications() {
        notifications.clear();
    }

    /**
     * Returns a new list with all the notification.
     * @return -> the notifications
     */
    public ArrayList<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    /**
     * Adds a genre recommendation
     * @return -> the string
     */
    public String addGenreRec() {
        String genre = player.getGenreRec();
        if (genre != null) {
            int seed = player.getTimeListened();
            Random random = new Random(seed);
            Song songRec = Admin.getSongsByGenre(genre)
                    .get(random.nextInt(Admin.getSongsByGenre(genre).size()));
            lastRecommendation = songRec;
            HomePage homepage = pageSystem.getHomePage();
            homepage.addSongRec(songRec.getName());
            return "The recommendations for user %s have been updated successfully."
                    .formatted(this.getUsername());
        }
        return "No new recommendations were found";
    }

    /**
     * Adds a playlist recommendation
     * @return -> the message
     */
    public String addPlaylistRec() {
        HomePage homepage = pageSystem.getHomePage();
        homepage.addPlaylistRec("%s's recommendations".formatted(this.getUsername()));
        return "The recommendations for user %s have been updated successfully."
                .formatted(this.getUsername());
    }

    /**
     * Adds a fan playlist recommendation
     * @return -> the message
     */
    public String addFansPlaylistRec() {
        HomePage homepage = pageSystem.getHomePage();
        homepage.addPlaylistRec("%s Fan Club recommendations"
                .formatted(player.getUserInteraction().getUsername()));
        return "The recommendations for user %s have been updated successfully."
                .formatted(this.getUsername());
    }
}
