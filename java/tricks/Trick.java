package tricks;

import cards.Card;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import tricks.exceptions.IllegalPlayException;
import tricks.exceptions.TrickTerminatedException;
import tricks.plays.Play;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * A class which stores all the plays from a given hand
 *
 * This is essentially a list of Plays, in the order in which they occurred.
 */
public class Trick implements Iterable<Play> {

    // ------------------------------------------------------------------------
    // Member variables

    /**
     * The plays, in the order in which they occurred.
     */
    private final ImmutableList<Play> plays;

    // ------------------------------------------------------------------------
    // Construction

    /**
     * Constructor. Instantiate via the builder instead.
     */
    private Trick(Iterator<Play> plays) {
        this.plays = ImmutableList.copyOf(plays);
    }

    /**
     * Create a new TrickBuilder.
     */
    public static TrickBuilder builder() {
      return new TrickBuilder();
    }

    // ------------------------------------------------------------------------
    // Getters

    /**
     * Get the number of points which were contained within this Trick.
     */
    public int getNumPoints() {
        return countNumPoints(plays);
    }

    // ------------------------------------------------------------------------
    // Implementation

    @Override
    public Iterator<Play> iterator() {
        return plays.iterator();
    }

    // ------------------------------------------------------------------------
    // Builder

    /**
     * A Builder for a Trick. All the mutation must happen here. After a full
     * Trick has been built, it's immutable.
     */
    public static final class TrickBuilder {

        private final Deque<Play> playsSoFar;

        private int numConsecutiveNulls;

        /**
         * Instantiate via the static builder() method.
         */
        private TrickBuilder() {
            this.playsSoFar = Lists.newLinkedList();
            this.numConsecutiveNulls = 0;
        }

        /**
         * Return true if the given play is legal, given what has come before it,
         * and false otherwise.
         */
        public boolean isLegalNext(Play play) {
            return play.isLegalGiven(playsSoFar.peek());
        }

        /**
         * Add the given Play to the trick. If the player passed, this
         * should be called with 'null'.
         *
         * If this play isn't legal given the last one, this throws an IllegalPlayException.
         * If this is called a fourth time after 3 consecutive 'null's, a
         * TrickTerminatedException will be thrown.
         */
        public TrickBuilder add(Play play) {
            if (numConsecutiveNulls == 3) {
                throw new TrickTerminatedException();
            }
            if (play.isLegalGiven(playsSoFar.peek())) {
                playsSoFar.push(play);
                numConsecutiveNulls = play == null ? numConsecutiveNulls + 1 : 0;
                return this;
            }
            else {
                throw new IllegalPlayException(playsSoFar.peek(), play);
            }
        }

        /**
         * Get the number of points which are contained within this TrickBuilder so far.
         */
        public int getNumPoints() {
           return countNumPoints(playsSoFar);
        }

        /**
         * Complete the trick, and get back an immutable object with all the information.
         * This should be called after 3 consecutive passes.
         * @return
         */
        public Trick build() {
            return new Trick(playsSoFar.descendingIterator());
        }

    }

    /**
     * Count the number of points in a bunch of Plays.
     */
    private static int countNumPoints(Iterable<? extends Play> plays) {
        int points = 0;
        for (Play play : plays) {
            for (Card card : play.getCards()) {
                points += card.getNumPoints();
            }
        }
        return points;
    }
}
