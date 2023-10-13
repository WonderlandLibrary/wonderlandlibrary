/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityGiantZombie
extends EntityMob {
    private static final String __OBFID = "CL_00001690";

    public EntityGiantZombie(World worldIn) {
        super(worldIn);
        this.setSize(this.width * 6.0f, this.height * 6.0f);
    }

    @Override
    public float getEyeHeight() {
        return 10.440001f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(50.0);
    }

    @Override
    public float func_180484_a(BlockPos p_180484_1_) {
        return this.worldObj.getLightBrightness(p_180484_1_) - 0.5f;
    }
}

