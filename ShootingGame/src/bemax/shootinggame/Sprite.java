package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public abstract class Sprite {
	protected Matrix matrix;
	protected float[] values;
	protected float x, y, dx, dy;
	protected Bitmap image;

	public Sprite(){
		matrix = new Matrix();
		values = new float[9];
		matrix.getValues(values);
		x = 0;
		y = -80;
		dx = 0;
		dy = 0;
	}

	public abstract void draw(Canvas canvas);

	public abstract void move();
}
