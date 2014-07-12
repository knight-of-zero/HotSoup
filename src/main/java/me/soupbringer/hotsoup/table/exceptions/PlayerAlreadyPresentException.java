package me.soupbringer.hotsoup.table.exceptions;

import me.soupbringer.hotsoup.table.Player;
import me.soupbringer.hotsoup.table.Position;

/**
 * Exception thrown whenever someone tries to sit in a position where another
 * player is already located.
 */
public class PlayerAlreadyPresentException extends RuntimeException {

    /**
     * Construct a new exception.
     *
     * @param alreadyPresent Who was already filling the spot where someone tried to sit?
     * @param newlyArrived Who tried to sit at the spot where someone already was?
     * @param position Which position did the conflict occur at?
     */
    public PlayerAlreadyPresentException(Player alreadyPresent, Player newlyArrived, Position position) {
        super(buildMessage(alreadyPresent, newlyArrived, position));
    }

    /**
     * Build a sensible error message using the given information.
     */
    private static String buildMessage(Player alreadyHere, Player newlyArrive, Position position) {
        return new StringBuilder()
          .append(newlyArrive.getName()).append(" tried to sit at position ")
          .append(position.toString()).append(" where ")
          .append(alreadyHere.getName()).append(" already was.")
          .toString();
    }

}
