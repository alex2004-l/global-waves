package app.user.monetization;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.user.Artist;
import app.user.utils.Merch;
import lombok.Getter;

public class ArtistMonetization {
    @Getter
    private Double merchRevenue;
    @Getter
    private Double songRevenue;
    @Getter
    private Double totalRevenue;
    @Getter
    private String mostProfitableSong;
    @Getter
    private Integer ranking;
    private Double mostProfitableSongProfit;
    @Getter
    private String artist;
    private static final  Double ONE_HUNDRED = 100.0;

    public ArtistMonetization(final String artist) {
        merchRevenue = 0.0;
        songRevenue = 0.0;
        totalRevenue = 0.0;
        this.ranking = 0;
        mostProfitableSong = "N/A";
        mostProfitableSongProfit = 0.0;
        this.artist = artist;
    }

    /**
     * Increase the total merch revenue of the artist with price
     * @param price -> the merch price
     */
    public void increaseMerchRevenue(final Double price) {
        merchRevenue += price;
    }

    /**
     * Increase the total song revenue with revenue
     * @param revenue -> the song revenue
     */
    public void increaseSongRevenue(final Double revenue) {
        songRevenue += revenue;
    }

    /**
     * Update the stats for artist monetization
     */
    public void updateArtistMonetization() {
        updateTotalRevenue();
        updateMostProfitableSong();
    }

    /**
     * Update the total revenue field
     */
    private void updateTotalRevenue() {
        totalRevenue = updateSongRevenue() + updateMerchRevenue();
    }

    /**
     * Update the total song revenue for the artist
     * @return total song revenue
     */
    private Double updateSongRevenue() {
        double revenue = 0.0;
        Artist currentArtist = (Artist) Admin.getUser(this.artist);
        for (Album album : currentArtist.getAlbums()) {
            for (Song song: album.getSongs()) {
                revenue += song.getRevenue();
            }
        }
        this.songRevenue = revenue;
        return revenue;
    }

    /**
     * Update the total merch revenue for the artist
     * @return the merch revenue
     */
    private Double updateMerchRevenue() {
        double revenue = 0.0;
        Artist currentArtist = (Artist) Admin.getUser(this.artist);
        for (Merch merch : currentArtist.getMerchandise()) {
            revenue += merch.getPrice() * merch.getNoOfItemsBought();
        }
        this.merchRevenue = revenue;
        return revenue;
    }

    /**
     * Update the most profitable song for the artist
     */
    private void updateMostProfitableSong() {
        Double maxSongRevenue = 0.0;
        String songName = "N/A";

        Artist currentArtist = (Artist) Admin.getUser(this.artist);
        for (Album album : currentArtist.getAlbums()) {
            for (Song song: album.getSongs()) {
                if (song.getRevenue() > maxSongRevenue
                        || (!song.getRevenue().equals(0.0)
                        && song.getRevenue().equals(maxSongRevenue)
                        && song.getName().compareTo(songName) < 0)) {
                    maxSongRevenue = song.getRevenue();
                    songName = song.getName();
                }
            }
        }
        this.mostProfitableSong = songName;
        this.mostProfitableSongProfit = maxSongRevenue;
    }

    /**
     * Update the artist's ranking
     * @param ranking -> new ranking
     */
    public void setRanking(final Integer ranking) {
        this.ranking = ranking;
    }
}
