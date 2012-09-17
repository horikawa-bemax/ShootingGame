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
	private Bullet[] bullets;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ���C�A�E�g�t�@�C���ǂݍ���
        setContentView(R.layout.main);

        // �Q�[����ʃI�u�W�F�N�g���쐬
        surfaceview = (SurfaceView)findViewById(R.id.GameView);

        // �Q�[����ʊǗ��p�I�u�W�F�N�g���쐬
        holder = surfaceview.getHolder();
        holder.addCallback(this);

        // �Q�[����ʂɑ΂��A�^�b�`�Z���T�[��L��ɂ���
        surfaceview.setOnTouchListener(this);

        // �G�@�z��̏���
        enemies = new Enemy[3];
        
        bullets = new Bullet[5];

        loop = true;
    }

    public void run() {
    /*## ���[�J���ϐ��̐錾 ##*/
    	// �G��`�����߂̃L�����o�X�I�u�W�F�N�g
    	Canvas canvas;

    	// �w�i�I�u�W�F�N�g�𐶐�
    	BackScreen backScreen  = new BackScreen(surfaceview.getResources());

    	// �G��`�����߂̃y���𐶐�
		Paint paint = new Paint(Color.BLACK);

		// �s��v�Z�p�̔z��𐶐���������
		float[] values = new float[9];
		matrix.getValues(values);

		// ���Ԍv���p�̕ϐ�
		long st, ed,dist;

	/*## �v���O���� ##*/
		// ��l���@�̐���
		myplane = new MyPlane(BitmapFactory.decodeResource(surfaceview.getResources(), R.drawable.myplane));

		// �G�@00�̐���
		enemies[0] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy00));

		// �G�@01�̐���
		enemies[1] = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy01));

		// �G�@02�̐���
		enemies[2]  = new Enemy00(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.enemy02));

		bullets[0] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[1] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[2] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[3] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));
		bullets[4] = new Bullet(BitmapFactory.decodeResource(surfaceview.getResources(),R.drawable.bullet));

		// ��l���@�̔z�u
//		myplane.move();

		// �G�@�̔z�u�i�����_���j
		enemies[0].reset();
		enemies[1].reset();
		enemies[2].reset();

		// �Q�[����ʕ`�揈��
		while(loop){
			// �`��J�n���Ԃ�ۑ�
			st = System.currentTimeMillis();

			myplane.shoot(bullets);
			
			// ��l���@�E�G�@�̈ʒu���X�V
			myplane.move();
			bullets[0].move();
			bullets[1].move();
			bullets[2].move();
			bullets[3].move();
			bullets[4].move();
			
			enemies[0].move();
			enemies[1].move();
			enemies[2].move();

			// ���ʂ��擾
			canvas = holder.lockCanvas();

			// �F�̃y����I��
			paint.setColor(Color.BLUE);

			// �S���ʂ�œh��Ԃ�
			canvas.drawRect(field, paint);

			// ���@�̉�ʃT�C�Y�ɍ��킹�ė��ʂ�ό`
			canvas.concat(matrix);

			// �w�i�摜��`�悷��
			backScreen.drawBackScreen(canvas);

			// ��l���@��`�悷��
			myplane.draw(canvas);
			
			bullets[0].draw(canvas);
			bullets[1].draw(canvas);
			bullets[2].draw(canvas);
			bullets[3].draw(canvas);
			bullets[4].draw(canvas);
			
			// �G�@��`�悷��
			enemies[0].draw(canvas);
			enemies[1].draw(canvas);
			enemies[2].draw(canvas);

			// �O�ʂƗ��ʂ���ւ���
			holder.unlockCanvasAndPost(canvas);

			// �`��I�����Ԃ�ۑ�
			ed = System.currentTimeMillis();

			// �`��ɂ����������Ԃ��v�Z
			dist = ed - st;

			Log.d("DIST",""+dist);

			// �`�掞�Ԃ�20�~���b��菭�Ȃ�������A�����҂�
			if(dist < 20){
				try {
					// 1�^�[����20�~���b�ɂȂ�悤�ɑ҂����Ԃ𒲐�����
					Thread.sleep(20-ed+st);
				} catch (InterruptedException e) {
					// TODO �����������ꂽ catch �u���b�N
					e.printStackTrace();
				}
			}
		}

	}

	private void Log(String string, String string2) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

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