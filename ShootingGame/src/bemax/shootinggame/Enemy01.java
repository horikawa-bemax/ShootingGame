package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Enemy01 extends Enemy {
	static Bitmap image;

	public Enemy01(){
		super(20);
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
			y = -80;
			x = rand.nextInt(400);
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
