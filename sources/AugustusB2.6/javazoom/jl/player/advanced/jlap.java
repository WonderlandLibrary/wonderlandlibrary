// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player.advanced;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import java.io.File;

public class jlap
{
    public static void main(final String[] args) {
        final jlap test = new jlap();
        if (args.length != 1) {
            test.showUsage();
            System.exit(0);
        }
        else {
            try {
                test.play(args[0]);
            }
            catch (Exception ex) {
                System.err.println(ex.getMessage());
                System.exit(0);
            }
        }
    }
    
    public void play(final String filename) throws JavaLayerException, IOException {
        final InfoListener lst = new InfoListener();
        playMp3(new File(filename), lst);
    }
    
    public void showUsage() {
        System.out.println("Usage: jla <filename>");
        System.out.println("");
        System.out.println(" e.g. : java javazoom.jl.player.advanced.jlap localfile.mp3");
    }
    
    public static AdvancedPlayer playMp3(final File mp3, final PlaybackListener listener) throws IOException, JavaLayerException {
        return playMp3(mp3, 0, Integer.MAX_VALUE, listener);
    }
    
    public static AdvancedPlayer playMp3(final File mp3, final int start, final int end, final PlaybackListener listener) throws IOException, JavaLayerException {
        return playMp3(new BufferedInputStream(new FileInputStream(mp3)), start, end, listener);
    }
    
    public static AdvancedPlayer playMp3(final InputStream is, final int start, final int end, final PlaybackListener listener) throws JavaLayerException {
        final AdvancedPlayer player = new AdvancedPlayer(is);
        player.setPlayBackListener(listener);
        new Thread() {
            @Override
            public void run() {
                try {
                    player.play(start, end);
                }
                catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }.start();
        return player;
    }
    
    public class InfoListener extends PlaybackListener
    {
        @Override
        public void playbackStarted(final PlaybackEvent evt) {
            System.out.println("Play started from frame " + evt.getFrame());
        }
        
        @Override
        public void playbackFinished(final PlaybackEvent evt) {
            System.out.println("Play completed at frame " + evt.getFrame());
            System.exit(0);
        }
    }
}
