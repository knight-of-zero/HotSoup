package cards;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.Set;

/**
 * A card in the deck.
 */
public class Card {

    // ------------------------------------------------------------------------
    // Class constants

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
    private static final Set<Suit> REAL_SUITS = Sets.immutableEnumSet(
        Suit.CLUBS,
        Suit.DIAMONDS,
        Suit.HEARTS,
        Suit.SPADES);

    /**
     * A table containing an instance of each kind of card.
     */
    private static final Table<Rank, Suit, Card> CARDS = allCards();

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
        if (rank.isSuited() && suit == Suit.UNNECESSARY) {
            throw new IllegalArgumentException("You can't make an unsuited " + rank);
        }
        if (!rank.isSuited() && suit != Suit.UNNECESSARY) {
            throw new IllegalArgumentException("Jokers shouldn't have a suit.");
        }

        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Get a card for the given rank & suit.
     */
    public static Card of(Rank rank, Suit suit) {
        return CARDS.get(rank, rank.isSuited() ? suit : Suit.UNNECESSARY);
    }

    // ------------------------------------------------------------------------
    // Getters

    /**
     * Return the rank of this card.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Return the suit of this card. Throws an IllegalStateException if this card
     * is a joker, since those don't have suits.
     */
    public Suit getSuit() {
        if (!rank.isSuited()) {
            throw new IllegalStateException("Jokers have no suit.");
        }
        return suit;
    }

    /**
     * Get the number of points that this card is worth.
     */
    public int getNumPoints() {
        switch (rank) {
            case FIVE:
                return 5;
            case TEN:
            case KING:
                return 10;
            default:
                return 0;
        }
    }

    // ------------------------------------------------------------------------
    // Implementation

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
        return rank.hashCode() ^ suit.hashCode();
    }

    // ------------------------------------------------------------------------
    // Helper methods

    /**
     * Make a table for all the possible cards, so we can cache these instances
     * and use them a bunch later.
     */
    private static Table<Rank, Suit, Card> allCards() {

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
