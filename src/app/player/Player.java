package app.player;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.User;
import app.user.UserEntry;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Player.
 */
public final class Player {
    private Enums.RepeatMode repeatMode;
    private boolean shuffle;
    private boolean paused;
    // sa nu uit sa sterg getter-ul
    @Getter
    private PlayerSource source;
    @Getter
    private String type;
    private final int skipTime = 90;

    private ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();

    private static final Song AD_BREAK = Admin.getSongs().get(0);
    private boolean adBreakCheck = false;

    private User owner;
    private ArrayList<AudioFile> playerHistory = new ArrayList<>();



    /**
     * Instantiates a new Player.
     */
    public Player(final User user) {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
        owner = user;
    }

    /**
     * Stop.
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        if (source != null) {
            playerHistory.addAll(source.getPlayHistory());
        }
        source = null;
        shuffle = false;
    }

    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark =
                    new PodcastBookmark(source.getAudioCollection().getName(),
                                        source.getIndex(),
                                        source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }

    /**
     * Create source player source.
     *
     * @param type      the type
     * @param entry     the entry
     * @param bookmarks the bookmarks
     * @return the player source
     */
    public static PlayerSource createSource(final String type,
                                            final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        } else if ("album".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.ALBUM, (AudioCollection) entry);
        }

        return null;
    }

    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }

    /**
     * Sets source.
     *
     * @param entry the entry
     * @param sourceType  the sourceType
     */
    public void setSource(final LibraryEntry entry, final String sourceType) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = sourceType;
        this.source = createSource(sourceType, entry, bookmarks);
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
    }

    /**
     * Pause.
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * Shuffle.
     *
     * @param seed the seed
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST
                || source.getType() == Enums.PlayerSourceType.ALBUM) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }

    /**
     * Repeat enums . repeat mode.
     *
     * @return the enums . repeat mode
     */
    public Enums.RepeatMode repeat() {
        if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
            if (source.getType() == Enums.PlayerSourceType.LIBRARY) {
                repeatMode = Enums.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Enums.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                    repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Enums.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }

    /**
     * Simulate player.
     *
     * @param time the time
     */
    public void simulatePlayer(final int time) {
        int elapsedTime = time;
        if (!paused) {
            while (elapsedTime >= source.getDuration()) {
                elapsedTime -= source.getDuration();
                next();
                if (paused) {
                    break;
                }
            }
            if (!paused) {
                source.skip(-elapsedTime);
            }
        }
    }

    /**
     * Sets add break flag
     */
    public void addAdBreak() {
        adBreakCheck = true;
    }

    /**
     * Unsets add break flag
     */
    public void resetAdBreak() {
        adBreakCheck = false;
    }

    /**
     * Clears player history
     */
    public void clearPlayerHistory() {
        playerHistory = new ArrayList<>();
        if (source != null) {
            source.resetHistory();
        }
    }

    /**
     * Gets player history
     * @return -> the player history
     */
    public ArrayList<AudioFile> getPlayerHistory() {
        ArrayList<AudioFile> result = new ArrayList<>(playerHistory);
        if (source != null) {
            result.addAll(source.getPlayHistory());
        }
        return result;
    }

    /**
     * Next.
     */
    public void next() {
        if (adBreakCheck) {
            source.setAudioFile(AD_BREAK);
            owner.updateFreeMonetization();
            resetAdBreak();
            return;
        }
        paused = source.setNextAudioFile(repeatMode, shuffle);
        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }

    /**
     * Prev.
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }

    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }

    /**
     * Skip next.
     */
    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-skipTime);
        }
    }

    /**
     * Skip prev.
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(skipTime);
        }
    }

    /**
     * Gets current audio file.
     *
     * @return the current audio file
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }
        return source.getAudioFile();
    }

    /**
     * Gets paused.
     *
     * @return the paused
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * Gets shuffle.
     *
     * @return the shuffle
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Gets stats.
     *
     * @return the stats
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }

    /**
     * special user instance that interacts with the player
     * @return host or artist
     */
    public UserEntry getUserInteraction() {
        if (source == null || this.type == null) {
            return null;
        }
        return switch (this.type) {
            case "album", "podcast" -> Admin.getUser((source.getAudioCollection()).getOwner());
            case "playlist", "song" -> Admin.getUser(((Song) (source.getAudioFile())).getArtist());
            default -> null;
        };
    }

    /**
     * album file that interacts with the player
     * @return album
     */
    public Album getAlbumInteraction() {
        if (source == null || this.type == null) {
            return null;
        }
        if ("album".equals(this.type)) {
            return (Album) source.getAudioCollection();
        }
        if ("song".equals(this.type) || "playlist".equals(this.type)) {
            return Admin.getAlbum(source.getAudioFile().getName(),
                    ((Song) source.getAudioFile()).getArtist());
        }
        return null;
    }

    /**
     * podcast file that interacts with the player
     * @return podcast
     */
    public Podcast getPodcastInteraction() {
        if (source == null || this.type == null) {
            return null;
        }
        if ("podcast".equals(this.type)) {
            return (Podcast) source.getAudioCollection();
        }
        return null;
    }

    /**
     * playlist file that interacts with the player
     * @return playlist
     */
    public Playlist getPlaylistsInteraction() {
        if (source == null || this.type == null) {
            return null;
        }
        if ("playlist".equals(this.type)) {
            return (Playlist) source.getAudioCollection();
        }
        return null;
    }

    /**
     * Returns genre of current audiofile
     * @return -> the genre
     */
    public String getGenreRec() {
        if (source == null || this.type == null || this.type.equals("podcast")) {
            return null;
        }
        return ((Song) source.getAudioFile()).getGenre();
    }

    /**
     * Gets time the current audiofile played
     * @return -> the time
     */
    public Integer getTimeListened() {
        return source.getAudioFile().getDuration() - source.getRemainedDuration();
    }
}
