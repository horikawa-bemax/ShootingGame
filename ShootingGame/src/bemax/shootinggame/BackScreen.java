package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class BackScreen {
	private Bitmap backScreen, bitmap;
	private int begin, mapLength, speed;
	private Bitmap[] reses;
	private int[][] map;
	private Rect r1,r2;

	public BackScreen(Resources res){
		reses = new Bitmap[]{
				BitmapFactory.decodeResource(res, R.drawable.bg00),
				BitmapFactory.decodeResource(res, R.drawable.bg01),
				BitmapFactory.decodeResource(res, R.drawable.bg02),
				BitmapFactory.decodeResource(res, R.drawable.bg03),
				BitmapFactory.decodeResource(res, R.drawable.bg04),
				BitmapFactory.decodeResource(res, R.drawable.bg05)
		};

		map = new int[][]{
			{0,1,0,0,0},
			{4,0,0,3,0},
			{0,0,0,0,0},
			{0,0,0,0,2},
			{0,0,1,0,0},
			{0,3,0,0,0},
			{0,0,0,0,4},
			{0,0,0,0,0},
			{0,5,0,0,0},
			{2,0,0,0,0},
			{0,0,0,4,0},
			{0,0,0,0,0},
			{0,0,3,0,0},
			{0,0,0,0,1},
			{0,1,0,0,0},
			{2,0,0,0,0},
			{0,0,4,0,0},
			{0,0,0,0,3},
			{0,1,0,0,0},
			{0,0,0,2,0}
		};

		mapLength = map.length;
		speed = 5;
		begin = (mapLength - 10)*96;
		backScreen = Bitmap.createBitmap(480,96*(map.length+9),Config.ARGB_8888);

		Canvas c = new Canvas(backScreen);
		Matrix m = new Matrix();
		for(int i=0; i<map.length + 9; i++){
			for(int j=0; j<5; j++){
				m.setTranslate(96*j, 96*i);
				c.drawBitmap(reses[map[(i%map.length)][j]], m, null);
			}
		}

		bitmap =  Bitmap.createBitmap(480,780,Config.ARGB_8888);
		r1 = new Rect();
		r2 = new Rect();
	}

	public void drawBackScreen(Canvas canvas){
		begin -= speed;
		if(begin < 0){
			begin = mapLength * 96 + begin;
		}
		r1.set(0, begin, 480, 780+begin);
		r2.set(0, 0, 480, 780);
		canvas.drawBitmap(backScreen,r1,r2,null );
	}
}
