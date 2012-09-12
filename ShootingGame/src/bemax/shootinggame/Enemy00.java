package bemax.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Enemy00 extends Enemy{

	public Enemy00(Bitmap img) {
		super();
		// �G�@�̉摜�f�[�^
		image = img;

		// �|�������̓��_
		point = 10;
	}

	// �G�@���ړ�������
	@Override
	public void move() {
		// �G�@�̍��W���X�V
		x +=  dx;
		y += dy;

		// ���܂��͉E����͂ݏo�悤�Ƃ���Ɣ��˂���
		if(x<0){
			x = 0;
			dx = -dx;
		}else if(x > 480-image.getWidth()){
			x = 480 - image.getWidth();
			dx = -dx;
		}

		// �G�@���ړ�
		matrix.setTranslate(x, y);

		// ������͂ݏo������A���Z�b�g����
		if(y > 800){
			reset();
		}
	}

	// �G�@���L�����o�X�ɕ`�悷��
	public void draw(Canvas canvas) {
		canvas.drawBitmap(image, matrix, null);
	}

	// ��l���@���^�[�Q�b�g�ɂ����G�@�̈ړ�
	public void move(MyPlane mp){
		move();
	}

	// �G�@���X�^�[�g�ʒu�Ƀ��Z�b�g����
	public void reset(){
		// �G�@�̏���y���W
		y = -image.getHeight() * 2;

		// �G�@�̏���x���W
		x = rand.nextFloat() * (480-image.getWidth());

		// �G�@��x�������ւ̏�������
		dx = 0;

		// �G�@��y�������ւ̏�������
		dy = rand.nextFloat() * 10 + 5; //=>5.0 ~ 14.999
	}
}
