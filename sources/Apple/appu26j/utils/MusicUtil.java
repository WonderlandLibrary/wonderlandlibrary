package appu26j.utils;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicUtil
{
    private static File previousFile = null;
    private static MediaPlayer mediaPlayer;
    private static boolean playing = false;
    
    public static void playMusic(File file, float volume)
    {
        if (playing)
        {
            pause();
        }
        
        if (previousFile == null || !previousFile.getAbsolutePath().equals(file.getAbsolutePath()))
        {
            Media media = new Media(file.toURI().toString());
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(media);
        }
        
        previousFile = file;
        mediaPlayer.setVolume(volume / 100);
        mediaPlayer.play();
        playing = true;
    }
    
    public static void pause()
    {
        mediaPlayer.pause();
        playing = false;
    }
    
    public static boolean isPlaying()
    {
        return playing;
    }
}
