package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bigkero extends Enemy {
	private Paint paint;

	public Bigkero(Resources r) {
		super(r);
		image = setImage(R.drawable.bigkero,160);
		shadow = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		point = 20;

		reset();
	}

	@Override
	public void move() {
		switch(state){
		case LIVE:
		case HIT:
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

	public void reset(){
		rect.offsetTo(rand.nextInt(480-imgWidth), -imgHeight);
		dx = 0;
		dy = 5;
		hp = 5;
	}
}
