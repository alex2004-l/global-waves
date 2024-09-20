package app.pageSystem.pages;

import app.user.Artist;
import app.user.utils.Event;
import app.user.utils.Merch;
import lombok.Getter;

import java.util.ArrayList;

public class ArtistPage implements Page {
    @Getter
    private final Artist artist;
    private ArrayList<String> albums;
    private ArrayList<Event> events;
    private ArrayList<Merch> merchandise;

    public ArtistPage(final Artist artist) {
        this.artist = artist;
        this.albums = artist.getAlbumsName();
        this.events = artist.getEvents();
        this.merchandise = artist.getMerchandise();
    }

    /**
     * refresh page
     */
    public void refresh() {
        this.albums = artist.getAlbumsName();
        this.events = artist.getEvents();
        this.merchandise = artist.getMerchandise();
    }

    /**
     * display page
     * @return String
     */
    public String display() {
        this.refresh();
        StringBuilder message = new StringBuilder("Albums:\n\t[");
        for (int i = 0; i < albums.size() - 1; ++i) {
            message.append(albums.get(i)).append(", ");
        }
        if (!albums.isEmpty()) {
            message.append(albums.get(albums.size() - 1));
        }
        message.append("]\n");
        message.append("\nMerch:\n\t[");
        for (int i = 0; i < merchandise.size() - 1; ++i) {
            message.append(merchandise.get(i).getName()).append(" - ")
                    .append(merchandise.get(i).getPrice()).append(":\n\t")
                    .append(merchandise.get(i).getDescription()).append(", ");
        }
        if (!merchandise.isEmpty()) {
            message.append(merchandise.get(merchandise.size() - 1).getName()).append(" - ")
                    .append(merchandise.get(merchandise.size() - 1).getPrice()).append(":\n\t")
                    .append(merchandise.get(merchandise.size() - 1).getDescription());
        }
        message.append("]\n");
        message.append("\nEvents:\n\t[");
        for (int i = 0; i < events.size() - 1; ++i) {
            message.append(events.get(i).getName()).append(" - ")
                    .append(events.get(i).getDate()).append(":\n\t")
                    .append(events.get(i).getDescription()).append(", ");
        }
        if (!events.isEmpty()) {
            message.append(events.get(events.size() - 1).getName()).append(" - ")
                    .append(events.get(events.size() - 1).getDate()).append(":\n\t")
                    .append(events.get(events.size() - 1).getDescription());
        }
        message.append("]");
        return message.toString();
    }

    /**
     * Checks if the page is an artist page
     * @return -> the boolean
     */
    public boolean isArtistPage() {
        return true;
    }
}
