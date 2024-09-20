package app.audio;

import app.DatabaseEntry;
import app.utils.Enums;
import lombok.Getter;


/**
 * The type Library entry.
 */
@Getter
public abstract class LibraryEntry extends DatabaseEntry {
    /**
     * Instantiates a new Library entry.
     *
     * @param name the name
     */
    public LibraryEntry(final String name) {
        super(name, Enums.DatabaseEntryType.LibraryEntry);
    }

    /**
     * Matches name boolean.
     *
     * @param nameFilter the name
     * @return the boolean
     */
    @Override
    public boolean matchesName(final String nameFilter) {
        return getName().toLowerCase().startsWith(nameFilter.toLowerCase());
    }

    public boolean isSong() {
        return false;
    }
}
