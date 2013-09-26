package my.pokerapp;


import my.pokerapp.Hand;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PokerAppActivity extends Activity {
	
	private Button getHandButton;
	private PokerSim pokerSim;
	private HandView handView;
	private Activity topLevelActivity;
	private static final boolean AUTO_SORT = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pokerSim = new PokerSim();
        retrieveViews();
        buildButtons();
        topLevelActivity = this;
    }

    /**
     * Get views from resource.
     */
	private void retrieveViews() {
		handView = (HandView) findViewById(R.id.hand_view);
	}

	private void buildButtons() {
		getHandButton = (Button) findViewById(R.id.button1);
		getHandButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pokerSim.setUpNewDeck();
				Hand hand = pokerSim.dealHand();
				System.out.println(hand);
				if(AUTO_SORT)hand.sortByRank();
				handView.loadNewCardImages(hand);
				pokerSim.init(topLevelActivity, hand);
			}
		});
		Button sortHandButton = (Button) findViewById(R.id.button2);
		sortHandButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(pokerSim.playerHasHand()){
					pokerSim.setPlayerHand(pokerSim.getPlayerHand().sortByRank());
					handView.loadNewCardImages(pokerSim.getPlayerHand());
				}
			}
		});
		Button tradeCardsButton = (Button) findViewById(R.id.button7);
		tradeCardsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				boolean[] tradeCardsBool = handView.getHighlightedCards();
				pokerSim.drawNewCards(tradeCardsBool);
				handView.loadNewCardImages(pokerSim.getPlayerHand());
				pokerSim.init(topLevelActivity, pokerSim.getPlayerHand());
			}
		});
	}
}