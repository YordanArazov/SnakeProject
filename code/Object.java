package code;

public class Object {
	int x, y, vx, vy;
	
	public Object (int x, int y, int vx, int vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
	}
	
	public void move() {
		this.x += this.vx;
		this.y += this.vy;
	}
	
	public boolean checkCollisionWithObject(Object o) {
		if(this.x == o.x && this.y == o.y) {
			return true;
		} else {
			return false;
		}
	}	
		
	public boolean checkCollisionWithWindow(int windowWidth, int windowHeight) {
		if (this.y >= windowHeight) {
			return true;
        } else if (this.y < 0) {
        	return true;
        } else if (this.x >= windowWidth) {
        	return true;
        } else if (this.x < 0) {
        	return true;
        } else {
			return false;
		}	
	}
	
}
