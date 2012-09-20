package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * スプライト（妖精さん）クラス
 * @author Masaaki.horikawa
 * 2012.9.13
 */
public abstract class Sprite {
	protected Matrix matrix;
	protected float[] values;
	protected float x, y, dx, dy;
	protected Bitmap image;

	/**
	 * コンストラクタ
	 */
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

	/**
	 * 画面に描画する
	 * @param canvas 描画する画面オブジェクト
	 */
	public abstract void draw(Canvas canvas);

	/**
	 * 移動する
	 */
	public abstract void move();
}
