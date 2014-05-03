package tricks.plays;

import cards.Card;

import java.util.Collection;

/**
 * Interface for classes which transform a collection of cards into
 * some sort of Play.
 *
 * These work on a best-effort basis. If they don't know how to convert
 * the given collection of cards into a valid Play, they'll return null.
 */
interface PlayTransformer {

    /**
     * Try to convert the given Collection of cards into a Play.
     * Return null if this Transformer can't figure out how.
     */
    Play tryConvert(Collection<? extends Card> cards);
}
