/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntitySpectralArrow
extends EntityArrow {
    private int duration = 200;

    public EntitySpectralArrow(World worldIn) {
        super(worldIn);
    }

    public EntitySpectralArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntitySpectralArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && !this.inGround) {
            this.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, this.duration, 0);
        living.addPotionEffect(potioneffect);
    }

    public static void registerFixesSpectralArrow(DataFixer fixer) {
        EntityArrow.registerFixesArrow(fixer, "SpectralArrow");
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Duration")) {
            this.duration = compound.getInteger("Duration");
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Duration", this.duration);
    }
}

