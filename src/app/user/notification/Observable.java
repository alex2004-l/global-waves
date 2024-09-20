package app.user.notification;

public interface Observable {
    /**
     * Adds observer in list
     * @param observer -> the list
     */
    void addObserver(Observer observer);

    /**
     * Removes observer from list
     * @param observer -> the list
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all observers
     * @param name -> the name of the announcement
     * @param description -> the description of the announcement
     */
    void notifyObservers(String name, String description);
}
