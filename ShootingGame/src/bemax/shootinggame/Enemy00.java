package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Enemy00 extends Enemy{

	public Enemy00(Bitmap img) {
		super();

		image = img;
		point = 10;
		makeShadow();
	}

	/**
	 * 動く
	 */
	@Override
	public void move() {
		// 座標更新
		x +=  dx;
		y += dy;

		// 横壁で跳ね返る
		if(x<0){
			x = 0;
			dx = -dx;
		}else if(x > 480-image.getWidth()){
			x = 480 - image.getWidth();
			dx = -dx;
		}

		// トランスフォーム
		matrix.setTranslate(x, y);

		// 画面から消失
		if(y > 800){
			reset();
		}
	}

	/**
	 * 描く
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	/**
	 * 動く
	 */
	public void move(MyPlane mp){
		move();
	}

	/**
	 * 初期位置のリセット
	 */
	public void reset(){
		y = -image.getHeight() * 2;
		x = rand.nextFloat() * (480-image.getWidth());
		dx = 0;
		dy = rand.nextFloat() * 10 + 5; //=>5.0 ~ 14.999
	}
}
