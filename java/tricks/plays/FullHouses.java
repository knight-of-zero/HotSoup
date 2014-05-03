package tricks.plays;

import cards.Card;
import cards.Rank;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;

/**
 * The Play for one or more consecutive full houses.
 */
public class FullHouses extends Play {

    /**
     * Construct through the Transformer.
     */
    private FullHouses(Collection<? extends Card> cards) {
        super(cards);
    }

    /**
     * Get a transformer which attempts to turn card collections into Triples.
     */
    public static PlayTransformer transformer() {
        return Transformer.INSTANCE;
    }


    @Override
    public boolean isLegalGiven(Play lastPlay) {
        return lastPlay instanceof FullHouses &&
                ((FullHouses) lastPlay).numCards() == this.numCards() &&
                ((FullHouses) lastPlay).lowestRank().compareTo(this.lowestRank()) < 0;
    }

    // ------------------------------------------------------------------------
    // Queries

    /**
     * The number of cards in this play.
     */
    private int numCards() {
        return getCards().size();
    }

    /**
     * Get the lowest rank in this Play.
     */
    private Rank lowestRank() {
        return getCards().firstEntry().getElement().getRank();
    }

    @Override
    Comparator<Card> newSorter() {
        final NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(getCards());
        return new Comparator<Card>() {
            @Override
            public int compare(Card card, Card card2) {
                int retVal = ranks.get(card.getRank()) - ranks.get(card2.getRank());
                return retVal == 0
                        ? card2.getRank().compareTo(card.getRank())
                        : retVal;
            }
        };
    }

    // ------------------------------------------------------------------------
    // Transformer

    /**
     * Transformer which attempts to convert collections of cards into FullHouses
     */
    private static final class Transformer implements PlayTransformer {

        private static final Transformer INSTANCE = new Transformer();


        @Override
        public Play tryConvert(Collection<? extends Card> cards) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
