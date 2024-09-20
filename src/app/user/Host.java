package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Collections.outputFiles.PodcastOutput;
import app.audio.Files.Episode;
import app.pageSystem.pages.HostPage;
import app.user.notification.Observable;
import app.user.notification.Observer;
import app.user.utils.Announcement;
import app.user.notification.Notification;
import app.user.wrapped.HostWrapped;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
public class Host extends UserEntry implements Observable {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;
    private final HostPage hostPage;
    private Integer userInteractions;
    private HostWrapped hostWrapped;
    private ArrayList<User> subscribers;

    public Host(final String username, final int age, final String city) {
        super(username, age, city, Enums.UserEntryType.HOST);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        hostPage = new HostPage(this);
        userInteractions = 0;
        hostWrapped = new HostWrapped();
    }

    /**
     * Add podcast for host
     * @param name podcast name
     * @param episodeInputs episodes input for episodes
     * @param owner podcast owner
     * @return
     */
    public String addPodcast(final String name,
                             final ArrayList<EpisodeInput> episodeInputs, final String owner) {
        if (podcasts.stream().anyMatch(podcast -> podcast.getName().equals(name))) {
            return this.getUsername() + " has another podcast with the same name.";
        }
        ArrayList<Episode> episodes = Admin.getEpisodesFromEpisodeInput(episodeInputs);
        if (episodes.stream().collect(Collectors
                        .groupingBy(e -> e.getName(), Collectors.counting())).values().stream()
                            .anyMatch(count -> count > 1)) {
            return this.getUsername() + " has the same episode in this podcast.";
        }
        Podcast newPodcast = new Podcast(name, owner, episodes);
        podcasts.add(newPodcast);
        Admin.addPodcast(newPodcast);
        return this.getUsername() + " has added new podcast successfully.";
    }

    /**
     * Add announcements for host
     * @param name announcement name
     * @param description announcement description
     * @return message
     */
    public String addAnnouncement(final String name, final String description) {
        if (announcements.stream().anyMatch(announcement -> announcement.getName().equals(name))) {
            return this.getUsername() + " has already added an announcement with this name.";
        }
        announcements.add(new Announcement(name, description));
        notifyObservers("New Announcement",
                "New Announcement from %s.".formatted(this.getUsername()));
        return this.getUsername() + " has successfully added new announcement.";
    }

    /**
     * Remove announcement for host
     * @param name announcement name
     * @return message
     */
    public String removeAnnouncement(final String name) {
        for (int i = 0; i < announcements.size(); ++i) {
            if (announcements.get(i).getName().equals(name)) {
                announcements.remove(i);
                return this.getUsername() + " has successfully deleted the announcement.";
            }
        }
        return this.getUsername() + " has no announcement with the given name.";
    }

    /**
     * Show podcasts
     * @return podcasts list as podcast input
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }
        return podcastOutputs;
    }

    /**
     * Get podcast by name
     * @param name podcast name
     * @return podcast
     */
    public Podcast getPodcastByName(final String name) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        return null;
    }

    /**
     *
     * @return false
     */
    public boolean isUser() {
        return false;
    }

    /**
     *
     * @return false
     */
    public boolean isArtist() {
        return false;
    }

    /**
     * for checking instance
     * @return true
     */
    public boolean isHost() {
        return true;
    }

    /**
     * Checks if host user can be deleted safely.
     * @return -> true, if it can, false otherwise
     */
    @Override
    public boolean checkIfDelete() {
        ArrayList<String> onlineUsers = new ArrayList<>(Admin.getOnlineUsers());
        for (String u : onlineUsers) {
            User user = (User) Admin.getUser(u);
            if (user.getCurrentPage() == hostPage) {
                return false;
            }
            if (user.playerInteraction() == this) {
                return false;
            }
        }
        for (Podcast podcast : podcasts) {
            if (!podcast.checkIfDelete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Update the wrapped for host, increasing episode plays and users listens
     * @param user -> the name of the user
     * @param episode -> the episode
     */
    public void updateWrapped(final String user, final Episode episode) {
        hostWrapped.incrementListener(user);
        hostWrapped.incrementEpisode(episode.getName());
        hostWrapped.updateListeners();
    }

    /**
     * Adds observer.
     * @param observer -> the observer
     */
    @Override
    public void addObserver(final Observer observer) {
        subscribers.add((User) observer);
    }

    /**
     * Removers observer.
     * @param observer -> the observer.
     */
    @Override
    public void removeObserver(final Observer observer) {
        subscribers.remove((User) observer);
    }

    /**
     * Notifies all observers
     * @param name -> the name of the announcement
     * @param description -> the description of the announcement
     */
    @Override
    public void notifyObservers(final String name, final String description) {
        Notification newNotification = new Notification(name, description);
        for (User user : subscribers) {
            user.update(newNotification);
        }
    }

    /**
     * Checks if user is  subscriber of artist.
     * @param user -> the username
     * @return -> the boolean
     */
    public boolean checkSubscriber(final String user) {
        return subscribers.stream().anyMatch(u -> u.getUsername().equals(user));
    }
}
