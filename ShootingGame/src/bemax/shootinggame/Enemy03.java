package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Enemy03 extends Enemy{
	private float px, py, deg;
	double d;

	public Enemy03(Bitmap img) {
		super();
		image = img;
		point = 10;
		px = 0;
		py = 0;
		deg = 0;
	}

	@Override
	public void move() {
		deg += 0.004;
		if(deg>360){
			deg = deg%360;
		}
		d = deg*180/Math.PI;
		px = (float)Math.cos(d);
		py = (float)Math.sin(d);

		x +=  dx;
		y += dy;

		if(x<0){
			x = 0;
			dx = -dx;
		}else if(x > 480-image.getWidth()){
			x = 480 - image.getWidth();
			dx = -dx;
		}

		matrix.setTranslate(x + px * 60, y + py * 60);

		if(y > 860){
			reset();
		}
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	public void move(MyPlane mp){
		move();
	}

}
