// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.player.advanced;

public class PlaybackEvent
{
    public static int STOPPED;
    public static int STARTED;
    private AdvancedPlayer source;
    private int frame;
    private int id;
    
    public PlaybackEvent(final AdvancedPlayer source, final int id, final int frame) {
        this.id = id;
        this.source = source;
        this.frame = frame;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getFrame() {
        return this.frame;
    }
    
    public void setFrame(final int frame) {
        this.frame = frame;
    }
    
    public AdvancedPlayer getSource() {
        return this.source;
    }
    
    public void setSource(final AdvancedPlayer source) {
        this.source = source;
    }
    
    static {
        PlaybackEvent.STOPPED = 1;
        PlaybackEvent.STARTED = 2;
    }
}
