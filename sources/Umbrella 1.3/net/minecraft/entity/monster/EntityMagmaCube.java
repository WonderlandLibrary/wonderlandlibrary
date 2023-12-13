/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMagmaCube
extends EntitySlime {
    private static final String __OBFID = "CL_00001691";

    public EntityMagmaCube(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2f);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    @Override
    public boolean func_180799_ab() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    @Override
    public int getTotalArmorValue() {
        return this.getSlimeSize() * 3;
    }

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        return 0xF000F0;
    }

    @Override
    public float getBrightness(float p_70013_1_) {
        return 1.0f;
    }

    @Override
    protected EnumParticleTypes func_180487_n() {
        return EnumParticleTypes.FLAME;
    }

    @Override
    protected EntitySlime createInstance() {
        return new EntityMagmaCube(this.worldObj);
    }

    @Override
    protected Item getDropItem() {
        return Items.magma_cream;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        Item var3 = this.getDropItem();
        if (var3 != null && this.getSlimeSize() > 1) {
            int var4 = this.rand.nextInt(4) - 2;
            if (p_70628_2_ > 0) {
                var4 += this.rand.nextInt(p_70628_2_ + 1);
            }
            for (int var5 = 0; var5 < var4; ++var5) {
                this.dropItem(var3, 1);
            }
        }
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }

    @Override
    protected void alterSquishAmount() {
        this.squishAmount *= 0.9f;
    }

    @Override
    protected void jump() {
        this.motionY = 0.42f + (float)this.getSlimeSize() * 0.1f;
        this.isAirBorne = true;
    }

    @Override
    protected void func_180466_bG() {
        this.motionY = 0.22f + (float)this.getSlimeSize() * 0.05f;
        this.isAirBorne = true;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected boolean canDamagePlayer() {
        return true;
    }

    @Override
    protected int getAttackStrength() {
        return super.getAttackStrength() + 2;
    }

    @Override
    protected String getJumpSound() {
        return this.getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    @Override
    protected boolean makesSoundOnLand() {
        return true;
    }
}

