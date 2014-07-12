package me.soupbringer.hotsoup.tricks.plays;

import java.util.Collection;

import me.soupbringer.hotsoup.deck.Card;

import com.google.common.collect.ImmutableSortedMultiset;

/**
 * Some number of consecutive pairs of cards.
 */
final class Pairs extends Play {

  // ------------------------------------------------------------------------
  // Construction

  /**
   * Make a Pairs from the given cards.
   */
  private Pairs(Collection<? extends Card> playedCards) {
    super(ImmutableSortedMultiset.orderedBy(Card::compareByRank).addAll(playedCards).build());
  }
  
  /** Try to parse the given Collection into a Pairs, and return null if we can't. */
  static Pairs tryPairs(Collection<? extends Card> cards) {
    return PlayUtils.isLegalSameRankOrdinaryPlay(cards, 2) ? new Pairs(cards) : null;
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
