package tricks.plays;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.collect.ImmutableSortedMultiset;

import deck.Card;
import deck.Rank;

/**
 * The Play for one or more consecutive full houses.
 */
public class FullHouses extends Play {

  /**
   * Construct a new play with FullHouses.
   */
  FullHouses(Collection<? extends Card> cards) {
    super(ImmutableSortedMultiset.orderedBy(makeSorter(cards)).addAll(cards).build());
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
}
