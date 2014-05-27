package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Rect;

/**
 * 敵キャラ「ゲコ」
 * 主人公機めがけて突っ込んでくる敵
 * @author masaaki
 *
 */
public class Geko extends Enemy {
	private final int DISP_SIZE = 96;		// 画面上の表示サイズ
	
	private boolean change;

	public Geko(Resources r){
		super(r);
		
		/* 画像をセットする */
		image = setImage(R.drawable.geko, DISP_SIZE); 			// 画像を読み込む
		shadowArry = getShadow();											// 当たり判定用の配列を作成
		imgWidth = image.getWidth();										// 画像の横幅
		imgHeight = image.getHeight();										// 画像の縦幅
		drawingExtent = new Rect(0, 0, imgWidth, imgHeight);	// 画像の描画範囲

		/* 撃破得点をセット */
		defeatPoint = 10;

		/* 初期化 */
		reset();
	}

	@Override
	public void move(MyPlane mp){
		switch(state){
		case LIVE:
		case HIT:
			if(change==false && mp.getY() - getY() <= 300){
				change = true;
				dy = 10;
			}
			if(change && mp.getX() < getX()){
				dx = getX() - mp.getX() > 5 ? -5 : mp.getX() - getX();
			}else if(change && mp.getX() > getX()){
				dx = mp.getX() - getX() > 5 ? 5 : mp.getX() - getX();
			}else{
				dx = 0;
			}
			drawingExtent.offset(dx, dy);

			matrix.setTranslate(getX(), getY());
			break;
		case DEAD:
			deadCount--;
			if(deadCount==0){
				state = HIDE;
				deadCount = 10;
				reset();
				matrix.setTranslate(getX(), getY());
			}
			break;
		case HIDE:
			hidingCount--;
			if(hidingCount==0){
				state = LIVE;
				hidingCount = 10;
			}
		}
	}

	/**
	 * 主人公機は関係ない動き
	 * [ゲコ]では未実装
	 */
	@Override
	public void move() {

	}

	/**
	 * リセット
	 */
	public void reset(){
		drawingExtent.offsetTo(rand.nextInt(480-imgWidth), -imgHeight);
		dx = 0;
		dy = 10;
		change = false;
		life = 1;
	}
}
