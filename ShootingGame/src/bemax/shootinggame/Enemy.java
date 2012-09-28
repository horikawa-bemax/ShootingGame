package bemax.shootinggame;

import java.util.Random;

/**
 * 敵クラス
 * @author Masaaki Horikawa
 *
 */
public abstract class Enemy extends Sprite{
	protected int point;	// 得点
	protected Random rand;	// 乱数

	/**
	 * コンストラクタ
	 */
	public Enemy(){
		super();

		// 乱数オブジェクト生成
		rand = new Random();
	}

	/**
	 * 主人公機にあわせて動く
	 * @param mp 主人公機
	 */
	public abstract void move(MyPlane mp);

	/**
	 * 動く
	 */
	@Override
	public abstract void move();

	/**
	 * 位置情報リセット
	 */
	public void reset(){
		y = -image.getHeight() * 2;
		x = rand.nextFloat() * (480-image.getWidth());
		dx = rand.nextInt(5) + 3;
		dy = rand.nextInt(5) + 5;
	}
}