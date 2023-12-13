// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.block.Block;

public class BlockUtils
{
    private static ReflectorClass ForgeBlock;
    private static ReflectorMethod ForgeBlock_setLightOpacity;
    private static boolean directAccessValid;
    
    public static void setLightOpacity(final Block block, final int opacity) {
        if (BlockUtils.directAccessValid) {
            try {
                block.setLightOpacity(opacity);
                return;
            }
            catch (IllegalAccessError var3) {
                BlockUtils.directAccessValid = false;
                if (!BlockUtils.ForgeBlock_setLightOpacity.exists()) {
                    throw var3;
                }
            }
        }
        Reflector.callVoid(block, BlockUtils.ForgeBlock_setLightOpacity, opacity);
    }
    
    static {
        BlockUtils.ForgeBlock = new ReflectorClass(Block.class);
        BlockUtils.ForgeBlock_setLightOpacity = new ReflectorMethod(BlockUtils.ForgeBlock, "setLightOpacity");
        BlockUtils.directAccessValid = true;
    }
}
