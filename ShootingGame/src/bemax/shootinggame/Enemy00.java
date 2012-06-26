package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Enemy00 extends Enemy{
	private float cx, cy, deg;
	static Bitmap image;
	double d;

	public Enemy00() {
		super(10);
		cx = 0;
		cy = 0;
		deg = 0;
	}

	@Override
	public void move() {
		deg += 0.002;
		if(deg>360){
			deg = deg%360;
		}
		d = deg*180/Math.PI;
		cx = (float)Math.cos(d);
		cy = (float)Math.sin(d);

		x +=  dx;
		y += dy;

		values[Matrix.MTRANS_X] = x + cx * 60;
		values[Matrix.MTRANS_Y] = y + cy * 40;
		matrix.setValues(values);

		if(y > 860){
			y = -80;
			x = rand.nextInt(340) + 60;
		}
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	public void move(MyPlane mp){
		move();
	}
}
