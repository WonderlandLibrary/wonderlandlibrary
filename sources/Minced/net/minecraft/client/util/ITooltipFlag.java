// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

public interface ITooltipFlag
{
    boolean isAdvanced();
    
    public enum TooltipFlags implements ITooltipFlag
    {
        NORMAL(false), 
        ADVANCED(true);
        
        final boolean isAdvanced;
        
        private TooltipFlags(final boolean advanced) {
            this.isAdvanced = advanced;
        }
        
        @Override
        public boolean isAdvanced() {
            return this.isAdvanced;
        }
    }
}
