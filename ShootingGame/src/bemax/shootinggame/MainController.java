package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainController extends Thread implements OnTouchListener{
	private SurfaceView gameView;
	private Handler handler;
	private Rect phgRect, logRect;
	private Point point;
	private Bitmap logImage;
	private MyPlane myplane;
	private Enemy[] enemies;
	private Bullet[] bullets;
	private BackScreen backScreen;
	private boolean loop;
	private int score, se;
	private MediaPlayer player;
	private SoundPool pool;
	private static final int ENEMIES = 10;
	private static final int BULLETS = 5;
	
	public MainController(SurfaceView view, Handler hnd){
		gameView = view;
		handler = hnd;
		float sx = gameView.getWidth() / 480f;
		float sy = gameView.getHeight() / 780f;
		if(sx<sy){
			sy = sx;
		}else{
			sx = sy;
		}
		phgRect = new Rect(0, 0, (int)(gameView.getWidth() * sx), (int)(gameView.getHeight() * sy));
		logRect = new Rect(0, 0, 480, 780);
		point = new Point((gameView.getWidth() - phgRect.width()) / 2, (gameView.getHeight() - phgRect.height()) / 2);
		phgRect.offset(point.x, point.y);
		
		logImage = Bitmap.createBitmap(logRect.width(), logRect.height(), Config.ARGB_8888);
		
		enemies = new Enemy[ENEMIES];
		bullets = new Bullet[BULLETS];

		myplane = new MyPlane(gameView.getResources());
		
		enemies[0] = new Enemy00(gameView.getResources());
		enemies[1] = new Enemy00(gameView.getResources());
		enemies[2] = new Enemy00(gameView.getResources());
		enemies[3] = new Enemy00(gameView.getResources());
		enemies[4] = new Enemy00(gameView.getResources());
		enemies[5] = new Enemy01(gameView.getResources());
		enemies[6] = new Enemy01(gameView.getResources());
		enemies[7] = new Enemy01(gameView.getResources());
		enemies[8] = new Enemy01(gameView.getResources());
		enemies[9] = new Enemy01(gameView.getResources());
		
		for(int i=0; i<bullets.length; i++){
			bullets[i] = new Bullet(gameView.getResources());
		}
		
		backScreen = new BackScreen(gameView.getResources());
		
		gameView.setOnTouchListener(this);
		
        /* サウンド関連の初期化 */
        player = MediaPlayer.create(gameView.getContext(), R.raw.field);
        player.setLooping(true);
        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        se = pool.load(gameView.getContext(), R.raw.bakuhatsu, 1);
	}

	public void run() {
		Canvas canvas, logCanvas;
		Paint paint;
		long st, et, dist;
		
		logCanvas = new Canvas(logImage);
		paint = new Paint();
		paint.setColor(Color.YELLOW);
		paint.setTextSize(30);
		
		int eNum = 3;
		score = 0;
		loop = true;
		
		player.start();
		
		while(loop){
			st = System.currentTimeMillis();
			
			/* 敵の出現数をコントロール */
			if(eNum < 10){
				eNum = score / 500 + 3;
			}
			
			/* 各エレメント移動 */
			for(int i=0; i<eNum; i++){
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
			
			/* 敵と弾のあたり判定 */
			for(int i=0; i<bullets.length; i++){
				for(int j=0; j<eNum; j++){
					if(!bullets[i].getReady() && enemies[j].state == Enemy.LIVE && bullets[i].hit(enemies[j])){
						if(enemies[j].damage()<=0){
							enemies[j].state = Enemy.DEAD;
							
							pool.play(se, 0.3f, 0.3f, 0, 0, 1.0f);
							
							score += enemies[j].getPoint();
						}
						bullets[i].reset();
					}
				}
			}
			
			/* 敵と自機のあたり判定 */
			hit_enemy:
			for(int i=0; i<eNum; i++){
				Log.d("hit",enemies[i].hit(myplane)?"HIT":"NG");
				if(enemies[i].state == Enemy.LIVE && enemies[i].hit(myplane)){
					loop = false;
					enemies[i].state = Enemy.DEAD;

					pool.play(se, 0.3f, 0.3f, 0, 0, 1.0f);

					break hit_enemy;
				}
			}
			
			/* 各エレメント描画 */
			logCanvas.drawColor(Color.BLACK);
			backScreen.drawBackScreen(logCanvas);
			for(int i=0; i<eNum; i++){
				enemies[i].draw(logCanvas);
			}
			for(int i=0; i<bullets.length; i++){
				bullets[i].draw(logCanvas);
			}
			myplane.draw(logCanvas);
			/* 得点を表示 */
			logCanvas.drawText("SCORE:" + score, 300, 30, paint);
			
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
		
		player.stop();
		
		/* メッセージ送信 */
		Message mes = new Message();
		mes.obj = new int[]{score};
		mes.what = 2;
		handler.sendMessage(mes);
	}
	
	/**
	 * ループをやめる
	 */
	public void endLoop(){
		loop = false;
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
