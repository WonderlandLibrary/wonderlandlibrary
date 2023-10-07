// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public class EntityPigZombie extends EntityZombie
{
    private static final UUID field_110189_bq;
    private static final AttributeModifier field_110190_br;
    private int angerLevel;
    private int randomSoundDelay;
    private UUID field_175459_bn;
    private static final String __OBFID = "CL_00001693";
    
    public EntityPigZombie(final World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }
    
    @Override
    public void setRevengeTarget(final EntityLivingBase p_70604_1_) {
        super.setRevengeTarget(p_70604_1_);
        if (p_70604_1_ != null) {
            this.field_175459_bn = p_70604_1_.getUniqueID();
        }
    }
    
    @Override
    protected void func_175456_n() {
        this.targetTasks.addTask(1, new AIHurtByAggressor());
        this.targetTasks.addTask(2, new AITargetAggressor());
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(EntityPigZombie.field_110186_bp).setBaseValue(0.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    @Override
    protected void updateAITasks() {
        final IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (this.func_175457_ck()) {
            if (!this.isChild() && !var1.func_180374_a(EntityPigZombie.field_110190_br)) {
                var1.applyModifier(EntityPigZombie.field_110190_br);
            }
            --this.angerLevel;
        }
        else if (var1.func_180374_a(EntityPigZombie.field_110190_br)) {
            var1.removeModifier(EntityPigZombie.field_110190_br);
        }
        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
            this.playSound("mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 1.8f);
        }
        if (this.angerLevel > 0 && this.field_175459_bn != null && this.getAITarget() == null) {
            final EntityPlayer var2 = this.worldObj.getPlayerEntityByUUID(this.field_175459_bn);
            this.setRevengeTarget(var2);
            this.attackingPlayer = var2;
            this.recentlyHit = this.getRevengeTimer();
        }
        super.updateAITasks();
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
    
    @Override
    public boolean handleLavaMovement() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setShort("Anger", (short)this.angerLevel);
        if (this.field_175459_bn != null) {
            tagCompound.setString("HurtBy", this.field_175459_bn.toString());
        }
        else {
            tagCompound.setString("HurtBy", "");
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.angerLevel = tagCompund.getShort("Anger");
        final String var2 = tagCompund.getString("HurtBy");
        if (var2.length() > 0) {
            this.field_175459_bn = UUID.fromString(var2);
            final EntityPlayer var3 = this.worldObj.getPlayerEntityByUUID(this.field_175459_bn);
            this.setRevengeTarget(var3);
            if (var3 != null) {
                this.attackingPlayer = var3;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        final Entity var3 = source.getEntity();
        if (var3 instanceof EntityPlayer) {
            this.becomeAngryAt(var3);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    private void becomeAngryAt(final Entity p_70835_1_) {
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);
        if (p_70835_1_ instanceof EntityLivingBase) {
            this.setRevengeTarget((EntityLivingBase)p_70835_1_);
        }
    }
    
    public boolean func_175457_ck() {
        return this.angerLevel > 0;
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.zombiepig.zpig";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.zombiepig.zpighurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.zombiepig.zpigdeath";
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int var3 = this.rand.nextInt(2 + p_70628_2_), var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.rotten_flesh, 1);
        }
        for (int var3 = this.rand.nextInt(2 + p_70628_2_), var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.gold_nugget, 1);
        }
    }
    
    @Override
    public boolean interact(final EntityPlayer p_70085_1_) {
        return false;
    }
    
    @Override
    protected void addRandomArmor() {
        this.dropItem(Items.gold_ingot, 1);
    }
    
    @Override
    protected void func_180481_a(final DifficultyInstance p_180481_1_) {
        this.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
    }
    
    @Override
    public IEntityLivingData func_180482_a(final DifficultyInstance p_180482_1_, final IEntityLivingData p_180482_2_) {
        super.func_180482_a(p_180482_1_, p_180482_2_);
        this.setVillager(false);
        return p_180482_2_;
    }
    
    static {
        field_110189_bq = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
        field_110190_br = new AttributeModifier(EntityPigZombie.field_110189_bq, "Attacking speed boost", 0.05, 0).setSaved(false);
    }
    
    class AIHurtByAggressor extends EntityAIHurtByTarget
    {
        private static final String __OBFID = "CL_00002206";
        
        public AIHurtByAggressor() {
            super(EntityPigZombie.this, true, new Class[0]);
        }
        
        @Override
        protected void func_179446_a(final EntityCreature p_179446_1_, final EntityLivingBase p_179446_2_) {
            super.func_179446_a(p_179446_1_, p_179446_2_);
            if (p_179446_1_ instanceof EntityPigZombie) {
                ((EntityPigZombie)p_179446_1_).becomeAngryAt(p_179446_2_);
            }
        }
    }
    
    class AITargetAggressor extends EntityAINearestAttackableTarget
    {
        private static final String __OBFID = "CL_00002207";
        
        public AITargetAggressor() {
            super(EntityPigZombie.this, EntityPlayer.class, true);
        }
        
        @Override
        public boolean shouldExecute() {
            return ((EntityPigZombie)this.taskOwner).func_175457_ck() && super.shouldExecute();
        }
    }
}
