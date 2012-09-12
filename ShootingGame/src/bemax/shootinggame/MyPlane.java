package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class MyPlane extends Sprite {
	private Bitmap image;
	private float px, py;
	private final int MOVE = 20;

	public MyPlane(Bitmap img){
		super();
		image = img;
		px  = x = 240 - image.getWidth()/2;
		py = y = 280 - image.getHeight();
		dx = dy = 0;
	}

	public void move() {
		float ddx = px - x;
		float ddy = py - y;
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);

		if(len >= MOVE){
			dx = ddx * MOVE / len;
			dy = ddy * MOVE / len;
		}else{
			dx = ddx;
			dy = ddy;
		}

		x += dx;
		y += dy;

		values[Matrix.MTRANS_X] = x;
		values[Matrix.MTRANS_Y] = y;
		matrix.setValues(values);
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public void setPlace(float tx, float ty){
		px = tx - image.getWidth()/2;
		py = ty - image.getHeight()/2;
	}
}
