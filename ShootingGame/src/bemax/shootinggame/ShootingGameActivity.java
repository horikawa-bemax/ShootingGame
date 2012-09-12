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

public class ShootingGameActivity extends Activity implements SurfaceHolder.Callback, Runnable, OnTouchListener{
    private SurfaceView surfaceview;
	private SurfaceHolder holder;
	private Rect field;
	private Matrix matrix;
	private boolean loop;
	private MyPlane myplane;
	private Enemy[] enemies;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // レイアウトファイル読み込み
        setContentView(R.layout.main);

        // ゲーム画面オブジェクトを作成
        surfaceview = (SurfaceView)findViewById(R.id.GameView);

        // ゲーム画面管理用オブジェクトを作成
        holder = surfaceview.getHolder();
        holder.addCallback(this);

        // ゲーム画面に対し、タッチセンサーを有効にする
        surfaceview.setOnTouchListener(this);

        // 敵機配列の初期化
        enemies = new Enemy[3];

        loop = true;
    }

    public void run() {
    /*## ローカル変数の宣言 ##*/
    	// 絵を描くためのキャンバスオブジェクト
    	Canvas canvas;

    	// 背景オブジェクトを生成
    	BackScreen backScreen  = new BackScreen(surfaceview.getResources());

    	// 絵を描くためのペンを生成
		Paint paint = new Paint(Color.BLACK);

		// 行列計算用の配列を生成し初期化する
		float[] values = new float[9];
		matrix.getValues(values);

		// 時間計測用の変数
		long st, ed,dist;

	/*## プログラム ##*/
		// 主人公機の生成
		myplane = new MyPlane(BitmapFactory.decodeResource(surfaceview.getResources(), R.drawable.myplane));

		// 敵機00の生成
		enemies[0] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy00));

		// 敵機01の生成
		enemies[1] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy01));

		// 敵機02の生成
		enemies[2]  = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy02));

		// 主人公機の配置
//		myplane.move();

		// 敵機の配置（ランダム）
		enemies[0].reset();
		enemies[1].reset();
		enemies[2].reset();

		// ゲーム画面描画処理
		while(loop){
			// 描画開始時間を保存
			st = System.currentTimeMillis();

			// 主人公機・敵機の位置を更新
			myplane.move();
			enemies[0].move();
			enemies[1].move();
			enemies[2].move();

			// 裏面を取得
			canvas = holder.lockCanvas();

			// 青色のペンを選択
			paint.setColor(Color.BLUE);

			// 全裏面を青で塗りつぶす
			canvas.drawRect(field, paint);

			// 実機の画面サイズに合わせて裏面を変形
			canvas.concat(matrix);

			// 背景画像を描画する
			backScreen.drawBackScreen(canvas);

			// 主人公機を描画する
			myplane.draw(canvas);

			// 敵機を描画する
			enemies[0].draw(canvas);
			enemies[1].draw(canvas);
			enemies[2].draw(canvas);

			// 前面と裏面を入れ替える
			holder.unlockCanvasAndPost(canvas);

			// 描画終了時間を保存
			ed = System.currentTimeMillis();

			// 描画にかかった時間を計算
			dist = ed - st;

			Log.d("DIST",""+dist);

			// 描画時間が20ミリ秒より少なかったら、少し待つ
			if(dist < 20){
				try {
					// 1ターンが20ミリ秒になるように待ち時間を調整する
					Thread.sleep(20-ed+st);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}

	}

	private void Log(String string, String string2) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		field = new Rect(0,0,width,height);
		matrix = new Matrix();
		float sx = 1.0f * width / 480;
		float sy = 1.0f * height / 780;
		float[] values = new float[9];
		matrix.getValues(values);
		if(sx<sy){
			float dy = (height - sx*780)/2;
			values[Matrix.MSCALE_X] = sx;
			values[Matrix.MSCALE_Y] = sx;
			values[Matrix.MTRANS_Y] = dy;
			matrix.setValues(values);
		}else{
			float dx = (width - sy*480)/2;
			values[Matrix.MSCALE_X] = sy;
			values[Matrix.MSCALE_Y] = sy;
			values[Matrix.MTRANS_X] = dx;
			matrix.setValues(values);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Thread t = new Thread(this);
		t.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		loop = false;
	}

	public boolean onTouch(View v, MotionEvent event) {
		float x, y;

		x = event.getX();
		y = event.getY();

		myplane.setPlace(x, y);

		return true;
	}
}