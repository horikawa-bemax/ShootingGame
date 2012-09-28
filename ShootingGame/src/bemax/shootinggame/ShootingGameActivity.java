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
 * 繝｡繧､繝ｳ繧｢繧ｯ繝�ぅ繝薙ユ繧｣
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
	 * 繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //繧ｳ繝ｳ繝�Φ繝�Χ繝･繝ｼ蛻晄悄蛹�        
        setContentView(R.layout.main);
        
        //繧ｵ繝ｼ繝輔ぉ繧､繧ｹ繝ｴ繝･繝ｼ髢｢騾｣蛻晄悄蛹�        
        surfaceview = (SurfaceView)findViewById(R.id.GameView);
        holder = surfaceview.getHolder();
        holder.addCallback(this);
        surfaceview.setOnTouchListener(this);

        //蜷�く繝｣繝ｩ繧ｯ繧ｿ繝ｼ逕ｨ驟榊�蛻晄悄蛹�        
        enemies = new Enemy[3];
        bullets = new Bullet[5];
        
        //繧ｲ繝ｼ繝�ｒ繝ｫ繝ｼ繝励＆縺帙ｋ繝輔Λ繧ｰ
        loop = true;
    }

    /**
     * 繝｡繧､繝ｳ繧ｹ繝ｬ繝�ラ
     */
    public void run() {
    	Canvas canvas;
    	BackScreen backScreen  = new BackScreen(surfaceview.getResources());
		Paint paint = new Paint(Color.BLACK);
		float[] values = new float[9];
		matrix.getValues(values);
		long st, ed,dist;

		//荳ｻ莠ｺ蜈ｬ讖溷�譛溷喧
		myplane = new MyPlane(BitmapFactory.decodeResource(surfaceview.getResources(), R.drawable.myplane));

		//謨ｵ讖溷�譛溷喧
		enemies[0] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy00));
		enemies[1] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy01));
		enemies[2]  = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy02));

		//蠑ｾ蛻晄悄蛹�		
		bullets[0] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[1] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[2] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[3] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[4] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));

		//謨ｵ讖溘�蛻晄悄菴咲ｽｮ繧堤｢ｺ螳�		
		enemies[0].reset();
		enemies[1].reset();
		enemies[2].reset();

		//蜃ｦ逅�Ν繝ｼ繝√Φ
		while(loop){
			//蜃ｦ逅�幕蟋区凾蛻ｻ繧定ｨ倬鹸
			st = System.currentTimeMillis();

			//荳ｻ莠ｺ蜈ｬ讖溘′蠑ｾ繧呈茶縺､
			myplane.shoot(bullets);

			//荳ｻ莠ｺ蜈ｬ讖溘′遘ｻ蜍輔☆繧�
			myplane.move();
			
			//蠑ｾ縺檎ｧｻ蜍輔☆繧�
			bullets[0].move();
			bullets[1].move();
			bullets[2].move();
			bullets[3].move();
			bullets[4].move();
			
			//謨ｵ縺檎ｧｻ蜍輔☆繧�
			enemies[0].move();
			enemies[1].move();
			enemies[2].move();

			//謠冗判髢句ｧ�			
			canvas = holder.lockCanvas();

			//蜑阪�逕ｻ髱｢繧貞�驛ｨ蝪励ｊ縺､縺ｶ縺�	
			paint.setColor(Color.BLUE);
			canvas.drawRect(field, paint);

			//諡｡螟ｧ邇�ｒ險ｭ螳�
			canvas.concat(matrix);
			
			//閭梧勹逕ｻ髱｢繧呈緒逕ｻ
			backScreen.drawBackScreen(canvas);

			//荳ｻ莠ｺ蜈ｬ讖溘ｒ謠冗判
			myplane.draw(canvas);
			
			//蠑ｾ繧呈緒逕ｻ
			bullets[0].draw(canvas);
			bullets[1].draw(canvas);
			bullets[2].draw(canvas);
			bullets[3].draw(canvas);
			bullets[4].draw(canvas);
			
			//謨ｵ繧呈緒逕ｻ
			enemies[0].draw(canvas);
			enemies[1].draw(canvas);
			enemies[2].draw(canvas);

			//謠冗判邨ゆｺ�
			holder.unlockCanvasAndPost(canvas);

			//蜃ｦ逅�ｵゆｺ�凾蛻ｻ繧定ｨ倬鹸
			ed = System.currentTimeMillis();

			//繧ｿ繧､繝溘Φ繧ｰ繧貞粋繧上○繧句�逅�
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
     * 逕ｻ髱｢繧ｵ繧､繧ｺ縺悟､画峩縺ｫ縺ｪ縺｣縺溷�蜷医�蜃ｦ逅�     */
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
	 * 逕ｻ髱｢縺悟�譛溷喧縺輔ｌ縺滓凾縺ｮ蜃ｦ逅�	 */
	public void surfaceCreated(SurfaceHolder holder) {
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * 逕ｻ髱｢縺檎�譽�＆繧後◆譎ゅ�蜃ｦ逅�	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		loop = false;
	}

	/**
	 * 逕ｻ髱｢縺ｫ繧ｿ繝�メ縺輔ｌ縺滓凾縺ｮ蜃ｦ逅�	 */
	public boolean onTouch(View v, MotionEvent event) {
		float x, y;

		//繧ｿ繝�メ縺輔ｌ縺毋蠎ｧ讓�y蠎ｧ讓吶°繧峨�荳ｻ莠ｺ蜈ｬ讖溘�逶ｮ逧�慍繧堤ｮ怜�
		x = (event.getX() - dx) / sx;
		y = (event.getY() - dy) / sy;

		//荳ｻ莠ｺ蜈ｬ讖溘↓逶ｮ逧�慍縺ｮx蠎ｧ讓�y蠎ｧ讓吶ｒ荳弱∴繧�		
		myplane.setPlace(x, y);

		//邯咏ｶ夂噪縺ｫ繧ｿ繝�メ繧呈─遏･縺吶ｋ縺溘ａ縺ｫtrue
		return true;
	}
}