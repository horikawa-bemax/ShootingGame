package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * �X�v���C�g�i�d������j�N���X
 * @author Masaaki.horikawa
 * 2012.9.13
 */
public abstract class Sprite {
	protected Matrix matrix;
	protected float[] values;
	protected float x, y, dx, dy;
	protected Bitmap image;

	/**
	 * �R���X�g���N�^
	 */
	public Sprite(){
		// matrix�̏�����
		matrix = new Matrix();
		values = new float[9];
		matrix.getValues(values);

		// x���W�Ay���W�Ax�����Ay�����̏�����
		x = 0;
		y = -80;
		dx = 0;
		dy = 0;
	}

	/**
	 * ��ʂɕ`�悷��
	 * @param canvas �`�悷���ʃI�u�W�F�N�g
	 */
	public abstract void draw(Canvas canvas);

	/**
	 * �ړ�����
	 */
	public abstract void move();
}
