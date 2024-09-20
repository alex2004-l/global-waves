package app.pageSystem;

public interface Command {
    /**
     * Execute command
     */
    void execute();

    /**
     * Undo command
     */
    void undo();
}
