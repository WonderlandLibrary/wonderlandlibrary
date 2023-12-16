// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.utils;

public class Timer
{
    private long lastTime;
    
    public Timer() {
        this.lastTime = System.currentTimeMillis();
    }
    
    public void reset() {
        this.lastTime = System.currentTimeMillis();
    }
    
    public boolean hasElapsed(final long millis, final boolean reset) {
        if (System.currentTimeMillis() - this.lastTime > millis) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public boolean hasElapsed(final double seconds, final boolean reset) {
        return this.hasElapsed((long)(seconds * 1000.0), reset);
    }
}
