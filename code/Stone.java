package code;

public class Stone extends Object {	//a rectangular shaped object
	public int width;
	public int height;
	public int colTimer;
	public Stone (int x, int y, int width, int height, int colTimer) {
		super(x, y, 0, 0);
		this.width = width;
		this.height = height;
		this.colTimer = colTimer;	//after the timer the stone can be collided with
	}
	
	public boolean checkCollisionWithObject(Object o) {
		if((o.x >= this.x && o.x < this.x+this.width) && 
				(o.y >= this.y && o.y < this.y+this.height)) {
			return true;
		} else {
			return false;
		}
	}
	
}
