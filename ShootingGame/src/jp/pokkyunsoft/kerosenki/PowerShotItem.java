package jp.pokkyunsoft.kerosenki;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * パワーショットのアイテム
 * このアイテムを取ると、パワーアップした弾を発射できる。
 * @author 
 *
 */
public class PowerShotItem extends Item {

	public PowerShotItem(Resources r) {
		super(r);
		image = setImage(R.drawable.item, ITEM_SIZE);
		shadowArry = getShadow();
		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		drawingExtent = new Rect(0,-imgHeight ,imgWidth, 0);

		reset();
	}

	@Override
	public void move() {
		drawingExtent.offset(dx, dy);
		matrix.setTranslate(getX(), getY());
		
		if(drawingExtent.top > 780){
			reset();
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if(showFlg){
			canvas.drawBitmap(image, matrix, null);
		}
	}
	
	@Override
	public void itemGet(MyPlane mp) {
		mp.powerUp();
		reset();
	}

	@Override
	protected void reset() {
		drawingExtent.set(0, -imgHeight, imgWidth, 0);
		dy = 0;
		showFlg = false;
	}

	@Override
	public void appear(int centerX, int centerY) {
		int x = centerX - imgWidth / 2;
		int y = centerY - imgHeight / 2;
		drawingExtent.set(x, y, x + imgWidth, y + imgHeight);
		dy = 5;
		showFlg = true;
	}
}
