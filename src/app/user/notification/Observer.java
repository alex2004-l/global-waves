package app.user.notification;

public interface Observer {
    /**
     * Updates notifications
     * @param notification -> the notification
     */
    void update(Notification notification);
}
