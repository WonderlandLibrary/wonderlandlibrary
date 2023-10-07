package cc.novoline.utils.music;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class WAVMusicPlayer implements IMusicPlayer {

    private Clip clip;

    @Override
    public void setup(String pathToAudio) throws Throwable {
        System.out.println("Trying to get " + pathToAudio);
        AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/" + pathToAudio));
        clip = AudioSystem.getClip();
        clip.open(ais);

        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-15.0f); // Reduce volume by 10 decibels.
    }

    @Override
    public void play() {
        clip.start();
    }

    @Override
    public void stop() {
        clip.loop(0);
        clip.setMicrosecondPosition(0);
        clip.stop();
    }

    @Override
    public void playLooping() {
        clip.loop(1337);
        play();
    }
}
