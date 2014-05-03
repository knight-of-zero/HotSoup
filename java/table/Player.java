package table;

import com.google.common.base.Function;

/**
 * A single Player in this table.
 */
public final class Player {

    /**
     * Function which translates Players to their names.
     */
    public static Function<Player, String> TO_NAME = new Function<Player, String>() {
        @Override
        public String apply(Player player) {
            return player == null ? null : player.getName();
        }
    };

    private final String name;
    private final String id;

    /**
     * Instantiate using the factory method.
     */
    private Player(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Create a new Player with the given name/id.
     */
    public static Player create(String name, String id) {
        return new Player(name, id);
    }

    // ------------------------------------------------------------------------
    // Getters

    /**
     * Get the name of this Player.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the id for this player.
     */
    public String getId() {
        return id;
    }

}
