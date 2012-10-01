package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

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
	protected int imgWidth, imgHeight;

	/**
	 * コンストラクタ
	 */
	public Sprite(Bitmap img){
		// matrix初期化
		matrix = new Matrix();
		values = new float[9];
		matrix.getValues(values);

		// パラメータ初期化
		x = 0;
		y = -80;
		dx = 0;
		dy = 0;

		// image初期化
		image = img;
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		makeShadow();
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

	/**
	 * 影データを作る
	 */
	public void makeShadow(){
		// 影配列を初期化
		shadow = new boolean[imgHeight][imgWidth];
		// ピクセル処理用の配列を初期化
		int[] pixels = new int[imgWidth * imgHeight];
		// imageからピクセル配列を生成
		image.getPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);

		for(int i=0; i<shadow.length; i++){
			for(int j=0; j<shadow[i].length;j++){
				// ピクセル配列からデータを取得
				int pixcel = pixels[ j + i * imgWidth ];
				// 透明度によって分岐
				if(Color.alpha(pixcel)==0){
					// 透明ならばfalse
					shadow[j][i] = false;
					// pixels[ j + i * imgWidth ] = Color.WHITE;
				}else{
					// 不透明ならばture
					shadow[j][i] = true;
					// pixels[ j + i * imgWidth ] = Color.BLACK;
				}
			}
		}
		/*
		Bitmap dummy = image.copy(Bitmap.Config.ARGB_8888, true);
		dummy.setPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);
		image = dummy;
		*/
	}

	public Rect getRect(){
		return new Rect((int)x, (int)y, (int)x+imgWidth, (int)y+imgHeight);
	}

	public boolean hit(Sprite sp){
		Rect mr = getRect();
		Rect spr = sp.getRect();

		if(mr.intersect(spr)){
			Rect rc = new Rect(mr);
			if(mr.contains(spr.left, spr.top)){
				rc.left = spr.left;
				rc.top = spr.top;
			}
			if(mr.contains(spr.right, spr.top)){
				rc.right = spr.right;
				rc.top = spr.top;
			}
			if(mr.contains(spr.left, spr.bottom)){
				rc.left = spr.left;
				rc.bottom = spr.bottom;
			}
			if(mr.contains(spr.right, spr.bottom)){
				rc.right = spr.right;
				rc.bottom = spr.bottom;
			}
			Log.d("left",""+rc.left);
			Log.d("top",""+rc.top);
			Log.d("right",""+rc.right);
			Log.d("bottom",""+rc.bottom);

			int w = rc.right - rc.left;
			int h = rc.bottom - rc.top;

			for(int i=0; i<h; i++){
				for(int j=0; j<w; j++){
					if(shadow[i+rc.top-mr.top][j+rc.left-mr.left]&sp.shadow[i+rc.top-spr.top][j+rc.left-spr.left]){
						return true;
					}
				}
			}

		}
		return false;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}
}
