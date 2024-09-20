package app.pageSystem;

import app.pageSystem.pages.HomePage;
import app.pageSystem.pages.LikedContentPage;
import app.pageSystem.pages.Page;
import app.user.User;
import lombok.Getter;

public class PageSystem {
    private User user;
    @Getter
    private Page currentPage;
    @Getter
    private final HomePage homePage;
    @Getter
    private final LikedContentPage likedContentPage;
    public PageSystem(final User user) {
        this.user = user;
        homePage = new HomePage(user);
        likedContentPage = new LikedContentPage(user);
        currentPage = homePage;
    }

    /**
     * Sets current page
     * @param currentPage -> the new page
     */
    public void setCurrentPage(final Page currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Displays page
     * @return -> the page as a string
     */
    public String displayPage() {
        return currentPage.display();
    }

}
