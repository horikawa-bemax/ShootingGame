package bemax.shootinggame;

import java.util.Random;

// �G�@�̒��ۃN���X
public abstract class Enemy extends Sprite{
	protected int point;			// ���j�����Ƃ��ɓ��链�_
	protected Random rand;	// ���������p�ϐ�

	// �R���X�g���N�^
	public Enemy(){
		super();

		// �V���������_���I�u�W�F�N�g�𐶐�
		rand = new Random();
	}

	// ��l���@��_�����ړ�������
	public abstract void move(MyPlane mp);

	// �ړ�������
	@Override
	public abstract void move();

	// �����ʒu�ɖ߂�
	public void reset(){
		y = -image.getHeight() * 2;
		x = rand.nextFloat() * (480-image.getWidth());
		dx = rand.nextInt(5) + 3;
		dy = rand.nextInt(5) + 5;
	}
}
