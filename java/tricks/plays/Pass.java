package tricks.plays;

import com.google.common.collect.ImmutableSortedMultiset;

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
  
  /**
   * Static method to get an instance of the 'pass'.
   */
  static Pass getInstance() {
    return INSTANCE;
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
