/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntitySpellcasterIllager
extends AbstractIllager {
    private static final DataParameter<Byte> field_193088_c = EntityDataManager.createKey(EntitySpellcasterIllager.class, DataSerializers.BYTE);
    protected int field_193087_b;
    private SpellType field_193089_bx = SpellType.NONE;

    public EntitySpellcasterIllager(World p_i47506_1_) {
        super(p_i47506_1_);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(field_193088_c, (byte)0);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.field_193087_b = compound.getInteger("SpellTicks");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("SpellTicks", this.field_193087_b);
    }

    @Override
    public AbstractIllager.IllagerArmPose func_193077_p() {
        return this.func_193082_dl() ? AbstractIllager.IllagerArmPose.SPELLCASTING : AbstractIllager.IllagerArmPose.CROSSED;
    }

    public boolean func_193082_dl() {
        if (this.world.isRemote) {
            return this.dataManager.get(field_193088_c) > 0;
        }
        return this.field_193087_b > 0;
    }

    public void func_193081_a(SpellType p_193081_1_) {
        this.field_193089_bx = p_193081_1_;
        this.dataManager.set(field_193088_c, (byte)p_193081_1_.field_193345_g);
    }

    protected SpellType func_193083_dm() {
        return !this.world.isRemote ? this.field_193089_bx : SpellType.func_193337_a(this.dataManager.get(field_193088_c).byteValue());
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.field_193087_b > 0) {
            --this.field_193087_b;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.func_193082_dl()) {
            SpellType entityspellcasterillager$spelltype = this.func_193083_dm();
            double d0 = entityspellcasterillager$spelltype.field_193346_h[0];
            double d1 = entityspellcasterillager$spelltype.field_193346_h[1];
            double d2 = entityspellcasterillager$spelltype.field_193346_h[2];
            float f = this.renderYawOffset * ((float)Math.PI / 180) + MathHelper.cos((float)this.ticksExisted * 0.6662f) * 0.25f;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double)f1 * 0.6, this.posY + 1.8, this.posZ + (double)f2 * 0.6, d0, d1, d2, new int[0]);
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - (double)f1 * 0.6, this.posY + 1.8, this.posZ - (double)f2 * 0.6, d0, d1, d2, new int[0]);
        }
    }

    protected int func_193085_dn() {
        return this.field_193087_b;
    }

    protected abstract SoundEvent func_193086_dk();

    public static enum SpellType {
        NONE(0, 0.0, 0.0, 0.0),
        SUMMON_VEX(1, 0.7, 0.7, 0.8),
        FANGS(2, 0.4, 0.3, 0.35),
        WOLOLO(3, 0.7, 0.5, 0.2),
        DISAPPEAR(4, 0.3, 0.3, 0.8),
        BLINDNESS(5, 0.1, 0.1, 0.2);

        private final int field_193345_g;
        private final double[] field_193346_h;

        private SpellType(int p_i47561_3_, double p_i47561_4_, double p_i47561_6_, double p_i47561_8_) {
            this.field_193345_g = p_i47561_3_;
            this.field_193346_h = new double[]{p_i47561_4_, p_i47561_6_, p_i47561_8_};
        }

        public static SpellType func_193337_a(int p_193337_0_) {
            for (SpellType entityspellcasterillager$spelltype : SpellType.values()) {
                if (p_193337_0_ != entityspellcasterillager$spelltype.field_193345_g) continue;
                return entityspellcasterillager$spelltype;
            }
            return NONE;
        }
    }

    public abstract class AIUseSpell
    extends EntityAIBase {
        protected int field_193321_c;
        protected int field_193322_d;

        @Override
        public boolean shouldExecute() {
            if (EntitySpellcasterIllager.this.getAttackTarget() == null) {
                return false;
            }
            if (EntitySpellcasterIllager.this.func_193082_dl()) {
                return false;
            }
            return EntitySpellcasterIllager.this.ticksExisted >= this.field_193322_d;
        }

        @Override
        public boolean continueExecuting() {
            return EntitySpellcasterIllager.this.getAttackTarget() != null && this.field_193321_c > 0;
        }

        @Override
        public void startExecuting() {
            this.field_193321_c = this.func_190867_m();
            EntitySpellcasterIllager.this.field_193087_b = this.func_190869_f();
            this.field_193322_d = EntitySpellcasterIllager.this.ticksExisted + this.func_190872_i();
            SoundEvent soundevent = this.func_190871_k();
            if (soundevent != null) {
                EntitySpellcasterIllager.this.playSound(soundevent, 1.0f, 1.0f);
            }
            EntitySpellcasterIllager.this.func_193081_a(this.func_193320_l());
        }

        @Override
        public void updateTask() {
            --this.field_193321_c;
            if (this.field_193321_c == 0) {
                this.func_190868_j();
                EntitySpellcasterIllager.this.playSound(EntitySpellcasterIllager.this.func_193086_dk(), 1.0f, 1.0f);
            }
        }

        protected abstract void func_190868_j();

        protected int func_190867_m() {
            return 20;
        }

        protected abstract int func_190869_f();

        protected abstract int func_190872_i();

        @Nullable
        protected abstract SoundEvent func_190871_k();

        protected abstract SpellType func_193320_l();
    }

    public class AICastingApell
    extends EntityAIBase {
        public AICastingApell() {
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            return EntitySpellcasterIllager.this.func_193085_dn() > 0;
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            EntitySpellcasterIllager.this.navigator.clearPathEntity();
        }

        @Override
        public void resetTask() {
            super.resetTask();
            EntitySpellcasterIllager.this.func_193081_a(SpellType.NONE);
        }

        @Override
        public void updateTask() {
            if (EntitySpellcasterIllager.this.getAttackTarget() != null) {
                EntitySpellcasterIllager.this.getLookHelper().setLookPositionWithEntity(EntitySpellcasterIllager.this.getAttackTarget(), EntitySpellcasterIllager.this.getHorizontalFaceSpeed(), EntitySpellcasterIllager.this.getVerticalFaceSpeed());
            }
        }
    }
}

