/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemClock
extends Item {
    public ItemClock() {
        this.addPropertyOverride(new ResourceLocation("time"), new IItemPropertyGetter(){
            double rotation;
            double rota;
            long lastUpdateTick;

            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                Entity entity;
                boolean flag = entityIn != null;
                Entity entity2 = entity = flag ? entityIn : stack.getItemFrame();
                if (worldIn == null && entity != null) {
                    worldIn = entity.world;
                }
                if (worldIn == null) {
                    return 0.0f;
                }
                double d0 = worldIn.provider.isSurfaceWorld() ? (double)worldIn.getCelestialAngle(1.0f) : Math.random();
                d0 = this.wobble(worldIn, d0);
                return (float)d0;
            }

            private double wobble(World p_185087_1_, double p_185087_2_) {
                if (p_185087_1_.getTotalWorldTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = p_185087_1_.getTotalWorldTime();
                    double d0 = p_185087_2_ - this.rotation;
                    d0 = MathHelper.func_191273_b(d0 + 0.5, 1.0) - 0.5;
                    this.rota += d0 * 0.1;
                    this.rota *= 0.9;
                    this.rotation = MathHelper.func_191273_b(this.rotation + this.rota, 1.0);
                }
                return this.rotation;
            }
        });
    }
}

