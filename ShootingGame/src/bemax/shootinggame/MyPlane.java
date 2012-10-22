package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * 主人公機
 * @author Masaaki Horikawa
 * 2012.9.19
 */
public class MyPlane extends Sprite {
	private int tx, ty;
	private int bno;						// 弾番号
	private long shoottime;			// 弾を撃った時間
	private final int MOVE = 20;	// 移動量

	/**
	 * コンストラクタ
	 * @param img 主人公機のビットマップデータ
	 */
	public MyPlane(Resources r){
		super(r);
		image = BitmapFactory.decodeResource(res, R.drawable.myplane);
		shadow = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0,0,imgWidth, imgHeight);

		// 位置の初期化
		rect.offsetTo(240 - imgWidth/2, 700 - imgHeight);
		tx = rect.centerX();
		ty = rect.centerY();
		dx = dy = 0;

		// 弾番号の初期化
		bno = 0;
	}

	/**
	 * 動く
	 */
	public void move() {
		// 目標地点との距離を算出
		int ddx = tx - rect.centerX();
		int ddy = ty - rect.centerY();
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);

		// 移動先座標を算出
		if(len >= MOVE){
			dx = (int)(ddx * MOVE / len);
			dy = (int)(ddy * MOVE / len);
		}else{
			dx = ddx;
			dy = ddy;
		}

		if(rect.left + dx < 0){
			dx = 0 - rect.left;
		}else if(rect.right + dx > 480){
			dx = 480 - rect.right;
		}

		if(rect.top + dy < 0){
			dy = 0 - rect.top;
		}else if(rect.bottom + dy > 780){
			dy = 780 - rect.bottom;
		}

		rect.offset(dx, dy);

		// マトリックス
		matrix.setTranslate(rect.left, rect.top);
		//matrix.setValues(values);
	}

	/**
	 * 描画する
	 */
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	/**
	 * 目的地をセットする
	 * @param tx 目的地のx座標
	 * @param ty 目的地のy座標
	 */
	public void setPlace(float tx, float ty){
		this.tx = (int)tx;
		this.ty = (int)ty;
	}

	/**
	 * 弾を撃つ
	 * @param b 弾
	 */
	public void shoot(Bullet[] b){
		// 前に弾を撃ったときとの時差を計測
		long interval = System.currentTimeMillis() - shoottime;

		// 弾をうつ条件が整ったら
		if(b[bno].getReady() && interval > 50){
			// 弾を撃つ
			b[bno].shoot(this);

			// 次の弾の準備
			bno++;
			if (bno==b.length){
				bno = 0;
			}

			// 弾を撃った時刻を記録
			shoottime = System.currentTimeMillis();
		}
	}
}
