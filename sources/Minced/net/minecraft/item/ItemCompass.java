// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;

public class ItemCompass extends Item
{
    public ItemCompass() {
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
            double rotation;
            double rota;
            long lastUpdateTick;
            
            @Override
            public float apply(final ItemStack stack, @Nullable World worldIn, @Nullable final EntityLivingBase entityIn) {
                if (entityIn == null && !stack.isOnItemFrame()) {
                    return 0.0f;
                }
                final boolean flag = entityIn != null;
                final Entity entity = flag ? entityIn : stack.getItemFrame();
                if (worldIn == null) {
                    worldIn = entity.world;
                }
                double d3;
                if (worldIn.provider.isSurfaceWorld()) {
                    double d1 = flag ? entity.rotationYaw : this.getFrameRotation((EntityItemFrame)entity);
                    d1 = MathHelper.positiveModulo(d1 / 360.0, 1.0);
                    final double d2 = this.getSpawnToAngle(worldIn, entity) / 6.283185307179586;
                    d3 = 0.5 - (d1 - 0.25 - d2);
                }
                else {
                    d3 = Math.random();
                }
                if (flag) {
                    d3 = this.wobble(worldIn, d3);
                }
                return MathHelper.positiveModulo((float)d3, 1.0f);
            }
            
            private double wobble(final World worldIn, final double p_185093_2_) {
                if (worldIn.getTotalWorldTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = worldIn.getTotalWorldTime();
                    double d0 = p_185093_2_ - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5, 1.0) - 0.5;
                    this.rota += d0 * 0.1;
                    this.rota *= 0.8;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0);
                }
                return this.rotation;
            }
            
            private double getFrameRotation(final EntityItemFrame p_185094_1_) {
                return MathHelper.wrapDegrees(180 + p_185094_1_.facingDirection.getHorizontalIndex() * 90);
            }
            
            private double getSpawnToAngle(final World p_185092_1_, final Entity p_185092_2_) {
                final BlockPos blockpos = p_185092_1_.getSpawnPoint();
                return Math.atan2(blockpos.getZ() - p_185092_2_.posZ, blockpos.getX() - p_185092_2_.posX);
            }
        });
    }
}
