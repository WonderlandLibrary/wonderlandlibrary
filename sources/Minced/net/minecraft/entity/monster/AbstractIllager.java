// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public abstract class AbstractIllager extends EntityMob
{
    protected static final DataParameter<Byte> AGGRESSIVE;
    
    public AbstractIllager(final World p_i47509_1_) {
        super(p_i47509_1_);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractIllager.AGGRESSIVE, (Byte)0);
    }
    
    protected boolean isAggressive(final int mask) {
        final int i = this.dataManager.get(AbstractIllager.AGGRESSIVE);
        return (i & mask) != 0x0;
    }
    
    protected void setAggressive(final int mask, final boolean value) {
        int i = this.dataManager.get(AbstractIllager.AGGRESSIVE);
        if (value) {
            i |= mask;
        }
        else {
            i &= ~mask;
        }
        this.dataManager.set(AbstractIllager.AGGRESSIVE, (byte)(i & 0xFF));
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ILLAGER;
    }
    
    public IllagerArmPose getArmPose() {
        return IllagerArmPose.CROSSED;
    }
    
    static {
        AGGRESSIVE = EntityDataManager.createKey(AbstractIllager.class, DataSerializers.BYTE);
    }
    
    public enum IllagerArmPose
    {
        CROSSED, 
        ATTACKING, 
        SPELLCASTING, 
        BOW_AND_ARROW;
    }
}
