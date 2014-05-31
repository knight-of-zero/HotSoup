package tricks.plays;

import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Sets;

import deck.Card;
import deck.Rank;

import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Set;

/**
 * A single card, or straight.
 *
 */
final class Straight extends Play {
  
  /** The set of 'usually non-consecutive' ranks. */
  private static final Set<Rank> NON_CONSECUTIVES =
    Sets.immutableEnumSet(Rank.TWO, Rank.LITTLE_JOKER, Rank.BIG_JOKER);

  // ------------------------------------------------------------------------
  // Construction

  /**
   * Make a Straight from the given cards
   */
  Straight(Collection<? extends Card> playedCards) {
    super(ImmutableSortedMultiset.orderedBy(Card::compareByRank).addAll(playedCards).build());
  }
  
  /** Create a new Straight, and return null if we can't. */
  public static Straight tryStraight(Collection<? extends Card> cards) {
    return isLegalStraight(cards) ? new Straight(cards) : null;
  }

  // ------------------------------------------------------------------------
  // Implementation

  @Override
  public boolean isLegalGiven(Play lastPlay) {
      return lastPlay instanceof Straight &&
             this.lowestRank().compareTo(((Straight) lastPlay).lowestRank()) > 0;
  }

  // -------------------------------------------------------------------------
  // Helper methods

  /**
   * Return true if the argument cards form a legal straight, and false otherwise.
   */
  private static boolean isLegalStraight(Collection<? extends Card> cards) {
    // Get the ranks involved
    NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);
    
    // Twos and jokers aren't allowed at all here
    if (!Collections.disjoint(ranks.keySet(), NON_CONSECUTIVES)) {
      return false;
    }
    
    // Make sure there's only one card per rank.
    if (cards.size() != ranks.size()) {
      return false;
    }
    
    // We're a legal straight if all the cards are consecutive, and there's
    // only one per rank.
    return PlayUtils.areConsecutives(ranks, 1);
  }
}
