// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;

public class ItemClock extends Item
{
    public ItemClock() {
        this.addPropertyOverride(new ResourceLocation("time"), new IItemPropertyGetter() {
            double rotation;
            double rota;
            long lastUpdateTick;
            
            @Override
            public float apply(final ItemStack stack, @Nullable World worldIn, @Nullable final EntityLivingBase entityIn) {
                final boolean flag = entityIn != null;
                final Entity entity = flag ? entityIn : stack.getItemFrame();
                if (worldIn == null && entity != null) {
                    worldIn = entity.world;
                }
                if (worldIn == null) {
                    return 0.0f;
                }
                double d0;
                if (worldIn.provider.isSurfaceWorld()) {
                    d0 = worldIn.getCelestialAngle(1.0f);
                }
                else {
                    d0 = Math.random();
                }
                d0 = this.wobble(worldIn, d0);
                return (float)d0;
            }
            
            private double wobble(final World p_185087_1_, final double p_185087_2_) {
                if (p_185087_1_.getTotalWorldTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = p_185087_1_.getTotalWorldTime();
                    double d0 = p_185087_2_ - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5, 1.0) - 0.5;
                    this.rota += d0 * 0.1;
                    this.rota *= 0.9;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0);
                }
                return this.rotation;
            }
        });
    }
}
