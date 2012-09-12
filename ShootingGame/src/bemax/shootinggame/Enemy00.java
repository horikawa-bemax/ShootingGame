package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Enemy00 extends Enemy{

	public Enemy00(Bitmap img) {
		super();
		// 敵機の画像データ
		image = img;

		// 倒した時の得点
		point = 10;
	}

	// 敵機を移動させる
	@Override
	public void move() {
		// 敵機の座標を更新
		x +=  dx;
		y += dy;

		// 左または右からはみ出ようとすると反射する
		if(x<0){
			x = 0;
			dx = -dx;
		}else if(x > 480-image.getWidth()){
			x = 480 - image.getWidth();
			dx = -dx;
		}

		// 敵機を移動
		matrix.setTranslate(x, y);

		// 下からはみ出したら、リセットする
		if(y > 800){
			reset();
		}
	}

	// 敵機をキャンバスに描画する
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	// 主人公機をターゲットにした敵機の移動
	public void move(MyPlane mp){
		move();
	}

	// 敵機をスタート位置にリセットする
	public void reset(){
		// 敵機の初期y座標
		y = -image.getHeight() * 2;

		// 敵機の初期x座標
		x = rand.nextFloat() * (480-image.getWidth());

		// 敵機のx軸方向への初期増分
		dx = 0;

		// 敵機のy軸方向への初期増分
		dy = rand.nextFloat() * 10 + 5; //=>5.0 ~ 14.999
	}
}
