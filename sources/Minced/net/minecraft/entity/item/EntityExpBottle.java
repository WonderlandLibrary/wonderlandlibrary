// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.potion.PotionUtils;
import net.minecraft.init.PotionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityThrowable;

public class EntityExpBottle extends EntityThrowable
{
    public EntityExpBottle(final World worldIn) {
        super(worldIn);
    }
    
    public EntityExpBottle(final World worldIn, final EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    public EntityExpBottle(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public static void registerFixesExpBottle(final DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "ThrowableExpBottle");
    }
    
    @Override
    public float getGravityVelocity() {
        return 0.07f;
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        if (!this.world.isRemote) {
            this.world.playEvent(2002, new BlockPos(this), PotionUtils.getPotionColor(PotionTypes.WATER));
            int i = 3 + this.world.rand.nextInt(5) + this.world.rand.nextInt(5);
            while (i > 0) {
                final int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
            }
            this.setDead();
        }
    }
}
