package app.pageSystem.pages;

import app.audio.Collections.Podcast;
import app.user.Host;
import app.user.utils.Announcement;
import lombok.Getter;

import java.util.ArrayList;

public class HostPage implements Page {
    @Getter
    private final Host host;
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;


    public HostPage(final Host host) {
        this.host = host;
        this.podcasts = host.getPodcasts();
        this.announcements = host.getAnnouncements();
    }

    /**
     * refresh page
     */
    public void refresh() {
        this.podcasts = host.getPodcasts();
        this.announcements = host.getAnnouncements();
    }

    /**
     * display page
     * @return the String
     */
    public String display() {
        this.refresh();
        StringBuilder message = new StringBuilder("Podcasts:\n\t[");
        for (int i = 0; i < podcasts.size(); ++i) {
            message.append(podcasts.get(i).getName()).append(":\n\t[");
            Podcast currentPodcast = podcasts.get(i);
            for (int j = 0; j < currentPodcast.getEpisodes().size(); ++j) {
                message.append(currentPodcast.getEpisodes().get(j).getName())
                        .append(" - ").append(currentPodcast.getEpisodes()
                                .get(j).getDescription());
                if (j == currentPodcast.getEpisodes().size() - 1) {
                    break;
                }
                message.append(", ");
            }
            message.append("]\n");
            if (i == podcasts.size() - 1) {
                break;
            }
            message.append(", ");
        }
        message.append("]\n\nAnnouncements:\n\t[");
        for (int i = 0; i < announcements.size(); ++i) {
            message.append(announcements.get(i).getName()).append(":\n\t")
                    .append(announcements.get(i).getDescription());
            if (i == announcements.size() - 1) {
                break;
            }
            message.append(", ");
        }
        message.append("]");
        return message.toString();
    }

    /**
     * Checks if page is host page
     * @return -> the boolean
     */
    @Override
    public boolean isHostPage() {
        return true;
    }
}
