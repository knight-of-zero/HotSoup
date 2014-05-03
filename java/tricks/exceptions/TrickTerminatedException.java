package tricks.exceptions;

/**
 * Exception thrown whenever we try to add a new Play to a TrickBuilder
 * *after* three consecutive passes. The Trick should have ended by then,
 * according to the table's rules, so this shouldn't happen.
 */
public class TrickTerminatedException extends RuntimeException {

    public TrickTerminatedException() {
        super("Attempted Play after 3 consecutive passes. This isn't legal.");
    }
}
