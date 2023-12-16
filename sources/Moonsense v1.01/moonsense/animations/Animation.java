// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.animations;

public class Animation
{
    private float value;
    private long lastTime;
    private float changePerMillisecond;
    private float start;
    private float end;
    boolean increasing;
    private Easing easing;
    
    public Animation(final float duration, final float start, final float end) {
        this((long)(duration * 1000.0f), start, end, Easing.LINEAR);
    }
    
    public Animation(final float duration, final float start, final float end, final Easing easing) {
        this((long)(duration * 1000.0f), start, end, easing);
    }
    
    public Animation(final long duration, final float start, final float end) {
        this(duration, start, end, Easing.LINEAR);
    }
    
    public Animation(final long duration, final float start, final float end, final Easing easing) {
        this.value = start;
        this.end = end;
        this.start = start;
        this.increasing = (end > start);
        final float difference = Math.abs(start - end);
        this.changePerMillisecond = difference / duration;
        this.lastTime = System.currentTimeMillis();
        this.easing = easing;
    }
    
    public static Animation fromChangePerSecond(final float changePerSecond, final float start, final float end) {
        return fromChangePerSecond(changePerSecond, start, end, Easing.LINEAR);
    }
    
    public static Animation fromChangePerSecond(final float changePerSecond, final float start, final float end, final Easing easing) {
        return new Animation(Math.abs(start - end) / changePerSecond, start, end, easing);
    }
    
    public void reset() {
        this.value = this.start;
        this.lastTime = System.currentTimeMillis();
    }
    
    public float getValue() {
        return this.getEased((this.easing != null) ? this.easing : Easing.LINEAR);
    }
    
    private float loadValue() {
        if (this.value == this.end) {
            return this.value;
        }
        if (this.increasing) {
            if (this.value >= this.end) {
                return this.value = this.end;
            }
            this.value += this.changePerMillisecond * (System.currentTimeMillis() - this.lastTime);
            if (this.value > this.end) {
                this.value = this.end;
            }
            this.lastTime = System.currentTimeMillis();
            return this.value;
        }
        else {
            if (this.value <= this.end) {
                return this.value = this.end;
            }
            this.value -= this.changePerMillisecond * (System.currentTimeMillis() - this.lastTime);
            if (this.value < this.end) {
                this.value = this.end;
            }
            this.lastTime = System.currentTimeMillis();
            return this.value;
        }
    }
    
    public boolean isDone() {
        return this.value == this.end;
    }
    
    public float getEased(final Easing easing) {
        if (easing == Easing.LINEAR) {
            return this.loadValue();
        }
        return map(Easings.getEasingValue(map(this.loadValue(), this.start, this.end, 0.0f, 1.0f), easing), 0.0f, 1.0f, this.start, this.end);
    }
    
    public float getStart() {
        return this.start;
    }
    
    public float getEnd() {
        return this.end;
    }
    
    public boolean hasNoDifference() {
        return this.getStart() == this.getEnd();
    }
    
    @Override
    public String toString() {
        return "Animation{value=" + this.value + ", lastTime=" + this.lastTime + ", changePerMillisecond=" + this.changePerMillisecond + ", start=" + this.start + ", end=" + this.end + ", increasing=" + this.increasing + ", easing=" + this.easing + '}';
    }
    
    private static float map(final float value, final float minInput, final float maxInput, final float minMapped, final float maxMapped) {
        return (value - minInput) / (maxInput - minInput) * (maxMapped - minMapped) + minMapped;
    }
}
