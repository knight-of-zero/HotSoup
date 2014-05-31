package tricks.plays;

import deck.Card;
import deck.Rank;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMultiset;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import tricks.IllegalPlayException;


/**
 * A single person's play. This is essentially a set of cards that one person
 * decided to play together.
 * 
 * Although this class is abstract, it has no public or protected constructors,
 * and so it can't be subclassed (or instantiated) outside of this package.
 * 
 * To construct one of these externally, use the static 'of' methods.
 */
public abstract class Play {
  
  /**
   * Static list of all our known transformers. Each time a list of cards gets
   * given to us, we'll run through these and try to use each one to create a
   * valid Play.
   */
  private static final ImmutableList<PlayTransformer> ALL_TRANSFORMERS =
    ImmutableList.<PlayTransformer>builder()
      .add(Pass::tryPass)
      .add(Single::trySingle)
      .add(Pairs::tryPairs)
      .add(Triples::tryTriples)
      .add(FullHouses::tryFullHouses)
      .add(Straight::tryStraight)
      .build();

  // ------------------------------------------------------------------------
  // Member variables

  /**
   * The cards which were involved in this Play, sorted in a nice display order.
   */
  private final ImmutableSortedMultiset<Card> playedCards;

  // ------------------------------------------------------------------------
  // Construction

  /**
   * Instantiate through the static factory method.
   *
   * No instantiation outside of this package.
   */
  Play(ImmutableSortedMultiset<Card> cards) {
    this.playedCards = cards;
  }

  /**
   * Check the given collection of cards, and return a Set of the possible Plays
   * which these cards can be interpreted as.
   * 
   * If the cards don't form a valid play, and empty set is returned.
   */
  public static Set<Play> findValid(Collection<? extends Card> cards) throws IllegalPlayException {
    Preconditions.checkNotNull(cards, "We aren't expecting nulls here.");
    
    Set<Play> validPlays = ALL_TRANSFORMERS.stream()
      .map(transformer -> transformer.tryConvert(cards))
      .filter(play -> play != null)
      .collect(Collectors.toSet());
    
    return ImmutableSet.copyOf(validPlays);
  }

  // ------------------------------------------------------------------------
  // public API

  /**
   * Get the number of points involved in this Play.
   */
  public final int countNumPoints() {
    return getCards().stream()
      .mapToInt(Card::getNumPoints)
      .sum();
  }
  
  /**
   * Get the cards involved in this Play.
   */
  public final ImmutableSortedMultiset<Card> getCards() {
    return playedCards;
  }
  
  /** Returns true if this Play is a pass, and false otherwise. */
  public boolean isPass() {
    return false;
  }

  /**
   * Returns true if this Play is legal given the last one, and false if not.
   */
  public abstract boolean isLegalGiven(Play lastPlay);
  
  // ------------------------------------------------------------------------
  // Boring object implementation
  
  /**
   * Two Plays are equal if all the cards being played are equal.
   * 
   * TODO: jacks full of queens and queens full of jacks.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Play) {
      return playedCards.equals(((Play) o).playedCards);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return playedCards.hashCode();
  }
  
  // --------------------------------------------------------------------------
  // Helper methods
  
  /**
   * Get the lowest rank in this Play.
   */
  final Rank lowestRank() {
    return playedCards.stream()
      .min(Card::compareByRank)
      .get()
      .getRank();
  }
}
