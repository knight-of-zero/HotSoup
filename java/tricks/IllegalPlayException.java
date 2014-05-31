package tricks;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import deck.Card;
import tricks.plays.Play;

/**
 * Exception thrown whenever someone tries to add an illegal 'play'
 * to a trick.
 */
public class IllegalPlayException extends RuntimeException {

  /** Serial version UID. */
  private static final long serialVersionUID = 1L;

  /** Constructor. Instantiate through the static methods. */
  private IllegalPlayException(String errorMesasge) {
    super(errorMesasge);
  }
  
  private IllegalPlayException(boolean prematureTermination) {
    super(prematureTermination
      ? "The trick terminated before 3 people passed"
      : "An extra Play was made after 3 people passed.");
  }
  
  /**
   * Static factory method to be used whenever a trick was
   * ended *before* 3 people passed.
   */
  public static IllegalPlayException prematureEnd() {
    return new IllegalPlayException(
      "The trick terminated, but 3 people haven't passed yet.");
  }
  
  /**
   * Static factory method to be used whenever a Play was made *after*
   * 3 people passed.
   */
  public static IllegalPlayException extraPlayMade() {
    return new IllegalPlayException(
      "An fourth Play was made, but 3 people already passed.");
  }
  
  /**
   * Static factory method to be used whenever 'thisPlay' was made *after*
   * 'lastPlay'... but it wasn't legal/shouldn't have happened.
   */
  public static IllegalPlayException illegalPlay(Play lastPlay, Play thisPlay) {
    checkNotNull(lastPlay, "lastPlay shouldn't be null.");
    checkNotNull(lastPlay, "thisPlay shouldn't be null.");
    checkArgument(
      !thisPlay.isLegalGiven(lastPlay),
      "This exception shouldn't be thrown if the play was legal...");
    
    String err = new StringBuilder()
      .append("Attempted to play ").append(thisPlay)
      .append(" after the previous person played ").append(lastPlay)
      .append(". This isn't a legal move.")
      .toString();
    return new IllegalPlayException(err);
  }
  
  /**
   * Construct an IllegalPlayException showing that the given cards *can't* be made
   * into a valid Play.
   * 
   * That is: our front-end screwed up, or someone's getting hacky.
   */
  public static IllegalPlayException malformedPlay(Collection<? extends Card> cards) {
    return new IllegalPlayException("These cards don't form a valid play: " + cards);
  }
}
