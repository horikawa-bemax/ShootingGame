package bemax.shootinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class BackScreen {
	private Bitmap backScreen;
	private int begin, mapLength, speed;
	private Bitmap[] reses;
	private int[][] map;

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
	}

	public Bitmap getBackScreen(){
		Bitmap bitmap = Bitmap.createBitmap(480,780,Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Matrix matrix= new Matrix();

		begin -= speed;
		if(begin < 0){
			begin = mapLength * 96 + begin;
		}
		int idx = begin / 96;
		int dx = begin % 96;

		for(int i=0; i<10; i++){
			for(int j=0; j<5; j++){
				matrix.setTranslate(96*j, 96*i-dx);
				canvas.drawBitmap(reses[map[((i+idx)%mapLength)][j]], matrix, null);
			}
		}
		return bitmap;
	}
}
