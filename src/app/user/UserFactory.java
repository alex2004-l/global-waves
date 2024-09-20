package app.user;

public final class UserFactory {
    private UserFactory() {

    }
    /**
     * Creates user depending on type
     * @param username username
     * @param age user age
     * @param city user city
     * @param type user type
     * @return new user
     */
    public static UserEntry createUser(final String username, final int age,
                                       final String city, final String type) {
        switch (type) {
            case "user" :
                return new User(username, age, city);
            case "artist":
                return new Artist(username, age, city);
            case "host":
                return new Host(username, age, city);
            default:
                throw new
                        IllegalArgumentException("The user type " + type + " is not recognized.");
        }
    }
}
