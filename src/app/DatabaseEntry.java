package app;

import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public abstract class DatabaseEntry {
    private final String name;
    private final Enums.DatabaseEntryType databaseEntryType;

    public DatabaseEntry(final String name, final Enums.DatabaseEntryType databaseEntryType) {
        this.name = name;
        this.databaseEntryType = databaseEntryType;
    }

    /**
     * Matches name boolean.
     *
     * @param nameFilter name filters
     * @return the boolean
     */
    public boolean matchesName(final String nameFilter) {
        return getName().toLowerCase().startsWith(nameFilter.toLowerCase());
    }


    /**
     * Matches album boolean.
     *
     * @param album the album
     * @return the boolean
     */
    public boolean matchesAlbum(final String album) {
        return false;
    }

    /**
     * Matches tags boolean.
     *
     * @param tags the tags
     * @return the boolean
     */
    public boolean matchesTags(final ArrayList<String> tags) {
        return false;
    }

    /**
     * Matches lyrics boolean.
     *
     * @param lyrics the lyrics
     * @return the boolean
     */
    public boolean matchesLyrics(final String lyrics) {
        return false;
    }

    /**
     * Matches genre boolean.
     *
     * @param genre the genre
     * @return the boolean
     */
    public boolean matchesGenre(final String genre) {
        return false;
    }

    /**
     * Matches artist boolean.
     *
     * @param artist the artist
     * @return the boolean
     */
    public boolean matchesArtist(final String artist) {
        return false;
    }

    /**
     * Matches release year boolean.
     *
     * @param releaseYear the release year
     * @return the boolean
     */
    public boolean matchesReleaseYear(final String releaseYear) {
        return false;
    }

    /**
     * Matches owner boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean matchesOwner(final String user) {
        return false;
    }

    /**
     * Is visible to user boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean isVisibleToUser(final String user) {
        return false;
    }

    /**
     * Matches followers boolean.
     *
     * @param followers the followers
     * @return the boolean
     */
    public boolean matchesFollowers(final String followers) {
        return false;
    }

    /**
     * Matches description boolean.
     *
     * @param description the description
     * @return the boolean
     */
    public boolean matchesDescription(final String description) {
        return false;
    }

    /**
     * Instance is user entry
     * @return the boolean
     */
    public boolean isUserEntry() {
        return false;
    }
}
