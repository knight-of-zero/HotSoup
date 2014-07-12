package me.soupbringer.hotsoup.table;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import me.soupbringer.hotsoup.deck.Card;
import me.soupbringer.hotsoup.table.exceptions.PlayerAlreadyPresentException;
import me.soupbringer.hotsoup.table.exceptions.UnexpectedPassException;
import me.soupbringer.hotsoup.tricks.plays.Play;

/**
 * A game of HotSoup, with all the mutators necessary to move the game state
 * along.
 */
public final class HotSoup {

    // ------------------------------------------------------------------------
    // Static references

    /**
     * The current active HotSoup game.
     */
    private static AtomicReference<HotSoup> REFERENCE =
      new AtomicReference<HotSoup>(new HotSoup());

    // ------------------------------------------------------------------------
    // Member variables

    private final BiMap<Position, Player> playerPositions;

    // ------------------------------------------------------------------------
    // Construction

    /**
     * Default constructor.
     */
    private HotSoup() {
        this.playerPositions = HashBiMap.create();
    }

    /**
     * Get the active HotSoup game. This is non-null.
     */
    public static synchronized HotSoup getActiveGame() {
        return REFERENCE.get();
    }

    /**
     * Start a new HotSoup game, and return it.
     */
    public static synchronized HotSoup newGame() {
        HotSoup newGame = new HotSoup();
        REFERENCE.set(newGame);
        return newGame;
    }

    // ------------------------------------------------------------------------
    // Game actions

    /**
     * Seat a player in a given position. If player is null, we will evict whoever's
     * currently sitting there, and ensure that the spot is empty.
     *
     * Throws a PlayerAlreadyPresentException if that seat has already been taken.
     * Throws a NullPointerException if position is null.
     */
    public void seatPlayer(Player player, Position position) {
        Preconditions.checkNotNull(position, "This shouldn't be null.");

        // If we're vacating the seat, then vacate it.
        if (player == null) {
            playerPositions.put(position, null);
        }
        // Otherwise, someone is trying to sit down.
        else {
            Player current = playerPositions.get(position);
            // Make sure that nobody's sitting there yet.
            if (current != null) {
                throw new PlayerAlreadyPresentException(current, player, position);
            }
            // And if it's safe, seat this new person.
            else {
                playerPositions.put(position, player);
            }
        }
    }

    /**
     * Attempt to play the given cards.
     *
     * If the given set of Cards form a valid Play, the gamestate will be
     * updated and true will be returned.
     *
     * If the given cards *can't* form a valid Play, our state will be unchanged
     * and false will be returned.
     *
     * This takes into account the previous plays
     */
    public boolean makePlay(Collection<? extends Card> play) {
        Collection<Play> validPlays = Play.all(play);
        if (validPlays.isEmpty()) {
            return false;
        }

        // TODO: Update our gamestate

        return true;
    }

    /**
     * Call this instead of makePlay if the active player decided to pass.
     *
     * This method returns true if the current trick will continue after this
     * user's pass, and false if the trick should end and a new one begins.
     *
     * We throw an UnexpectedPassException if the game-state thinks it's
     * someone's lead, but they decided to pass anyway.
     */
    public boolean pass() throws UnexpectedPassException {
       // TODO: implement

       return true;
    }

    // ------------------------------------------------------------------------
    // Queries

    /**
     * Returns true if the seat at the given position is vacant, and false if
     * someone is currently sitting there.
     */
    public boolean isVacant(Position position) {
        return playerPositions.get(position) == null;
    }

    /**
     * Returns true if the given player is in this game, or false otherwise.
     */
    public boolean includesPlayer(Player player) {
        return playerPositions.values().contains(player);
    }

    /**
     * Get the cards available to the current player. If the player isn't active
     * in the current game, an empty list is returned.
     */
    public Collection<Card> getCards(Player player) {
        // TODO: implement
        return ImmutableList.of();
    }

    /**
     * Get the vacant positions in this game.
     */
    public Set<Position> getVacantPositions() {
        return Sets.difference(EnumSet.allOf(Position.class), playerPositions.keySet());
    }

    /**
     * Get a map from position to the name of the player sitting at that position.
     * The name will be null if no player is currently seated at a given position.
     */
    public Map<Position, String> getSeatedPlayers() {
        return Maps.transformValues(playerPositions, Player.TO_NAME);
    }

    /**
     * Get the number of cards left at each position. If we're in between
     * active hands, all values in the map will be 0.
     */
    public Map<Position, Integer> getRemainingCardsMap() {
        // TODO: implement
        return ImmutableMap.of();
    }

    /**
     * Get the score of the north/south team.
     */
    public int scoreNS() {
        return 0; // TODO: implement
    }

    /**
     * Get the score of the east/west team.
     */
    public int scoreEW() {
        return 0; // TODO: implement
    }

    /**
     * Get the location where this player sits.
     */
    public Position locationOf(Player player) {
        return playerPositions.inverse().get(player);
    }

    /**
     * Get the cards played so far in the current trick. Returns an empty list if
     * we're waiting on the first player to lead.
     */
    public List<Card> trickSoFar() {
        // TOOD: implement
        return ImmutableList.of();
    }
}
