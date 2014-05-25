package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Rect;

public class Kero extends Enemy{
	private final int DISP_SIZE = 96;		// 画面上の表示サイズ
	
	private final int HP = 1;

	public Kero(Resources r) {
		super(r);
		image = setImage(R.drawable.kero, DISP_SIZE);
		shadowArry = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		life = HP;
		defeatPoint = 10;

		reset();
	}

	/**
	 * 動く
	 */
	@Override
	public void move() {
		switch(state){
		case LIVE:
		case HIT:
			// 座標更新
			rect.offset(dx, dy);
			// 横壁で跳ね返る
			if(rect.left < 0){
				rect.offsetTo(0, rect.top);
				dx = -dx;
			}else if(rect.right > 480){
				rect.offsetTo(480-imgWidth, rect.top);
				dx = -dx;
			}

			matrix.setTranslate(getX(), getY());
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

	public void reset(){
		rect.offsetTo(rand.nextInt(480-imgWidth), -imgHeight);
		dx = rand.nextInt(5) - 9;
		dy = rand.nextInt(5) + 5;
	}

	/**
	 * 主人公機の動きに合わせた動き
	 * 「ケロ」では未実装
	 */
	@Override
	public void move(MyPlane mp) {
		
	}
}
