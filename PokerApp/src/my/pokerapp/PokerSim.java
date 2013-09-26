package my.pokerapp;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import my.pokerapp.Card;
import my.pokerapp.Card.Rank;
import my.pokerapp.Card.Suit;
import my.pokerapp.Hand;
import my.pokerapp.Hand.HandType;

public class PokerSim {
	
	private final static int HANDSIZE = 5;
	private boolean playerHasHand = false;
	private Hand playerHand;

	/**
	 * Copy of the deck that we use as a basis to shuffle new random decks.
	 */
	private static final ArrayList<Card> unShuffledDeck = new ArrayList<Card>();
	{
		int numSuits = Suit.values().length;
		int numRanks = Rank.values().length;
		for(int i = 0; i < numSuits; i++){
			Suit curSuit = Suit.values()[i];
			for(int j = 0; j < numRanks; j++){
				//Put cards in deck.
				Rank curRank = Rank.values()[j];
				Card cardToAdd = new Card(curRank,curSuit);
				unShuffledDeck.add(cardToAdd);
				System.out.println("Added " + cardToAdd);
			}
		}
	} 
	
	private ArrayList<Card> shuffledDeck;
	
	public Hand dealHand(){
		Hand hand = new Hand();
		int i = 0;
		while(i < HANDSIZE){
			Card randCard = shuffledDeck.remove(0);
			hand.add(randCard);
			i++;
		}
		playerHasHand = true;
		playerHand = hand;
		return hand;
	}

	public void init(Activity parent, Hand playerHand) {
		HandType playerHandType = playerHand.evaluate();
		Log.d("EvaluatedHand", playerHandType.getDisplayText());
		TextView infoView = (TextView) parent.findViewById(R.id.info_text_view);
		infoView.setText("You have: "+playerHandType.getDisplayText());
		infoView.invalidate();
	}

	public void setUpNewDeck() {
		shuffledDeck = new ArrayList<Card>(unShuffledDeck);
		Collections.shuffle(shuffledDeck);
	}
	
	public boolean playerHasHand() {
		return playerHasHand;
	}

	public Hand getPlayerHand() {
		return playerHand;
	}

	public void drawNewCards(boolean[] tradeCard) {
		for(int i = 0; i < playerHand.getSize(); i++){
			if(tradeCard[i]){
				Log.d("TradeCard", "Traded "+playerHand.getCardNumber(i));
				playerHand.tradeForCard(i, shuffledDeck.remove(0));
				Log.d("TradeCard", "Received "+playerHand.getCardNumber(i));
			}
		}
		
	}

	public void setPlayerHand(Hand newHand) {
		playerHand = newHand;
	}
}
