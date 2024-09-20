package app.user.notification;

import lombok.Getter;

@Getter
public class Notification {
    private final String name;
    private final String description;

    public Notification(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
