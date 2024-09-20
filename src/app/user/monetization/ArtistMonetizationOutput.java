package app.user.monetization;

import lombok.Getter;

@Getter
public class ArtistMonetizationOutput {
    private Double merchRevenue;
    private Double songRevenue;
    private Integer ranking;
    private String mostProfitableSong;
    private static final Double ONE_HUNDRED = 100.0;

    public ArtistMonetizationOutput(final ArtistMonetization artistMonetization) {
        this.merchRevenue = Math
                .round(artistMonetization.getMerchRevenue() * ONE_HUNDRED) / ONE_HUNDRED;
        this.songRevenue = Math
                .round(artistMonetization.getSongRevenue() * ONE_HUNDRED) / ONE_HUNDRED;
        this.ranking = artistMonetization.getRanking();
        this.mostProfitableSong = artistMonetization.getMostProfitableSong();
    }
}
