// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.render;

public abstract class Animation
{
    public abstract float easeIn(final float p0, final float p1, final float p2, final float p3);
    
    public abstract float easeIn(final float p0, final float p1, final float p2, final float p3, final float p4);
    
    public abstract float easeOut(final float p0, final float p1, final float p2, final float p3);
    
    public abstract float easeOut(final float p0, final float p1, final float p2, final float p3, final float p4);
    
    public abstract float easeInOut(final float p0, final float p1, final float p2, final float p3);
    
    public abstract float easeInOut(final float p0, final float p1, final float p2, final float p3, final float p4);
}
