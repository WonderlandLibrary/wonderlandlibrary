// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.state.IBlockState;
import java.util.HashSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.Random;

public class Explosion
{
    private final boolean isFlaming;
    private final boolean isSmoking;
    private final Random explosionRNG;
    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;
    private final List affectedBlockPositions;
    private final Map field_77288_k;
    private static final String __OBFID = "CL_00000134";
    
    public Explosion(final World worldIn, final Entity p_i45752_2_, final double p_i45752_3_, final double p_i45752_5_, final double p_i45752_7_, final float p_i45752_9_, final List p_i45752_10_) {
        this(worldIn, p_i45752_2_, p_i45752_3_, p_i45752_5_, p_i45752_7_, p_i45752_9_, false, true, p_i45752_10_);
    }
    
    public Explosion(final World worldIn, final Entity p_i45753_2_, final double p_i45753_3_, final double p_i45753_5_, final double p_i45753_7_, final float p_i45753_9_, final boolean p_i45753_10_, final boolean p_i45753_11_, final List p_i45753_12_) {
        this(worldIn, p_i45753_2_, p_i45753_3_, p_i45753_5_, p_i45753_7_, p_i45753_9_, p_i45753_10_, p_i45753_11_);
        this.affectedBlockPositions.addAll(p_i45753_12_);
    }
    
    public Explosion(final World worldIn, final Entity p_i45754_2_, final double p_i45754_3_, final double p_i45754_5_, final double p_i45754_7_, final float p_i45754_9_, final boolean p_i45754_10_, final boolean p_i45754_11_) {
        this.explosionRNG = new Random();
        this.affectedBlockPositions = Lists.newArrayList();
        this.field_77288_k = Maps.newHashMap();
        this.worldObj = worldIn;
        this.exploder = p_i45754_2_;
        this.explosionSize = p_i45754_9_;
        this.explosionX = p_i45754_3_;
        this.explosionY = p_i45754_5_;
        this.explosionZ = p_i45754_7_;
        this.isFlaming = p_i45754_10_;
        this.isSmoking = p_i45754_11_;
    }
    
