package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Canvas;

/**
 * ワイドショットのアイテム。
 * このアイテムを取ると、弾が広範囲に発射できる。
 * @author 
 *
 */
public class WideShotItem extends Item {

	public WideShotItem(Resources r) {
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
