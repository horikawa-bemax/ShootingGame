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
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ShootingGameActivity extends Activity implements SurfaceHolder.Callback, Runnable{
    private SurfaceView surfaceview;
	private SurfaceHolder holder;
	private Rect field;
	private Matrix matrix;
	private boolean loop;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        surfaceview = (SurfaceView)findViewById(R.id.GameView);

        holder = surfaceview.getHolder();
        holder.addCallback(this);

        loop = true;
    }

    public void run() {
    	Canvas canvas;
    	BackScreen backScreen  = new BackScreen(surfaceview.getResources());
		Paint paint = new Paint(Color.BLACK);
		float[] values = new float[9];
		matrix.getValues(values);
		long st, ed,dist;

		// Imageのセット
		MyPlane.image = BitmapFactory.decodeResource(surfaceview.getResources(), R.drawable.myplane);
		Enemy00.image = BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy00);
		Enemy01.image = BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy01);
		Enemy02.image = BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy02);

		MyPlane myplane = new MyPlane();
		Enemy00 e0 = new Enemy00();
		Enemy01 e1 = new Enemy01();
		Enemy02 e2 = new Enemy02();

		myplane.move(200,600,3,0);
		e0.move(100, -80, 0, 2);
		e1.move(200, -80, 0, 12);
		e2.move(400, -80, 4, 2);

		while(loop){
			st = System.currentTimeMillis();

			canvas = holder.lockCanvas();
			paint.setColor(Color.BLUE);
			canvas.drawRect(field, paint);

			myplane.move();
			e0.move();
			e1.move(myplane);
			e2.move();

			canvas.concat(matrix);
			backScreen.drawBackScreen(canvas);
			myplane.draw(canvas);
			e0.draw(canvas);
			e1.draw(canvas);
			e2.draw(canvas);

			holder.unlockCanvasAndPost(canvas);

			ed = System.currentTimeMillis();
			dist = ed - st;
			Log.d("DIST",""+dist);
			if(dist < 20){
				try {
					Thread.sleep(30-ed+st);
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
}