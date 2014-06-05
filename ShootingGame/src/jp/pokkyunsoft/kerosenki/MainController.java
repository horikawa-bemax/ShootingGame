package jp.pokkyunsoft.kerosenki;

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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * ゲームのメインコントローラ
 * @author Masaaki Horikawa
 * 2014.5.22
 */
public class MainController implements Runnable, OnTouchListener, SurfaceHolder.Callback{
	private SurfaceView gameView;
	private SurfaceHolder holder;
	private Handler handler;
	private Rect phgRect, logRect;
	private Point point;
	private Bitmap logImage;
	private MyPlane myplane;
	private Enemy[] enemies;
	private Bullet[] myBullets;
	private Bullet[] enemyBullets;
	private Item item;
	private BackScreen backScreen;
	private boolean loop, gameover;
	private int score, se;
	private MediaPlayer player;
	private SoundPool pool;
	private static final int ENEMIES = 10;
	private static final int MAX_BULLETS = 6;
	
	/**
	 * コンストラクタ
	 * @param view	ゲームを描くサーフェイスビュー
	 * @param hnd	画面切り替え用のハンドラ
	 */
	public MainController(SurfaceView view, Handler hnd){
		gameView = view;
		holder = gameView.getHolder();
		holder.addCallback(this);
		handler = hnd;
		
		/* 敵と弾の配列を初期化 */
		enemies = new Enemy[ENEMIES];
		myBullets = new MyBullet[MAX_BULLETS];
		//enemyBullets = new EnemyBullet[20];

		/* 自機の初期化 */
		myplane = new MyPlane(gameView.getResources());
		
		/* 敵の初期化 */
		enemies[0] = new Kero(gameView.getResources());
		enemies[1] = new Kero(gameView.getResources());
		enemies[2] = new Bigkero(gameView.getResources());
		enemies[3] = new Kero(gameView.getResources());
		enemies[4] = new Geko(gameView.getResources());
		enemies[5] = new Kero(gameView.getResources());
		enemies[6] = new Kero(gameView.getResources());
		enemies[7] = new Bigkero(gameView.getResources());
		enemies[8] = new Kero(gameView.getResources());
		enemies[9] = new Geko(gameView.getResources());
		
		/* 自機 弾の初期化 */
		for(int i=0; i<myBullets.length; i++){
			myBullets[i] = new MyBullet(gameView.getResources());
		}
		
		/* 敵 弾の初期化 */
		
		/* アイテムの初期化 */
		item = new PowerShotItem(gameView.getResources());
		
		/* 背景の初期化 */
		backScreen = new BackScreen(gameView.getResources());
		
		/* 画面タッチ用のリスナ */
		gameView.setOnTouchListener(this);
	}

