package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

// �Q�[���ɓo�ꂷ��L�����N�^�[�S�ʂɋ��ʂ���N���X
public abstract class Sprite {
/*## �C���X�^���X�ϐ� ##*/
	// �摜�̈ړ����]���v�Z���邽�߂̕ϐ�
	protected Matrix matrix;
	protected float[] values;

	// �L�����N�^�[��x���W�Ay���W�Ax�������̈ړ��ʁAy�������̈ړ���
	protected float x, y, dx, dy;

	// �L�����N�^�[�̉摜�f�[�^
	protected Bitmap image;

/*## �R���X�g���N�^ ##*/
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

/*## �C���X�^���X���\�b�h ##*/
	// �L�����N�^�[���L�����o�X�ɕ`�悷��
	public abstract void draw(Canvas canvas);

	// �L�����N�^�[���ړ�������
	public abstract void move();
}
