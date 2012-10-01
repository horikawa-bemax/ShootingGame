package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * 弾クラス
 * @author Masaaki Horikawa
 */
public class Bullet extends Sprite {
	private boolean ready;	//弾が発射状態かそうでないかを示すフラグ

	public Bullet(Bitmap img){
		super(img);
		dx = 0;
		dy = 0;
		ready = true;
	}

	/**
	 * 弾を画面に描画する
	 */
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	/**
	 * 弾を移動させる
	 */
	@Override
	public void move() {
		//弾のy座標を更新
		y += dy;

		//画面上部から消えた場合の処理
		if (y < -image.getHeight()){
			//弾の移動量を0にする
			dy = 0;

			//発射準備OKにする
			ready = true;
		}

		//トランスフォーム
		values[Matrix.MTRANS_X] = x;
		values[Matrix.MTRANS_Y] = y;
		matrix.setValues(values);
	}

	/**
	 * 弾を発射させる
	 * @param x 発射位置のx座標
	 * @param y 発射位置のy座標
	 */
	public void shoot(float bx, float by){
		//弾の初期位置を設定
		x = bx - image.getWidth()/2;
		y = by;

		//弾の移動量を設定
		dy = -25;

		//発射準備NGにする＝＞発射中
		ready = false;
	}

	/**
	 * 弾が発射中かどうか
	 * @return 発射中ならtrue,未発射ならfalse
	 */
	public boolean getReady(){
		return ready;
	}

	public void reset(){
		x = 0;
		y = -image.getWidth();
		ready = true;
	}
}
