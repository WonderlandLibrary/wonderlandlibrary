// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.border;

public interface IBorderListener
{
    void onSizeChanged(final WorldBorder p0, final double p1);
    
    void func_177692_a(final WorldBorder p0, final double p1, final double p2, final long p3);
    
    void onCenterChanged(final WorldBorder p0, final double p1, final double p2);
    
    void onWarningTimeChanged(final WorldBorder p0, final int p1);
    
    void onWarningDistanceChanged(final WorldBorder p0, final int p1);
    
    void func_177696_b(final WorldBorder p0, final double p1);
    
    void func_177695_c(final WorldBorder p0, final double p1);
}
