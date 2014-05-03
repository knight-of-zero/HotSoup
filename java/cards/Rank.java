package cards;

import java.util.EnumSet;

/**
 * The power level of the cards, in order from least to greatest.
 */
public enum Rank {

    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE,
    TWO,
    LITTLE_JOKER,
    BIG_JOKER;

    // ------------------------------------------------------------------------

    /**
     * Returns true if this rank is a joker, and false otherwise.
     */
    public boolean isJoker() {
        return EnumSet.of(LITTLE_JOKER, BIG_JOKER).contains(this);
    }

    /**
     * Returns true if this rank can be consecutive with other ranks,
     * and false otherwise.
     */
    public boolean canBeConsecutive() {
        return !EnumSet.of(TWO, LITTLE_JOKER, BIG_JOKER).contains(this);
    }

    /**
     * Returns true if cards of this Rank can be suited, and false otherwise.
     */
    public boolean isSuited() {
        return !isJoker();
    }
}
