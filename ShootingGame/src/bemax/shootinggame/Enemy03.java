package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Enemy03 extends Enemy{
	private int px, py, len;
	private float deg;

	public Enemy03(Resources r) {
		super(r);
		image = setImage(R.drawable.enemy00);
		shadow = getShadow(image);
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		hp = 3;
		point = 40;

		px = 0;
		py = 0;
		deg = 0;
		len = 20;
	}

	@Override
	public void move() {
		deg += 0.004;
		if(deg>360){
			deg = deg%360;
		}
		double d = deg*180/Math.PI;
		int lx = (int)(Math.cos(d)*len);
		int ly = (int)(Math.sin(d)*len);

		rect.offset(dx, dy);

		if(rect.left < 0){
			rect.offsetTo(0, rect.top);
			dx = -dx;
		}else if(rect.right > 480){
			rect.offsetTo(480-imgWidth, rect.top);
			dx = -dx;
		}

		matrix.setTranslate(getX()+lx, getY()+ly);

		if(rect.top > 800){
			reset();
		}
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	public void move(MyPlane mp){
		move();
	}

	public void reset(){
		px = rand.nextInt() * (480 - imgWidth);
		py = rand.nextInt() - imgHeight;
		deg = 0;
	}
}
