package tricks.plays;

import java.util.Collection;

import com.google.common.collect.ImmutableSortedMultiset;

import deck.Card;

/**
 * Play used whenever someone passes.
 */
final class Pass extends Play {

  /**
   * A Singleton is safe here, because all passes are the same.
   */
  private static final Pass INSTANCE = new Pass();
  
  /** Constructor. Get an instance through the static factory method. */
  private Pass() {
    super(ImmutableSortedMultiset.of());
  }
  
  /** Try to parse the given Cards into a Pass, and return null if that's not possible. */
  static Pass tryPass(Collection<? extends Card> cards) {
    return cards.isEmpty() ? INSTANCE : null;
  }
  
  @Override
  public boolean isPass() {
    return true;
  }

  @Override
  public boolean isLegalGiven(Play lastPlay) {
    return true;
  }
}
