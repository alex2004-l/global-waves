package app.pageSystem.pages;

public interface Page {
    /**
     * display page
     * @return page display as String
     */
    String display();
    /**
     * refresh page
     */
    void refresh();

    default boolean isArtistPage() {
        return false;
    }

    default boolean isHostPage() {
        return false;
    }
}
