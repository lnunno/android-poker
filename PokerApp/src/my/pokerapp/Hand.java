package my.pokerapp;

import java.util.ArrayList;
import java.util.PriorityQueue;

import my.pokerapp.Card;
import my.pokerapp.Card.Rank;
import my.pokerapp.Card.Suit;

public class Hand {

	public static enum HandType {
		HIGH_CARD("High Card"), ONE_PAIR("One Pair"), TWO_PAIR("Two Pair"), THREE_OF_A_KIND(
				"Three of a Kind"), STRAIGHT("Straight"), FLUSH("Flush"), FULL_HOUSE(
				"Full House"), FOUR_OF_A_KIND("Four of a Kind"), STRAIGHT_FLUSH(
				"Straight Flush"), ROYAL_FLUSH("Royal Flush");

		private final String displayText;

		private HandType(String displayText) {
			this.displayText = displayText;
		}

		/**
		 * Returns text that is better formatted than the enum text.
		 */
		public String getDisplayText() {
			return displayText;
		}
	}

	private ArrayList<Card> cards = new ArrayList<Card>();
	private ArrayList<Card> sortedCards = new ArrayList<Card>();

	public int getSize() {
		return cards.size();
	}

	public void add(Card c) {
		cards.add(c);
	}
	
	public void tradeForCard(int cardIndexInHand, Card c){
		cards.set(cardIndexInHand, c);
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < cards.size(); i++)
			s += cards.get(i) + "  ";
		return s;
	}

	public Card getCardNumber(int i) {
		return cards.get(i);
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * Sorts hand based on rank. e.g. 6,3,2,4,5 will be 2,3,4,5,6. Duplicate
	 * ranks are ok.
	 */
	public Hand sortByRank() {
		// Use Sorted set.
		PriorityQueue<Card> sortedHand = new PriorityQueue<Card>(5,
				Card.getRankComparator());
		for (Card c : cards)
			sortedHand.add(c);
		sortedCards.clear();
		Card c = sortedHand.poll();
		while (c != null) {
			sortedCards.add(c);
			c = sortedHand.poll();
		}
		Hand sorted = new Hand();
		for(Card card : sortedCards) sorted.add(card);
		return sorted;
	}

	/**
	 * Determine what HandType (Straight,Flush, etc) is of the hand.
	 */
	public HandType evaluate() {
		// Sort cards in hand first.
		sortByRank();
		HandEvaluator evaluator = new HandEvaluator();
		return evaluator.evaluate(sortedCards);
	}

	/**
	 * Encapsulates the hand evaluation behavior of determining what a hand's rank is based on the all the cards' ranks and suits.
	 * @author Lucas
	 */
	private class HandEvaluator {
		
		public HandType evaluate(ArrayList<Card> cardsInHand){
			boolean straight = isStraight(cardsInHand);
			boolean flush = isFlush(cardsInHand);
			// By default, return HIGH_CARD since this is the most common.
			if (straight && flush)
				return HandType.STRAIGHT_FLUSH;
			else if (straight)
				return HandType.STRAIGHT;
			else if (flush)
				return HandType.FLUSH;
			else if (fourOfAKind(cardsInHand))
				return HandType.FOUR_OF_A_KIND;
			else if (fullHouse(cardsInHand))
				return HandType.FULL_HOUSE;
			else if (threeOfAKind(cardsInHand))
				return HandType.THREE_OF_A_KIND;
			else{
				//Can only two pair, one pair, or high card.
				int numPairs = countPairs(cardsInHand);
				switch(numPairs){
				case 1:
					return HandType.ONE_PAIR;
				case 2:
					return HandType.TWO_PAIR;
				default:
					return HandType.HIGH_CARD;
				}
			}
		}
		
		/* Assume sorted hand */
		private int countPairs(ArrayList<Card> hand) {
			int pairs = 0;
			Card prevCard = hand.get(0);
			for(int i = 1; i < hand.size(); i++){
				Card curCard = hand.get(i);
				if(curCard.getCardRank() == prevCard.getCardRank())
					pairs++;
				prevCard = curCard;
			}
			return pairs;
		}

		private boolean isPair(Card a, Card b) {
			return (a.getCardRank() == b.getCardRank());
		}

		/* Assume sorted hand and already caught full house cases. */
		private boolean threeOfAKind(ArrayList<Card> hand) {
			if (tripsLeft(hand) || tripsRight(hand)
					|| isPair(hand.get(1), hand.get(3)))
				return true;
			return false;
		}

		/* Assume sorted hand */
		private boolean fullHouse(ArrayList<Card> hand) {
			if (!(tripsLeft(hand) && isPair(hand.get(3), hand.get(4)))
					&& !(tripsRight(hand) && isPair(hand.get(0), hand.get(1))))
				return false;
			return true;
		}

		private boolean tripsRight(ArrayList<Card> hand) {
			return (hand.get(2).getCardRank() == hand.get(4).getCardRank());
		}

		private boolean tripsLeft(ArrayList<Card> hand) {
			return (hand.get(0).getCardRank() == hand.get(2).getCardRank());
		}

		/* Assume sorted hand */
		private boolean fourOfAKind(ArrayList<Card> hand) {
			if ((hand.get(0).getCardRank() != hand.get(3).getCardRank())
					&& (hand.get(1).getCardRank() != hand.get(4).getCardRank()))
				return false;
			return true;
		}

		/* Assume hand is sorted by rank. */
		private boolean isFlush(ArrayList<Card> hand) {
			Card first = hand.get(0);
			Suit firstSuit = first.getCardSuit();
			for (int i = 1; i < hand.size(); i++) {
				Card cur = hand.get(i);
				// All must be the same suit, therefore compare to first suit.
				if (cur.getCardSuit().compareTo(firstSuit) != 0)
					return false;
			}
			return true;
		}

		/* Assume hand is sorted by rank. */
		private boolean isStraight(ArrayList<Card> hand) {
			Card prevCard = hand.get(0); // prev card = first card to start.
			Rank prevRank = prevCard.getCardRank();
			for (int i = 1; i < hand.size(); i++) {
				Card curCard = hand.get(i);
				Rank curRank = curCard.getCardRank();
				if (curRank.ordinal() != prevRank.ordinal() + 1)
					return false;
				// Else continue evaluating.
				prevCard = curCard;
				prevRank = curRank;
			}
			return true;
		}
	}
}