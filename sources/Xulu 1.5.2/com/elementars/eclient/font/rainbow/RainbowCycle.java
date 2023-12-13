// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.font.rainbow;

public class RainbowCycle implements Cloneable
{
    public ColorChangeType red;
    public ColorChangeType green;
    public ColorChangeType blue;
    public int r;
    public int g;
    public int b;
    
    public RainbowCycle() {
        this.red = ColorChangeType.INCREASE;
        this.green = ColorChangeType.NONE;
        this.blue = ColorChangeType.NONE;
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }
    
    public RainbowCycle clone() {
        try {
            return (RainbowCycle)super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }
    }
}
