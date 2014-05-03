package cards;

import java.util.Comparator;

/**
 * Comparator which sorts cards based on rank.
 */
public class RankComparator implements Comparator<Card> {

    /**
     * We have no state... so we may as well be a singleton.
     */
    private static final RankComparator INSTANCE = new RankComparator();

    /**
     * Instantiate through the static factory method.
     */
    private RankComparator() { }

    /**
     * Get a Comparator which sorts cards based on rank.
     */
    public static RankComparator create() {
        return INSTANCE;
    }

    @Override
    public int compare(Card card, Card card2) {
        return card.getRank().compareTo(card2.getRank());
    }
}
