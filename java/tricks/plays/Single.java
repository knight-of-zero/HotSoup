package tricks.plays;

import java.util.Collection;

import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Ordering;

import deck.Card;

/**
 * Play representing a single card.
 */
class Single extends Play {

  /** Make a Single from the given card. */
  private Single(Card card) {
    super(new ImmutableSortedMultiset.Builder<Card>(Ordering.arbitrary()).add(card).build());
  }
  
  
  /** Try to parse the given Cards into a Single, and return null if that's not possible. */
  static Single trySingle(Collection<? extends Card> cards) {
    return cards.size() == 1 ? new Single(cards.iterator().next()) : null;
  }

  @Override
  public boolean isLegalGiven(Play lastPlay) {
    return lastPlay instanceof Single &&
        ((Single) lastPlay).getCard().compareByRank(getCard()) < 0;
  }

  /**
   * Get the lone card involved in this Play.
   */
  private Card getCard() {
    return getCards().firstEntry().getElement();
  }
}
