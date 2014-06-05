package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 主人公機
 * @author Masaaki Horikawa
 * 2012.9.19
 */
public class MyPlane extends Sprite {
	/* 移動に関する変数 */
	private final int MOVE = 20;	// 移動量
	private int tx, ty;
	/* 弾に関する変数 */
	private int bno;				// 弾番号
	private long shoottime;			// 弾を撃った時間
	private int firingInterval;		// 発射間隔
	private int bulletNum;			// 連射できる弾数
	
	private final int DEFAULT_BULLET_NUM = 3;
	private final int DEFAULT_FIRING_INTERVAL = 150;
	
	/**
	 * コンストラクタ
	 * @param img 主人公機のビットマップデータ
	 */
	public MyPlane(Resources r){
		super(r);
		image = setImage(R.drawable.myplane,96);
		shadowArry = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		drawingExtent = new Rect(0,0,imgWidth, imgHeight);

		reset();
	}


	/**
	 * 動く
	 */
	public void move() {
		// 目標地点との距離を算出
		int ddx = tx - drawingExtent.centerX();
		int ddy = ty - drawingExtent.centerY() - 80;
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);

		// 移動先座標を算出
		if(len >= MOVE){
			dx = (int)(ddx * MOVE / len);
			dy = (int)(ddy * MOVE / len);
		}else{
			dx = ddx;
			dy = ddy;
		}

		if(drawingExtent.left + dx < 0){
			dx = 0 - drawingExtent.left;
		}else if(drawingExtent.right + dx > 480){
			dx = 480 - drawingExtent.right;
		}

		if(drawingExtent.top + dy < 0){
			dy = 0 - drawingExtent.top;
		}else if(drawingExtent.bottom + dy > 780){
			dy = 780 - drawingExtent.bottom;
		}

		drawingExtent.offset(dx, dy);

		// マトリックス
		matrix.setTranslate(drawingExtent.left, drawingExtent.top);
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
		if(b[bno].getReady() && interval > firingInterval){
			// 弾を撃つ
			b[bno].shoot(this.drawingExtent.centerX(), this.drawingExtent.top);

			// 次の弾の準備
			bno++;
			if (bno >= bulletNum){
				bno = 0;
			}

			// 弾を撃った時刻を記録
			shoottime = System.currentTimeMillis();
		}
	}

	/**
	 * 弾の発射が早くなる
	 */
	public void speedUp(){
		bulletNum = DEFAULT_BULLET_NUM * 2;
		firingInterval = DEFAULT_FIRING_INTERVAL / 2;
	}
	
	/**
	 * 弾の威力が上がる
	 */
	public void powerUp(){
		bulletNum = DEFAULT_BULLET_NUM * 2;
		firingInterval = DEFAULT_FIRING_INTERVAL / 2;
	}
	
	/**
	 * 弾が広範囲に飛ぶ
	 */
	public void wideUp(){
		
	}

	@Override
	protected void reset() {
		// 位置の初期化
		drawingExtent.offsetTo(240 - imgWidth/2, 700 - imgHeight);
		tx = drawingExtent.centerX();
		ty = drawingExtent.centerY();
		dx = dy = 0;

		/* 発射間隔の初期化 */
		firingInterval = DEFAULT_FIRING_INTERVAL;
		bulletNum = DEFAULT_BULLET_NUM;
	}
}
