package app.pageSystem;

import java.util.LinkedList;

public class PageSystemInvoker {
    private LinkedList<Command> history = new LinkedList<>();
    private LinkedList<Command> undoHistory = new LinkedList<>();

    /**
     * Restarts invoker
     */
    public void restart() {
        history = new LinkedList<>();
        undoHistory = new LinkedList<>();
    }

    /**
     * Executes command
     * @param command -> the command
     */
    public void execute(final Command command) {
        history.push(command);
        command.execute();
        undoHistory = new LinkedList<>();
    }

    /**
     * Undos the command
     * @return -> true, if the command can be reversed, false otherwise
     */
    public boolean undo() {
        if (history.isEmpty()) {
            return false;
        }

        Command command = history.pop();
        if (command != null) {
            command.undo();
            undoHistory.push(command);
        }
        return true;
    }

    /**
     * Redos the command
     * @return -> true, if the command can be reversed, false otherwise
     */
    public boolean redo() {
        if (undoHistory.isEmpty()) {
            return false;
        }

        Command command = undoHistory.pop();
        if (command != null) {
            command.execute();
            history.push(command);
        }
        return true;
    }
}
