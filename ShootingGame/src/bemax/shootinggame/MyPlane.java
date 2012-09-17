package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

// ��l���@�N���X
public class MyPlane extends Sprite {
/*## �C���X�^���X�ϐ� ##*/
	// �v�Z�p�̕ϐ�
	private float px, py;
	private int bno;
	private long shoottime;

	// ��l���@�̈ړ���(�萔)
	private final int MOVE = 20;

/*## �R���X�g���N�^ ##*/
	public MyPlane(Bitmap img){
		super();
		// �摜�̏���
		image = img;

		// x���W�̏���
		px  = x = 240 - image.getWidth()/2;

		// y���W�̏���
		py = y = 280 - image.getHeight();

		// �ړ��ʂ̏���
		dx = dy = 0;
		
		bno = 0;
	}

/*## �C���X�^���X���\�b�h ##*/
	// �ړ�����
	public void move() {
		// �O����̒藝�ɂ��A���n�_�ƃ^�b�`�_�̋������v�Z����
		float ddx = px - x;
		float ddy = py - y;
		float len = (float)Math.sqrt(ddx*ddx+ddy*ddy);

		// �ړ��ʂ̌���
		if(len >= MOVE){
			dx = ddx * MOVE / len;
			dy = ddy * MOVE / len;
		}else{
			dx = ddx;
			dy = ddy;
		}

		// �ړ���̍��W������
		x += dx;
		y += dy;

		// ��l���@���ړ�������
		values[Matrix.MTRANS_X] = x;
		values[Matrix.MTRANS_Y] = y;
		matrix.setValues(values);
	}

	// �`�悷��
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	// x���W��Ԃ�
	public float getX(){
		return x;
	}

	// y���W��Ԃ�
	public float getY(){
		return y;
	}

	// �^�b�`�����ʒu����Apx,py�𑪒�
	public void setPlace(float tx, float ty){
		px = tx - image.getWidth()/2;
		py = ty - image.getHeight()/2;
	}
	
	public void shoot(Bullet[] b){
		if(!b[bno].getFly() && System.currentTimeMillis()-shoottime > 200){
			b[bno].shoot(x + image.getWidth()/2, y);
			bno++;
			if (bno==5){
				bno = 0;
			}
			shoottime = System.currentTimeMillis();
		}
	}
}
