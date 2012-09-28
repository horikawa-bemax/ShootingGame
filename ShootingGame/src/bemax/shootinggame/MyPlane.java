package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

/**
 * ��l���@�N���X
 * @author Masaaki Horikawa
 * 2012.9.19
 */
public class MyPlane extends Sprite {
	private float px, py;
	private int bno;						//���ɔ��˂���e�̔ԍ�
	private long shoottime;			//�e�𔭎˂�������
	private final int MOVE = 20;		//��l���@�̈ړ���

	/**
	 * �R���X�g���N�^
	 * @param img ��l���@�̉摜�f�[�^
	 */
	public MyPlane(Bitmap img){
		super();
		//�摜�f�[�^��ݒ�
		image = img;
		makeShadow();

		//����l��ݒ�
		px  = x = 240 - image.getWidth()/2;
		py = y = 280 - image.getHeight();
		dx = dy = 0;
		
		//���ɔ��˂���e�̔ԍ�������
		bno = 0;
	}

	/**
	 * �ړ�����
	 */	
	public void move() {
		//���ݒn����ړI�n�܂ł̋������v�Z����
		float ddx = px - x;
		float ddy = py - y;
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);  // �O����̒藝�Ōv�Z

		//�����ɉ����Ĉړ��ʂ����߂�
		if(len >= MOVE){
			//�������ړ��ʂ�蒷���Ƃ��́A�ړ��ʕ������������Ȃ�
			dx = ddx * MOVE / len;
			dy = ddy * MOVE / len;
		}else{
			//�������ړ��ʂ��Z���Ƃ��́A�ړI�n�܂ňړ�
			dx = ddx;
			dy = ddy;
		}

		//x���W,y���W���X�V
		x += dx;
		y += dy;

		// �g�����X�t�H�[��
		values[Matrix.MTRANS_X] = x;
		values[Matrix.MTRANS_Y] = y;
		matrix.setValues(values);
	}

	/**
	 * �`�悷��
	 */
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	/**
	 * x���W��Ԃ�
	 * @return ��l���@��x���W
	 */
	public float getX(){
		return x;
	}

	/**
	 * y���W��Ԃ�
	 * @return ��l���@��y���W
	 */
	public float getY(){
		return y;
	}

	/**
	 * �ړI�n��ݒ肷��
	 * @param tx �ړI�n��x���W
	 * @param ty �ړI�n��y���W
	 */
	public void setPlace(float tx, float ty){
		px = tx - image.getWidth()/2;
		py = ty - image.getHeight()/2;
	}
	
	/**
	 * �e�𔭎�
	 * @param b �e�I�u�W�F�N�g
	 */
	public void shoot(Bullet[] b){
		//�O��e����������������̌o�ߎ��Ԃ��Z�o����
		long interval = System.currentTimeMillis() - shoottime;
		
		//�e�������Ă��悢��ԂȂ�΁A�e������
		if(b[bno].getReady() && interval > 200){
			//�e�𔭎˂�����
			b[bno].shoot(x + image.getWidth()/2, y);
			
			//���̒e�̔ԍ��Ƀ��Z�b�g
			bno++;
			if (bno==5){
				bno = 0;
			}
			
			//�e�𔭎˂����������L�^
			shoottime = System.currentTimeMillis();
		}
	}
}
