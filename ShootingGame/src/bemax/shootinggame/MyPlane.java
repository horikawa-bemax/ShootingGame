package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class MyPlane extends Sprite {
	static Bitmap image;

	public MyPlane(){
		super();
	}

	public void move() {
		x += dx;
		y += dy;
		if(x<0 || x > 399){
			dx = -dx;
		}
		if(y<0 || y > 699){
			dy = -dy;
		}
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
}
