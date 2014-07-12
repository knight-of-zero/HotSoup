package me.soupbringer.hotsoup.tricks.plays;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMultiset;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.soupbringer.hotsoup.deck.Card;
import me.soupbringer.hotsoup.deck.Rank;
import me.soupbringer.hotsoup.tricks.IllegalPlayException;


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
   * If the cards don't form a valid play, this returns an empty set.
   */
  public static Set<Play> all(Collection<? extends Card> cards) throws IllegalPlayException {
    checkNotNull(cards, "We aren't expecting nulls here.");
    return newValidPlayStream(cards).collect(Collectors.toSet());
  }
  
  /**
   * Get *a* valid play for the given cards. There's no guarantee on which
   * one this is.
   * 
   * If no plays are valid, this returns null.
   */
  public static Play one(Collection<? extends Card> cards) throws IllegalPlayException {
    checkNotNull(cards, "We aren't expecting nulls here.");
    Optional<Play> maybeValid = newValidPlayStream(cards).findFirst();
    return maybeValid.isPresent() ? maybeValid.get() : null;
  }
  
  /**
   * Get a valid play for the single card.
   */
  public static Play one(Card card) {
    return Single.of(checkNotNull(card)); 
  }
  
  /**
   * Get the 'pass' play. There's only one of these.
   */
  public static Play pass() {
    return Pass.tryPass(ImmutableList.of());
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
   * TODO: jacks full of queens and queens full of jacks, and consecutive bombs.
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
  
  /** Get a stream of all the Plays which are valid interpretations of these cards. */
  private static Stream<Play> newValidPlayStream(Collection<? extends Card> cards) {
    return ALL_TRANSFORMERS.stream()
      .map(transformer -> transformer.tryConvert(cards))
      .filter(play -> play != null);
  }
}
