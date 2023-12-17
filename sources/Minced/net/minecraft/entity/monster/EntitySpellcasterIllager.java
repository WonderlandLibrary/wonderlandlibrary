// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public abstract class EntitySpellcasterIllager extends AbstractIllager
{
    private static final DataParameter<Byte> SPELL;
    protected int spellTicks;
    private SpellType activeSpell;
    
    public EntitySpellcasterIllager(final World p_i47506_1_) {
        super(p_i47506_1_);
        this.activeSpell = SpellType.NONE;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySpellcasterIllager.SPELL, (Byte)0);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.spellTicks = compound.getInteger("SpellTicks");
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("SpellTicks", this.spellTicks);
    }
    
    @Override
    public IllagerArmPose getArmPose() {
        return this.isSpellcasting() ? IllagerArmPose.SPELLCASTING : IllagerArmPose.CROSSED;
    }
    
    public boolean isSpellcasting() {
        if (this.world.isRemote) {
            return this.dataManager.get(EntitySpellcasterIllager.SPELL) > 0;
        }
        return this.spellTicks > 0;
    }
    
    public void setSpellType(final SpellType spellType) {
        this.activeSpell = spellType;
        this.dataManager.set(EntitySpellcasterIllager.SPELL, (byte)spellType.id);
    }
    
    protected SpellType getSpellType() {
        return this.world.isRemote ? SpellType.getFromId(this.dataManager.get(EntitySpellcasterIllager.SPELL)) : this.activeSpell;
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.isSpellcasting()) {
            final SpellType entityspellcasterillager$spelltype = this.getSpellType();
            final double d0 = entityspellcasterillager$spelltype.particleSpeed[0];
            final double d2 = entityspellcasterillager$spelltype.particleSpeed[1];
            final double d3 = entityspellcasterillager$spelltype.particleSpeed[2];
            final float f = this.renderYawOffset * 0.017453292f + MathHelper.cos(this.ticksExisted * 0.6662f) * 0.25f;
            final float f2 = MathHelper.cos(f);
            final float f3 = MathHelper.sin(f);
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + f2 * 0.6, this.posY + 1.8, this.posZ + f3 * 0.6, d0, d2, d3, new int[0]);
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - f2 * 0.6, this.posY + 1.8, this.posZ - f3 * 0.6, d0, d2, d3, new int[0]);
        }
    }
    
    protected int getSpellTicks() {
        return this.spellTicks;
    }
    
    protected abstract SoundEvent getSpellSound();
    
    static {
        SPELL = EntityDataManager.createKey(EntitySpellcasterIllager.class, DataSerializers.BYTE);
    }
    
    public class AICastingApell extends EntityAIBase
    {
        public AICastingApell() {
            this.setMutexBits(3);
        }
        
        @Override
        public boolean shouldExecute() {
            return EntitySpellcasterIllager.this.getSpellTicks() > 0;
        }
        
        @Override
        public void startExecuting() {
            super.startExecuting();
            EntitySpellcasterIllager.this.navigator.clearPath();
        }
        
        @Override
        public void resetTask() {
            super.resetTask();
            EntitySpellcasterIllager.this.setSpellType(SpellType.NONE);
        }
        
        @Override
        public void updateTask() {
            if (EntitySpellcasterIllager.this.getAttackTarget() != null) {
                EntitySpellcasterIllager.this.getLookHelper().setLookPositionWithEntity(EntitySpellcasterIllager.this.getAttackTarget(), (float)EntitySpellcasterIllager.this.getHorizontalFaceSpeed(), (float)EntitySpellcasterIllager.this.getVerticalFaceSpeed());
            }
        }
    }
    
    public abstract class AIUseSpell extends EntityAIBase
    {
        protected int spellWarmup;
        protected int spellCooldown;
        
        @Override
        public boolean shouldExecute() {
            return EntitySpellcasterIllager.this.getAttackTarget() != null && !EntitySpellcasterIllager.this.isSpellcasting() && EntitySpellcasterIllager.this.ticksExisted >= this.spellCooldown;
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return EntitySpellcasterIllager.this.getAttackTarget() != null && this.spellWarmup > 0;
        }
        
        @Override
        public void startExecuting() {
            this.spellWarmup = this.getCastWarmupTime();
            EntitySpellcasterIllager.this.spellTicks = this.getCastingTime();
            this.spellCooldown = EntitySpellcasterIllager.this.ticksExisted + this.getCastingInterval();
            final SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                EntitySpellcasterIllager.this.playSound(soundevent, 1.0f, 1.0f);
            }
            EntitySpellcasterIllager.this.setSpellType(this.getSpellType());
        }
        
        @Override
        public void updateTask() {
            --this.spellWarmup;
            if (this.spellWarmup == 0) {
                this.castSpell();
                EntitySpellcasterIllager.this.playSound(EntitySpellcasterIllager.this.getSpellSound(), 1.0f, 1.0f);
            }
        }
        
        protected abstract void castSpell();
        
        protected int getCastWarmupTime() {
            return 20;
        }
        
        protected abstract int getCastingTime();
        
        protected abstract int getCastingInterval();
        
        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();
        
        protected abstract SpellType getSpellType();
    }
    
    public enum SpellType
    {
        NONE(0, 0.0, 0.0, 0.0), 
        SUMMON_VEX(1, 0.7, 0.7, 0.8), 
        FANGS(2, 0.4, 0.3, 0.35), 
        WOLOLO(3, 0.7, 0.5, 0.2), 
        DISAPPEAR(4, 0.3, 0.3, 0.8), 
        BLINDNESS(5, 0.1, 0.1, 0.2);
        
        private final int id;
        private final double[] particleSpeed;
        
        private SpellType(final int idIn, final double xParticleSpeed, final double yParticleSpeed, final double zParticleSpeed) {
            this.id = idIn;
            this.particleSpeed = new double[] { xParticleSpeed, yParticleSpeed, zParticleSpeed };
        }
        
        public static SpellType getFromId(final int idIn) {
            for (final SpellType entityspellcasterillager$spelltype : values()) {
                if (idIn == entityspellcasterillager$spelltype.id) {
                    return entityspellcasterillager$spelltype;
                }
            }
            return SpellType.NONE;
        }
    }
}
