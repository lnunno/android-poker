package my.pokerapp;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class HandView extends View implements OnTouchListener{

	private static final boolean DRAW_BOUNDS = false;
	private Paint paint = new Paint();
	private int width;
	private int height;
	private int cardWidth;
	private int cardHeight;
	private int cardBuffer;
	private int cardsTopY;
	private ArrayList<Drawable> handPics = new ArrayList<Drawable>();
	private ArrayList<Rect> cardBounds = new ArrayList<Rect>();
	private Resources resources;
	private boolean[] highlightedCards = new boolean[5];
	private boolean highlightPossible = false;
	
	public HandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		resources = getResources();
		this.setOnTouchListener(this);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		if(w == 0 || h == 0) return;
		width = getWidth();
		height = getHeight();
		cardWidth = width/6;
		cardHeight = height/4;
		cardBuffer = width/75;
		detectCardBounds();
	}
	
	/**
	 * Set up the rects that will hold the card images.
	 */
	private void detectCardBounds() {
		int left = cardBuffer;
		cardsTopY = height - ((height/4)+cardBuffer);
		int right = cardWidth-cardBuffer;
		int bottom = cardsTopY+cardHeight;
		Log.d("HandView", "Width="+width+" Height="+height);
		for(int i = 0; i < 5; i++){
			cardBounds.add(new Rect(left,cardsTopY,right,bottom));
			left+=(cardWidth+cardBuffer);
			right+=(cardWidth+cardBuffer);
		}
	}
	
	@Override
	public void onDraw(Canvas canvas){
		//Draw card bounds.
		if(DRAW_BOUNDS){
			drawCardBounds(canvas);
		}
		Log.d("HandView", "Width="+width+" Height="+height);
		//Draw card images.
		drawCardImagesInHand(canvas);
		highlightCards(canvas);
	}

	/**
	 * Highlight cards that have been selected.
	 * @param canvas
	 */
	private void highlightCards(Canvas canvas) {
		paint.setColor(Color.BLUE);
		paint.setAlpha(50);
		for(int i = 0; i < cardBounds.size(); i++){
			boolean highlighted = highlightedCards[i];
			if(highlighted){
				//Draw highlight over card.
				canvas.drawRect(cardBounds.get(i), paint);
			}
		}
		paint.setAlpha(255); //Fully Opaque. Reset.
	}

	private void drawCardImagesInHand(Canvas canvas) {
		for(int i = 0; i < handPics.size(); i++){
			//First set bounds.
			Drawable d = handPics.get(i);
			d.setBounds(cardBounds.get(i));
			//Then draw.
			d.draw(canvas);
		}
	}

	private void drawCardBounds(Canvas canvas) {
		paint.setColor(Color.BLACK);
		for(Rect r : cardBounds){
			canvas.drawRect(r, paint);
		}
		
	}

	public void loadNewCardImages(Hand hand) {
		//Clear previous images if there were any.
		if(handPics.size() > 0) handPics.clear();
		//Reset highlight values.
		for(int i = 0; i < highlightedCards.length; i++) highlightedCards[i] = false;
		for(Card c : hand.getCards()){
			String name = Card.toFileName(c);
			Log.d("LoadingImages", name);
			int id = resources.getIdentifier(name, "drawable", "my.pokerapp");
			Drawable d = resources.getDrawable(id);
			handPics.add(d);
		}
		highlightPossible = true;
		//Force re-draw.
		invalidate();
	}
	
	/*
	 * Detects when someone clicks the view. See if a card was clicked.
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public boolean onTouch(View v, MotionEvent event){
		//Don't process touches if highlights aren't possible.
		if(!highlightPossible) return true;
		//Only process down touches.
		if(event.getAction() != MotionEvent.ACTION_DOWN) return true;
		int x = (int) event.getX();
		int y = (int) event.getY();
		Log.d("HandView", "Touched " + x + ","+y);
		if(y < cardsTopY){
			//Ignore touches above the cards.
			return true;
		}
		//Check if the touch is within the bounds of each card.
		for(int i = 0; i < cardBounds.size(); i++){
			Rect r = cardBounds.get(i);
			if(r.contains(x,y)){
				boolean prev = highlightedCards[i];
				highlightedCards[i] = prev ^ true;
				if(highlightedCards[i] != prev ) invalidate();
				return true;
			}
		}
		return true;
	}

	public boolean[] getHighlightedCards() {
		return highlightedCards;
	}

}
