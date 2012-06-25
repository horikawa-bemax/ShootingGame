package bemax.shootinggame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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
		Paint paint = new Paint(Color.BLACK);
		float[] values = new float[9];
		matrix.getValues(values);
		while(loop){
			canvas = holder.lockCanvas();
			canvas.drawRect(field, paint);

			canvas.concat(matrix);

			Bitmap bitmap = BitmapFactory.decodeResource(surfaceview.getResources(), R.drawable.ic_launcher);

			canvas.drawBitmap(bitmap, 0, 0, null);
			canvas.drawBitmap(bitmap, 480-bitmap.getWidth(),750-bitmap.getHeight(), paint);

			holder.unlockCanvasAndPost(canvas);
		}

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		field = new Rect(0,0,width,height);
		matrix = new Matrix();
		float sx = 1.0f * width / 480;
		float sy = 1.0f * height / 750;
		float[] values = new float[9];
		matrix.getValues(values);
		if(sx<sy){
			float dy = (height - sx*750)/2;
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