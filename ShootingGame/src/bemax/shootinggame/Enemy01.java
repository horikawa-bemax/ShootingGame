package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Enemy01 extends Enemy {

	public Enemy01(Bitmap img){
		super();
		image = img;
		point = 20;
	}

	@Override
	public void move(MyPlane mp) {
		if(mp.getX() < x){
			dx = x-mp.getX()>5?-5:mp.getX()-x;
		}else if(mp.getX() > x){
			dx = mp.getX()-x>5?5:mp.getX()-x;
		}else{
			dx = 0;
		}
		x += dx;
		y += dy;

		matrix.setTranslate(x, y);

		if(y>780){
			reset();
		}
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	@Override
	public void move() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
