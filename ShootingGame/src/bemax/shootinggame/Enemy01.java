package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Rect;

public class Enemy01 extends Enemy {
	private boolean change;

	public Enemy01(Resources r){
		super(r);
		image = setImage(R.drawable.enemy01);
		shadow = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth,imgHeight);

		hp = 2;
		point = 20;

		deadcount = 10;
		hidecount = 10;

		reset();
	}

	@Override
	public void move(MyPlane mp){
		switch(state){
		case LIVE:
		case HIT:
			if(change==false && mp.getY() - getY() <= 300){
				change = true;
				dy = 10;
			}
			if(change && mp.getX() < getX()){
				dx = getX() - mp.getX() > 5 ? -5 : mp.getX() - getX();
			}else if(change && mp.getX() > getX()){
				dx = mp.getX() - getX() > 5 ? 5 : mp.getX() - getX();
			}else{
				dx = 0;
			}
			rect.offset(dx, dy);

			matrix.setTranslate(getX(), getY());
			break;
		case DEAD:
			deadcount--;
			if(deadcount==0){
				state = HIDE;
				deadcount = 10;
				reset();
				matrix.setTranslate(getX(), getY());
			}
			break;
		case HIDE:
			hidecount--;
			if(hidecount==0){
				state = LIVE;
				hidecount = 10;
			}
		}
	}

	@Override
	public void move() {

	}

	/**
	 * リセット
	 */
	public void reset(){
		rect.offsetTo(rand.nextInt(480-imgWidth), -imgHeight);
		dx = 0;
		dy = 15;
		change = false;
		hp = 2;
	}
}
