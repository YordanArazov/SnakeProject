package code;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
	
	Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound() {
		soundURL[0] = getClass().getResource("/sound/daylight.wav");
		soundURL[1] = getClass().getResource("/sound/enchanted.wav");
		soundURL[2] = getClass().getResource("/sound/hiss.wav");
		soundURL[3] = getClass().getResource("/sound/pow.wav");
		soundURL[4] = getClass().getResource("/sound/crash.wav");
		soundURL[5] = getClass().getResource("/sound/play.wav");
		soundURL[6] = getClass().getResource("/sound/switch.wav");
		soundURL[7] = getClass().getResource("/sound/swoosh.wav");
		soundURL[8] = getClass().getResource("/sound/impact.wav");
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			if(i == 2) {
				FloatControl gainControl = 
					    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(-15.0f); // Reduce volume by 15 decibels.
			}
			if(i == 7) {
				FloatControl gainControl = 
					    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(20.0f); // Increase volume by 20 decibels.
			}
		} catch(Exception e) {
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
}
