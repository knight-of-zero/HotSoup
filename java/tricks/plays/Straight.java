package tricks.plays;

import cards.Card;
import cards.Rank;
import cards.RankComparator;
import cards.Suit;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Set;

/**
 * A single card, or straight.
 *
 */
final class Straight extends Play {

    // ------------------------------------------------------------------------
    // Construction

    /**
     * Constructor. Instantiate through the static factory method.
     */
    private Straight(Collection<? extends Card> playedCards) {
        super(playedCards);
    }

    /**
     * Get a transformer which attempts to convert Collections of Cards into a Straight.
     */
    public static PlayTransformer transformer() {
        return Transformer.INSTANCE;
    }

    // ------------------------------------------------------------------------
    // Implementation

    @Override
    public boolean isLegalGiven(Play lastPlay) {
        return lastPlay instanceof Straight &&
               this.getLowestRank().compareTo(((Straight) lastPlay).getLowestRank()) > 0;
    }

    @Override
    Comparator<Card> newSorter() {
        return RankComparator.create();
    }

    /**
     * Return the length of the straight.
     */
    public int length() {
        return getCards().size();
    }

    /**
     * Get the rank of the lowest played card in the straight.
     */
    public Rank getLowestRank() {
        return getCards().firstEntry().getElement().getRank();
    }

    // ------------------------------------------------------------------------
    // Validation

    /**
     * Returns true if at least two suits are represented by the cards, and false
     * otherwise.
     *
     * This method returns false if any jokers are involved, since those have no suit.
     */
    private static boolean allSameSuit(Iterable<? extends Card> cards) {
        Set<Suit> suits = Sets.newHashSet();
        for (Card card : cards) {
            if (!card.getRank().isSuited()) {
                return false;
            }
            suits.add(card.getSuit());
            if (suits.size() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if these cards have consecutive ranks, and false otherwise.
     */
    private static boolean consecutiveRanks(Iterable<? extends Card> cards) {
        // Make a set out of the ranks, because it's convenient.
        NavigableSet<Rank> ranks = Sets.newTreeSet();
        int numCards = 0;
        for (Card card : cards) {
            ranks.add(card.getRank());
            numCards++;
        }

        // Make sure the cards don't contain any duplicates.
        if (ranks.size() != numCards) {
            return false;
        }

        // Then check to make sure they're all consecutive
        //
        // Nothing above aces are consecutive
        Rank lowestRank = ranks.first();
        if (lowestRank.compareTo(Rank.ACE) > 0) {
            return false;
        }
        int lastRankValue = lowestRank.ordinal();
        for (Rank rank : ranks.tailSet(ranks.first(), false)) {
            if (rank.compareTo(Rank.ACE) > 0) {
                return false;
            }
            // Each card in our sorted set must be one more than the one before it.
            if (lastRankValue + 1 != rank.ordinal()) {
                return false;
            }
            lastRankValue = rank.ordinal();
        }
        return true;
    }

    // ------------------------------------------------------------------------
    // Transformer class

    /**
     * Transformer which attempts to creates new Straights.
     */
    private static class Transformer implements PlayTransformer {

        /**
         * Singleton because we have no state.
         */
        private static final Transformer INSTANCE = new Transformer();

        @Override
        public Straight tryConvert(Collection<? extends Card> played) {
            // Straights must be at least five cards long.
            if (played.size() < 5) {
                return null;
            }

            if (!consecutiveRanks(played)) {
                return null;
            }
            return new Straight(played);
        }
    }
}
