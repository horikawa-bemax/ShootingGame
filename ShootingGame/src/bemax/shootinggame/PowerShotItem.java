package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Canvas;

/**
 * パワーショットのアイテム
 * このアイテムを取ると、パワーアップした弾を発射できる。
 * @author 
 *
 */
public class PowerShotItem extends Item {

	public PowerShotItem(Resources r) {
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
