package app;

import app.audio.Collections.outputFiles.AlbumOutput;
import app.audio.Collections.outputFiles.PlaylistOutput;
import app.audio.Collections.outputFiles.PodcastOutput;
import app.pageSystem.pages.ArtistPage;
import app.pageSystem.pages.HostPage;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.user.UserEntry;
import app.user.monetization.ArtistMonetization;
import app.user.monetization.ArtistMonetizationOutput;
import app.user.utils.Merch;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = ((User) user).search(filters, type);
        String message;
        if (results == null) {
            message = user.getUsername() +  " is offline.";
            results = new ArrayList<>();
        } else {
            message = "Search returned " + results.size() + " results";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).results(results).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());

        String message = ((User) user).select(commandInput.getItemNumber());
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

//        ObjectNode objectNode = objectMapper.createObjectNode();
//        objectNode.put("command", commandInput.getCommand());
//        objectNode.put("user", commandInput.getUsername());
//        objectNode.put("timestamp", commandInput.getTimestamp());
//        objectNode.put("message", result);
//
//        return objectNode;
        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).load();
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).playPause();
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).repeat();

        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = ((User) user).shuffle(seed);
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).forward();
        if (message == null) {
            message = user.getUsername() + " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).backward();
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).like();
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).next();
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).prev();
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).createPlaylist(commandInput.getPlaylistName(),
                                             commandInput.getTimestamp());
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).addRemoveInPlaylist(commandInput.getPlaylistId());
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).switchPlaylistVisibility(commandInput.getPlaylistId());
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = ((User) user).showPlaylists();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).playlists(playlists).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).follow();
        if (message == null) {
            message = user.getUsername() +  " is offline.";
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }



    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        PlayerStats stats = ((User) user).getPlayerStats();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).stats(stats).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = ((User) user).showPreferredSongs();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).result(songs).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = ((User) user).getPreferredGenre();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).preferredGenre(preferredGenre).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .top5(songs).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .top5(playlists).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Gets top 5 albums.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Admin.getTop5Albums();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .top5(albums).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Gets top 5 artists.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> albums = Admin.getTop5Artists();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .top5(albums).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Add user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String message = Admin.addUser(commandInput.getUsername(), commandInput.getAge(),
                commandInput.getCity(), commandInput.getType());

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Delete user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        String message = Admin.deleteUser(commandInput.getUsername());

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Remove album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        String message = Admin.removeAlbum(commandInput.getUsername(), commandInput.getName());

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Remove podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        String message = Admin.removePodcast(commandInput.getUsername(), commandInput.getName());

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Change page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).changePage(commandInput.getNextPage());

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Switch connection status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = "";
        if (user == null) {
            message += "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (user.isHost() || user.isArtist()) {
                message += user.getUsername() + " is not a normal user.";
            } else {
                ((User) user).switchOnlineStatus();
                message += user.getUsername() + " has changed status successfully.";
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return  commandOutput.asObjectNode();
    }

    /**
     * Get online users object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        ArrayList<String> result = Admin.getOnlineUsers();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .result(result).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Get all users object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        ArrayList<String> result = Admin.getAllUsers();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .result(result).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Add album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (!user.isArtist()) {
                message = user.getUsername() + " is not an artist.";
            } else {
                message = ((Artist) user).addAlbum(commandInput.getName(),
                        commandInput.getSongs(), commandInput.getReleaseYear(),
                            commandInput.getDescription());
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();
        return commandOutput.asObjectNode();
    }

    /**
     * Add podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (!user.isHost()) {
                message = user.getUsername() + " is not a host.";
            } else {
                message = ((Host) user).addPodcast(commandInput.getName(),
                        commandInput.getEpisodes(), commandInput.getUsername());
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Add event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (!user.isArtist()) {
                message = user.getUsername() + " is not an artist.";
            } else {
                message = ((Artist) user).addEvent(commandInput.getName(),
                        commandInput.getDescription(), commandInput.getDate());
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Remove event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (!user.isArtist()) {
                message = user.getUsername() + " is not an artist.";
            } else {
                message = ((Artist) user).removeEvent(commandInput.getName());
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Add merch object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (!user.isArtist()) {
                message = user.getUsername() + " is not an artist.";
            } else {
                message = ((Artist) user).addMerchandise(commandInput.getName(),
                            commandInput.getDescription(), commandInput.getPrice());
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Add announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (!user.isHost()) {
                message = user.getUsername() + " is not a host.";
            } else {
                message = ((Host) user).addAnnouncement(commandInput.getName(),
                        commandInput.getDescription());
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Remove announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (!user.isHost()) {
                message = user.getUsername() + " is not a host.";
            } else {
                message = ((Host) user).removeAnnouncement(commandInput.getName());
            }
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Show albums object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        ArrayList<AlbumOutput> albums = ((Artist) user).showAlbums();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).albums(albums).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Show podcasts object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        ArrayList<PodcastOutput> podcasts = ((Host) user).showPodcasts();

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).podcasts(podcasts).build();

        return commandOutput.asObjectNode();
    }

    /**
     * Print current page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message;
        if (!((User) user).getOnlineStatus()) {
            message = user.getUsername() +  " is offline.";
        } else {
            message = ((User) user).printCurrentPage();
        }

        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .pageUser(commandInput.getUsername()).message(message).build();

        return commandOutput.asObjectNode();
    }

    //strategy pattern (?) | visitor pattern

    /**
     * Gets the wrapped statistics for a user entry from the database
     * @param commandInput -> the command input
     * @return wrapped statistics as object node
     */
    public static ObjectNode wrapped(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        if (userEntry.isUser()) {
            User user = (User) userEntry;
            if (!user.getUserWrapped().validWrapped()) {
                return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                        .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                        .message("No data to show for user " + user.getUsername() + ".")
                        .build().asObjectNode();
            }
            CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                    commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                    .user(commandInput.getUsername())
                    .topArtists(user.getUserWrapped().topArtists())
                    .topGenres(user.getUserWrapped().topGenres())
                    .topSongs(user.getUserWrapped().topSongs())
                    .topAlbums(user.getUserWrapped().topAlbums())
                    .topEpisodes(user.getUserWrapped().topEpisodes())
                    .build();
            return commandOutput.asObjectNode();
        }
        if (userEntry.isArtist()) {
            Artist artist = (Artist) userEntry;
            if (!artist.getArtistWrapped().validWrapped()) {
                return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                        .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                        .message("No data to show for artist " + artist.getUsername() + ".")
                        .build().asObjectNode();
            }
            CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                    commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                    .user(commandInput.getUsername())
                    .topSongs(artist.getArtistWrapped().topSongs())
                    .topAlbums(artist.getArtistWrapped().topAlbums())
                    .topFans(artist.getArtistWrapped().topFans())
                    .listeners(artist.getArtistWrapped().getListeners())
                    .build();
            return commandOutput.asObjectNode();
        }
        Host host = (Host) userEntry;
        if (!host.getHostWrapped().validWrapped()) {
            return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                    .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                    .message("No data to show for host " + host.getUsername() + ".")
                    .build().asObjectNode();
        }
        CommandOutput commandOutput = new CommandOutput.CommandOutputBuilder(
                commandInput.getCommand()).timestamp(commandInput.getTimestamp())
                .user(commandInput.getUsername()).topEpisodes(host.getHostWrapped().topEpisodes())
                .listeners(host.getHostWrapped().getListeners())
                .build();
        return commandOutput.asObjectNode();
    }

    /**
     * Buy premium for a user
     * @param commandInput -> the command input
     * @return -> message as object node
     */
    public static ObjectNode buyPremium(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        String message;
        // sa mut din logica in clasa admin ori user
        if (userEntry == null) {
            message = "The username %s doesn't exist.".formatted(commandInput.getUsername());
        } else {
            User user = (User) userEntry;
            if (user.getPremium()) {
                message = "%s is already a premium user.".formatted(commandInput.getUsername());
            } else {
                user.switchPremium();
                message = "%s bought the subscription successfully."
                        .formatted(commandInput.getUsername());
            }
        }
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername()).
                message(message).build().asObjectNode();
    }

    /**
     * Cancel premium subscription for a user
     * @param commandInput -> the command input
     * @return -> message as object node
     */
    public static ObjectNode cancelPremium(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        String message;
        // de adaugat logica in clasa admin (?)
        if (userEntry == null) {
            message = "The username %s doesn't exist.".formatted(commandInput.getUsername());
        } else {
            User user = (User) userEntry;
            if (!user.getPremium()) {
                message = "%s is not a premium user.".formatted(commandInput.getUsername());
            } else {
                user.switchPremium();
                user.updatePremiumMonetization();
                message = "%s cancelled the subscription successfully."
                        .formatted(commandInput.getUsername());
            }
        }
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername()).
                message(message).build().asObjectNode();
    }

    /**
     * Add ad break in the user's queue
     * @param commandInput -> the command input
     * @return -> message as object node
     */
    public static ObjectNode adBreak(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        String message;
        if (userEntry == null) {
            message = "The username %s doesn't exist.".formatted(commandInput.getUsername());
        } else {
            if (((User) userEntry).getPlayerPaused()) {
                message = "%s is not playing any music.".formatted(commandInput.getUsername());
            } else {
                ((User) userEntry).addAdBreak();
                ((User) userEntry).setAdRevenue(commandInput.getPrice().doubleValue());
                message = "Ad inserted successfully.";
            }
        }
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .message(message).build().asObjectNode();
    }

    /**
     * Subscribes user to content creator
     * @param commandInput -> the command input
     * @return -> the object node
     */
    public static ObjectNode subscribe(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        String message;
        if (userEntry == null) {
            message = "The username %s doesn't exist.".formatted(commandInput.getUsername());
        } else {
            User user = (User) userEntry;
            if (user.getCurrentPage().isArtistPage()) {
                Artist artist = ((ArtistPage) user.getCurrentPage()).getArtist();
                if (artist.checkSubscriber(user.getUsername())) {
                    artist.removeObserver(user);
                    message = "%s unsubscribed from %s successfully."
                            .formatted(user.getUsername(), artist.getUsername());
                } else {
                    artist.addObserver(user);
                    message = "%s subscribed to %s successfully."
                            .formatted(user.getUsername(), artist.getUsername());
                }
            } else if (user.getCurrentPage().isHostPage()) {
                Host host = ((HostPage) user.getCurrentPage()).getHost();
                if (host.checkSubscriber(user.getUsername())) {
                    host.removeObserver(user);
                    message = "%s unsubscribed from %s successfully."
                            .formatted(user.getUsername(), host.getUsername());
                } else {
                    host.addObserver(user);
                    message = "%s subscribed to %s successfully."
                            .formatted(user.getUsername(), host.getUsername());
                }
            } else {
                message = "To subscribe you need to be on the page of an artist or host.";
            }
        }
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .message(message).build().asObjectNode();
    }

    /**
     * Gets notifications for user
     * @param commandInput -> the command input
     * @return -> the object node
     */
    public static ObjectNode getNotifications(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());

        CommandOutput commandOutput = new CommandOutput
                .CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .notifications(((User) userEntry).getNotifications()).build();
        ((User) userEntry).resetNotifications();
        return commandOutput.asObjectNode();
    }

    /**
     * Buy merch
     * @param commandInput
     * @return
     */
    public static ObjectNode buyMerch(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        String message;
        if (userEntry == null) {
            message = "The username %s doesn't exist.".formatted(commandInput.getUsername());
        } else {
            if (!((User) userEntry).getCurrentPage().isArtistPage()) {
                message = "Cannot buy merch from this page.";
            } else {
                Artist artist = ((ArtistPage) (((User) userEntry).getCurrentPage())).getArtist();
                message = "The merch %s doesn't exist.".formatted(commandInput.getName());
                for (Merch merch : artist.getMerchandise()) {
                    if (merch.getName().equals(commandInput.getName())) {
                        if (!Admin.artistMonetizationExists(artist.getName())) {
                            Admin.addArtistMonetization(artist);
                        }
                        ((User) userEntry).addMerch(merch.getName());
                        merch.buyMerch();
                        message = "%s has added new merch successfully."
                                .formatted(commandInput.getUsername());
                        break;
                    }
                }
            }
        }
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .message(message).build().asObjectNode();
    }

    /**
     * Prints merch for current user
     * @param commandInput -> the command input
     * @return -> the object node
     */
    public static ObjectNode seeMerch(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        if (userEntry == null) {
            String message = "The username %s doesn't exist.".formatted(commandInput.getUsername());
            return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                    .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                    .message(message).build().asObjectNode();
        } else {
            User user = (User) userEntry;
            return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                    .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                    .result(user.getMerch()).build().asObjectNode();
        }
    }

    /**
     * Sets current page to next page
     * @param commandInput -> the command input
     * @return -> the object node
     */
    public static ObjectNode nextPage(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).nextPage();
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .message(message).build().asObjectNode();
    }

    /**
     * Sets current page to previous page
     * @param commandInput -> the command input
     * @return -> the object node
     */
    public static ObjectNode previousPage(final CommandInput commandInput) {
        UserEntry user = Admin.getUser(commandInput.getUsername());
        String message = ((User) user).prevPage();
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .message(message).build().asObjectNode();
    }

    /**
     * Update user recommendations
     * @param commandInput -> the command input
     * @return -> object node
     */
    public static ObjectNode updateRecommendations(final CommandInput commandInput) {
        UserEntry userEntry = Admin.getUser(commandInput.getUsername());
        String message;
        if (userEntry == null) {
            message = "The username %s doesn't exist.".formatted(commandInput.getUsername());
        } else if (!userEntry.isUser()) {
            message = "%s is not a normal user.".formatted(commandInput.getUsername());
        } else {
            User user = (User) userEntry;
            switch (commandInput.getRecommendationType()) {
                case "random_song":
                    message = user.addGenreRec();
                    break;
                case "random_playlist":
                    message = user.addPlaylistRec();
                    break;
                case "fans_playlist":
                    message = user.addFansPlaylistRec();
                    break;
                default:
                    message = "No new recommendations were found";
            }
        }
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .message(message).build().asObjectNode();
    }

    public static ObjectNode loadRecommendations(final CommandInput commandInput) {
        User user = (User) Admin.getUser(commandInput.getUsername());
        String message = user.loadRecommendations();
        return new CommandOutput.CommandOutputBuilder(commandInput.getCommand())
                .timestamp(commandInput.getTimestamp()).user(commandInput.getUsername())
                .message(message).build().asObjectNode();
    }

    /**
     * Method called at the end of the program for providing statistics about the artists
     * @return -> endProgram as object node
     */
    public static ObjectNode endProgram() {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "endProgram");
        ObjectNode objectNode1 = objectMapper.createObjectNode();
        List<ArtistMonetization> artistMonetizations = Admin.getArtistMonetizations();

        for (ArtistMonetization artistMonetization : artistMonetizations) {
            objectNode1.put(artistMonetization.getArtist(),
                    objectMapper.valueToTree(new ArtistMonetizationOutput(artistMonetization)));
        }

        objectNode.put("result", objectMapper.valueToTree(objectNode1));
        return objectNode;
    }

}
