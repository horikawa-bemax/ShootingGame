package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * ゲームの背景クラス
 * @author masaaki horikawa
 * 2014.5.24
 */
public class BackScreen {
	private final int BGSCREEN_WIDTH = 480;	// 背景の幅
	private final int BGSCREEN_HEIGHT = 780;	// 背景の高さ
	private final int PARTS_SIZE = 96;				// 背景パーツのサイズ
	private final int MAP_RAW = 8;						// 画面に表示される背景マップの縦の個数
	private Bitmap backScreen;							// 背景のビットマップ
	private int scrollStartPoint;							// スクロール開始位置のポインタ
	private int mapLength;									// 背景マップの長さ
	private int scrollSpeed;									// 背景をスクロールするスピード
	private Bitmap[] partsArry;							// 背景パーツの配列
	private int[][] backScreenMap;						// 背景マップ
	private Rect srcRange;									// 背景画像の転送元範囲
	private Rect destRange;								// 背景画像の転送先範囲

	/**
	 * コンストラクタ
	 * @param res	リソース
	 */
	public BackScreen(Resources res){
		/* 背景パーツを読み込んで配列にセットする */
		partsArry = new Bitmap[]{
				BitmapFactory.decodeResource(res, R.drawable.bg00),
				BitmapFactory.decodeResource(res, R.drawable.bg01),
				BitmapFactory.decodeResource(res, R.drawable.bg02),
				BitmapFactory.decodeResource(res, R.drawable.bg03),
				BitmapFactory.decodeResource(res, R.drawable.bg04),
				BitmapFactory.decodeResource(res, R.drawable.bg05)
		};

		/* 背景マップを作る */
		backScreenMap = new int[][]{
			{0,1,0,0,0},
			{4,0,0,3,0},
			{0,0,0,0,0},
			{0,0,0,0,2},
			{0,0,1,0,0},
			{0,3,0,0,0},
			{0,0,0,0,4},
			{0,0,0,0,0},
			{0,5,0,0,0},
			{2,0,0,0,0},
			{0,0,0,4,0},
			{0,0,0,0,0},
			{0,0,3,0,0},
			{0,0,0,0,1},
			{0,1,0,0,0},
			{2,0,0,0,0},
			{0,0,4,0,0},
			{0,0,0,0,3},
			{0,1,0,0,0},
			{0,0,0,2,0}
		};

		/* マップスクロール用の変数を初期化 */
		mapLength = backScreenMap.length;
		scrollSpeed = 5;
		
		/* スクロール開始位置を指定 */
		scrollStartPoint =
			(mapLength - (MAP_RAW+2)) * PARTS_SIZE;				// マップの最後 - (1画面 + 2行)からスタート
		
		/* 背景用のビットマップの領域を初期化 */
		backScreen = Bitmap.createBitmap(
			BGSCREEN_WIDTH,															// 背景の幅
			PARTS_SIZE * (backScreenMap.length + (MAP_RAW+1)),		// 背景の高さ+1画面+1行分の余分
			Config.ARGB_8888);															// 32ビットフルカラーモード

		/* マップを描くキャンバスを準備 */
		Canvas c = new Canvas(backScreen);
		
		/* キャンバスに背景を描く */
		Matrix m = new Matrix();
		for(int i=0; i<backScreenMap.length + 9; i++){
			for(int j=0; j<5; j++){
				m.setTranslate(PARTS_SIZE*j, PARTS_SIZE*i);
				c.drawBitmap(partsArry[backScreenMap[(i%backScreenMap.length)][j]], m, null);
			}
		}

		/* 背景画像を転送するための矩形 */
		srcRange = new Rect();
		destRange = new Rect(0,0,BGSCREEN_WIDTH, BGSCREEN_HEIGHT);
	}

	/**
	 * キャンバスに背景を描く
	 * @param canvas		背景を描くキャンバス
	 */
	public void drawBackScreen(Canvas canvas){
		/* スクロール開始位置を、スピード分ずらす */
		scrollStartPoint -= scrollSpeed;
		
		/* マップの一番上まで来た時の処理 */
		if(scrollStartPoint < 0){
			scrollStartPoint = mapLength * PARTS_SIZE + scrollStartPoint;	//スクロール開始位置を下に移す
		}
		
		/* 背景ビットマップから、キャンバスに転送する */
		srcRange.set(0, scrollStartPoint, BGSCREEN_WIDTH, BGSCREEN_HEIGHT+scrollStartPoint);
		canvas.drawBitmap(backScreen,srcRange,destRange,null );
	}
}