	/**
	 * メインルーチン
	 */
	public void run() {
		Canvas canvas, logCanvas;
		Paint paint;
		long st, et, dist;
		
		/* 画面の初期化 */
		logCanvas = new Canvas(logImage);	// 論理画面からキャンバスを作成
		paint = new Paint();				// ペイントの宣言
		paint.setColor(Color.YELLOW);		// 得点表示の文字色
		paint.setTextSize(30);				// 得点表示の文字サイズ
		
		/* 値の初期化 */
		int eNum = 3;		// 初期の敵数
		score = 0;			// 初期のスコア
		loop = true;		// 初期のループフラグ
		gameover = false;	// 初期のゲームオーバーフラグ
		
		/* プレイヤースレッド（自機）をスタート */
		player.start();
		
		/* メインルーチン */
		try{
			while(loop){
				/* ループ開始時の時刻を記録 */
				st = System.currentTimeMillis();
				
				/* 敵の出現数をコントロール (300点ごとに敵が増えるように) */
				if(eNum < 10 && eNum < score / 300 + 3){
					eNum++;									// 敵の数を増やす
				}
				
				/* 敵の移動 */
				for(int i=0; i<eNum; i++){
					enemies[i].move(myplane);
					if(enemies[i].outOfArea(logRect)){
						enemies[i].reset();
					}
				}
				
				/* 敵の砲撃 */
				
				/* 敵弾の移動 */
				
				/* アイテムの移動 */
				item.move();
				
				/* 自機の移動 */
				myplane.move();
				
				/* 自機の砲撃 */
				myplane.shoot(myBullets);
				
				/* 自弾の移動 */
				for(int i=0; i<myBullets.length; i++){
					myBullets[i].move();
				}
				
				/* 敵と弾のあたり判定 */
				for(int i=0; i<myBullets.length; i++){
					for(int j=0; j<eNum; j++){
						if(!myBullets[i].getReady() && enemies[j].state == Enemy.LIVE && myBullets[i].hit(enemies[j])){
							if(enemies[j].damage()<=0){
								enemies[j].state = Enemy.DEAD;
								
								/* 爆発音再生 */
								pool.play(se, 0.3f, 0.3f, 0, 0, 1.0f);
								
								/* スコア更新 */
								score += enemies[j].getDefeatPoint();
								
								if(enemies[j].getClass()==(Class<?>)Bigkero.class && (int)(Math.random()*10) < 2 ){
									item.appear(enemies[j].centerX(), enemies[j].centerY());
								}
							}
							myBullets[i].reset();
						}
					}
				}
				
				/* 敵と自機のあたり判定 */
				hit_enemy:
				for(int i=0; i<eNum; i++){
					if(enemies[i].state == Enemy.LIVE && enemies[i].hit(myplane)){
						loop = false;
						enemies[i].state = Enemy.DEAD;
	
						pool.play(se, 0.3f, 0.3f, 0, 0, 1.0f);
						
						gameover = true;
						
						break hit_enemy;
					}
				}
				
				/* 自機と敵弾の当たり判定 */
				
				/* 自機とアイテムの当たり判定 */
				if(myplane.hit(item)){
					item.itemGet(myplane);
				}
				
				/* 各エレメント描画 */
				logCanvas.drawColor(Color.BLACK);
				backScreen.drawBackScreen(logCanvas);
				for(int i=0; i<eNum; i++){
					enemies[i].draw(logCanvas);
				}
				for(int i=0; i<myBullets.length; i++){
					myBullets[i].draw(logCanvas);
				}
				item.draw(logCanvas);
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
						Thread.sleep(20 - dist);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
		player.stop();
		
		/* メッセージ送信 */
		if(gameover){
			Message mes = new Message();
			mes.obj = new int[]{score};
			mes.what = 2;
			handler.sendMessage(mes);
		}
	}
	
	/**
	 * 画面にタッチされたとき
	 * @param v		タッチされたヴュー
	 * @param event	発生したイベント
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
	
	  /**
     * サーフェイスが変化したときに実行される
     * @param holder
     * @param format
     * @param width		サーフェイスの横幅
     * @param height	サーフェイスの縦幅
     * */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		/* 論理画面と物理画面のスケールを決定する */
		float sx = width / 480.0f;		// 論理画面と物理画面の横方向のスケール
		float sy = height / 780.0f;		// 論理画面と物理画面の縦方向のスケール
		if(sx < sy){
			sy = sx;
		}else{
			sx = sy;
		}
		
		/* 論理画面の位置決定 */
		phgRect = new Rect(0, 0, (int)(480 * sx), (int)(780 * sy));	// 物理画面の矩形
		logRect = new Rect(0, 0, 480, 780);							// 論理画面の矩形
		point = new Point((width - phgRect.width()) / 2, (height - phgRect.height()) / 2);	// 物理画面上の論理画面の左上のポイント
		phgRect.offset(point.x, point.y);							// 物理画面の中央に論理画面がくるようにする
		
		/* 論理画面用のビットマップを作成 */
		logImage = Bitmap.createBitmap(logRect.width(), logRect.height(), Config.ARGB_8888);
		
		/* メインルーチンを開始 */
		Thread t  = new Thread(this);
		t.start();
	}

	/**
	 * サーフェイスが生成されたとき
	 * @param holder
	 */
	public void surfaceCreated(SurfaceHolder holder) {
        /* BGMの初期化 */
        player = MediaPlayer.create(gameView.getContext(), R.raw.field);
        player.setLooping(true);
        
        /* 効果音の初期化 */
        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        se = pool.load(gameView.getContext(), R.raw.bakuhatsu, 1);
	}

	/**
	 * サーフェイスが消去されたとき
	 * @param holder
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		/* ゲームを強制的に終了します */
		this.loop = false;
	}
}
