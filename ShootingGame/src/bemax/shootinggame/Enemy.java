package bemax.shootinggame;

import java.util.Random;

public abstract class Enemy extends Sprite{
	protected int point;
	protected Random rand;

	public Enemy(){
		super();
		rand = new Random();
	}

	public abstract void move(MyPlane mp);

	public abstract void move();

	public void reset(){
		y = -image.getHeight() * 2;
		x = rand.nextFloat() * (480-image.getWidth());
		dx = rand.nextInt(5) + 3;
		dy = rand.nextInt(5) + 5;
	}
}
