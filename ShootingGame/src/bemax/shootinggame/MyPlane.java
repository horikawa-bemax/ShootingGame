package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * 主人公機
 * @author Masaaki Horikawa
 * 2012.9.19
 */
public class MyPlane extends Sprite {
	private float px, py;
	private int bno;						// 弾番号
	private long shoottime;			// 弾を撃った時間
	private final int MOVE = 20;	// 移動量

	/**
	 * コンストラクタ
	 * @param img 主人公機のビットマップデータ
	 */
	public MyPlane(Bitmap img){
		super(img);

		// 位置の初期化
		px  = x = 240 - image.getWidth()/2;
		py = y = 280 - image.getHeight();
		dx = dy = 0;

		// 弾番号の初期化
		bno = 0;
	}

	/**
	 * 動く
	 */
	public void move() {
		// 目標地点との距離を算出
		float ddx = px - x;
		float ddy = py - y;
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);

		// 移動先座標を算出
		if(len >= MOVE){
			dx = ddx * MOVE / len;
			dy = ddy * MOVE / len;
		}else{
			dx = ddx;
			dy = ddy;
		}

		x += dx;
		y += dy;

		// マトリックス
		values[Matrix.MTRANS_X] = x;
		values[Matrix.MTRANS_Y] = y;
		matrix.setValues(values);
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
		px = tx - image.getWidth()/2;
		py = ty - image.getHeight()/2;
	}

	/**
	 * 弾を撃つ
	 * @param b 弾
	 */
	public void shoot(Bullet[] b){
		// 前に弾を撃ったときとの時差を計測
		long interval = System.currentTimeMillis() - shoottime;

		// 弾をうつ条件が整ったら
		if(b[bno].getReady() && interval > 200){
			// 弾を撃つ
			b[bno].shoot(x + image.getWidth()/2, y);

			// 次の弾の準備
			bno++;
			if (bno==5){
				bno = 0;
			}

			// 弾を撃った時刻を記録
			shoottime = System.currentTimeMillis();
		}
	}
}
