package me.soupbringer.hotsoup.deck;

/**
 * The power level of the cards, in order from least to greatest.
 */
public enum Rank {

    THREE(0, false, true),
    FOUR(0, false, true),
    FIVE(5, false, true),
    SIX(0, false, true),
    SEVEN(0, false, true),
    EIGHT(0, false, true),
    NINE(0, false, true),
    TEN(10, false, true),
    JACK(0, false, true),
    QUEEN(0, false, true),
    KING(10, false, true),
    ACE(0, false, true),
    
    TWO(0, false, false),
    
    LITTLE_JOKER(0, true, false),
    BIG_JOKER(0, true, false);

    // ------------------------------------------------------------------------
    
    private final int numPoints;
    private final boolean isJoker;
    private final boolean canBeConsecutive;
    
    /**
     * Constructor for a Rank.
     *
     * @param numPoints The number of points which cards of this rank are worth.
     * @param isJoker True if this rank is a joker, and false otherwise.
     * @param canBeConsecutive True if this rank can be considered 'consecutive'
     *                         with other ranks, and false otherwise.
     */
    private Rank(int numPoints, boolean isJoker, boolean canBeConsecutive) {
      this.numPoints = numPoints;
      this.isJoker = isJoker;
      this.canBeConsecutive = canBeConsecutive;
    }
    
    /**
     * Return the number of points that cards of this rank are worth.
     */
    public int getNumPoints() {
      return numPoints;
    }

    /**
     * Returns true if this rank is a joker, and false otherwise.
     */
    public boolean isJoker() {
      return isJoker;
    }

    /**
     * Returns true if this rank can be consecutive with other ranks,
     * and false otherwise.
     */
    public boolean canBeConsecutive() {
      return canBeConsecutive;
    }

    /**
     * Returns true if cards of this Rank can be suited, and false otherwise.
     */
    public boolean isSuited() {
      return !isJoker();
    }
}
