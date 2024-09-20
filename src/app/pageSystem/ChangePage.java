package app.pageSystem;

import app.pageSystem.pages.Page;

public class ChangePage implements Command {
    private PageSystem pageSystem;
    private Page prevPage;
    private Page nextPage;

    public ChangePage(final PageSystem pageSystem, final Page page) {
        this.pageSystem = pageSystem;
        this.nextPage = page;
    }

    /**
     * Execute the command
     */
    @Override
    public void execute() {
        prevPage = pageSystem.getCurrentPage();
        pageSystem.setCurrentPage(nextPage);
    }

    /**
     * Undo the command
     */
    @Override
    public void undo() {
        nextPage = prevPage;
        prevPage = pageSystem.getCurrentPage();
        pageSystem.setCurrentPage(nextPage);
        nextPage = prevPage;
    }
}
