package my.pokerapp.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import my.pokerapp.Card;
import my.pokerapp.Card.Rank;
import my.pokerapp.Card.Suit;
import my.pokerapp.Hand;
import my.pokerapp.Hand.HandType;

import org.junit.Before;
import org.junit.Test;

public class HandEvaluatorTest {
	
	private static final boolean DEBUG = true;
	
	private Hand basicStraight = new Hand();
	{
		//2,3,4,5,6
		basicStraight.add(new Card(Rank.SIX,Suit.CLUBS));
		basicStraight.add(new Card(Rank.TWO,Suit.CLUBS));
		basicStraight.add(new Card(Rank.THREE,Suit.HEARTS));
		basicStraight.add(new Card(Rank.FIVE,Suit.DIAMONDS));
		basicStraight.add(new Card(Rank.FOUR,Suit.SPADES));
	}

	private Hand basicNotStraight = generateHandBySingles(new Card(Rank.KING,Suit.CLUBS), 
			new Card(Rank.TWO, Suit.HEARTS), new Card(Rank.THREE, Suit.DIAMONDS), 
			new Card(Rank.FOUR,Suit.SPADES), new Card(Rank.FIVE,Suit.DIAMONDS));
	
	public Hand generateHandBySingles(Card ... cards){
		return generateHand(cards);
	}
	
	/* To generate hands easier for testing. */
	public Hand generateHand(Card[] cards){
		Hand h = new Hand();
		for(Card c : cards) h.add(c);
		return h;
	}

	@Test
	public void testStraight() {
		HandType resultingType = basicStraight.evaluate();
		System.out.println(resultingType);
		assertEquals(HandType.STRAIGHT, resultingType);
		resultingType = basicNotStraight.evaluate();
		System.out.println(resultingType);
		assertEquals(resultingType, HandType.HIGH_CARD);
	}
	
	public static void assertHandType(Hand hand, HandType handType){
		HandType evaluatedType = hand.evaluate();
		if(DEBUG) System.out.println(hand + " is a " + evaluatedType);
		assertEquals(handType, evaluatedType);
	}
	
