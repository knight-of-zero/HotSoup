package tricks.plays;

import cards.Card;
import cards.Rank;
import cards.RankComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Set;

/**
 * A Play for consecutive three-of-a-kinds
 */
public class Triples extends Play {

    /**
     * Instantiate through the PlayTransformer.
     */
    private Triples(Collection<? extends Card> playedCards) {
        super(playedCards);
    }

    /**
     * Get a transformer which attempts to turn card collections into Triples.
     */
    public static PlayTransformer transformer() {
        return Transformer.INSTANCE;
    }

    @Override
    public boolean isLegalGiven(Play lastPlay) {
        return lastPlay instanceof Triples
                && ((Triples) lastPlay).numCards() == this.numCards()
                && ((Triples) lastPlay).lowestRank().compareTo(this.lowestRank()) < 0;
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
     * Transformer which attempts to turn card collections into Triples.
     */
    private static class Transformer implements PlayTransformer {

        private static final Transformer INSTANCE = new Transformer();

        @Override
        public Play tryConvert(Collection<? extends Card> cards) {
            return areLegalTriplets(cards) ? new Triples(cards) : null;
        }

        /**
         * Return true if the argument cards make up a legal play of consecutive
         * three-of-a-kinds, and false otherwise.
         */
        private static boolean areLegalTriplets(Collection<? extends Card> cards) {
            // Make a map from Rank to the number of cards with that rank.
            NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);

            // Jokers must come in lone triplets
            Set<Rank> ranksPresent = ranks.keySet();
            if (cards.size() == 3 && PlayUtils.allJokers(ranksPresent)) {
                return true;
            }

            // Twos must come in lone triplets
            if (cards.size() == 3 && ranksPresent.size() == 1 && ranksPresent.contains(Rank.TWO)) {
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
            return PlayUtils.areConsecutives(ranks, 3);
        }
    }
}
