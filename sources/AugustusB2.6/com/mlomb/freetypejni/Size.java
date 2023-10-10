// 
// Decompiled by Procyon v0.5.36
// 

package com.mlomb.freetypejni;

public class Size extends Utils.Pointer
{
    public Size(final long n) {
        super(n);
    }
    
    public SizeMetrics getMetrics() {
        final long ft_Size_Get_metrics = FreeType.FT_Size_Get_metrics(this.pointer);
        if (ft_Size_Get_metrics <= 0L) {
            return null;
        }
        return new SizeMetrics(ft_Size_Get_metrics);
    }
}