	@Test
	public void testFlush(){
		Hand flushHand = generateHandBySingles(new Card(Rank.KING,Suit.CLUBS), 
				new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS), 
				new Card(Rank.FOUR,Suit.CLUBS), new Card(Rank.FIVE,Suit.CLUBS));
		assertHandType(flushHand,HandType.FLUSH);
		Hand notFlushHand = generateHandBySingles(new Card(Rank.KING,Suit.CLUBS), 
				new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.THREE, Suit.CLUBS), 
				new Card(Rank.FOUR,Suit.CLUBS), new Card(Rank.FIVE,Suit.HEARTS));
		assertHandType(notFlushHand,HandType.HIGH_CARD);
	}
	
	@Test
	public void testFourOfAKind(){
		Hand fourHand = generateHandBySingles(new Card(Rank.KING,Suit.CLUBS), 
				new Card(Rank.KING, Suit.HEARTS), new Card(Rank.KING, Suit.DIAMONDS), 
				new Card(Rank.KING,Suit.SPADES), new Card(Rank.FIVE,Suit.CLUBS));
		assertHandType(fourHand, HandType.FOUR_OF_A_KIND);
		Hand fourHand2 = generateHandBySingles(new Card(Rank.KING,Suit.CLUBS), 
				new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.FIVE, Suit.DIAMONDS), 
				new Card(Rank.FIVE,Suit.SPADES), new Card(Rank.FIVE,Suit.CLUBS));
		assertHandType(fourHand2, HandType.FOUR_OF_A_KIND);
	}
	
	@Test
	public void testFullHouse(){
		Hand fullHand = generateHandBySingles(new Card(Rank.JACK,Suit.CLUBS), 
				new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.JACK, Suit.DIAMONDS), 
				new Card(Rank.ACE,Suit.SPADES), new Card(Rank.ACE,Suit.CLUBS));
		assertHandType(fullHand, HandType.FULL_HOUSE);
		Hand fullHand2 = generateHandBySingles(new Card(Rank.NINE,Suit.CLUBS), 
				new Card(Rank.NINE, Suit.HEARTS), new Card(Rank.NINE, Suit.DIAMONDS), 
				new Card(Rank.EIGHT,Suit.SPADES), new Card(Rank.EIGHT,Suit.CLUBS));
		assertHandType(fullHand2, HandType.FULL_HOUSE);
	}
	
	@Test
	public void testThreeOfAKind(){
		Hand threeHand = generateHandBySingles(new Card(Rank.TEN,Suit.CLUBS), 
				new Card(Rank.TEN, Suit.HEARTS), new Card(Rank.TEN, Suit.DIAMONDS), 
				new Card(Rank.KING,Suit.SPADES), new Card(Rank.FIVE,Suit.CLUBS));
		assertHandType(threeHand, HandType.THREE_OF_A_KIND);
		Hand threeHand2 = generateHandBySingles(new Card(Rank.JACK,Suit.CLUBS), 
				new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.JACK, Suit.DIAMONDS), 
				new Card(Rank.FIVE,Suit.SPADES), new Card(Rank.TWO,Suit.CLUBS));
		assertHandType(threeHand2, HandType.THREE_OF_A_KIND);
	}
	
	@Test 
	public void testOnePair(){
		Hand onePair = generateHandBySingles(new Card(Rank.NINE,Suit.CLUBS), 
				new Card(Rank.NINE, Suit.HEARTS), new Card(Rank.ACE, Suit.DIAMONDS), 
				new Card(Rank.KING,Suit.SPADES), new Card(Rank.JACK,Suit.CLUBS));
		assertHandType(onePair, HandType.ONE_PAIR);
		Hand onePair2 = generateHandBySingles(new Card(Rank.NINE,Suit.CLUBS), 
				new Card(Rank.TEN, Suit.HEARTS), new Card(Rank.ACE, Suit.DIAMONDS), 
				new Card(Rank.KING,Suit.SPADES), new Card(Rank.ACE,Suit.CLUBS));
		assertHandType(onePair2, HandType.ONE_PAIR);
	}
	
	@Test 
	public void testTwoPair(){
		Hand twoPair = generateHandBySingles(new Card(Rank.NINE,Suit.CLUBS), 
				new Card(Rank.NINE, Suit.HEARTS), new Card(Rank.ACE, Suit.DIAMONDS), 
				new Card(Rank.KING,Suit.SPADES), new Card(Rank.ACE,Suit.CLUBS));
		assertHandType(twoPair, HandType.TWO_PAIR);
		Hand twoPair2 = generateHandBySingles(new Card(Rank.THREE,Suit.CLUBS), 
				new Card(Rank.TEN, Suit.HEARTS), new Card(Rank.TEN, Suit.DIAMONDS), 
				new Card(Rank.KING,Suit.SPADES), new Card(Rank.THREE,Suit.HEARTS));
		assertHandType(twoPair2, HandType.TWO_PAIR);
	}
	
	@Test 
	public void testHighCard(){
		Hand high = generateHandBySingles(new Card(Rank.THREE,Suit.CLUBS), 
				new Card(Rank.TWO, Suit.HEARTS), new Card(Rank.FOUR, Suit.DIAMONDS), 
				new Card(Rank.FIVE,Suit.SPADES), new Card(Rank.KING,Suit.CLUBS));
		assertHandType(high, HandType.HIGH_CARD);
		Hand high2 = generateHandBySingles(new Card(Rank.JACK,Suit.CLUBS), 
				new Card(Rank.TWO, Suit.HEARTS), new Card(Rank.QUEEN, Suit.DIAMONDS), 
				new Card(Rank.TEN,Suit.SPADES), new Card(Rank.KING,Suit.HEARTS));
		assertHandType(high2, HandType.HIGH_CARD);
	}

}
