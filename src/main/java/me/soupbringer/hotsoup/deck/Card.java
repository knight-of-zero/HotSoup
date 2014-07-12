package me.soupbringer.hotsoup.deck;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

/**
 * A card in the deck.
 */
public final class Card implements Serializable {

  // ------------------------------------------------------------------------
  // Class constants

  /** The serializable version. */
  private static final long serialVersionUID = 1L;

  /**
   * Ranks which actually have suits.
   */
  private static final Set<Rank> SUITED_RANKS = Sets.immutableEnumSet(
    Rank.TWO,
    Rank.THREE,
    Rank.FOUR,
    Rank.FIVE,
    Rank.SIX,
    Rank.SEVEN,
    Rank.EIGHT,
    Rank.NINE,
    Rank.TEN,
    Rank.JACK,
    Rank.QUEEN,
    Rank.KING,
    Rank.ACE);

  /**
   * Suits which appear in a normal deck.
   */
  private static final Set<Suit> REAL_SUITS =
    Sets.immutableEnumSet(Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES);

  /**
   * A table containing an instance of each kind of card.
   */
  private static final ImmutableTable<Rank, Suit, Card> CARDS = allCards();

  // ------------------------------------------------------------------------
  // Member variables

  /**
   * The rank of this card.
   */
  private final Rank rank;

  /**
   * The suit of this card. This is guaranteed to be UNNECESSARY if the rank is
   * a Joker, and guaranteed to be something *else* otherwise.
   */
  private final Suit suit;

  // ------------------------------------------------------------------------
  // Construction

  /**
   * Use the static factory method to construct one of these.
   */
  private Card(Rank rank, Suit suit) {
    Preconditions.checkArgument(
      rank.isSuited() != (suit == Suit.UNNECESSARY),
      "Suit " + suit + " not valid for rank " + rank);

    this.rank = rank;
    this.suit = suit;
  }

  /**
   * Get a card for the given rank & suit. If the rank is unsuited (jokers),
   * the suit doesn't matter.
   */
  public static Card of(Rank rank, Suit suit) {
    Card card = CARDS.get(rank, rank.isSuited() ? suit : Suit.UNNECESSARY);
    Preconditions.checkArgument(
        card != null,
        "Internal error... this card should be cached: " + rank + ", " + suit);
    return CARDS.get(rank, rank.isSuited() ? suit : Suit.UNNECESSARY);
  }

  // ------------------------------------------------------------------------
  // Getters

  /** Return the rank of this card. */
  public Rank getRank() {
    return rank;
  }
  
  /** Returns true if this Card has a suit, and false otherwise. */
  public boolean hasSuit() {
    return rank.isSuited();
  }

  /**
   * Return the suit of this card.
   * 
   * @throws IllegalStateException if hasSuit() is false (ie: this card is a joker).
   */
  public Suit getSuit() {
    Preconditions.checkState(rank.isSuited(), "Jokers have no suit.");
    return suit;
  }

  /** Get the number of points that this card is worth. */
  public int getNumPoints() {
    return rank.getNumPoints();
  }

  // ------------------------------------------------------------------------
  // Comparison
  
  /**
   * Returns a positive int if the rank of this card is greater than the
   * rank of the argument, a negative int if the rank of this card is less
   * than the rank of the second, and 0 if the rank of this card equals the
   * rank of the other.
   */
  public int compareByRank(Card other) {
    return getRank().compareTo(other.getRank());
  }
  
  // ------------------------------------------------------------------------
  // Implementation

  /** Two cards are equal if their ranks and sutis are equal. */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Card) {
      Card casted = (Card) o;
      return this.rank == casted.rank && this.suit == casted.suit;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(rank, suit);
  }
  
  @Override
  public String toString() {
    switch (rank) {
      case LITTLE_JOKER:
        return "little joker";
      case BIG_JOKER:
        return "big joker";
      default:
        return rank + " of " + suit;
    }
  }

  // ------------------------------------------------------------------------
  // Helper methods

  /**
   * Make a table for all the possible cards, so we can cache these instances
   * and use them a bunch later.
   */
  private static ImmutableTable<Rank, Suit, Card> allCards() {
    ImmutableTable.Builder<Rank, Suit, Card> builder = ImmutableTable.builder();
    for (Rank suitedRank : SUITED_RANKS) {
      for (Suit suit : REAL_SUITS) {
        builder.put(suitedRank, suit, new Card(suitedRank, suit));
      }
    }
    builder.put(Rank.LITTLE_JOKER, Suit.UNNECESSARY, new Card(Rank.LITTLE_JOKER, Suit.UNNECESSARY));
    builder.put(Rank.BIG_JOKER, Suit.UNNECESSARY, new Card(Rank.BIG_JOKER, Suit.UNNECESSARY));
    return builder.build();
  }
}
