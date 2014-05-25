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
 * 2012.9.13 update 2014.5.22
 */
public abstract class Sprite {
	protected Resources res;						// リソース元
	protected Matrix matrix;						// 画像移動ようマトリックス
	//protected float[] values;						// 
	protected int dx;									// 横方向の移動量
	protected int dy;									// 縦方向の移動量
	protected Bitmap image;						// スプライトのイメージ画像
	protected boolean[][] shadowArry;		// 当たり判定用の配列
	protected int imgWidth;						// イメージ画像の幅
	protected int imgHeight;						// イメージ画像の高さ
	protected Rect rect;

	/**
	 * コンストラクタ
	 * @param r	リソース
	 */
	public Sprite(Resources r){
		// リソースの初期化
		res = r;

		// matrix初期化
		matrix = new Matrix();
	}

	/**
	 * キャンバスにこのスプライトを描く
	 * @param canvas 描画をするキャンパス
	 */
	public abstract void draw(Canvas canvas);

	/**
	 * このスプライトを動かす
	 */
	public abstract void move();

	/**
	 * このスプライトの影データを作る
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
	 * このスプライトの矩形を返す
	 * @return	矩形
	 */
	public Rect getRect(){
		return rect;
	}

	/**
	 * エリアから出たかどうかを判定する
	 * @param area	判定対象のエリア
	 */
	public boolean outOfArea(Rect area){
		return !Rect.intersects(rect, area);
	}
	
	/**
	 * 他のスプライトとの当たり判定を行う
	 * @param sp	判定対象のスプライト
	 * @return		当たっていればtrue
	 */
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
					if(shadowArry[i+mr.top-rect.top][j+mr.left-rect.left] && sp.shadowArry[i+mr.top-spr.top][j+mr.left-spr.left]){
						 hit = true;
						 break bingo;
					}
				}
			}
		}
		return hit;
	}

	/**
	 * リソースから画像ファイルを読み込んで、ビットマップを作る
	 * @param id	画像リソースのID
	 * @param size	取り込む画像の出力サイズ
	 * @return 取り込んだビットマップ
	 */
	protected Bitmap setImage(int id, int size){
		int decodeScale = 1;	// 画像デコード時のスケール
		int	longSide = 0;			// 長辺のサイズ
		Bitmap result;
		BitmapFactory.Options op = new BitmapFactory.Options();
		
		// ビットマップのサイズだけを先読みして、効果的に画像を読み込む準備
		op.inJustDecodeBounds = true;
		result = BitmapFactory.decodeResource(res, id, op);
		if(op.outWidth > op.outHeight){
			longSide = op.outWidth;
		}else{
			longSide = op.outHeight;
		}
		for(; decodeScale*size <= longSide; decodeScale *= 2){
			if(decodeScale*2*size > longSide) break;
		}
		op.inJustDecodeBounds = false;
		
		// ビットマップ画像を取り込む
		op.inSampleSize = decodeScale;
		result = BitmapFactory.decodeResource(res, id, op);
		Matrix matrix = new Matrix();
		float matrixScale = 1.0f;
		if(result.getWidth() > result.getHeight()){
			matrixScale = (float)size / result.getWidth();
		}else{
			matrixScale = (float)size / result.getHeight();
		}
		matrix.postScale(matrixScale, matrixScale);
		result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
		return result;
	}
	
	/**
	 * このスプライトのX座標を返す
	 * @return	X座標
	 */
	public int getX(){
		return rect.left;
	}

	/**
	 * このスプライトのY座標を返す
	 * @return	Y座標
	 */
	public int getY(){
		return rect.top;
	}
}
