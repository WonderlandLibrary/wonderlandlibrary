// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;

public class EntitySpider extends EntityMob
{
    private static final String __OBFID = "CL_00001699";
    
    public EntitySpider(final World worldIn) {
        super(worldIn);
        this.setSize(1.4f, 0.9f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.field_175455_a);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4f));
        this.tasks.addTask(4, new AISpiderAttack(EntityPlayer.class));
        this.tasks.addTask(4, new AISpiderAttack(EntityIronGolem.class));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new AISpiderTarget(EntityPlayer.class));
        this.targetTasks.addTask(3, new AISpiderTarget(EntityIronGolem.class));
    }
    
    @Override
    protected PathNavigate func_175447_b(final World worldIn) {
        return new PathNavigateClimber(this, worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.spider.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.spider.say";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.spider.death";
    }
    
    @Override
    protected void func_180429_a(final BlockPos p_180429_1_, final Block p_180429_2_) {
        this.playSound("mob.spider.step", 0.15f, 1.0f);
    }
    
    @Override
    protected Item getDropItem() {
        return Items.string;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        super.dropFewItems(p_70628_1_, p_70628_2_);
        if (p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0)) {
            this.dropItem(Items.spider_eye, 1);
        }
    }
    
    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }
    
    @Override
    public void setInWeb() {
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
    
    @Override
    public boolean isPotionApplicable(final PotionEffect p_70687_1_) {
        return p_70687_1_.getPotionID() != Potion.poison.id && super.isPotionApplicable(p_70687_1_);
    }
    
    public boolean isBesideClimbableBlock() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setBesideClimbableBlock(final boolean p_70839_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_70839_1_) {
            var2 |= 0x1;
        }
        else {
            var2 &= 0xFFFFFFFE;
        }
        this.dataWatcher.updateObject(16, var2);
    }
    
    @Override
    public IEntityLivingData func_180482_a(final DifficultyInstance p_180482_1_, final IEntityLivingData p_180482_2_) {
        Object p_180482_2_2 = super.func_180482_a(p_180482_1_, p_180482_2_);
        if (this.worldObj.rand.nextInt(100) == 0) {
            final EntitySkeleton var3 = new EntitySkeleton(this.worldObj);
            var3.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
            var3.func_180482_a(p_180482_1_, null);
            this.worldObj.spawnEntityInWorld(var3);
            var3.mountEntity(this);
        }
        if (p_180482_2_2 == null) {
            p_180482_2_2 = new GroupData();
            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1f * p_180482_1_.func_180170_c()) {
                ((GroupData)p_180482_2_2).func_111104_a(this.worldObj.rand);
            }
        }
        if (p_180482_2_2 instanceof GroupData) {
            final int var4 = ((GroupData)p_180482_2_2).field_111105_a;
            if (var4 > 0 && Potion.potionTypes[var4] != null) {
                this.addPotionEffect(new PotionEffect(var4, Integer.MAX_VALUE));
            }
        }
        return (IEntityLivingData)p_180482_2_2;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.65f;
    }
    
    class AISpiderAttack extends EntityAIAttackOnCollide
    {
        private static final String __OBFID = "CL_00002197";
        
        public AISpiderAttack(final Class p_i45819_2_) {
            super(EntitySpider.this, p_i45819_2_, 1.0, true);
        }
        
        @Override
        public boolean continueExecuting() {
            final float var1 = this.attacker.getBrightness(1.0f);
            if (var1 >= 0.5f && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            }
            return super.continueExecuting();
        }
        
        @Override
        protected double func_179512_a(final EntityLivingBase p_179512_1_) {
            return 4.0f + p_179512_1_.width;
        }
    }
    
    class AISpiderTarget extends EntityAINearestAttackableTarget
    {
        private static final String __OBFID = "CL_00002196";
        
        public AISpiderTarget(final Class p_i45818_2_) {
            super(EntitySpider.this, p_i45818_2_, true);
        }
        
        @Override
        public boolean shouldExecute() {
            final float var1 = this.taskOwner.getBrightness(1.0f);
            return var1 < 0.5f && super.shouldExecute();
        }
    }
    
    public static class GroupData implements IEntityLivingData
    {
        public int field_111105_a;
        private static final String __OBFID = "CL_00001700";
        
        public void func_111104_a(final Random p_111104_1_) {
            final int var2 = p_111104_1_.nextInt(5);
            if (var2 <= 1) {
                this.field_111105_a = Potion.moveSpeed.id;
            }
            else if (var2 <= 2) {
                this.field_111105_a = Potion.damageBoost.id;
            }
            else if (var2 <= 3) {
                this.field_111105_a = Potion.regeneration.id;
            }
            else if (var2 <= 4) {
                this.field_111105_a = Potion.invisibility.id;
            }
        }
    }
}
