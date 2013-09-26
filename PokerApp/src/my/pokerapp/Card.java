package my.pokerapp;

import java.util.Comparator;


public class Card{
	
	public static enum Suit { HEARTS, SPADES, DIAMONDS, CLUBS};
	public static enum Rank {
		TWO ("2", 2),
		THREE ("3",3),
		FOUR("4",4),
		FIVE("5",5),
		SIX("6",6),
		SEVEN("7",7),
		EIGHT("8",8),
		NINE("9",9),
		TEN("10",10),
		JACK("jack",11),
		QUEEN("queen",12),
		KING("king",13),
		ACE("ace",14);
		
		private final String fileTranslation;
		private final int value;
		private Rank(String fileTranslation, int value){
			this.fileTranslation = fileTranslation;
			this.value = value;
		}
		public String getFileTranslation() {
			return fileTranslation;
		}
		public int getValue(){
			return value;
		}
		};
	
	private final Rank cardRank;
	private final Suit cardSuit;
	
	public Card(Rank r, Suit s){
		cardRank = r;
		cardSuit = s;
	}

	public Rank getCardRank() {
		return cardRank;
	}
	
	public Suit getCardSuit(){
		return cardSuit;
	}

	@Override
	public String toString() {
		return cardRank + " of " + cardSuit;
	}
	
	/**
	 * Takes a card and returns the file name s.t. it can be retrieved from 
	 * @return
	 */
	public static final String toFileName(Card c){
		String name = "card"+c.getCardRank().getFileTranslation()+"of"+c.getCardSuit().toString().toLowerCase();
		return name;
	}
	
	private final static Comparator<Card> rankComparator = new Comparator<Card>(){

		public int compare(Card lhs, Card rhs) {
			//Just use the rank's comparator.
			return lhs.getCardRank().compareTo(rhs.getCardRank());
		}
		
	};
	public static final Comparator<Card> getRankComparator(){
		return rankComparator;
	}
}
