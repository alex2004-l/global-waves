package app.user.utils;

import lombok.Getter;

@Getter
public class Merch {
    private final String name;
    private final String description;
    private final Integer price;
    private Integer noOfItemsBought;

    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
        noOfItemsBought = 0;
    }

    /**
     * Increases number of items bought.
     */
    public void buyMerch() {
        noOfItemsBought++;
    }
}
