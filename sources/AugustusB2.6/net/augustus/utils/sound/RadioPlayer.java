// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils.sound;

import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import java.net.URL;
import net.augustus.utils.TimeHelper;
import javazoom.jl.player.Player;
import net.augustus.utils.interfaces.MM;

public class RadioPlayer implements MM
{
    private Thread thread;
    private Player player;
    private String current;
    private final TimeHelper timeHelper;
    
    public RadioPlayer() {
        this.player = null;
        this.timeHelper = new TimeHelper();
    }
    
    public void start(final String url, final String current) {
        if (this.timeHelper.reached(2000L)) {
            final Player player;
            final Exception ex;
            Exception e;
            (this.thread = new Thread(() -> {
                try {
                    try {
                        new Player(new URL(url).openStream());
                        this.player = player;
                    }
                    catch (JavaLayerException | IOException ex2) {
                        e = ex;
                        e.printStackTrace();
                    }
                    this.player.setGain((float)(RadioPlayer.mm.radio.volume.getValue() * 0.8600000143051147 - 80.0));
                    this.player.play();
                }
                catch (JavaLayerException e2) {
                    e2.printStackTrace();
                }
                return;
            })).start();
            this.timeHelper.reset();
            this.current = "" + current;
        }
    }
    
    public void setVolume() {
        if (this.thread != null) {
            this.player.setGain((float)(RadioPlayer.mm.radio.volume.getValue() * 0.8600000143051147 - 80.0));
        }
    }
    
    public void stop() {
        if (this.thread != null) {
            this.thread.interrupt();
        }
        this.thread = null;
        if (this.player != null) {
            this.player.close();
        }
    }
    
    public String getCurrent() {
        return this.current;
    }
    
    public void setCurrent(final String current) {
        this.current = current;
    }
}
