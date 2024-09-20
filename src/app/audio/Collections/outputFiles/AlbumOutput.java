package app.audio.Collections.outputFiles;

import app.audio.Collections.Album;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class AlbumOutput {
    private final String name;
    private final ArrayList<String> songs;

    public AlbumOutput(final Album album) {
        this.name = album.getName();
        this.songs = new ArrayList<>();
        for (Song song : album.getSongs()) {
            this.songs.add(song.getName());
        }
    }
}
