package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class MyBullet extends Bullet {
	private final int BULLET_SIZE_DEFAULT = 32;
	private final int BULLET_SIZE_BIG = 96;
	private final int BULLET_SPEED = 25;
	private Bitmap[] images;
	
	public MyBullet(Resources r) {
		super(r);
		
		/* 弾の画像読み込み */
		images = new Bitmap[2];
		images[0] = setImage(R.drawable.mybullet, BULLET_SIZE_DEFAULT);
		images[1] = setImage(R.drawable.mybullet, BULLET_SIZE_BIG);
		image = images[0];
		
		init();
	}

	@Override
	public void shoot(int x, int y) {
		/* 弾の初期位置を設定 */
		x = x - imgWidth / 2;
		drawingExtent.set(x, y, x+imgWidth, y+imgHeight);

		/* 弾の初速度を設定 */
		dy = -BULLET_SPEED;

		/* 発射準備NGにする＝＞発射中 */
		readyFlg = false;
	}
}
