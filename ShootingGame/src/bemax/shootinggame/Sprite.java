package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	protected Resources res;
	protected Matrix matrix;
	protected float[] values;
	protected int dx, dy;
	protected Bitmap image;
	protected boolean[][] shadow;
	protected int imgWidth, imgHeight;
	protected Rect rect;

	/**
	 * コンストラクタ
	 */
	public Sprite(Resources r){
		// リソースの初期化
		res = r;

		// matrix初期化
		matrix = new Matrix();
		//values = new float[9];
		//matrix.getValues(values);
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
	protected static boolean[][] getShadow(Bitmap img){
		int h = img.getHeight();
		int w = img.getWidth();

		// 影配列を初期化
		boolean[][] sdw = new boolean[h][w];
		// ピクセル処理用の配列を初期化
		int[] pixels = new int[w * h];
		// imageからピクセル配列を生成
		img.getPixels(pixels, 0, w, 0, 0, w, h);

		for(int i=0; i<sdw.length; i++){
			for(int j=0; j<sdw[i].length;j++){
				// ピクセル配列からデータを取得
				int pixcel = pixels[ j + i * w ];
				// 透明度によって分岐
				if(Color.alpha(pixcel)==0){
					// 透明ならばfalse
					sdw[j][i] = false;
					// pixels[ j + i * imgWidth ] = Color.WHITE;
				}else{
					// 不透明ならばture
					sdw[j][i] = true;
					// pixels[ j + i * imgWidth ] = Color.BLACK;
				}
			}
		}
		/*
		Bitmap dummy = image.copy(Bitmap.Config.ARGB_8888, true);
		dummy.setPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);
		image = dummy;
		*/
		return sdw;
	}

	public Rect getRect(){
		return rect;
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

	protected Bitmap setImage(int id){
		return BitmapFactory.decodeResource(res, id);
	}

	public int getX(){
		return rect.left;
	}

	public int getY(){
		return rect.top;
	}
}
