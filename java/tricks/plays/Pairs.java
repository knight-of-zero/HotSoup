package tricks.plays;

import java.util.Collection;

import com.google.common.collect.ImmutableSortedMultiset;

import deck.Card;

/**
 * Some number of consecutive pairs of cards.
 */
final class Pairs extends Play {

  // ------------------------------------------------------------------------
  // Construction

  /**
   * Make a Pairs from the given cards.
   */
  Pairs(Collection<? extends Card> playedCards) {
    super(ImmutableSortedMultiset.orderedBy(Card::compareByRank).addAll(playedCards).build());
  }

  // ------------------------------------------------------------------------
  // Implementation

  @Override
  public boolean isLegalGiven(Play lastPlay) {
    return lastPlay instanceof Pairs
        && ((Pairs) lastPlay).numCards() == this.numCards()
        && ((Pairs) lastPlay).lowestRank().compareTo(this.lowestRank()) < 0;
  }

  // ------------------------------------------------------------------------
  // Queries

  /**
   * Get the number of cards in this Pairs.
   */
  private int numCards() {
    return getCards().size();
  }
}
