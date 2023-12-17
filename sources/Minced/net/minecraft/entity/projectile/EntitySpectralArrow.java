// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntitySpectralArrow extends EntityArrow
{
    private int duration;
    
    public EntitySpectralArrow(final World worldIn) {
        super(worldIn);
        this.duration = 200;
    }
    
    public EntitySpectralArrow(final World worldIn, final EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.duration = 200;
    }
    
    public EntitySpectralArrow(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
        this.duration = 200;
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
    protected void arrowHit(final EntityLivingBase living) {
        super.arrowHit(living);
        final PotionEffect potioneffect = new PotionEffect(MobEffects.GLOWING, this.duration, 0);
        living.addPotionEffect(potioneffect);
    }
    
    public static void registerFixesSpectralArrow(final DataFixer fixer) {
        EntityArrow.registerFixesArrow(fixer, "SpectralArrow");
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Duration")) {
            this.duration = compound.getInteger("Duration");
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Duration", this.duration);
    }
}
