/*
 * Decompiled with CFR 0.152.
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
    @Override
    public float getBrightness(float f) {
        return 1.0f;
    }

    @Override
    public int getBrightnessForRender(float f) {
        return 0xF000F0;
    }

    @Override
    protected void alterSquishAmount() {
        this.squishAmount *= 0.9f;
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        Item item = this.getDropItem();
        if (item != null && this.getSlimeSize() > 1) {
            int n2 = this.rand.nextInt(4) - 2;
            if (n > 0) {
                n2 += this.rand.nextInt(n + 1);
            }
            int n3 = 0;
            while (n3 < n2) {
                this.dropItem(item, 1);
                ++n3;
            }
        }
    }

    @Override
    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }

    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    @Override
    protected void handleJumpLava() {
        this.motionY = 0.22f + (float)this.getSlimeSize() * 0.05f;
        this.isAirBorne = true;
    }

    @Override
    protected String getJumpSound() {
        return this.getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2f);
    }

    @Override
    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.FLAME;
    }

    @Override
    protected void jump() {
        this.motionY = 0.42f + (float)this.getSlimeSize() * 0.1f;
        this.isAirBorne = true;
    }

    @Override
    protected EntitySlime createInstance() {
        return new EntityMagmaCube(this.worldObj);
    }

    @Override
    protected int getAttackStrength() {
        return super.getAttackStrength() + 2;
    }

    @Override
    protected boolean canDamagePlayer() {
        return true;
    }

    @Override
    protected Item getDropItem() {
        return Items.magma_cream;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    protected boolean makesSoundOnLand() {
        return true;
    }

    @Override
    public int getTotalArmorValue() {
        return this.getSlimeSize() * 3;
    }

    public EntityMagmaCube(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
}

