/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.world.IBlockAccess
 */
package net.dev.important.injection.forge.mixins.optimize;

import net.dev.important.injection.access.IBlock;
import net.dev.important.injection.access.IWorld;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Block.class})
public abstract class MixinBlock
implements IBlock {
    @Shadow
    public abstract int func_149750_m();

    @Shadow
    public abstract int func_149717_k();

    @Override
    public int getLightValue(IBlockAccess iBlockAccess, int n, int n2, int n3) {
        Block block = ((IWorld)iBlockAccess).getBlockState(n, n2, n3).func_177230_c();
        if (!this.equals(block)) {
            return ((IBlock)block).getLightValue(iBlockAccess, n, n2, n3);
        }
        return this.func_149750_m();
    }

    @Override
    public int getLightOpacity(IBlockAccess iBlockAccess, int n, int n2, int n3) {
        return this.func_149717_k();
    }
}

