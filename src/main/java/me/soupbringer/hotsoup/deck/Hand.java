package me.soupbringer.hotsoup.deck;

import java.util.Arrays;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;

/**
 * A single hand for a single player. When constructed initially, this is essentially
 * a set of 26 cards. Cards can then be removed from it, but never added to it.
 */
public final class Hand {
  
  /** The size of each new hand. */
  private static int NEW_HAND_SIZE = 26;

  /**
   * The cards in this hand.
   * There should only ever be at most 2 copies of the same card in here.
   */
  private final Multiset<Card> cards;
  
  // --------------------------------------------------------------------------
  // Construction

  /** Constructor for a new Hand. Instantiate through the Builder. */
  private Hand(Iterable<? extends Card> cards) {
    this.cards = LinkedHashMultiset.create(cards);
  }
  
  /** Get a new Builder for a Hand. */
  public static Builder builder() {
    return new Builder();
  }
  
  // --------------------------------------------------------------------------
  // Public API
  
  /** Return true if this hand is done (the player got out), and false otherwise. */
  public boolean isOut() {
    return cards.isEmpty();
  }
  
  /**
   * Get the number of cards left in this hand.
   */
  public int numCardsLeft() {
    return cards.size();
  }
  
  /**
   * Play the argument card from this hand.
   *
   * @param cards The cards to be played from this hand.
   * @return True if this hand is now out (after playing), and false if it still has cards left.
   * @throws CardDoesNotExistInHandException If one of the argument cards doesn't
   *                                         exist in this hand.
   */
  public boolean play(Card single) {
    removeCard(single);
    return isOut();
  }
  
  /**
   * Play all the argument cards from this hand.
   * 
   * @param cards The cards to be played from this hand.
   * @return True if this hand is now out (after playing), and false if it still has cards left.
   * @throws CardDoesNotExistInHandException If one of the argument cards doesn't
   *                                         exist in this hand.
   */
  public boolean playAll(Card... cards) {
    return playAll(Arrays.asList(cards));
  }
  
  /**
   * Play all the argument cards from this hand.
   * 
   * @param cards The cards to be played from this hand.
   * @return True if this hand is now out (after playing), and false if it still has cards left.
   * @throws CardDoesNotExistInHandException If one of the argument cards doesn't
   *                                         exist in this hand.
   */
  public boolean playAll(Iterable<Card> cards) {
    for (Card card : cards) {
      removeCard(card);
    }
    return isOut();
  }
  
  /** 
   * Remove the given card from the hand.
   * Throw an exception if it doesn't *exist* in this hand.
   */
  private void removeCard(Card card) {
    if (!cards.contains(card)) {
      throw new CardDoesNotExistInHandException(card);
    }
    cards.remove(card);
  }
  
  // --------------------------------------------------------------------------
  // Builder

  /**
   * A Builder for a new Hand.
   */
  public static final class Builder {
    
    /** The cards in this hand. */
    private final Multiset<Card> cards;
    
    /** A Builder for a Hand. This starts out empty. */
    private Builder() {
      this.cards = LinkedHashMultiset.create();
    }

    /** Add the given Card to the hand. */
    public Builder add(Card card) {
      Preconditions.checkNotNull(card);
      // If we have 2 of the same card in our hand already, we'd be adding a third.
      // Since we're only using two decks, this shouldn't be possible. Something must
      // have gone wrong in the shuffle/deal/deck-creation code.
      if (cards.count(card) == 2) {
        throw new DuplicateCardInHandException(card);
      }
      cards.add(card);
      return this;
    }
    
    /** Add all the argument Cards to this Hand. */
    public Builder addAll(Card... cards) {
      return addAll(Arrays.asList(cards));
    }
    
    /** Add all the argument Cards to this Hand. */
    public Builder addAll(Iterable<Card> cards) {
      for (Card card : cards) {
        add(card);
      }
      return this;
    }
    
    /** Build the elements of this Builder into a new Hand. */
    public Hand build() {
      if (cards.size() != NEW_HAND_SIZE) {
        throw new InvalidNumberOfCardsForHandException(cards.size());
      }
      return new Hand(cards);
    }
  }
  
  // --------------------------------------------------------------------------
  // Exceptions

  /** Exception thrown if you try to build a Hand with two copies of the same card. */
  public static final class DuplicateCardInHandException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    private DuplicateCardInHandException(Card duplicate) {
      super("Attempted to add the same exact card to the same hand three times: " + duplicate);
    }
  }
  
  /** Exception thrown if you try to build a Hand with an invalid number of cards. */
  public static final class InvalidNumberOfCardsForHandException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    private InvalidNumberOfCardsForHandException(int handSize) {
      super("We expect 26 cards in each hand. You tried to build a hand with " + handSize);
    }
  }
  
  /** Exception thrown whenever someone tries to play a card which isn't in their hand. */
  public static final class CardDoesNotExistInHandException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private CardDoesNotExistInHandException(Card card) {
      super("We were told to play " + card + ", but it doesn't exist in this hand.");
    }
  }
}
