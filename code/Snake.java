package code;

import java.util.ArrayList;

public class Snake {
	int speed = 1;
	Input input;
	public ArrayList<Object> body = new ArrayList<Object>();
	public int length;
	public int hunger;
	public int applesEaten = 0;
	
	public Snake (int xStart, int yStart, int vx, int vy, int length, Input input, int hunger) {
		this.input = input;
		this.length = length;
		this.hunger = hunger;
		int x = xStart;
		int y = yStart;
		for (int i = 0; i < length; i++) {
			this.body.add(new Object(x, y, vx, vy));
			x-=vx;
		}
	}	
	
	public void move() {
		for (int i = this.length-1; i >= 0; i--) {
			if (i==0) {
				this.body.get(i).move();
			} else {
				Object prevObj = this.body.get(i-1);
				Object currentObj = this.body.get(i);
				currentObj.x = prevObj.x;
				currentObj.y = prevObj.y;
				currentObj.vx = prevObj.vx;
				currentObj.vy = prevObj.vy;
			}
			
		}	
	}
}
