package bemax.shootinggame;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class ShootingGameActivity extends Activity implements SurfaceHolder.Callback, OnTouchListener{
    private SurfaceView surfaceview;
    private ImageView titleView, endView;
	private SurfaceHolder holder;
	private Handler handler;
	private MediaPlayer player;
	private SoundPool sePool;
	private HashMap<Integer, Integer> map;
	private MainController mainController;

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
					txt.setText("" + ((int[])msg.obj)[0]);

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

    /**
     * アクティビティが画面から消えたとき
     */
    public void onStop(){
    	super.onStop();
    	if(player != null){
    		player.stop();
    	}
    }


    /**
     * サーフェイスが変化したとき
     * */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		/* ゲームのコントローラを生成、スタート */
		mainController = new MainController(surfaceview, handler);
		mainController.start();
	}

	/**
	 * サーフェイスが生成されたとき
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		//ここでMainControllerをnewしちゃだめ！ surfaceViewがまだ出来上がってないから！
	}

	/**
	 * サーフェイスが消去されたとき
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		/* ゲームを強制的に終了します */
		mainController.endLoop();
	}

	/**
	 * 画面にタッチされたとき
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if(v == titleView){
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