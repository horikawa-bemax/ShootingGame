package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Rect;

public class Enemy00 extends Enemy{
	private final int HP = 1;

	public Enemy00(Resources r) {
		super(r);
		image = setImage(R.drawable.enemy00);
		shadow = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		hp = HP;
		point = 10;

		reset();
	}

	/**
	 * 動く
	 */
	@Override
	public void move() {
		switch(state){
		case LIVE:
		case HIT:
			// 座標更新
			rect.offset(dx, dy);
			// 横壁で跳ね返る
			if(rect.left < 0){
				rect.offsetTo(0, rect.top);
				dx = -dx;
			}else if(rect.right > 480){
				rect.offsetTo(480-imgWidth, rect.top);
				dx = -dx;
			}

			matrix.setTranslate(getX(), getY());
			break;
		case DEAD:
			deadcount--;
			if(deadcount==0){
				state = HIDE;
				deadcount = 10;
				hp = HP;
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

}
