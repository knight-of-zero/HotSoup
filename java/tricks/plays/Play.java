package tricks.plays;

import cards.Card;
import cards.RankComparator;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A single person's play. This is essentially a set of cards that one person
 * decided to play together.
 */
public abstract class Play {

    /**
     * Static list of all our known transformers. Each time a list of cards gets
     * given to us, we'll run through these and try to use each one to create a
     * valid Play.
     */
    private static final List<PlayTransformer> ALL_TRANSFORMERS = makeTransformerList();

    // ------------------------------------------------------------------------
    // Member variables

    private final ImmutableSortedMultiset<Card> playedCards;

    // ------------------------------------------------------------------------
    // Construction

    /**
     * Instantiate through the static factory method.
     *
     * No instantiation outside of this package.
     */
    Play(Collection<? extends Card> playedCards) {
        ImmutableSortedMultiset.Builder<Card> copiedCards =
            ImmutableSortedMultiset.orderedBy(RankComparator.create());
        copiedCards.addAll(playedCards);
        this.playedCards = copiedCards.build();
    }

    /**
     * Construct a Play from the given list of cards.
     *
     * If these cards don't form a legal play, null is returned.
     */
    public static Play of(Collection<? extends Card> cards) {
        Play result = null;
        for (PlayTransformer transformer : ALL_TRANSFORMERS) {
            result = transformer.tryConvert(cards);
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    // ------------------------------------------------------------------------
    // public API

    /**
     * Get the cards involved in this Play.
     */
    public final ImmutableSortedMultiset<Card> getCards() {
        return playedCards;
    }

    /**
     * Returns true if this Play is legal given the last one, and false if not.
     */
    public abstract boolean isLegalGiven(Play lastPlay);

    /**
     * Get a comparator which can be used to order the cards for this type of Play.
     */
    abstract Comparator<Card> newSorter();

    // ------------------------------------------------------------------------
    // Helper methods

    /**
     * Make a List of all our known PlayTransformers.
     */
    private static List<PlayTransformer> makeTransformerList() {
        List<PlayTransformer> transformers = Lists.newArrayList();
        transformers.add(Single.transformer());
        transformers.add(Straight.transformer());
        transformers.add(Pairs.transformer());
        transformers.add(Triples.transformer());
        transformers.add(FullHouses.transformer());

        return Collections.unmodifiableList(transformers);

    }

}
