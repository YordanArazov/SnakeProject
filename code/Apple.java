package code;

public class Apple extends Object{
	
	public Apple (int rand) {
		super(0, 0, 0, 0);
		calculateRandomPosition(rand);
	}
	
	private void calculateRandomPosition(int rand) {
		this.x = (int) (Math.random() * rand);	//Math.random = from 0 to 1
    	this.y = (int) (Math.random() * rand);	//Rand is max position => position from 0 to maximum
	}
}
