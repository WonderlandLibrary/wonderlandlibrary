// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class FrameTimer
{
    private final long[] frames;
    private int lastIndex;
    private int counter;
    private int index;
    
    public FrameTimer() {
        this.frames = new long[240];
    }
    
    public void addFrame(final long runningTime) {
        this.frames[this.index] = runningTime;
        ++this.index;
        if (this.index == 240) {
            this.index = 0;
        }
        if (this.counter < 240) {
            this.lastIndex = 0;
            ++this.counter;
        }
        else {
            this.lastIndex = this.parseIndex(this.index + 1);
        }
    }
    
    public int getLagometerValue(final long time, final int multiplier) {
        final double d0 = time / 1.6666666E7;
        return (int)(d0 * multiplier);
    }
    
    public int getLastIndex() {
        return this.lastIndex;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int parseIndex(final int rawIndex) {
        return rawIndex % 240;
    }
    
    public long[] getFrames() {
        return this.frames;
    }
}
