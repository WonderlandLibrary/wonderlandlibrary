package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

public class EntitySilverfish
extends EntityMob {
    private AISummonSilverfish summonSilverfish;

    public EntitySilverfish(World worldIn) {
        super(worldIn);
        this.setSize(0.4f, 0.3f);
        this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.summonSilverfish = new AISummonSilverfish(this);
        this.tasks.addTask(3, (EntityAIBase)this.summonSilverfish);
        this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)this, EntityPlayer.class, 1.0, false));
        this.tasks.addTask(5, (EntityAIBase)new AIHideInStone(this));
        this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityPlayer.class, true));
    }

    public double getYOffset() {
        return 0.2;
    }

    public float getEyeHeight() {
        return 0.1f;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected String getLivingSound() {
        return "mob.silverfish.say";
    }

    protected String getHurtSound() {
        return "mob.silverfish.hit";
    }

    protected String getDeathSound() {
        return "mob.silverfish.kill";
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source instanceof EntityDamageSource || source == DamageSource.magic) {
            this.summonSilverfish.func_179462_f();
        }
        return super.attackEntityFrom(source, amount);
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound("mob.silverfish.step", 0.15f, 1.0f);
    }

    protected Item getDropItem() {
        return null;
    }

    public void onUpdate() {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    public float getBlockPathWeight(BlockPos pos) {
        return this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.stone ? 10.0f : super.getBlockPathWeight(pos);
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity((Entity)this, 5.0);
            return entityplayer == null;
        }
        return false;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
}
