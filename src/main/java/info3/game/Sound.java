package info3.game;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	String m_name;
	String m_filename;

	Clip[] clips = new Clip[16];
	URL soundUrl[] = new URL[16];
	boolean volumed = false;

	public Sound() {
		soundUrl[0] = getClass().getResource("/src/main/resources/jump16.wav");
		soundUrl[1] = getClass().getResource("/src/main/resources/sword.wav");
		soundUrl[2] = getClass().getResource("/src/main/resources/pickaxe.wav");
		soundUrl[3] = getClass().getResource("/src/main/resources/drink.wav");
		soundUrl[4] = getClass().getResource("/src/main/resources/mobdeath.wav");
		soundUrl[5] = getClass().getResource("/src/main/resources/playerdeath.wav");
		soundUrl[6] = getClass().getResource("/src/main/resources/tostatue.wav");
		soundUrl[7] = getClass().getResource("/src/main/resources/mlem.wav");
		soundUrl[8] = getClass().getResource("/src/main/resources/eat.wav");
		soundUrl[9] = getClass().getResource("/src/main/resources/stal.wav");
		soundUrl[10] = getClass().getResource("/src/main/resources/allsocle.wav");
		soundUrl[11] = getClass().getResource("/src/main/resources/step3.wav");
		soundUrl[12] = getClass().getResource("/src/main/resources/block.wav");
		soundUrl[13] = getClass().getResource("/src/main/resources/menu.wav");
		soundUrl[14] = getClass().getResource("/src/main/resources/click.wav");
		soundUrl[15] = getClass().getResource("/src/main/resources/theme.wav");

		for (int i = 0; i < soundUrl.length; i++) {

			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
				clips[i] = AudioSystem.getClip();
				clips[i].open(ais);
				clips[i].start();
				clips[i].stop();
				FloatControl gainControl = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(0f));
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setVolume() {
		for (int i = 0; i < soundUrl.length; i++) {
			FloatControl gainControl = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f * (float) Math.log10(1.1f));
		}
	}

	public void play(int i) {
		if (!volumed) {
			setVolume();
			volumed = true;
		}
		clips[i].stop();
		clips[i].setMicrosecondPosition(0);
		clips[i].start();
	}

	public void loop(int i) {
		clips[i].loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop(int i) {
		clips[i].stop();
	}

}
