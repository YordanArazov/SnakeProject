package code;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input extends KeyAdapter {
	Integer[] controls;
	boolean right = true;
	boolean left = false;
	boolean up = false;
	boolean down = false;
	boolean speed = false;
	boolean action = false;
	
	public Input (Integer[] controls) {
		this.controls = controls;
	}
    @Override
    public void keyPressed(KeyEvent e) {
    	action = true;
        int key = e.getKeyCode();
        speed = false;
        if (key == controls[0]) {
			if(left){speed=true;}
			if(!right) {left = true; up = false; down = false;}
        } else if (key == controls[1]) {
			if(up){speed=true;}
			if(!down) {up = true; left = false; right = false;}
		} else if (key == controls[2]) {
			if(right){speed=true;}
			if(!left) {right = true; up = false; down = false;}
		} else if (key == controls[3]) {
			if(down){speed=true;}
			if(!up) {down = true; left = false; right = false;}
		}
    }
    
    public void resetControls() {
    	this.right = true;
    	this.left = false;
    	this.up = false;
    	this.down = false;
    	this.speed = false;
    }
}