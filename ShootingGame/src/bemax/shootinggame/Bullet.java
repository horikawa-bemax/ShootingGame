package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 弾クラス
 * @author Masaaki Horikawa
 */
public class Bullet extends Sprite {
	private boolean ready;	//弾が発射状態かそうでないかを示すフラグ

	public Bullet(Resources r){
		super(r);
		image = setImage(R.drawable.bullet,48);
		shadow = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		rect = new Rect(0, 0, imgWidth, imgHeight);

		dx = 0;
		dy = 0;

		ready = true;

		reset();
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
		// 矩形を平行移動
		rect.offset(dx, dy);

		//画面上部から消えた場合の処理
		if (rect.bottom <= 0){
			// 弾の移動量を0にする
			dy = 0;
			// 発射準備OKにする
			ready = true;
			// 弾の位置を初期化
			reset();
		}

		//トランスフォーム
		matrix.setTranslate(getX(), getY());
	}

	/**
	 * 弾を発射させる
	 * @param x 発射位置のx座標
	 * @param y 発射位置のy座標
	 */
	public void shoot(MyPlane mp){
		//弾の初期位置を設定
		int x = mp.rect.centerX() - imgWidth / 2;
		int y = mp.rect.top;
		rect.set(x, y, x+imgWidth, y+imgHeight);

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
		rect.offsetTo(0, -imgHeight);
		ready = true;
	}
}
