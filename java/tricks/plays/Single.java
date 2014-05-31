package tricks.plays;

import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Ordering;

import deck.Card;

/**
 * Play representing a single card.
 */
class Single extends Play {

  /** Make a Single from the given card. */
  Single(Card card) {
    super(new ImmutableSortedMultiset.Builder<Card>(Ordering.arbitrary()).add(card).build());
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
