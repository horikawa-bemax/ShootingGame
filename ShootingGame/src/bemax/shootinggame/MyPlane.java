package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class MyPlane extends Sprite {
	private Bitmap image;
	private float px, py;

	public MyPlane(Bitmap img){
		super();
		image = img;
		px  = x = 240 - image.getWidth()/2;
		py = y = 280 - image.getHeight();
		dx = dy = 0;
	}

	public void move() {
		dx = px - x;
		dy = py - y;

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

	public void setPlace(float x, float y){
		px = x - image.getWidth()/2;
		py = y - image.getHeight()/2;
	}
}
