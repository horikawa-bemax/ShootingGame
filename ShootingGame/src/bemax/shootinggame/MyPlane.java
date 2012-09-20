package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

/**
 * 主人公機クラス
 * @author Masaaki Horikawa
 * 2012.9.19
 */
public class MyPlane extends Sprite {
	private float px, py;
	private int bno;						//次に発射する弾の番号
	private long shoottime;			//弾を発射した時刻
	private final int MOVE = 20;		//主人公機の移動量

	/**
	 * コンストラクタ
	 * @param img 主人公機の画像データ
	 */
	public MyPlane(Bitmap img){
		super();
		//画像データを設定
		image = img;

		//初期値を設定
		px  = x = 240 - image.getWidth()/2;
		py = y = 280 - image.getHeight();
		dx = dy = 0;
		
		//次に発射する弾の番号を初期化
		bno = 0;
	}

	/**
	 * 移動する
	 */	
	public void move() {
		//現在地から目的地までの距離を計算する
		float ddx = px - x;
		float ddy = py - y;
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);  // 三平方の定理で計算

		//距離に応じて移動量を決める
		if(len >= MOVE){
			//距離が移動量より長いときは、移動量分しか動かさない
			dx = ddx * MOVE / len;
			dy = ddy * MOVE / len;
		}else{
			//距離が移動量より短いときは、目的地まで移動
			dx = ddx;
			dy = ddy;
		}

		//x座標,y座標を更新
		x += dx;
		y += dy;

		// トランスフォーム
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
	 * x座標を返す
	 * @return 主人公機のx座標
	 */
	public float getX(){
		return x;
	}

	/**
	 * y座標を返す
	 * @return 主人公機のy座標
	 */
	public float getY(){
		return y;
	}

	/**
	 * 目的地を設定する
	 * @param tx 目的地のx座標
	 * @param ty 目的地のy座標
	 */
	public void setPlace(float tx, float ty){
		px = tx - image.getWidth()/2;
		py = ty - image.getHeight()/2;
	}
	
	/**
	 * 弾を発射
	 * @param b 弾オブジェクト
	 */
	public void shoot(Bullet[] b){
		//前回弾を撃った時刻からの経過時間を算出する
		long interval = System.currentTimeMillis() - shoottime;
		
		//弾を撃ってもよい状態ならば、弾を撃つ
		if(b[bno].getReady() && interval > 200){
			//弾を発射させる
			b[bno].shoot(x + image.getWidth()/2, y);
			
			//次の弾の番号にリセット
			bno++;
			if (bno==5){
				bno = 0;
			}
			
			//弾を発射した時刻を記録
			shoottime = System.currentTimeMillis();
		}
	}
}
