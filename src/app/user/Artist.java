package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.outputFiles.AlbumOutput;
import app.audio.Files.Song;
import app.pageSystem.pages.ArtistPage;
import app.user.notification.Observable;
import app.user.notification.Observer;
import app.user.utils.Event;
import app.user.utils.Merch;
import app.user.notification.Notification;
import app.user.wrapped.ArtistWrapped;
import app.utils.Enums;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
public class Artist extends UserEntry implements Observable {
    private ArrayList<Album> albums;
    private ArrayList<Event> events;
    private ArrayList<Merch> merchandise;
    private final ArtistPage artistPage;
    private ArtistWrapped artistWrapped;
    private ArrayList<User> subscribers;

    public Artist(final String username, final int age, final String city) {
        super(username, age, city, Enums.UserEntryType.ARTIST);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchandise = new ArrayList<>();
        artistPage = new ArtistPage(this);
        artistWrapped = new ArtistWrapped();
        subscribers = new ArrayList<>();
    }

    /**
     * Add album for user
     * @param name album name
     * @param songInputs inputs for the songs
     * @param releaseYear release year
     * @param description the description
     * @return message
     */
    public String addAlbum(final String name, final ArrayList<SongInput> songInputs,
                                final int releaseYear, final String description) {
        if (albums.stream().anyMatch(album -> album.getName().equals(name))) {
            return this.getUsername() + " has another album with the same name.";
        }
        ArrayList<Song> songs = Admin.getSongsFromSongInput(songInputs);
        if (songs.stream().collect(Collectors.groupingBy(s -> s.getName(), Collectors.counting()))
                .values().stream()
                .anyMatch(count -> count > 1)) {
            return this.getUsername() + " has the same song at least twice in this album.";
        }
        Admin.addSongs(songs);
        Album newAlbum = new Album(name, this.getUsername(), description, releaseYear, songs, 0);
        albums.add(newAlbum);
        Admin.addAlbum(newAlbum);
        notifyObservers("New Album", "New Album from %s.".formatted(this.getUsername()));
        return this.getUsername() + " has added new album successfully.";
    }

    /**
     * Add event
     * @param name event name
     * @param description event description
     * @param date event date
     * @return message
     */
    public String addEvent(final String name, final String description, final String date) {
        for (Event event : events) {
            if (event.getName().equals(name)) {
                String s = " has another event with the same name.";
                return this.getUsername() + s;
            }
        }
        if (!Event.checkDate(date)) {
            return "Event for " + this.getUsername() + " does not have a valid date.";
        }
        events.add(new Event(name, description, date));
        notifyObservers("New Event", "New Event from %s.".formatted(this.getUsername()));
        return this.getUsername() + " has added new event successfully.";
    }

    /**
     * Remove event
     * @param name event name
     * @return message
     */
    public String removeEvent(final String name) {
        for (int i = 0; i < events.size(); ++i) {
            if (events.get(i).getName().equals(name)) {
                events.remove(i);
                return this.getUsername() + " deleted the event successfully.";
            }
        }
        return this.getUsername() + " doesn't have an event with the given name.";
    }

    /**
     * Add merchandise.
     * @param name merchandise name
     * @param description merchandise description
     * @param price merchandise price
     * @return message
     */
    public String addMerchandise(final String name, final String description, final int price) {
        for (Merch merch : merchandise) {
            if (merch.getName().equals(name)) {
                String s = " has merchandise with the same name.";
                return this.getUsername() + s;
            }
        }
        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }
        merchandise.add(new Merch(name, description, price));
        notifyObservers("New Merchandise", "New Merchandise from %s."
                .formatted(this.getUsername()));
        return this.getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Show albums.
     * @return albums as an array of AlbumOutputs
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }
        return albumOutputs;
    }

    /**
     * Gets albums names
     * @return list of names
     */
    public ArrayList<String> getAlbumsName() {
        ArrayList<String> result = new ArrayList<>();
        for (Album album : albums) {
            result.add(album.getName());
        }
        return result;
    }

    /**
     * check if an album can be deleted
     * @return the boolean
     */
    @Override
    public boolean checkIfDelete() {
        ArrayList<String> onlineUsers = new ArrayList<>(Admin.getOnlineUsers());
        for (String u : onlineUsers) {
            User user = (User) Admin.getUser(u);
            if (user.playerInteraction() == this) {
                return false;
            }
            if (user.getCurrentPage() == artistPage) {
                return false;
            }
        }
        for (Album album : albums) {
            if (!album.checkIfDelete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets an album with a certain name.
     * @param name album the
     * @return album entry
     */
    public Album getAlbumByName(final String name) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Gets likes count
     * @return total likes for all albums
     */
    public int getLikesCount() {
        int result = 0;
        for (Album album : albums) {
            result += album.getLikesCount();
        }
        return result;
    }

    /**
     * Updates the wrapped statistics for the artist's song received as parameter
     * and increases the count of total listens for the user
     * @param user -> name of the user
     * @param song -> song
     */
    public void updateWrapped(final String user, final Song song) {
        artistWrapped.incrementFans(user);
        artistWrapped.incrementAlbum(song.getAlbum());
        artistWrapped.incrementSong(song.getName());
        artistWrapped.updateListeners();
    }

    /**
     *
     * @return false
     */
    public boolean isUser() {
        return false;
    }

    /**
     * For checking parent instance
     * @return true
     */
    public boolean isArtist() {
        return true;
    }

    /**
     *
     * @return false
     */
    public boolean isHost() {
        return false;
    }

    /**
     * Adds observer
     * @param observer -> the observer
     */
    @Override
    public void addObserver(final Observer observer) {
        subscribers.add((User) observer);
    }

    /**
     * Removes observer.
     * @param observer
     */
    @Override
    public void removeObserver(final Observer observer) {
        subscribers.remove((User) observer);
    }

    /**
     * Notifies all observers
     * @param name -> the name of the announcement
     * @param description -> the description of the announcement
     */
    @Override
    public void notifyObservers(final String name, final String description) {
        Notification newNotification = new Notification(name, description);
        for (User user : subscribers) {
            user.update(newNotification);
        }
    }

    /**
     * Checks if user is  subscriber of artist.
     * @param user -> the username
     * @return -> the boolean
     */
    public boolean checkSubscriber(final String user) {
        return subscribers.stream().anyMatch(u -> u.getUsername().equals(user));
    }
}
