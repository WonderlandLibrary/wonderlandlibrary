/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemCompass
extends Item {
    public ItemCompass() {
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter(){
            double rotation;
            double rota;
            long lastUpdateTick;

            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                double d0;
                Entity entity;
                if (entityIn == null && !stack.isOnItemFrame()) {
                    return 0.0f;
                }
                boolean flag = entityIn != null;
                Entity entity2 = entity = flag ? entityIn : stack.getItemFrame();
                if (worldIn == null) {
                    worldIn = entity.world;
                }
                if (worldIn.provider.isSurfaceWorld()) {
                    double d1 = flag ? (double)entity.rotationYaw : this.getFrameRotation((EntityItemFrame)entity);
                    d1 = MathHelper.func_191273_b(d1 / 360.0, 1.0);
                    double d2 = this.getSpawnToAngle(worldIn, entity) / (Math.PI * 2);
                    d0 = 0.5 - (d1 - 0.25 - d2);
                } else {
                    d0 = Math.random();
                }
                if (flag) {
                    d0 = this.wobble(worldIn, d0);
                }
                return MathHelper.positiveModulo((float)d0, 1.0f);
            }

            private double wobble(World worldIn, double p_185093_2_) {
                if (worldIn.getTotalWorldTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = worldIn.getTotalWorldTime();
                    double d0 = p_185093_2_ - this.rotation;
                    d0 = MathHelper.func_191273_b(d0 + 0.5, 1.0) - 0.5;
                    this.rota += d0 * 0.1;
                    this.rota *= 0.8;
                    this.rotation = MathHelper.func_191273_b(this.rotation + this.rota, 1.0);
                }
                return this.rotation;
            }

            private double getFrameRotation(EntityItemFrame p_185094_1_) {
                return MathHelper.clampAngle(180 + p_185094_1_.facingDirection.getHorizontalIndex() * 90);
            }

            private double getSpawnToAngle(World p_185092_1_, Entity p_185092_2_) {
                BlockPos blockpos = p_185092_1_.getSpawnPoint();
                return Math.atan2((double)blockpos.getZ() - p_185092_2_.posZ, (double)blockpos.getX() - p_185092_2_.posX);
            }
        });
    }
}

