package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Bullet extends Sprite {
	private boolean fly;
	
	public Bullet(Bitmap img){
		image = img;
		dx = 0;
		dy = 0;
		fly = false;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO 自動生成されたメソッド・スタブ
		canvas.drawBitmap(image, matrix, null);
	}

	@Override
	public void move() {
		// TODO 自動生成されたメソッド・スタブ
		y += dy;
		if (y < -image.getHeight()){
			y = -image.getHeight();
			dy = 0;
			fly = false;
		}
		
		values[Matrix.MTRANS_X] = x;
		values[Matrix.MTRANS_Y] = y;
		matrix.setValues(values);
	}

	public void shoot(float x, float y){
		this.x = x - image.getWidth()/2;
		this.y = y;
		dy = -25;
		fly = true;
	}
	
	public boolean getFly(){
		return fly;
	}
}
