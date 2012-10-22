package bemax.shootinggame;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * 敵クラス
 * @author Masaaki Horikawa
 *
 */
public abstract class Enemy extends Sprite{
	protected int point;			// 得点
	protected Random rand;	// 乱数
	protected int hp;				// 体力
	protected byte state;			// 状態を表す変数
	protected int deadcount;	// 死んでる状態を続けるカウント
	protected int hidecount;		// 隠れている状態を続けるカウント
	protected Bitmap burst;		// 爆発画像
	protected Bitmap flash;
	protected static final byte LIVE = 0;	// 生きている状態
	protected static final byte DEAD = 1;	// 死んでいる状態
	protected static final byte HIDE = 2;	// 隠れている状態
	protected static final byte HIT = 3;

	/**
	 * コンストラクタ
	 */
	public Enemy(Resources r){
		super(r);
		// 体力を1に初期化
		hp = 1;
		// 状態を「生き」に設定
		state = LIVE;
		// 乱数オブジェクト生成
		rand = new Random();
		// DEAD状態を続けるカウント数
		deadcount = 10;
		hidecount = 15;
		// 爆発画像
		burst = BitmapFactory.decodeResource(r, R.drawable.burst);

		/* flash画像初期化 */
		flash = BitmapFactory.decodeResource(r, R.drawable.flash);
	}

	/**
	 * 主人公機にあわせて動く
	 * @param mp 主人公機
	 */
	public void move(MyPlane mp){
		move();
	}

	/**
	 * 動く
	 */
	@Override
	public abstract void move();

	/**
	 * 位置情報リセット
	 */
	public void reset(){
		rect.offsetTo(rand.nextInt(480-imgWidth), -imgHeight);
		dx = rand.nextInt(5) - 9;
		dy = rand.nextInt(5) + 5;
	}

	public void draw(Canvas canvas){
		switch(state){
		case LIVE:
		case HIT:
			canvas.drawBitmap(image, matrix, null);
			break;
		case DEAD:
			canvas.drawBitmap(burst, matrix, null);
			break;
		case HIDE:
		}
	}

	public void setState(byte s){
		state = s;
	}

	protected int damage(){
		hp--;
		return hp;
	}

	protected int getPoint(){
		return point;
	}
}