package bemax.shootinggame;

import android.content.res.Resources;

/**
 * 敵を倒したときに現れるアイテムのクラス
 * @author 
 *
 */
public abstract class Item extends Sprite{
	protected final int ITEM_SIZE = 80;

	public Item(Resources r) {
		super(r);

	}
	
}
