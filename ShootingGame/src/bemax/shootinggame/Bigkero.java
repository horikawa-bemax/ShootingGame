package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Rect;

/**
 * 敵キャラ、「ビグケロ」
 * 白い大型のケロ。 倒すとたまにアイテムを出す
 * @author masaaki horikawa
 * 2014.5.25
 */
public class Bigkero extends Enemy {
	private final int DISP_SIZE = 160;	// 画面上の表示サイズ

	/** コンストラクタ */
	public Bigkero(Resources r) {
		super(r);
		
		/* キャライメージ初期化 */
		image = setImage(R.drawable.bigkero, DISP_SIZE);		// イメージ読み込み
		shadowArry = getShadow();								// 当たり判定用の配列を作成
		imgWidth = image.getWidth();							// イメージの幅
		imgHeight = image.getHeight();							// イメージの高さ
		drawingExtent = new Rect(0, 0, imgWidth, imgHeight);		// イメージの範囲
		
		/* 撃破ポイント */
		defeatPoint = 20;

		/* 初期化 */
		reset();
	}

	/**
	 * キャラを動かす
	 */
	@Override
	public void move() {
		switch(state){
		case LIVE:	// 生きている状態
		case HIT:		// 弾が当たった状態
			
			/* dx,dy分だけキャラを移動させる */
			drawingExtent.offset(dx, dy);
			matrix.setTranslate(getX(), getY());
			
			break;
		case DEAD:	// 死んでいる状態
			deadCount--;
			if(deadCount==0){
				state = HIDE;
				deadCount = 10;
				reset();
				matrix.setTranslate(getX(), getY());
			}
			break;
		case HIDE:	// 出番待ちの状態
			hidingCount--;
			if(hidingCount==0){
				state = LIVE;
				hidingCount = 10;
			}
		}
	}

	/* 初期状態に戻す */
	public void reset(){
		drawingExtent.offsetTo(rand.nextInt(480-imgWidth), -imgHeight);
		dx = 0;
		dy = 5;
		life = 5;
	}

	/**
	 * 主人公機に合わせた動き
	 * 「ビグケロ」では未実装
	 */
	@Override
	public void move(MyPlane mp) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
