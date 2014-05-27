package jp.pokkyunsoft.kerosenki;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * 敵の抽象クラス。すべての敵キャラの親クラス
 * @author Masaaki Horikawa
 * 
 */
public abstract class Enemy extends Sprite{
	protected static final byte LIVE = 0;	// 生きている状態
	protected static final byte DEAD = 1;	// 死んでいる状態
	protected static final byte HIDE = 2;	// 出番待ちの状態
	protected static final byte HIT = 3;		// 弾が当たった状態
	protected int defeatPoint;						// 破壊得点
	protected Random rand;						// 乱数
	protected int life;									// 体力
	protected byte state;							// 状態を表す変数
	protected int deadCount;						// 死んでいる状態を続けるカウント
	protected int hidingCount;						// 隠れている状態を続けるカウント
	protected Bitmap burstImage;				// 爆発画像


	/**
	 * コンストラクタ
	 */
	public Enemy(Resources r){
		super(r);
		
		/* パラメータの初期化 */
		life = 1;								// 体力を1に初期化
		state = LIVE;					// 状態を「生き」に設定
		rand = new Random();		// 乱数オブジェクト生成
		deadCount = 10;				// 死んでいる状態を続けるカウント数
		hidingCount = 15;				// 再登場するまでのカウント数

		/* 爆発画像を読み込む */
		burstImage = BitmapFactory.decodeResource(r, R.drawable.burst);
	}

	/**
	 * 主人公機の位置を狙ってくる敵の動き
	 * @param mp 主人公機
	 */
	public abstract void move(MyPlane mp);

	/**
	 * 敵の動き
	 */
	@Override
	public abstract void move();

	/**
	 * 初期データに戻す
	 */
	public abstract void reset();

	/**
	 * 現在の状態に合わせて、キャンバス上に描く
	 * @param canvas		キャンバス
	 */
	public void draw(Canvas canvas){
		switch(state){
		case LIVE:	// 生きている状態
			canvas.drawBitmap(image, matrix, null);
			break;
		case DEAD:	// やられた状態
			canvas.drawBitmap(burstImage, matrix, null);
			break;
		case HIDE:	// 出番待ちの状態
		}
	}

	/**
	 * ステータスをセットする
	 * @param s		ステータス
	 */
	public void setState(byte s){
		state = s;
	}

	/**
	 * ダメージを受ける
	 * @return	ダメージを受けた後の残りライフ
	 */
	protected int damage(){
		life--;
		return life;
	}

	/**
	 * 撃破ポイントを返す
	 * @return
	 */
	protected int getDefeatPoint(){
		return defeatPoint;
	}
}