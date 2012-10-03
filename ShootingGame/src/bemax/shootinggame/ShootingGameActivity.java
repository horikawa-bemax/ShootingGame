package bemax.shootinggame;

import android.app.Activity;
import android.content.res.Resources;
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
	 * アクティビティが作られたとき実行される
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // コンテンツビューをセット
        setContentView(R.layout.main);

        // サーフェイスビューをセット
        surfaceview = (SurfaceView)findViewById(R.id.GameView);
        holder = surfaceview.getHolder();
        holder.addCallback(this);
        surfaceview.setOnTouchListener(this);

        // 敵配列と弾配列を初期化
        enemies = new Enemy[3];
        bullets = new Bullet[5];

        // ゲームルーチンを継続する
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
		Resources res = surfaceview.getResources();

		// 主人公機を初期化
		myplane = new MyPlane(res);

		// 敵を初期化
		enemies[0] = new Enemy00(res);
		enemies[1] = new Enemy00(res);
		enemies[2] = new Enemy00(res);

		// 弾を初期化
		for(int i=0; i<bullets.length; i++){
			bullets[i] = new Bullet(res);
		}

		// 敵の初期位置をリセット
		for(int i=0; i<enemies.length; i++){
			enemies[i].reset();
		}

		// メインルーチン
		while(loop){
			// ループ開始時刻
			st = System.currentTimeMillis();

			// 主人公機が弾を撃つ
			myplane.shoot(bullets);

			// 主人公機が動く
			myplane.move();

			// 弾を動かす
			for(int i=0; i<bullets.length; i++){
				bullets[i].move();
			}

			// 敵を動かす
			for(int i=0; i<enemies.length; i++){
				enemies[i].move(myplane);
			}

			// 弾と敵との当たり判定処理
			for(int i=0; i<bullets.length; i++){
				for(int j=0; j<enemies.length; j++){
					if(!bullets[i].getReady() && enemies[j].state==Enemy.LIVE && bullets[i].hit(enemies[j])){
						if(enemies[j].damage()<=0){
							enemies[j].setState(Enemy.DEAD);
						}
						bullets[i].reset();
					}
				}
			}

			// 主人公機と敵のあたり判定
			hit_enemy:
			for(int i=0; i<enemies.length; i++){
				Log.d("hit",enemies[i].hit(myplane)?"HIT":"NG");
				if(enemies[i].state == Enemy.LIVE && enemies[i].hit(myplane)){
					loop = false;
					enemies[i].state = Enemy.DEAD;
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
			for(int i=0; i<enemies.length; i++){
				enemies[i].draw(canvas);
			}

			// キャンバスをアンロック
			holder.unlockCanvasAndPost(canvas);

			// ルーチンの終了時刻
			ed = System.currentTimeMillis();

			// タイミングを合わせる
			dist = ed - st;

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
	}

	/**
	 * 画面にタッチされたとき
	 */
	public boolean onTouch(View v, MotionEvent event) {
		float x, y;

		// タッチされた座標を算出
		x = (event.getX() - dx) / sx;
		y = (event.getY() - dy) / sy;

		// 目標地点をセット
		myplane.setPlace(x, y);

		// 継続的にタッチを感知する
		return true;
	}
}