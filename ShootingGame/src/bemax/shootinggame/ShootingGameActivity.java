package bemax.shootinggame;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * メインアクティビティ
 * @author Masaaki Horikawa
 * 2012.9.19
 */
public class ShootingGameActivity extends Activity implements SurfaceHolder.Callback, Runnable, OnTouchListener{
    private SurfaceView surfaceview;
    private ImageView titleView, endView;
	private SurfaceHolder holder;
	private Rect field;
	private Matrix matrix;
	private boolean loop;
	private MyPlane myplane;
	private Enemy[] enemies;
	private Bullet[] bullets;
	private float sx,sy,dx,dy;
	private int score;
	private Handler handler;
	private MediaPlayer player;
	private SoundPool sePool;
	private HashMap<Integer, Integer> map;

	/**
	 * アクティビティが作られたとき実行される
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* サウンド関連初期化 */
        map = new HashMap<Integer, Integer>();
        sePool = new SoundPool(10,AudioManager.STREAM_MUSIC,1);
        map.put(R.raw.bakuhatsu, sePool.load(this, R.raw.bakuhatsu, 1));
        map.put(R.raw.hassya, sePool.load(this, R.raw.hassya, 1));
        map.put(R.raw.click, sePool.load(this, R.raw.click, 1));

        final Object thisObj = this;
        handler = new Handler(){
        		public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch(msg.what){
				case 0:
					setContentView(R.layout.title);
					titleView = (ImageView)findViewById(R.id.plane_image);
					titleView.setOnTouchListener((OnTouchListener)thisObj);

			        /* サウンド関連の初期化 */
			        player = MediaPlayer.create((Context)thisObj, R.raw.opening);
			        player.setLooping(true);
			        player.start();

					break;
				case 1:
					setContentView(R.layout.main);
			        // サーフェイスビューをセット
			        surfaceview = (SurfaceView)findViewById(R.id.GameView);
			        holder = surfaceview.getHolder();
			        holder.addCallback((SurfaceHolder.Callback)thisObj);
			        surfaceview.setOnTouchListener((OnTouchListener)thisObj);

			        // 敵配列と弾配列を初期化
			        enemies = new Enemy[10];
			        bullets = new Bullet[5];

			        // ゲームルーチンを継続する
			        loop = true;

			        /* スコアをリセット */
			        score = 0;

			        /* サウンド関連の初期化 */
			        player = MediaPlayer.create((Context)thisObj, R.raw.field);
			        player.setLooping(true);
			        player.start();

					break;
				case 2:
					setContentView(R.layout.end);
					endView = (ImageView)findViewById(R.id.end_image);
					endView.setOnTouchListener((OnTouchListener)thisObj);

					TextView txt = (TextView)findViewById(R.id.score_text);
					txt.setText("" + score);

			        /* サウンド関連の初期化 */
			        player = MediaPlayer.create((Context)thisObj, R.raw.gameover);
			        player.setLooping(true);
			        player.start();

					break;
				}
			}
        };

        handler.sendEmptyMessage(0);
    }

    public void onStop(){
    	super.onStop();
    	if(player != null){
    		player.stop();
    	}
    }

    /**
     * メインスレッド
     */
    public void run() {
    	Canvas canvas;
    	BackScreen backScreen  = new BackScreen(surfaceview.getResources());
		Paint paint = new Paint(Color.BLACK);
		float[] values = new float[9];
		matrix.getValues(values);
		long st, ed,dist;
		Resources res = surfaceview.getResources();

		paint.setTextSize(30);

		// 主人公機を初期化
		myplane = new MyPlane(res);

		// 敵を初期化
		enemies[0] = new Enemy00(res);
		enemies[1] = new Enemy00(res);
		enemies[2] = new Enemy00(res);
		enemies[3] = new Enemy00(res);
		enemies[4] = new Enemy00(res);
		enemies[5] = new Enemy01(res);
		enemies[6] = new Enemy01(res);
		enemies[7] = new Enemy01(res);
		enemies[8] = new Enemy01(res);
		enemies[9] = new Enemy01(res);

		// 弾を初期化
		for(int i=0; i<bullets.length; i++){
			bullets[i] = new Bullet(res);
		}

		// 敵の初期位置をリセット
		for(int i=0; i<enemies.length; i++){
			enemies[i].reset();
		}

		int e = 0;

		// メインルーチン
		while(loop){
			// ループ開始時刻
			st = System.currentTimeMillis();

			if(e < 10){
				e = score / 500 + 3;
			}

			// 主人公機が弾を撃つ
			myplane.shoot(bullets);

			// 主人公機が動く
			myplane.move();

			// 弾を動かす
			for(int i=0; i<bullets.length; i++){
				bullets[i].move();
			}

			// 敵を動かす
			for(int i=0; i<e; i++){
				enemies[i].move(myplane);
			}

			// 弾と敵との当たり判定処理
			for(int i=0; i<bullets.length; i++){
				for(int j=0; j<e; j++){
					if(!bullets[i].getReady() && enemies[j].state == Enemy.LIVE && bullets[i].hit(enemies[j])){
						if(enemies[j].damage()<=0){
							enemies[j].state = Enemy.DEAD;
							/*
							sePool.play(map.get(R.raw.bakuhatsu), 0.5f, 0.5f, 0, 0, 1.0f);
							*/
							score += enemies[j].getPoint();
						}
						bullets[i].reset();
					}
				}
			}

			// 主人公機と敵のあたり判定
			hit_enemy:
			for(int i=0; i<e; i++){
				Log.d("hit",enemies[i].hit(myplane)?"HIT":"NG");
				if(enemies[i].state == Enemy.LIVE && enemies[i].hit(myplane)){
					loop = false;
					enemies[i].state = Enemy.DEAD;

					sePool.play(map.get(R.raw.bakuhatsu), 0.5f, 0.5f, 0, 0, 1.0f);

					break hit_enemy;
				}
			}

			// キャンバスロック
			canvas = holder.lockCanvas();

			// キャンバス初期化
			paint.setColor(Color.BLUE);
			canvas.drawRect(field, paint);

			// キャンパスの変形
			canvas.concat(matrix);

			// 背景を描画
			backScreen.drawBackScreen(canvas);

			// 主人公機を描画
			myplane.draw(canvas);

			// 弾を描画
			for(int i=0; i<bullets.length; i++){
				bullets[i].draw(canvas);
			}

			// 敵を描画
			for(int i=0; i<e; i++){
				enemies[i].draw(canvas);
			}

			/* 得点を表示 */
			paint.setColor(Color.YELLOW);
			canvas.drawText("SCORE:" + score, 300, 30, paint);

			// キャンバスをアンロック
			holder.unlockCanvasAndPost(canvas);

			// ルーチンの終了時刻
			ed = System.currentTimeMillis();

			// タイミングを合わせる
			dist = ed - st;

			if(dist < 20){
				try {
					Thread.sleep(20-ed+st);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}

		Message mes = new Message();
		mes.obj = new int[]{score};
		mes.what = 2;
		handler.sendMessage(mes);
	}

    /**
     * サーフェイスが変化したとき
     * */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		field = new Rect(0,0,width,height);
		matrix = new Matrix();
		sx = 1.0f * width / 480;
		sy = 1.0f * height / 780;
		float[] values = new float[9];
		matrix.getValues(values);
		if(sx<sy){
			dx = 0;
			dy = (height - sx*780)/2;
			sy = sx;
		}else{
			dx = (width - sy*480)/2;
			dy = 0;
			sx = sy;
		}
		values[Matrix.MSCALE_X] = sx;
		values[Matrix.MSCALE_Y] = sy;
		values[Matrix.MTRANS_X] = dx;
		values[Matrix.MTRANS_Y] = dy;
		matrix.setValues(values);
	}

	/**
	 * サーフェイスが生成されたとき
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * サーフェイスが消去されたとき
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		loop = false;

			player.stop();
	}

	/**
	 * 画面にタッチされたとき
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if(v == surfaceview){
			float x, y;

			// タッチされた座標を算出
			x = (event.getX() - dx) / sx;
			y = (event.getY() - dy) / sy;

			// 目標地点をセット
			myplane.setPlace(x, y);

			// 継続的にタッチを感知する
			return true;
		}else if(v == titleView){
			handler.sendEmptyMessage(1);

			player.stop();
			sePool.play(map.get(R.raw.click), 0.5f, 0.5f, 0, 0, 1.0f);
		}else if(v == endView){
			handler.sendEmptyMessage(0);

			player.stop();
			sePool.play(map.get(R.raw.click), 0.5f, 0.5f, 0, 0, 1.0f);
		}

		return false;
	}
}