package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Enemy02 extends Enemy {
	private Paint paint;
	private int alp;

	public Enemy02(Resources r) {
		super(r);
		image = BitmapFactory.decodeResource(res, R.drawable.enemy02);
		shadow = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		hp = 3;
		point = 30;

		alp = 128;
		paint = new Paint();
		paint.setAlpha(alp);
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

		rect.offset(dx, dy);

		if(rect.left < 0){
			rect.offsetTo(0, rect.top);
			dx = -dx;
		}else if(rect.right > 480){
			rect.offsetTo(480 - imgWidth, rect.top);
			dx = -dx;
		}

		matrix.setTranslate(getX(), getY());

		if(rect.top > 800){
			reset();
		}
	}

}
