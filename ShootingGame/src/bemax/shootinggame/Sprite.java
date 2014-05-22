package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;

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
	protected boolean[][] getShadow(){
		int w = image.getWidth();
		int h = image.getHeight();

		// 影配列を初期化
		boolean[][] sdw = new boolean[h][w];
		// ピクセル処理用の配列を初期化
		int[] pixels = new int[w * h];
		// imageからピクセル配列を生成
		image.getPixels(pixels, 0, w, 0, 0, w, h);

		for(int i=0; i<sdw.length; i++){
			for(int j=0; j<sdw[i].length;j++){
				// ピクセル配列からデータを取得
				int pixcel = pixels[ j + i * w ];
				// 透明度によって分岐
				if(Color.alpha(pixcel)==0){
					// 透明ならばfalse
					sdw[i][j] = false;
					pixels[ j + i * imgWidth ] = Color.WHITE;
				}else{
					// 不透明ならばture
					sdw[i][j] = true;
					pixels[ j + i * imgWidth ] = Color.BLACK;
				}
			}
		}

		Bitmap dummy = image.copy(Bitmap.Config.ARGB_8888, true);
		dummy.setPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);
		image = dummy;

		return sdw;
	}

	/**
	 * 矩形を返す
	 * @return
	 */
	public Rect getRect(){
		return rect;
	}

	/**
	 * エリアから出たかどうかを判定する
	 */
	public boolean outOfArea(Rect area){
		return !Rect.intersects(rect, area);
	}
	
	public boolean hit(Sprite sp){
		boolean hit = false;
		Rect mr = new Rect(rect);
		Rect spr = sp.getRect();

		if(mr.intersect(spr)){
			int w = mr.width();
			int h = mr.height();

			bingo:
			for(int i=0; i<h; i++){
				for(int j=0; j<w; j++){
					if(shadow[i+mr.top-rect.top][j+mr.left-rect.left] && sp.shadow[i+mr.top-spr.top][j+mr.left-spr.left]){
						 hit = true;
						 break bingo;
					}
				}
			}
		}
		return hit;
	}

	protected Bitmap setImage(int id, int size){
		Bitmap result;
		BitmapFactory.Options op = new BitmapFactory.Options();
		return BitmapFactory.decodeResource(res, id);
	}

	public int getX(){
		return rect.left;
	}

	public int getY(){
		return rect.top;
	}
}
