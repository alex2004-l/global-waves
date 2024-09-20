package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import app.user.UserEntry;
import app.user.UserFactory;
import app.user.Artist;
import app.user.Host;
import app.user.monetization.ArtistMonetization;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import main.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Admin.
 */
public final class Admin {
    private static List<UserEntry> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static List<ArtistMonetization> artistMonetizations = new ArrayList<>();
    //private static List<Artist> artists = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Transforms a list of SongInput objects into a list of Song objects
     *
     * @param songInputs
     * @return songInputs as an array of Songs
     */
    public static ArrayList<Song> getSongsFromSongInput(final ArrayList<SongInput> songInputs) {
        ArrayList<Song> result = new ArrayList<>();
        for (SongInput songInput : songInputs) {
            result.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
        return result;
    }

    /**
     * Transforms a list of EpisodeInput objects into a list of Episode objects
     *
     * @param episodeInputs
     * @return episodeInputs as an array of Episodes
     */
    public static ArrayList<Episode> getEpisodesFromEpisodeInput(
                        final ArrayList<EpisodeInput> episodeInputs) {
        ArrayList<Episode> episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : episodeInputs) {
            episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                    episodeInput.getDescription()));
        }
        return episodes;
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(),
                                            podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Adds a list of songs in the admin database
     * @param newSongs
     */
    public static void addSongs(final ArrayList<Song> newSongs) {
        if (songs == null) {
            songs = new ArrayList<>();
        }
        songs.addAll(newSongs);
    }

    /**
     * Adds an album in the admin database
     * @param album
     */
    public static void addAlbum(final Album album) {
        albums.add(album);
    }

    /**
     * Adds an podcast in the admin database
     * @param podcast
     */
    public static void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Add monetization in database for an artist whose audiofiles were played
     * or whose merch was bought
     * @param artist -> the artist
     */
    public static void addArtistMonetization(final Artist artist) {
        artistMonetizations.add(new ArtistMonetization(artist.getName()));
    }

    /**
     * Check if an artist already exist in the monetization list from the database
     * @param artist -> the name of the artist
     * @return -> true, if exists, false otherwise
     */
    public static boolean artistMonetizationExists(final String artist) {
        return artistMonetizations.stream()
                .anyMatch(monetization -> monetization.getArtist().equals(artist));
    }

    /**
     * Method that updates the monetization for all artists at the end of the program
     * @return -> an unmodifiable list with the artists monetization statistics
     */
    public static List<ArtistMonetization> getArtistMonetizations() {
        updatePremiumMonetization();
        for (ArtistMonetization artistMonetization : artistMonetizations) {
            artistMonetization.updateArtistMonetization();
        }
        Collections.sort(artistMonetizations,
                Comparator.comparing(ArtistMonetization::getTotalRevenue)
                .reversed().thenComparing(ArtistMonetization::getArtist));
        for (int i = 0; i < artistMonetizations.size(); ++i) {
            artistMonetizations.get(i).setRanking(i + 1);
        }
        return Collections.unmodifiableList(artistMonetizations);
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets albums.
     * @return the albums
     */
    public static List<Album> getAlbums() {
        ArrayList<Album> result = new ArrayList<>();
        for (UserEntry artist: getArtists()) {
            result.addAll(((Artist) artist).getAlbums());
        }
        return result;
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (UserEntry user : users) {
            if (user.isUser()) {
                playlists.addAll(((User) user).getPlaylists());
            }
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static UserEntry getUser(final String username) {
        for (UserEntry user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets an album based on its name and its owner
     * @param name album name
     * @param artist artist name
     * @return album entry in admin database
     */
    public static Album getAlbum(final String name, final String artist) {
        for (Album album : albums) {
            if (album.getName().equals(name) && album.getOwner().equals(artist)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (UserEntry user : users) {
            if (user.isUser()) {
                ((User) user).simulateTime(elapsed);
            }
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Gets top 5 albums
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<Album> sortedAlbum = new ArrayList<>(getAlbums());
        sortedAlbum.sort(Comparator.comparingInt(Album::getLikesCount)
                .reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbum) {
            if (count >= LIMIT) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * Gets top 5 artists.
     * @return the top 5 artists
     */
    public static List<String> getTop5Artists() {
        List<UserEntry> artistsList = getArtists();
        List<Artist> sortedArtists = new ArrayList<>();
        for (UserEntry artist : artistsList) {
            sortedArtists.add((Artist) artist);
        }
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikesCount)
                .reversed()
                .thenComparing(Artist::getName, Comparator.naturalOrder()));
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (UserEntry artist : sortedArtists) {
            if (count >= LIMIT) {
                break;
            }
            topArtists.add(artist.getName());
            count++;
        }
        return topArtists;
    }

    /**
     * Adds user in database
     * @param username username
     * @param age username's age
     * @param city username's city
     * @param userType username's user type -> user, host, artist
     * @return
     */
    public static String addUser(final String username, final int age,
                                 final String city, final String userType) {
        if (Admin.getUser(username) != null) {
            return "The username " + username + " is already taken.";
        }
        Admin.users.add(UserFactory.createUser(username, age, city, userType));
        return "The username " + username + " has been added successfully.";
    }

    /**
     * Deletes user from database
     * @param username username
     * @return
     */
    public static String deleteUser(final String username) {
        if (Admin.getUser(username) == null) {
            return "The username " + username + " doesn't exist.";
        }
        UserEntry user = Admin.getUser(username);
        if (user.isUser()) {
            if (user.checkIfDelete() && !((User) user).getPremium()) {
                Admin.deletePlaylists((User) user);
                for (Playlist playlist : ((User) user).getFollowedPlaylists()) {
                    playlist.decreaseFollowers();
                }
                Admin.users.remove(user);
                return username + " was successfully deleted.";
            } else {
                return username + " can't be deleted.";
            }
        }
        if (user.isHost()) {
            if (user.checkIfDelete()) {
                for (Podcast podcast : ((Host) user).getPodcasts()) {
                    Admin.podcasts.remove(podcast);
                }
                Admin.users.remove(user);
                return username + " was successfully deleted.";
            } else {
                return username + " can't be deleted.";
            }
        }

        if (user.isArtist()) {
            if (user.checkIfDelete()) {
                for (Album album : ((Artist) user).getAlbums()) {
                    Admin.deleteAlbum(album);
                }
                Admin.users.remove(user);
                return username + " was successfully deleted.";
            } else {
                return username + " can't be deleted.";
            }
        }

        return new String();
    }

    /**
     * Deletes album from database
     * @param username album owner
     * @param name album name
     * @return
     */
    public static String removeAlbum(final String username, final String name) {
        UserEntry user = Admin.getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (!user.isArtist()) {
            return username + " is not an artist.";
        }
        if (((Artist) user).getAlbums().stream().noneMatch(album -> album.getName().equals(name))) {
            return username + " doesn't have an album with the given name.";
        }
        Album album = ((Artist) user).getAlbumByName(name);
        if (album.checkIfDelete()) {
            Admin.albums.remove(album);
            ((Artist) user).getAlbums().remove(album);
            return username + " deleted the album successfully.";
        } else {
            return username + " can't delete this album.";
        }

    }

    /**
     * Deletes podcast from database
     * @param username host name
     * @param name podcast name
     * @return
     */
    public static String removePodcast(final String username, final String name) {
        UserEntry user = Admin.getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (!user.isHost()) {
            return username + " is not a host.";
        }
        if (((Host) user).getPodcasts().stream()
                .noneMatch(podcast -> podcast.getName().equals(name))) {
            return username + " doesn't have a podcast with the given name.";
        }
        Podcast podcast = ((Host) user).getPodcastByName(name);
        if (podcast.checkIfDelete()) {
            Admin.podcasts.remove(podcast);
            ((Host) user).getPodcasts().remove(podcast);
            return username + " deleted the podcast successfully.";
        } else {
            return username + " can't delete this podcast.";
        }
    }

    /**
     * Gets a list of online users
     * @return online users
     */
    public static ArrayList<String> getOnlineUsers() {
        ArrayList<String> result = new ArrayList<>();
        for (UserEntry user : users) {
            if (user.isUser() && ((User) user).getOnlineStatus()) {
                result.add(user.getUsername());
            }
        }
        return result;
    }

    /**
     * Gets a list of artists
     * @return artists
     */
    public static ArrayList<UserEntry> getArtists() {
        ArrayList<UserEntry> result = users.stream()
                .filter(UserEntry::isArtist)
                .collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    /**
     * Gets a list of hosts
     * @return hosts
     */
    public static ArrayList<UserEntry> getHosts() {
        ArrayList<UserEntry> result = users.stream()
                .filter(UserEntry::isHost)
                .collect(Collectors.toCollection(ArrayList::new));
        return result;
    }


    // to update function
    /**
     * Gets a list of all users
     * @return all users
     */
    public static ArrayList<String> getAllUsers() {
        ArrayList<String> result = new ArrayList<>();
        for (UserEntry user : users) {
            if (user.isUser()) {
                result.add(user.getUsername());
            }
        }
        for (UserEntry user: users) {
            if (user.isArtist()) {
                result.add(user.getUsername());
            }
        }
        for (UserEntry user: users) {
            if (user.isHost()) {
                result.add(user.getUsername());
            }
        }
        return result;
    }

    /**
     * Deletes an album from database, all his songs and dependencies
     * @param album
     */
    public static void deleteAlbum(final Album album) {
        ArrayList<String> allUsers = Admin.getAllUsers();
        for (Song song : album.getSongs()) {
            Admin.songs.remove(song);
            for (String u : allUsers) {
                UserEntry user = Admin.getUser(u);
                if (user != null && user.isUser()) {
                    ((User) user).getLikedSongs().remove(song);
                }
            }
        }
        Admin.albums.remove(album);
    }

    /**
     * Deletes an playlist and all his dependencies
     * @param user
     */
    public static void deletePlaylists(final User user) {
        ArrayList<String> allUsers = Admin.getAllUsers();
        for (Playlist playlist : user.getPlaylists()) {
            for (String u : allUsers) {
                UserEntry userEntry = Admin.getUser(u);
                if (userEntry != null && userEntry.isUser()) {
                    ((User) userEntry).getFollowedPlaylists().remove(playlist);
                }
            }
        }
    }

    /**
     * For every normal user in the database updates the revenue for the songs
     * listened while the user had a premium account
     */
    public static void updatePremiumMonetization() {
        for (UserEntry userEntry: users) {
            if (userEntry.isUser()) {
                User user = (User) userEntry;
                user.updatePremiumMonetization();
            }
        }
    }

    /**
     * Gets the owner of a podcast episode
     * @param episode -> the episode
     * @return -> the host
     */
    public static Host getHostByEpisode(final Episode episode) {
        for (Podcast podcast : podcasts) {
            for (Episode ep : podcast.getEpisodes()) {
                if (ep.getName().equals(episode.getName())) {
                    return (Host) Admin.getUser(podcast.getOwner());
                }
            }
        }
        return null;
    }

    /**
     * Gets a list of all song with a certain genre
     * @param genre -> searched genre
     * @return -> a list on songs
     */
    public static List<Song> getSongsByGenre(final String genre) {
        return new ArrayList<>(getSongs().stream()
                .filter(s -> s.getGenre().equals(genre)).toList());
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        artistMonetizations = new ArrayList<>();
        timestamp = 0;
    }

}
