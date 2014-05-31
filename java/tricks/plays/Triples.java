package tricks.plays;

import java.util.Collection;

import com.google.common.collect.ImmutableSortedMultiset;

import deck.Card;

/**
 * A Play for consecutive three-of-a-kinds
 */
public class Triples extends Play {

  /**
   * Make a Triples from the given cards.
   */
  Triples(Collection<? extends Card> playedCards) {
    super(ImmutableSortedMultiset.orderedBy(Card::compareByRank).addAll(playedCards).build());
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
