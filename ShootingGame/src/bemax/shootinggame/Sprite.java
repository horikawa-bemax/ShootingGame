package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

/**
 * スプライトクラス
 * @author Masaaki horikawa
 * 2012.9.13
 */
public abstract class Sprite {
	protected Matrix matrix;
	protected float[] values;
	protected float x, y, dx, dy;
	protected Bitmap image;
	protected boolean[][] shadow;

	/**
	 * コンストラクタ
	 */
	public Sprite(){
		// matrix初期化
		matrix = new Matrix();
		values = new float[9];
		matrix.getValues(values);

		// パラメータ初期化
		x = 0;
		y = -80;
		dx = 0;
		dy = 0;
	}

	/**
	 * 描く
	 * @param canvas 描画をするキャンパスオブジェクト
	 */
	public abstract void draw(Canvas canvas);

	/**
	 * 動く
	 */
	public abstract void move();
	
	public void makeShadow(){
		shadow = new boolean[image.getHeight()][image.getWidth()];
		int[] pixels = new int[image.getWidth()*image.getHeight()];
		image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
		for(int i=0; i<shadow.length; i++){
			for(int j=0; j<shadow[i].length;j++){
				int pixcel = pixels[j+i*image.getWidth()];
				if(Color.alpha(pixcel)==0){
					shadow[j][i] = false;
					pixels[j+i*image.getWidth()] = Color.WHITE;
				}else{
					shadow[j][i] = true;
					pixels[j+i*image.getWidth()] = Color.BLACK;
				}
			}
		}
		Bitmap dummy = image.copy(Bitmap.Config.ARGB_8888, true);
		dummy.setPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
		image = dummy;
	}
}
