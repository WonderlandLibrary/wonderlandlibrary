/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class AbstractIllager
extends EntityMob {
    protected static final DataParameter<Byte> field_193080_a = EntityDataManager.createKey(AbstractIllager.class, DataSerializers.BYTE);

    public AbstractIllager(World p_i47509_1_) {
        super(p_i47509_1_);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(field_193080_a, (byte)0);
    }

    protected boolean func_193078_a(int p_193078_1_) {
        byte i = this.dataManager.get(field_193080_a);
        return (i & p_193078_1_) != 0;
    }

    protected void func_193079_a(int p_193079_1_, boolean p_193079_2_) {
        int i = this.dataManager.get(field_193080_a).byteValue();
        i = p_193079_2_ ? (i |= p_193079_1_) : (i &= ~p_193079_1_);
        this.dataManager.set(field_193080_a, (byte)(i & 0xFF));
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ILLAGER;
    }

    public IllagerArmPose func_193077_p() {
        return IllagerArmPose.CROSSED;
    }

    public static enum IllagerArmPose {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW;

    }
}

