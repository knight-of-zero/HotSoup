package tricks.plays;

import deck.Card;
import deck.Rank;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
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
   * The set of 'usually non-consecutive' ranks.
   */
  private static final Set<Rank> NON_CONSECUTIVES =
    Sets.immutableEnumSet(Rank.TWO, Rank.LITTLE_JOKER, Rank.BIG_JOKER);
  
  /**
   * Static list of all our known transformers. Each time a list of cards gets
   * given to us, we'll run through these and try to use each one to create a
   * valid Play.
   */
  private static final ImmutableList<PlayTransformer> ALL_TRANSFORMERS =
    ImmutableList.<PlayTransformer>builder()
      .add((cards) -> isLegalPass(cards) ? Pass.getInstance() : null)
      .add((cards) -> isLegalSingle(cards) ? new Single(cards.iterator().next()) : null)
      .add((cards) -> isLegalPairs(cards) ? new Pairs(cards) : null)
      .add((cards) -> isLegalTriples(cards) ? new Triples(cards) : null)
      .add((cards) -> isLegalFullHouses(cards) ? new FullHouses(cards) : null)
      .add((cards) -> isLegalStraight(cards) ? new Straight(cards) : null)
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
  Rank lowestRank() {
    return playedCards.stream()
      .min(Card::compareByRank)
      .get()
      .getRank();
  }
  
  /** Return true if these cards are a legal pass, and false otherwise. */
  private static boolean isLegalPass(Collection<? extends Card> cards) {
    return cards.size() == 0;
  }
  
  /** Return true if these cards are a legal single, and false otherwise. */
  private static boolean isLegalSingle(Collection<? extends Card> cards) {
    return cards.size() == 1;
  }
  
  /**
   * Return true if these cards are a legal set of pairs, and false otherwise.
   */
  private static boolean isLegalPairs(Collection<? extends Card> cards) {
    return isLegalSameRankOrdinaryPlay(cards, 2);

  }
  
  /**
   * Return true if the argument cards make up a legal play of consecutive
   * three-of-a-kinds, and false otherwise.
   */
  private static boolean isLegalTriples(Collection<? extends Card> cards) {
    return isLegalSameRankOrdinaryPlay(cards, 3);
  }
  
  /**
   * Return true if the argument cards form a legal straight, and false otherwise.
   */
  private static boolean isLegalStraight(Collection<? extends Card> cards) {
    // Get the ranks involved
    NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);
    
    // Twos and jokers aren't allowed at all here
    if (!Collections.disjoint(ranks.keySet(), NON_CONSECUTIVES)) {
      return false;
    }
    
    // Make sure there's only one card per rank.
    if (cards.size() != ranks.size()) {
      return false;
    }
    
    // We're a legal straight if all the cards are consecutive, and there's
    // only one per rank.
    return PlayUtils.areConsecutives(ranks, 1);
  }
  
  /**
   * Return true if the argument cards form a legal full houses play,
   * and false otherwise.
   */
  private static boolean isLegalFullHouses(Collection<? extends Card> cards) {
    // Get the ranks involved.
    NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);
    
    // If the triples aren't consecutive, this isn't a legal full house set.
    NavigableMap<Rank, Integer> ranksOfTriples = Maps.filterValues(ranks, Predicates.equalTo(3));
    if (!PlayUtils.areConsecutives(ranksOfTriples, 3)) {
      return false;
    }
    
    // We need pairs to go with our triples
    if (ranksOfTriples.size() != Maps.filterValues(ranks, Predicates.equalTo(2)).size()) {
      return false;
    }
    
    // Make sure there aren't any "stray" cards
    if (cards.size() % 5 != 0) {
      return false;
    }

    // If we got here, we're good
    return true;
  }
  
  /**
   * A farther helper method to remove duplicate code from the
   * doubles & triples validation.
   */
  private static boolean isLegalSameRankOrdinaryPlay(
    Collection<? extends Card> cards,
    int numRequired)
  {
    // Make a map from Rank to the number of cards with that rank.
    NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);
    Set<Rank> ranksPresent = ranks.keySet();
    
    // Jokers and twos must come in as a single set. No continuity allowed.
    if (cards.size() == numRequired) {
      if (PlayUtils.allJokers(ranksPresent)) {
        return true;
      }
      if (ranksPresent.size() == 1 && ranksPresent.contains(Rank.TWO)) {
        return true;
      }
    }
  
    // Otherwise, twos and jokers aren't allowed
    if (!Collections.disjoint(ranksPresent, NON_CONSECUTIVES)) {
      return false;
    }
  
    // Finally, make sure that the ranks are consecutive.
    return PlayUtils.areConsecutives(ranks, numRequired);
  }
}
