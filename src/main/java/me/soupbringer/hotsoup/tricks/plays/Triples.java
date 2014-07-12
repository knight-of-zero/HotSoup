package me.soupbringer.hotsoup.tricks.plays;

import java.util.Collection;

import me.soupbringer.hotsoup.deck.Card;

import com.google.common.collect.ImmutableSortedMultiset;

/**
 * A Play for consecutive three-of-a-kinds
 */
public class Triples extends Play {

  /**
   * Make a Triples from the given cards.
   */
  private Triples(Collection<? extends Card> playedCards) {
    super(ImmutableSortedMultiset.orderedBy(Card::compareByRank).addAll(playedCards).build());
  }
  
  /** Try to parse the given Collection into a Triples, and return null if we can't. */
  public static Triples tryTriples(Collection<? extends Card> cards) {
    return PlayUtils.isLegalSameRankOrdinaryPlay(cards, 3) ? new Triples(cards) : null;
  }

  @Override
  public boolean isLegalGiven(Play lastPlay) {
    if (lastPlay instanceof Triples) {
      Triples casted = (Triples) lastPlay;
      return casted.numCards() == this.numCards()
          && casted.lowestRank().compareTo(this.lowestRank()) < 0;
    }
    return false;
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
