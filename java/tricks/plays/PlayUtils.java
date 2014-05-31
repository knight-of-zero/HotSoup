package tricks.plays;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import deck.Card;
import deck.Rank;

import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Set;

/**
 * Common utility methods to help in formulating Plays.
 */
public final class PlayUtils {

  private static Set<Rank> JOKERS = Sets.immutableEnumSet(Rank.LITTLE_JOKER, Rank.BIG_JOKER);
  
  /** The set of 'usually non-consecutive' ranks. */
  private static final Set<Rank> NON_CONSECUTIVES =
    Sets.immutableEnumSet(Rank.TWO, Rank.LITTLE_JOKER, Rank.BIG_JOKER);
  
  /**
   * No need to instantiate utils classes.
   */
  private PlayUtils() { }

  /**
   * Returns true if the rank map represents consecutive pairs (with no error checks for
   * twos & jokers), and false otherwise.
   */
  public static boolean areConsecutives(NavigableMap<Rank, Integer> numRanks, int numRequired) {
    Preconditions.checkNotNull(numRanks, "We aren't expecting nulls here.");

    // Let's count empty maps as invalid...
    if (numRanks.size() == 0 && numRequired != 0) {
      return false;
    }
    
    // Make sure that the required number of cards exist at each rank.
    if (numRanks.values().stream().anyMatch(numInRank -> numInRank.intValue() != numRequired)) {
      return false;
    }
    
    // Make sure that all the Ranks are contiguous.
    return numRanks.lastKey().ordinal() - numRanks.firstKey().ordinal() == numRanks.size() - 1;
  }

  /**
   * Using the argument cards, count how many of them occur in each rank, and return it.
   */
  public static NavigableMap<Rank, Integer> newRankMap(Collection<? extends Card> cards) {
    NavigableMap<Rank, Integer> ranks = Maps.newTreeMap();
    for (Card card : cards) {
      ranks.merge(card.getRank(), 1, Integer::sum);
    }
    return ImmutableSortedMap.copyOf(ranks);
  }

  /**
   * Returns true if the ranks set has only jokers, and false otherwise.
   */
  public static boolean allJokers(Set<Rank> ranks) {
    return JOKERS.containsAll(ranks);
  }
  
  /**
   * A helper method to remove duplicate code from the
   * doubles & triples validation.
   */
  public static boolean isLegalSameRankOrdinaryPlay(
    Collection<? extends Card> cards,
    int numRequired)
  {
    // Make a map from Rank to the number of cards with that rank.
    NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);
    Set<Rank> ranksPresent = ranks.keySet();
    
    // Jokers and twos must come in as a single set. No continuity allowed.
    if (cards.size() == numRequired) {
      if (PlayUtils.allJokers(ranksPresent)) {
        return true;
      }
      if (ranksPresent.size() == 1 && ranksPresent.contains(Rank.TWO)) {
        return true;
      }
    }
  
    // Otherwise, twos and jokers aren't allowed
    if (!Collections.disjoint(ranksPresent, NON_CONSECUTIVES)) {
      return false;
    }
  
    // Finally, make sure that the ranks are consecutive.
    return PlayUtils.areConsecutives(ranks, numRequired);
  }
}
