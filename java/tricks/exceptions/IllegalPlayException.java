package tricks.exceptions;

import tricks.plays.Play;

/**
 * Exception thrown whenever someone tries to add an illegal 'play'
 * to a trick.
 */
public class IllegalPlayException extends RuntimeException {

    public IllegalPlayException(Play lastPlay, Play thisPlay) {
        super(buildMessage(lastPlay, thisPlay));

    }

    /**
     * Build an error message explaining why the given play isn't legal.
     *
     * @param lastPlay The last legal play in the trick.
     * @param thisPlay The play which this current person is trying to make.
     * @return A string error message with details about the error.
     */
    private static String buildMessage(Play lastPlay, Play thisPlay) {
        return new StringBuilder()
                .append("Attempted to play ").append(thisPlay)
                .append(" after the previous person played ").append(lastPlay)
                .append(". This isn't a legal move.")
                .toString();
    }

}
