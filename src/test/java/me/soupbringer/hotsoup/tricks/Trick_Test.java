package me.soupbringer.hotsoup.tricks;

import static org.junit.Assert.*;

import java.util.Set;

import me.soupbringer.hotsoup.deck.Card;
import me.soupbringer.hotsoup.deck.Rank;
import me.soupbringer.hotsoup.deck.Suit;
import me.soupbringer.hotsoup.tricks.IllegalPlayException;
import me.soupbringer.hotsoup.tricks.Trick;
import me.soupbringer.hotsoup.tricks.plays.Play;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Test cases for the Trick class
 */
public class Trick_Test {

  /**
   * Make sure that a simple play with three passes can be built without error.
   */
  @Test
  public void testSinglePlay() {
    Trick.builder(Card.of(Rank.THREE, Suit.HEARTS))
      .pass()
      .pass()
      .pass()
      .end();
  }
  
  /**  Make sure that a 'normal' trick can be built properly. */
  @Test
  public void testOrdinaryTrick() {
    Trick.builder(Play.one(Card.of(Rank.THREE, Suit.HEARTS)))
      .play(Card.of(Rank.FOUR, Suit.HEARTS))
      .play(Card.of(Rank.SEVEN, Suit.HEARTS))
      .play(Card.of(Rank.KING, Suit.HEARTS))
      .play(Card.of(Rank.TWO, Suit.HEARTS))
      .play(Card.of(Rank.LITTLE_JOKER, Suit.UNNECESSARY))
      .play(Card.of(Rank.BIG_JOKER, Suit.UNNECESSARY))
      .pass()
      .pass()
      .pass()
      .end();
  }
  
  /** Make sure we can count the points properly. */
  @Test
  public void testPointCounting() {
    Trick everything = Trick.builder(Play.one(Card.of(Rank.THREE, Suit.HEARTS)))
      .play(Card.of(Rank.FOUR, Suit.HEARTS))
      .play(Card.of(Rank.FIVE, Suit.HEARTS))
      .play(Card.of(Rank.SIX, Suit.HEARTS))
      .play(Card.of(Rank.SEVEN, Suit.HEARTS))
      .play(Card.of(Rank.EIGHT, Suit.HEARTS))
      .play(Card.of(Rank.NINE, Suit.HEARTS))
      .play(Card.of(Rank.TEN, Suit.HEARTS))
      .play(Card.of(Rank.JACK, Suit.HEARTS))
      .play(Card.of(Rank.QUEEN, Suit.HEARTS))
      .play(Card.of(Rank.KING, Suit.HEARTS))
      .play(Card.of(Rank.ACE, Suit.HEARTS))
      .play(Card.of(Rank.TWO, Suit.HEARTS))
      .play(Card.of(Rank.LITTLE_JOKER, Suit.UNNECESSARY))
      .play(Card.of(Rank.BIG_JOKER, Suit.UNNECESSARY))
      .pass()
      .pass()
      .pass()
      .end();
    assertEquals(25, everything.countNumPoints());
  }
  
  /** Make sure we can count the points properly. */
  @Test
  public void testDuplicateCardPointCounting() {
    Set<Card> cards = ImmutableSet.of(
      Card.of(Rank.KING, Suit.HEARTS),
      Card.of(Rank.KING, Suit.CLUBS),
      Card.of(Rank.KING, Suit.SPADES),
      Card.of(Rank.TEN, Suit.HEARTS),
      Card.of(Rank.TEN, Suit.CLUBS));
    Play fullHouse = Play.one(cards);
    Trick walked = Trick.builder(fullHouse).pass().pass().pass().end();
    assertEquals(50, walked.countNumPoints());
  }
  
  // --------------------------------------------------------------------------
  // Failures

  /** Make sure we're not allowed to pass on the first Play. */
  @Test(expected = IllegalArgumentException.class)
  public void testFirstPassFail() {
    Trick.builder(Play.pass());
  }
  
  /** Make sure we're not allowed to pass four times in a row. */
  @Test(expected = IllegalPlayException.class)
  public void testFourPassesFail() {
    Trick.builder(Card.of(Rank.THREE, Suit.HEARTS))
      .play(Play.pass())
      .play(Play.pass())
      .play(Play.pass())
      .play(Play.pass());
  }
  
  @Test(expected = IllegalPlayException.class)
  public void testCardTooLowFail() {
    Trick.builder(Card.of(Rank.FIVE, Suit.HEARTS))
      .play(Card.of(Rank.THREE, Suit.HEARTS));
  }
}
