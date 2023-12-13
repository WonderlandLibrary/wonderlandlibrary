// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockCarrot extends BlockCrops
{
    private static final String __OBFID = "CL_00000212";
    
    @Override
    protected Item getSeed() {
        return Items.carrot;
    }
    
    @Override
    protected Item getCrop() {
        return Items.carrot;
    }
}
