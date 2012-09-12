package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

// ゲームに登場するキャラクター全般に共通するクラス
public abstract class Sprite {
/*## インスタンス変数 ##*/
	// 画像の移動や回転を計算するための変数
	protected Matrix matrix;
	protected float[] values;

	// キャラクターのx座標、y座標、x軸方向の移動量、y軸方向の移動量
	protected float x, y, dx, dy;

	// キャラクターの画像データ
	protected Bitmap image;

/*## コンストラクタ ##*/
	public Sprite(){
		// matrixの初期化
		matrix = new Matrix();
		values = new float[9];
		matrix.getValues(values);

		// x座標、y座標、x増分、y増分の初期化
		x = 0;
		y = -80;
		dx = 0;
		dy = 0;
	}

/*## インスタンスメソッド ##*/
	// キャラクターをキャンバスに描画する
	public abstract void draw(Canvas canvas);

	// キャラクターを移動させる
	public abstract void move();
}
