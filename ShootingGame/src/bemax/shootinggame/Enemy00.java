package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class Enemy00 extends Enemy{

	public Enemy00(Resources r) {
		super(r);
		image = setImage(R.drawable.enemy00);
		shadow = getShadow(image);
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		hp = 3;
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
			// 画面から消失
			if(rect.top > 800){
				reset();
			}
			matrix.setTranslate(getX(), getY());
			Log.d("x=",""+getX());
			Log.d("y=",""+getY());

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

	/**
	 * 動く
	 */
	public void move(MyPlane mp){
		move();
	}
}
