package bemax.shootinggame;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * メインアクティビティ
 * @author Masaaki Horikawa
 * 2012.9.19
 */
public class ShootingGameActivity extends Activity implements SurfaceHolder.Callback, Runnable, OnTouchListener{
    private SurfaceView surfaceview;
	private SurfaceHolder holder;
	private Rect field;
	private Matrix matrix;
	private boolean loop;
	private MyPlane myplane;
	private Enemy[] enemies;
	private Bullet[] bullets;
	private float sx,sy,dx,dy;

	/**
	 * コンストラクタ
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //コンテンツヴュー初期化
        setContentView(R.layout.main);
        
        //サーフェイスヴュー関連初期化
        surfaceview = (SurfaceView)findViewById(R.id.GameView);
        holder = surfaceview.getHolder();
        holder.addCallback(this);
        surfaceview.setOnTouchListener(this);

        //各キャラクター用配列初期化
        enemies = new Enemy[3];
        bullets = new Bullet[5];
        
        //ゲームをループさせるフラグ
        loop = true;
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

		//主人公機初期化
		myplane = new MyPlane(BitmapFactory.decodeResource(surfaceview.getResources(), R.drawable.myplane));

		//敵機初期化
		enemies[0] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy00));
		enemies[1] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy01));
		enemies[2]  = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy02));

		//弾初期化
		bullets[0] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[1] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[2] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[3] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[4] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));

		//敵機の初期位置を確定
		enemies[0].reset();
		enemies[1].reset();
		enemies[2].reset();

		//処理ルーチン
		while(loop){
			//処理開始時刻を記録
			st = System.currentTimeMillis();

			//主人公機が弾を撃つ
			myplane.shoot(bullets);

			//主人公機が移動する
			myplane.move();
			
			//弾が移動する
			bullets[0].move();
			bullets[1].move();
			bullets[2].move();
			bullets[3].move();
			bullets[4].move();
			
			//敵が移動する
			enemies[0].move();
			enemies[1].move();
			enemies[2].move();

			//描画開始
			canvas = holder.lockCanvas();

			//前の画面を全部塗りつぶす
			paint.setColor(Color.BLUE);
			canvas.drawRect(field, paint);

			//拡大率を設定
			canvas.concat(matrix);
			
			//背景画面を描画
			backScreen.drawBackScreen(canvas);

			//主人公機を描画
			myplane.draw(canvas);
			
			//弾を描画
			bullets[0].draw(canvas);
			bullets[1].draw(canvas);
			bullets[2].draw(canvas);
			bullets[3].draw(canvas);
			bullets[4].draw(canvas);
			
			//敵を描画
			enemies[0].draw(canvas);
			enemies[1].draw(canvas);
			enemies[2].draw(canvas);

			//描画終了
			holder.unlockCanvasAndPost(canvas);

			//処理終了時刻を記録
			ed = System.currentTimeMillis();

			//タイミングを合わせる処理
			dist = ed - st;
			Log.d("DIST",""+dist);
			if(dist < 20){
				try {
					Thread.sleep(20-ed+st);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

    /**
     * 画面サイズが変更になった場合の処理
     */
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
	 * 画面が初期化された時の処理
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * 画面が破棄された時の処理
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		loop = false;
	}

	/**
	 * 画面にタッチされた時の処理
	 */
	public boolean onTouch(View v, MotionEvent event) {
		float x, y;

		//タッチされたx座標,y座標から、主人公機の目的地を算出
		x = (event.getX() - dx) / sx;
		y = (event.getY() - dy) / sy;

		//主人公機に目的地のx座標,y座標を与える
		myplane.setPlace(x, y);

		//継続的にタッチを感知するためにtrue
		return true;
	}
}