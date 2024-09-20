package app.user;

import app.DatabaseEntry;
import app.utils.Enums;
import lombok.Getter;

@Getter
public abstract class UserEntry extends DatabaseEntry {
    private String username;
    private int age;
    private String city;
    private Enums.UserEntryType userType;

    public UserEntry(final String username, final int age, final String city,
                     final Enums.UserEntryType userType) {
        super(username, Enums.DatabaseEntryType.UserEntry);
        this.username = username;
        this.age = age;
        this.city = city;
        this.userType = userType;
    }

    /**
     * @return the boolean
     */
    public abstract boolean isUser();
    /**
     * @return the boolean
     */
    public abstract boolean isArtist();
    /**
     * @return the boolean
     */
    public abstract boolean isHost();

    /**
     * Checks if the username matches the name filter
     * @param nameFilter name filters
     * @return bool
     */
    @Override
    public boolean matchesName(final String nameFilter) {
        return getUsername().toLowerCase().startsWith(nameFilter.toLowerCase());
    }

    /**
     * Checks if the database entry is a user entry
     * @return bool
     */
    @Override
    public boolean isUserEntry() {
        return true;
    }

    /**
     * check if user can be deleted
     * @return
     */
    public boolean checkIfDelete() {
        return false;
    }
}
