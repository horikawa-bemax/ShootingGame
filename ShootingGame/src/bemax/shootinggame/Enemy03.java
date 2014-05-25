package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Rect;

/**
 * まだ未実装な敵キャラ
 * @author masaaki
 *
 */
public class Enemy03 extends Enemy{
	private int len;
	private float deg;
	private final int HP = 3;

	public Enemy03(Resources r) {
		super(r);
		image = setImage(R.drawable.kero,80);
		shadowArry = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		life = HP;
		defeatPoint = 40;

		deg = 0;
		len = 40;

		reset();
	}

	@Override
	public void move() {
		switch(state){
		case LIVE:

			deg += 0.005;
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
			break;

		case DEAD:
			deadCount--;
			if(deadCount==0){
				state = HIDE;
				deadCount = 10;
				life = HP;
				reset();
				matrix.setTranslate(getX(), getY());
			}
			break;
		case HIDE:
			hidingCount--;
			if(hidingCount==0){
				state = LIVE;
				hidingCount = 10;
			}
		}
	}

	public void move(MyPlane mp){
		move();
	}

	public void reset(){
		deg = 0;
		rect.offsetTo(rand.nextInt(480-imgWidth), -imgHeight);
		dx = rand.nextInt(5) - 10;
		dy = rand.nextInt(5) + 5;
	}
}
