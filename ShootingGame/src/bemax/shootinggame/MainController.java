package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainController extends Thread implements OnTouchListener{
	private SurfaceView gameView;
	private Rect phgRect, logRect;
	private Point point;
	private Bitmap logImage;
	private MyPlane myplane;
	private Enemy[] enemies;
	private Bullet[] bullets;
	private boolean loop;
	private int score;
	private static final int ENEMIES = 10;
	private static final int BULLETS = 5;
	
	public MainController(SurfaceView view){
		gameView = view;
		float sx = view.getWidth() / 480f;
		float sy = view.getHeight() / 780f;
		if(sx<sy){
			sy = sx;
		}else{
			sx = sy;
		}
		phgRect = new Rect(0, 0, (int)(view.getWidth() * sx), (int)(view.getHeight() * sy));
		logRect = new Rect(0, 0, 480, 780);
		point = new Point((view.getWidth() - phgRect.width()) / 2, (view.getHeight() - phgRect.height()) / 2);
		phgRect.offset(point.x, point.y);
		
		logImage = Bitmap.createBitmap(logRect.width(), logRect.height(), Config.ARGB_8888);
		
		enemies = new Enemy[ENEMIES];
		bullets = new Bullet[BULLETS];

		myplane = new MyPlane(view.getResources());
		
		enemies[0] = new Enemy00(view.getResources());
		enemies[1] = new Enemy00(view.getResources());
		enemies[2] = new Enemy00(view.getResources());
		enemies[3] = new Enemy00(view.getResources());
		enemies[4] = new Enemy00(view.getResources());
		enemies[5] = new Enemy01(view.getResources());
		enemies[6] = new Enemy01(view.getResources());
		enemies[7] = new Enemy01(view.getResources());
		enemies[8] = new Enemy01(view.getResources());
		enemies[9] = new Enemy01(view.getResources());
		
		for(int i=0; i<bullets.length; i++){
			bullets[i] = new Bullet(view.getResources());
		}
		
		loop = true;
		
		score = 0;
		
		gameView.setOnTouchListener(this);
	}

	public void run() {
		Canvas canvas, logCanvas;
		long st, et, dist;
		
		logCanvas = new Canvas(logImage);
		
		
		while(loop){
			st = System.currentTimeMillis();
			
			
			/* 各エレメント移動 */
			for(int i=0; i<enemies.length; i++){
				enemies[i].move(myplane);
				if(enemies[i].outOfArea(logRect)){
					enemies[i].reset();
				}
			}
			for(int i=0; i<bullets.length; i++){
				bullets[i].move();
			}
			myplane.move();
			myplane.shoot(bullets);
			
			/* あたり判定 */
			
			/* 各エレメント描画 */
			logCanvas.drawColor(Color.BLACK);
			for(int i=0; i<enemies.length; i++){
				enemies[i].draw(logCanvas);
			}
			for(int i=0; i<bullets.length; i++){
				bullets[i].draw(logCanvas);
			}
			myplane.draw(logCanvas);
			
			/* 実際の画面に反映 */
			canvas = gameView.getHolder().lockCanvas();
			canvas.drawBitmap(logImage, logRect, phgRect, null);
			gameView.getHolder().unlockCanvasAndPost(canvas);
			
			et = System.currentTimeMillis();
			dist = et - st;
			if(dist<20){
				try {
					sleep(20 - dist);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 画面にタッチされたとき
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if(v == gameView){
			float x, y;
			Point p = new Point((int)event.getX(), (int)event.getY());
			p.offset(-point.x, -point.y);
			x = p.x * logRect.width() / (float)phgRect.width();
			y = p.y * logRect.height() / (float)phgRect.height();

			// 目標地点をセット
			myplane.setPlace(x, y);
		}
		return true;
	}
}
