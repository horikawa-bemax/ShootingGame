package bemax.shootinggame;

import java.util.Random;

// 敵機の抽象クラス
public abstract class Enemy extends Sprite{
	protected int point;			// 撃破したときに入る得点
	protected Random rand;	// 乱数発生用変数

	// コンストラクタ
	public Enemy(){
		super();

		// 新しいランダムオブジェクトを生成
		rand = new Random();
	}

	// 主人公機を狙った移動をする
	public abstract void move(MyPlane mp);

	// 移動をする
	@Override
	public abstract void move();

	// 初期位置に戻る
	public void reset(){
		y = -image.getHeight() * 2;
		x = rand.nextFloat() * (480-image.getWidth());
		dx = rand.nextInt(5) + 3;
		dy = rand.nextInt(5) + 5;
	}
}
