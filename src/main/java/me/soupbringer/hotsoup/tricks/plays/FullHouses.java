package me.soupbringer.hotsoup.tricks.plays;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;

import me.soupbringer.hotsoup.deck.Card;
import me.soupbringer.hotsoup.deck.Rank;
import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Maps;

/**
 * The Play for one or more consecutive full houses.
 */
public class FullHouses extends Play {

  /**
   * Construct a new play with FullHouses.
   */
  private FullHouses(Collection<? extends Card> cards) {
    super(ImmutableSortedMultiset.orderedBy(makeSorter(cards)).addAll(cards).build());
  }
  
  /** Parse the given cards into a FullHouses, and return null if we can't. */
  public static FullHouses tryFullHouses(Collection<? extends Card> cards) {
    return isLegalFullHouses(cards) ? new FullHouses(cards) : null;
  }


  @Override
  public boolean isLegalGiven(Play lastPlay) {
    return lastPlay instanceof FullHouses &&
            ((FullHouses) lastPlay).numCards() == this.numCards() &&
            ((FullHouses) lastPlay).lowestRank().compareTo(this.lowestRank()) < 0;
  }

  // ------------------------------------------------------------------------
  // Queries

  /**
   * The number of cards in this play.
   */
  private int numCards() {
    return getCards().size();
  }

  /**
   * Make a new Comparator that sorts the cards so that the triple always occurs before
   * the double, regardless of Rank.
   * 
   * Once sorted by 'set of three' and 'set of two', this should sort the
   * cards by rank (lowest ranks first).
   */
  private static Comparator<Card> makeSorter(Collection<? extends Card> cards) {
    final NavigableMap<Rank, Integer> ranks = PlayUtils.newRankMap(cards);

    return new Comparator<Card>() {
      @Override
      public int compare(Card card1, Card card2) {
        Rank rank1 = card1.getRank();
        Rank rank2 = card2.getRank();
        
        checkArgument(ranks.containsKey(rank1), "Unexpected card: " + card1);
        checkArgument(ranks.containsKey(rank2), "Unexpected card: " + card2);

        // First Make sure the triples come before the doubles.
        int quantityDiff = ranks.get(rank1) - ranks.get(rank2);
        if (quantityDiff != 0) {
          return quantityDiff;
        }
        
        // If that didnt' help, then compare the ranks as normal.
        return rank1.compareTo(rank2);
      }
    };
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
}
