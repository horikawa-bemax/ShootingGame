package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

// 主人公機クラス
public class MyPlane extends Sprite {
/*## インスタンス変数 ##*/
	// 計算用の変数
	private float px, py;

	// 主人公機の移動量(定数)
	private final int MOVE = 20;

/*## コンストラクタ ##*/
	public MyPlane(Bitmap img){
		super();
		// 画像の初期化
		image = img;

		// x座標の初期化
		px  = x = 240 - image.getWidth()/2;

		// y座標の初期化
		py = y = 280 - image.getHeight();

		// 移動量の初期化
		dx = dy = 0;
	}

/*## インスタンスメソッド ##*/
	// 移動する
	public void move() {
		// 三平方の定理により、現地点とタッチ点の距離を計算する
		float ddx = px - x;
		float ddy = py - y;
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);

		// 移動量の決定
		if(len >= MOVE){
			dx = ddx * MOVE / len;
			dy = ddy * MOVE / len;
		}else{
			dx = ddx;
			dy = ddy;
		}

		// 移動先の座標を決定
		x += dx;
		y += dy;

		// 主人公機を移動させる
		values[Matrix.MTRANS_X] = x;
		values[Matrix.MTRANS_Y] = y;
		matrix.setValues(values);
	}

	// 描画する
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	// x座標を返す
	public float getX(){
		return x;
	}

	// y座標を返す
	public float getY(){
		return y;
	}

	// タッチした位置から、px,pyを測定
	public void setPlace(float tx, float ty){
		px = tx - image.getWidth()/2;
		py = ty - image.getHeight()/2;
	}
}
