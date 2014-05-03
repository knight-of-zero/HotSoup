package tricks.plays;

import cards.Card;
import cards.RankComparator;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Comparator;

/**
 * Play representing a single card.
 */
class Single extends Play {

    private Single(Card card) {
        super(Lists.newArrayList(card));
    }

    static PlayTransformer transformer() {
        return Transformer.INSTANCE;
    }

    @Override
    public boolean isLegalGiven(Play lastPlay) {
        return lastPlay instanceof Single &&
               ((Single) lastPlay).getCard().getRank().compareTo(this.getCard().getRank()) < 0;
    }

    @Override
    Comparator<Card> newSorter() {
        return RankComparator.create();
    }

    /**
     * Get the lone card involved in this Play.
     */
    private Card getCard() {
        return getCards().firstEntry().getElement();
    }

    /**
     * Transformer which turns cards into Single plays.
     */
    private static final class Transformer implements PlayTransformer {

        /**
         * Singleton because no state.
         */
        private static final Transformer INSTANCE = new Transformer();

        @Override
        public Play tryConvert(Collection<? extends Card> cards) {
            // TODO: We can probably make a Map<Card, Single> and cache instances here
            return cards.size() == 1 ? new Single(cards.iterator().next()) : null;
        }
    }
}
