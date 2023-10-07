// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.dispenser;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem
{
    private static final String __OBFID = "CL_00001195";
    
    @Override
    public final ItemStack dispense(final IBlockSource source, final ItemStack stack) {
        final ItemStack var3 = this.dispenseStack(source, stack);
        this.playDispenseSound(source);
        this.spawnDispenseParticles(source, BlockDispenser.getFacing(source.getBlockMetadata()));
        return var3;
    }
    
    protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
        final EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
        final IPosition var4 = BlockDispenser.getDispensePosition(source);
        final ItemStack var5 = stack.splitStack(1);
        doDispense(source.getWorld(), var5, 6, var3, var4);
        return stack;
    }
    
    public static void doDispense(final World worldIn, final ItemStack stack, final int speed, final EnumFacing p_82486_3_, final IPosition position) {
        final double var5 = position.getX();
        double var6 = position.getY();
        final double var7 = position.getZ();
        if (p_82486_3_.getAxis() == EnumFacing.Axis.Y) {
            var6 -= 0.125;
        }
        else {
            var6 -= 0.15625;
        }
        final EntityItem var8 = new EntityItem(worldIn, var5, var6, var7, stack);
        final double var9 = worldIn.rand.nextDouble() * 0.1 + 0.2;
        var8.motionX = p_82486_3_.getFrontOffsetX() * var9;
        var8.motionY = 0.20000000298023224;
        var8.motionZ = p_82486_3_.getFrontOffsetZ() * var9;
        final EntityItem entityItem = var8;
        entityItem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937 * speed;
        final EntityItem entityItem2 = var8;
        entityItem2.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937 * speed;
        final EntityItem entityItem3 = var8;
        entityItem3.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937 * speed;
        worldIn.spawnEntityInWorld(var8);
    }
    
    protected void playDispenseSound(final IBlockSource source) {
        source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
    }
    
    protected void spawnDispenseParticles(final IBlockSource source, final EnumFacing facingIn) {
        source.getWorld().playAuxSFX(2000, source.getBlockPos(), this.func_82488_a(facingIn));
    }
    
    private int func_82488_a(final EnumFacing facingIn) {
        return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
    }
}