    public void doExplosionA() {
        final HashSet var1 = Sets.newHashSet();
        final boolean var2 = true;
        for (int var3 = 0; var3 < 16; ++var3) {
            for (int var4 = 0; var4 < 16; ++var4) {
                for (int var5 = 0; var5 < 16; ++var5) {
                    if (var3 == 0 || var3 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15) {
                        double var6 = var3 / 15.0f * 2.0f - 1.0f;
                        double var7 = var4 / 15.0f * 2.0f - 1.0f;
                        double var8 = var5 / 15.0f * 2.0f - 1.0f;
                        final double var9 = Math.sqrt(var6 * var6 + var7 * var7 + var8 * var8);
                        var6 /= var9;
                        var7 /= var9;
                        var8 /= var9;
                        float var10 = this.explosionSize * (0.7f + this.worldObj.rand.nextFloat() * 0.6f);
                        double var11 = this.explosionX;
                        double var12 = this.explosionY;
                        double var13 = this.explosionZ;
                        final float var14 = 0.3f;
                        while (var10 > 0.0f) {
                            final BlockPos var15 = new BlockPos(var11, var12, var13);
                            final IBlockState var16 = this.worldObj.getBlockState(var15);
                            if (var16.getBlock().getMaterial() != Material.air) {
                                final float var17 = (this.exploder != null) ? this.exploder.getExplosionResistance(this, this.worldObj, var15, var16) : var16.getBlock().getExplosionResistance(null);
                                var10 -= (var17 + 0.3f) * 0.3f;
                            }
                            if (var10 > 0.0f && (this.exploder == null || this.exploder.func_174816_a(this, this.worldObj, var15, var16, var10))) {
                                var1.add(var15);
                            }
                            var11 += var6 * 0.30000001192092896;
                            var12 += var7 * 0.30000001192092896;
                            var13 += var8 * 0.30000001192092896;
                            var10 -= 0.22500001f;
                        }
                    }
                }
            }
        }
        this.affectedBlockPositions.addAll(var1);
        final float var18 = this.explosionSize * 2.0f;
        int var4 = MathHelper.floor_double(this.explosionX - var18 - 1.0);
        int var5 = MathHelper.floor_double(this.explosionX + var18 + 1.0);
        final int var19 = MathHelper.floor_double(this.explosionY - var18 - 1.0);
        final int var20 = MathHelper.floor_double(this.explosionY + var18 + 1.0);
        final int var21 = MathHelper.floor_double(this.explosionZ - var18 - 1.0);
        final int var22 = MathHelper.floor_double(this.explosionZ + var18 + 1.0);
        final List var23 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(var4, var19, var21, var5, var20, var22));
        final Vec3 var24 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);
        for (int var25 = 0; var25 < var23.size(); ++var25) {
            final Entity var26 = var23.get(var25);
            if (!var26.func_180427_aV()) {
                final double var27 = var26.getDistance(this.explosionX, this.explosionY, this.explosionZ) / var18;
                if (var27 <= 1.0) {
                    double var28 = var26.posX - this.explosionX;
                    double var29 = var26.posY + var26.getEyeHeight() - this.explosionY;
                    double var30 = var26.posZ - this.explosionZ;
                    final double var31 = MathHelper.sqrt_double(var28 * var28 + var29 * var29 + var30 * var30);
                    if (var31 != 0.0) {
                        var28 /= var31;
                        var29 /= var31;
                        var30 /= var31;
                        final double var32 = this.worldObj.getBlockDensity(var24, var26.getEntityBoundingBox());
                        final double var33 = (1.0 - var27) * var32;
                        var26.attackEntityFrom(DamageSource.setExplosionSource(this), (int)((var33 * var33 + var33) / 2.0 * 8.0 * var18 + 1.0));
                        final double var34 = EnchantmentProtection.func_92092_a(var26, var33);
                        final Entity entity = var26;
                        entity.motionX += var28 * var34;
                        final Entity entity2 = var26;
                        entity2.motionY += var29 * var34;
                        final Entity entity3 = var26;
                        entity3.motionZ += var30 * var34;
                        if (var26 instanceof EntityPlayer) {
                            this.field_77288_k.put(var26, new Vec3(var28 * var33, var29 * var33, var30 * var33));
                        }
                    }
                }
            }
        }
    }
    
    public void doExplosionB(final boolean p_77279_1_) {
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0f, (1.0f + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2f) * 0.7f);
        if (this.explosionSize >= 2.0f && this.isSmoking) {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        else {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        if (this.isSmoking) {
            for (final BlockPos var3 : this.affectedBlockPositions) {
                final Block var4 = this.worldObj.getBlockState(var3).getBlock();
                if (p_77279_1_) {
                    final double var5 = var3.getX() + this.worldObj.rand.nextFloat();
                    final double var6 = var3.getY() + this.worldObj.rand.nextFloat();
                    final double var7 = var3.getZ() + this.worldObj.rand.nextFloat();
                    double var8 = var5 - this.explosionX;
                    double var9 = var6 - this.explosionY;
                    double var10 = var7 - this.explosionZ;
                    final double var11 = MathHelper.sqrt_double(var8 * var8 + var9 * var9 + var10 * var10);
                    var8 /= var11;
                    var9 /= var11;
                    var10 /= var11;
                    double var12 = 0.5 / (var11 / this.explosionSize + 0.1);
                    var12 *= this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3f;
                    var8 *= var12;
                    var9 *= var12;
                    var10 *= var12;
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (var5 + this.explosionX * 1.0) / 2.0, (var6 + this.explosionY * 1.0) / 2.0, (var7 + this.explosionZ * 1.0) / 2.0, var8, var9, var10, new int[0]);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var5, var6, var7, var8, var9, var10, new int[0]);
                }
                if (var4.getMaterial() != Material.air) {
                    if (var4.canDropFromExplosion(this)) {
                        var4.dropBlockAsItemWithChance(this.worldObj, var3, this.worldObj.getBlockState(var3), 1.0f / this.explosionSize, 0);
                    }
                    this.worldObj.setBlockState(var3, Blocks.air.getDefaultState(), 3);
                    var4.onBlockDestroyedByExplosion(this.worldObj, var3, this);
                }
            }
        }
        if (this.isFlaming) {
            for (final BlockPos var3 : this.affectedBlockPositions) {
                if (this.worldObj.getBlockState(var3).getBlock().getMaterial() == Material.air && this.worldObj.getBlockState(var3.offsetDown()).getBlock().isFullBlock() && this.explosionRNG.nextInt(3) == 0) {
                    this.worldObj.setBlockState(var3, Blocks.fire.getDefaultState());
                }
            }
        }
    }
    
    public Map func_77277_b() {
        return this.field_77288_k;
    }
    
    public EntityLivingBase getExplosivePlacedBy() {
        return (this.exploder == null) ? null : ((this.exploder instanceof EntityTNTPrimed) ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : ((this.exploder instanceof EntityLivingBase) ? ((EntityLivingBase)this.exploder) : null));
    }
    
    public void func_180342_d() {
        this.affectedBlockPositions.clear();
    }
    
    public List func_180343_e() {
        return this.affectedBlockPositions;
    }
}
