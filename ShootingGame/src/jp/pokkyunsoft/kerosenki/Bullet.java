package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 弾クラス
 * @author Masaaki Horikawa
 */
public class Bullet extends Sprite {
	protected final int BULLET_SIZE = 48;		// 弾のサイズ
	private boolean readyFlg;							// 弾が発射状態かそうでないかを示すフラグ

	/**
	 * コンストラクタ
	 * @param r		リソース
	 */
	public Bullet(Resources r){
		super(r);
		
		/* 画像の初期化 */
		image = setImage(R.drawable.bullet, BULLET_SIZE);		// イメージを取り込み
		shadowArry = getShadow();											// 当たり判定用配列をセット
		imgWidth = image.getWidth();										// 画像の横幅
		imgHeight = image.getHeight();										// 画像の縦幅
		drawingExtent = new Rect(0, 0, imgWidth, imgHeight);	// 画像の描画範囲をセット

		/* 移動量の初期化 */
		dx = 0;
		dy = 0;

		/* フラグの初期化 */
		readyFlg = true;

		/* 弾の初期化 */
		reset();
	}

	/**
	 * 弾を描画する
	 * @param canvas	 描画するキャンバス
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
		/* 描画範囲をdx,dy分平衡移動する */
		drawingExtent.offset(dx, dy);

		/* 画面上部から消えた場合の処理 */
		if (drawingExtent.bottom <= 0){
			/* 弾の移動量を0にする */
			dy = 0;
			
			/* 発射可能状態にする */
			readyFlg = true;
			
			/* 弾の位置を初期化 */
			reset();
		}

		/* 新しい位置を求める */
		matrix.setTranslate(getX(), getY());
	}

	/**
	 * 弾を発射する
	 * @param x 発射位置のx座標
	 * @param y 発射位置のy座標
	 */
	public void shoot(MyPlane mp){
		/* 弾の初期位置を設定 */
		int x = mp.drawingExtent.centerX() - imgWidth / 2;
		int y = mp.drawingExtent.top;
		drawingExtent.set(x, y, x+imgWidth, y+imgHeight);

		/* 弾の初速度を設定 */
		dy = -25;

		/* 発射準備NGにする＝＞発射中 */
		readyFlg = false;
	}

	/**
	 * 弾が発射中かどうか
	 * @return 発射中ならtrue,未発射ならfalse
	 */
	public boolean getReady(){
		return readyFlg;
	}

	/**
	 * 弾の初期化
	 */
	public void reset(){
		/* 弾の位置を画面の外にする */
		drawingExtent.offsetTo(0, -imgHeight);
		
		/* 弾を発射可能状態にする */
		readyFlg = true;
	}
}
