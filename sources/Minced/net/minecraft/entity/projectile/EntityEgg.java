// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable
{
    public EntityEgg(final World worldIn) {
        super(worldIn);
    }
    
    public EntityEgg(final World worldIn, final EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    public EntityEgg(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public static void registerFixesEgg(final DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownEgg");
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 3) {
            final double d0 = 0.08;
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, (this.rand.nextFloat() - 0.5) * 0.08, (this.rand.nextFloat() - 0.5) * 0.08, (this.rand.nextFloat() - 0.5) * 0.08, Item.getIdFromItem(Items.EGG));
            }
        }
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0f);
        }
        if (!this.world.isRemote) {
            if (this.rand.nextInt(8) == 0) {
                int i = 1;
                if (this.rand.nextInt(32) == 0) {
                    i = 4;
                }
                for (int j = 0; j < i; ++j) {
                    final EntityChicken entitychicken = new EntityChicken(this.world);
                    entitychicken.setGrowingAge(-24000);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                    this.world.spawnEntity(entitychicken);
                }
            }
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }
}
