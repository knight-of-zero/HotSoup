package tricks.plays;

import com.google.common.collect.ImmutableSortedMultiset;

import deck.Card;
import deck.Rank;

import java.util.Collection;

/**
 * A single card, or straight.
 *
 */
final class Straight extends Play {

  // ------------------------------------------------------------------------
  // Construction

  /**
   * Make a Straight from the given cards
   */
  Straight(Collection<? extends Card> playedCards) {
    super(ImmutableSortedMultiset.orderedBy(Card::compareByRank).addAll(playedCards).build());
  }

  // ------------------------------------------------------------------------
  // Implementation

  @Override
  public boolean isLegalGiven(Play lastPlay) {
      return lastPlay instanceof Straight &&
             this.getLowestRank().compareTo(((Straight) lastPlay).getLowestRank()) > 0;
  }

  /** Get the rank of the lowest played card in the straight. */
  private Rank getLowestRank() {
    return getCards().firstEntry().getElement().getRank();
  }
}
