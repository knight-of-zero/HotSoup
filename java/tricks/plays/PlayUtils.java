package tricks.plays;

import cards.Card;
import cards.Rank;
import com.google.common.collect.Maps;

import java.util.EnumSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

/**
 * Common utility methods to help in formulating Plays.
 */
public final class PlayUtils {

    /**
     * No need to instantiate utils classes.
     */
    private PlayUtils() { }

    /**
     * Returns true if the rank map represents consecutive pairs (with no error checks for
     * twos & jokers), and false otherwise.
     */
    public static boolean areConsecutives(NavigableMap<Rank, Integer> numRanks, int numRequired) {
        if (numRanks == null) {
            throw new NullPointerException("This method won't work this way.");
        }
        Map.Entry<Rank, Integer> thisEntry = numRanks.firstEntry();
        while (thisEntry != null) {
            if (!thisEntry.getValue().equals(numRequired)) {
                return false;
            }
            Map.Entry<Rank, Integer> lastEntry = thisEntry;
            thisEntry = numRanks.higherEntry(thisEntry.getKey());
            if (thisEntry != null && thisEntry.getKey().ordinal() != lastEntry.getKey().ordinal() + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Using the argument cards, count how many of them occur in each rank, and return it.
     */
    public static NavigableMap<Rank, Integer> newRankMap(Iterable<? extends Card> cards) {
        NavigableMap<Rank, Integer> ranks = Maps.newTreeMap();
        for (Card card : cards) {
            Rank thisRank = card.getRank();
            if (ranks.containsKey(thisRank)) {
                ranks.put(thisRank, ranks.get(thisRank) + 1);
            }
            else {
                ranks.put(thisRank, 1);
            }
        }
        return ranks;
    }

    /**
     * Returns true if the ranks set has only jokers, and false otherwise.
     */
    public static boolean allJokers(Set<Rank> ranks) {
        return EnumSet.of(Rank.LITTLE_JOKER, Rank.BIG_JOKER).containsAll(ranks);
    }
}
