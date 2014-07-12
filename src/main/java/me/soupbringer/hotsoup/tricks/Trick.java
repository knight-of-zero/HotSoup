package me.soupbringer.hotsoup.tricks;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import me.soupbringer.hotsoup.deck.Card;
import me.soupbringer.hotsoup.tricks.IllegalPlayException;
import me.soupbringer.hotsoup.tricks.plays.Play;

/**
 * A class which stores all the plays from a given hand
 *
 * This is essentially a list of Plays, in the order in which they occurred.
 */
public final class Trick implements Iterable<Play> {

  // ------------------------------------------------------------------------
  // Member variables

  /**
   * The plays, in the order in which they occurred.
   */
  private final ImmutableList<Play> plays;

  // ------------------------------------------------------------------------
  // Construction

  /**
   * Constructor. Instantiate via the builder instead.
   */
  private Trick(Iterator<Play> plays) {
    this.plays = ImmutableList.copyOf(plays);
  }

  /**
   * Create a new Builder for a Trick.
   * 
   * @param lead The first Play made during this trick.
   */
  public static Builder builder(Play lead) {
    Preconditions.checkNotNull(lead, "The lead can't be null.");
    Preconditions.checkArgument(!lead.isPass(), "You can't lead off with a pass.");
    return new Builder(lead);
  }
  
  /**
   * Create a new Builder for a Trick.
   * 
   * @param lead The first (Single) card played during this trick.
   */
  public static Builder builder(Card card) {
    Preconditions.checkNotNull(card, "The lead card can't be null.");
    return new Builder(Play.one(card));
  }

  // ------------------------------------------------------------------------
  // Getters

  /**
   * Get the number of points which were contained within this Trick.
   */
  public int countNumPoints() {
    return countNumPoints(plays);
  }

  // ------------------------------------------------------------------------
  // Implementation

  @Override
  public Iterator<Play> iterator() {
    return plays.iterator();
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof Trick) {
      return plays.equals(((Trick) o).plays);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return plays.hashCode();
  }

  // ------------------------------------------------------------------------
  // Builder

  /**
   * A Builder for a Trick. All the mutation must happen here. After a full
   * Trick has been built, it's immutable.
   */
  public static final class Builder {

    private final Deque<Play> playsSoFar;

    private int numConsecutivePasses;

    /**
     * Instantiate via the static builder() method.
     */
    private Builder(Play lead) {
      this.playsSoFar = Lists.newLinkedList();
      this.numConsecutivePasses = 0;
      this.playsSoFar.push(lead);
    }

    /**
     * Return true if the given play is legal, given what has come before it,
     * and false otherwise.
     */
    public boolean isLegalNext(Play play) {
      Preconditions.checkNotNull(play, "Not expecting a null Play here.");
      return play.isLegalGiven(playsSoFar.peek());
    }

    /**
     * Add the given Play to the trick. If the player passed, this
     * should be called with 'null'.
     *
     * If this play isn't legal given the last one, this throws an IllegalPlayException.
     * If this is called a fourth time after 3 consecutive 'null's, a
     * TrickTerminatedException will be thrown.
     */
    public Builder play(Play play) {
      Preconditions.checkNotNull(play, "We aren't accepting null plays here.");
      
      // If 3 people passed before us, the trick should be over...
      if (numConsecutivePasses == 3) {
        throw IllegalPlayException.extraPlayMade();
      }
      
      // If the play is legal, add it
      if (play.isLegalGiven(playsSoFar.peek())) {
        playsSoFar.push(play);
        numConsecutivePasses = play.isPass() ? numConsecutivePasses + 1 : 0;
        return this;
      }
      
      // Otherwise, throw the exception.
      else {
        throw IllegalPlayException.illegalPlay(playsSoFar.peek(), play);
      }
    }
    
    /**
     * Add the given single card (play) to the trick.
     * 
     * This is a shortcut for adding a Single to the trick.
     */
    public Builder play(Card card) {
      return play(Play.one(card));
    }
    
    /** Shortcut for adding a 'pass' play. */
    public Builder pass() {
      return play(Play.pass());
    }

    /**
     * Get the number of points which are contained within this TrickBuilder so far.
     */
    public int getNumPoints() {
      return countNumPoints(playsSoFar);
    }

    /**
     * Complete the trick, and get back an immutable object with all the information.
     *
     * We expect this to be called after 3 consecutive passes.
     */
    public Trick end() {
      if (numConsecutivePasses != 3) {
        throw IllegalPlayException.prematureEnd();
      }
      return new Trick(playsSoFar.descendingIterator());
    }
  }

  // --------------------------------------------------------------------------
  // Helper methods

  /**
   * Count the number of points in a bunch of Plays.
   */
  private static int countNumPoints(Collection<? extends Play> plays) {
    return plays.stream()
      .mapToInt(Play::countNumPoints)
      .sum();
  }
}
