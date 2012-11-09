package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Enemy02 extends Enemy {
	private Paint paint;
	private int alp;

	public Enemy02(Resources r) {
		super(r);
		image = BitmapFactory.decodeResource(res, R.drawable.enemy02);
		shadow = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		hp = 1;
		point = 30;

		alp = 128;
		paint = new Paint();
		paint.setAlpha(alp);
	}

	@Override
	public void move() {
		switch(state){
		case LIVE:
		case HIT:
			alp++;
			if(alp>199){
				alp = 0;
			}
			switch(alp/50){
			case 0:
				paint.setAlpha((alp%50)*5);
			case 1:
				break;
			case 2:
				paint.setAlpha(255 - (alp%50)*5);
			case 3:
				break;
			}

			rect.offset(dx, dy);

			if(rect.left < 0){
				rect.offsetTo(0, rect.top);
				dx = -dx;
			}else if(rect.right > 480){
				rect.offsetTo(480 - imgWidth, rect.top);
				dx = -dx;
			}

			matrix.setTranslate(getX(), getY());

			if(rect.top > 800){
				reset();
			}

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
		dy = 10;
		hp = 2;
	}
}
