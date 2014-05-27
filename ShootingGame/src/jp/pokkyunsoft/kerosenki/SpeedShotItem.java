package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Canvas;

/**
 * スピードショットのアイテム
 * このアイテムを取ると、弾の発射速度、発射可能弾数が増加する。
 * @author masaaki
 *
 */
public class SpeedShotItem extends Item {

	public SpeedShotItem(Resources r) {
		super(r);
		// TODO アイテム画像の読み込み、パラメータの初期化
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO キャンバスにアイテムを描く
		
	}

	@Override
	public void move() {
		// TODO アイテムの描画座標をdx,dy分移動し、書き換える
		
	}



}
