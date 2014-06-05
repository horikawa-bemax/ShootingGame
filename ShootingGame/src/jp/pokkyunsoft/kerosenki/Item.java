package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;

/**
 * 敵を倒したときに現れるアイテムのクラス
 * @author 
 *
 */
public abstract class Item extends Sprite{
	protected final int ITEM_SIZE = 80;
	protected boolean showFlg;

	public Item(Resources r) {
		super(r);
		showFlg = false;
	}
	
	/**
	 * アイテムをゲットしたときに実行する
	 * @param mp
	 */
	public abstract void itemGet(MyPlane mp);
	
	public abstract void appear(int centerX, int centerY);
}
