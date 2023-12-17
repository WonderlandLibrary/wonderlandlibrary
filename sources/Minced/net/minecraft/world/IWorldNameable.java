// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.text.ITextComponent;

public interface IWorldNameable
{
    String getName();
    
    boolean hasCustomName();
    
    ITextComponent getDisplayName();
}
