package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy02 extends Enemy {
	private Paint paint;
	private int alp;

	public Enemy02(Bitmap img) {
		super();
		image = img;
		point = 30;
		alp = 128;
		paint = new Paint();
		paint.setAlpha(alp);
	}

	@Override
	public void move(MyPlane mp) {

	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, paint);

	}

	@Override
	public void move() {
		alp++;
		if(alp>199){
			alp = 0;
		}
		switch(alp/50){
		case 0:
			paint.setAlpha((alp%50)*5);
		case 1:
			break;
		case 2:
			paint.setAlpha(255 - (alp%50)*5);
		case 3:
			break;
		}

		x += dx;
		y += dy;

		if(x<0){
			x = 0;
			dx = -dx;
		}else if(x > 480-image.getWidth()){
			x = 480 - image.getWidth();
			dx = -dx;
		}

		matrix.setTranslate(x, y);

		if(y > 800){
			reset();
		}
	}

}
