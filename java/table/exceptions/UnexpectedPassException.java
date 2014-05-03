package table.exceptions;

import table.Player;

/**
 * Exception used whenever a user decides to pass, but a pass was illegal
 * at that point in the game.
 *
 * This should only happen if someone tries to pass when it's their lead.
 */
public class UnexpectedPassException extends Exception {

    public UnexpectedPassException(Player whoPassed) {
        super(buildMessage(whoPassed));
    }

    /**
     * Build an error message with more useful debugging information about
     * who passed when they shouldn't have.
     */
    private static String buildMessage(Player whoPassed) {
        return new StringBuilder()
                .append(whoPassed)
                .append(" attempted to pass, but we expected it to be their lead.")
                .toString();
    }
}
