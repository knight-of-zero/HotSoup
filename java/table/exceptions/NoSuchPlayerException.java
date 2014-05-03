package table.exceptions;

import table.Player;

/**
 * Exception thrown whenever someone asks the HotSoup about a player which doesn't actually
 * exist in the table.
 */
public class NoSuchPlayerException extends RuntimeException {

    public NoSuchPlayerException(Player player) {
        super(player + " does not exist at this table. Something went really wrong.");
    }
}
