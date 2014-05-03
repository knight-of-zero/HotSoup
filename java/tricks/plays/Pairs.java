package tricks.plays;

import cards.Card;
import cards.Rank;
import cards.RankComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Set;

/**
 * Some number of consecutive pairs of cards.
 */
final class Pairs extends Play {

    // ------------------------------------------------------------------------
    // Construction

    /**
     * Construct through the transformer.
     */
    private Pairs(Collection<? extends Card> playedCards) {
        super(playedCards);
    }

    /**
     * Get a Transformer which attempts to make Pairs out of the given cards.
     */
    static PlayTransformer transformer() {
        return Transformer.INSTANCE;
    }

    // ------------------------------------------------------------------------
    // Implementation

    @Override
    public boolean isLegalGiven(Play lastPlay) {
        return lastPlay instanceof Pairs
                && ((Pairs) lastPlay).numCards() == this.numCards()
                && ((Pairs) lastPlay).lowestRank().compareTo(this.lowestRank()) < 0;
    }

    @Override
    Comparator<Card> newSorter() {
        return RankComparator.create();
    }

    // ------------------------------------------------------------------------
    // Queries

    /**
     * Get the number of cards in this Pairs.
     */
    private int numCards() {
        return getCards().size();
    }

    /**
     * Get the lowest ranked card in these Pairs.
     */
    private Rank lowestRank() {
        return getCards().firstEntry().getElement().getRank();
    }

    // ------------------------------------------------------------------------
    // Transformer

    /**
     * Transformer which tries to create Pairs out of the cards.
     */
    private static class Transformer implements PlayTransformer {

        /**
         * Singletons are ok when we have no state.
         */
        private static final Transformer INSTANCE = new Transformer();

        @Override
        public Play tryConvert(Collection<? extends Card> cards) {
            return isLegalPairs(cards) ? new Pairs(cards) : null;
        }
    }

    /**
     * Return true if these cards are a legal set of pairs, and false otherwise.
     */
    private static boolean isLegalPairs(Collection<? extends Card> cards) {
        // Make a map from Rank to the number of cards with that rank.
        NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);

        // Pairs of jokers mix & match... also JOKER BOMB!
        Set<Rank> ranksPresent = ranks.keySet();
        if ((cards.size() == 2 || cards.size() == 4) && PlayUtils.allJokers(ranksPresent)) {
            return true;
        }

        // Lone pairs of twos are also ok.
        if (cards.size() == 2 && ranksPresent.size() == 1 && ranksPresent.contains(Rank.TWO)) {
            return true;
        }

        // Otherwise, twos and jokers aren't allowed
        if (ranksPresent.contains(Rank.TWO) ||
            ranksPresent.contains(Rank.LITTLE_JOKER) ||
            ranksPresent.contains(Rank.BIG_JOKER))
        {
            return false;
        }

        // Finally, loop through our map and make sure that we actually have
        // consecutive pairs
        return PlayUtils.areConsecutives(ranks, 2);
    }

}
