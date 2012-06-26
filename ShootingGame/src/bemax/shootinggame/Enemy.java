package bemax.shootinggame;

import java.util.Random;

public abstract class Enemy extends Sprite{
	protected int point;
	protected Random rand;

	public Enemy(int point){
		super();
		this.point = point;
		rand = new Random();
	}

	public void setPoint(int p){
		this.point = p;
	}

	public abstract void move(MyPlane mp);
}
