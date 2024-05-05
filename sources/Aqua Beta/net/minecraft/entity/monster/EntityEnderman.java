package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityEnderman
extends EntityMob {
    private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString((String)"020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier attackingSpeedBoostModifier = new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", (double)0.15f, 0).setSaved(false);
    private static final Set<Block> carriableBlocks = Sets.newIdentityHashSet();
    private boolean isAggressive;

    public EntityEnderman(World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 2.9f);
        this.stepHeight = 1.0f;
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)this, 1.0, false));
        this.tasks.addTask(7, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.tasks.addTask(10, (EntityAIBase)new AIPlaceBlock(this));
        this.tasks.addTask(11, (EntityAIBase)new AITakeBlock(this));
        this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, (EntityAIBase)new AIFindPlayer(this));
        this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityEndermite.class, 10, true, false, (Predicate)new /* Unavailable Anonymous Inner Class!! */));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.3f);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Object)new Short(0));
        this.dataWatcher.addObject(17, (Object)new Byte(0));
        this.dataWatcher.addObject(18, (Object)new Byte(0));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        IBlockState iblockstate = this.getHeldBlockState();
        tagCompound.setShort("carried", (short)Block.getIdFromBlock((Block)iblockstate.getBlock()));
        tagCompound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        IBlockState iblockstate = tagCompund.hasKey("carried", 8) ? Block.getBlockFromName((String)tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF) : Block.getBlockById((int)tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
        this.setHeldBlockState(iblockstate);
    }

    private boolean shouldAttackPlayer(EntityPlayer player) {
        ItemStack itemstack = player.inventory.armorInventory[3];
        if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock((Block)Blocks.pumpkin)) {
            return false;
        }
        Vec3 vec3 = player.getLook(1.0f).normalize();
        Vec3 vec31 = new Vec3(this.posX - player.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0f) - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
        double d0 = vec31.lengthVector();
        double d1 = vec3.dotProduct(vec31 = vec31.normalize());
        return d1 > 1.0 - 0.025 / d0 ? player.canEntityBeSeen((Entity)this) : false;
    }

    public float getEyeHeight() {
        return 2.55f;
    }

    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, (this.rand.nextDouble() - 0.5) * 2.0, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5) * 2.0, new int[0]);
            }
        }
        this.isJumping = false;
        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        float f;
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0) {
            this.setScreaming(false);
        }
        if (this.worldObj.isDaytime() && (f = this.getBrightness(1.0f)) > 0.5f && this.worldObj.canSeeSky(new BlockPos((Entity)this)) && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f) {
            this.setAttackTarget(null);
            this.setScreaming(false);
            this.isAggressive = false;
            this.teleportRandomly();
        }
        super.updateAITasks();
    }

    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5) * 64.0;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d0, d1, d2);
    }

    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3 vec3 = new Vec3(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0f) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3 = vec3.normalize();
        double d0 = 16.0;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - vec3.xCoord * d0;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3.yCoord * d0;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - vec3.zCoord * d0;
        return this.teleportTo(d1, d2, d3);
    }

    protected boolean teleportTo(double x, double y, double z) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        if (this.worldObj.isBlockLoaded(blockpos)) {
            boolean flag1 = false;
            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                Block block = this.worldObj.getBlockState(blockpos1).getBlock();
                if (block.getMaterial().blocksMovement()) {
                    flag1 = true;
                    continue;
                }
                this.posY -= 1.0;
                blockpos = blockpos1;
            }
            if (flag1) {
                super.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                if (this.worldObj.getCollidingBoundingBoxes((Entity)this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            this.setPosition(d0, d1, d2);
            return false;
        }
        int i = 128;
        for (int j = 0; j < i; ++j) {
            double d6 = (double)j / ((double)i - 1.0);
            float f = (this.rand.nextFloat() - 0.5f) * 0.2f;
            float f1 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            float f2 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            double d3 = d0 + (this.posX - d0) * d6 + (this.rand.nextDouble() - 0.5) * (double)this.width * 2.0;
            double d4 = d1 + (this.posY - d1) * d6 + this.rand.nextDouble() * (double)this.height;
            double d5 = d2 + (this.posZ - d2) * d6 + (this.rand.nextDouble() - 0.5) * (double)this.width * 2.0;
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double)f, (double)f1, (double)f2, new int[0]);
        }
        this.worldObj.playSoundEffect(d0, d1, d2, "mob.endermen.portal", 1.0f, 1.0f);
        this.playSound("mob.endermen.portal", 1.0f, 1.0f);
        return true;
    }

    protected String getLivingSound() {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    protected String getHurtSound() {
        return "mob.endermen.hit";
    }

    protected String getDeathSound() {
        return "mob.endermen.death";
    }

    protected Item getDropItem() {
        return Items.ender_pearl;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        Item item = this.getDropItem();
        if (item != null) {
            int i = this.rand.nextInt(2 + lootingModifier);
            for (int j = 0; j < i; ++j) {
                this.dropItem(item, 1);
            }
        }
    }

    public void setHeldBlockState(IBlockState state) {
        this.dataWatcher.updateObject(16, (Object)((short)(Block.getStateId((IBlockState)state) & 0xFFFF)));
    }

    public IBlockState getHeldBlockState() {
        return Block.getStateById((int)(this.dataWatcher.getWatchableObjectShort(16) & 0xFFFF));
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source.getEntity() == null || !(source.getEntity() instanceof EntityEndermite)) {
            if (!this.worldObj.isRemote) {
                this.setScreaming(true);
            }
            if (source instanceof EntityDamageSource && source.getEntity() instanceof EntityPlayer) {
                if (source.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)source.getEntity()).theItemInWorldManager.isCreative()) {
                    this.setScreaming(false);
                } else {
                    this.isAggressive = true;
                }
            }
            if (source instanceof EntityDamageSourceIndirect) {
                this.isAggressive = false;
                for (int i = 0; i < 64; ++i) {
                    if (!this.teleportRandomly()) continue;
                    return true;
                }
                return false;
            }
        }
        boolean flag = super.attackEntityFrom(source, amount);
        if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
            this.teleportRandomly();
        }
        return flag;
    }

    public boolean isScreaming() {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }

    public void setScreaming(boolean screaming) {
        this.dataWatcher.updateObject(18, (Object)((byte)(screaming ? 1 : 0)));
    }

    static /* synthetic */ AttributeModifier access$000() {
        return attackingSpeedBoostModifier;
    }

    static /* synthetic */ boolean access$100(EntityEnderman x0, EntityPlayer x1) {
        return x0.shouldAttackPlayer(x1);
    }

    static /* synthetic */ boolean access$202(EntityEnderman x0, boolean x1) {
        x0.isAggressive = x1;
        return x0.isAggressive;
    }

    static /* synthetic */ Set access$300() {
        return carriableBlocks;
    }

    static {
        carriableBlocks.add((Object)Blocks.grass);
        carriableBlocks.add((Object)Blocks.dirt);
        carriableBlocks.add((Object)Blocks.sand);
        carriableBlocks.add((Object)Blocks.gravel);
        carriableBlocks.add((Object)Blocks.yellow_flower);
        carriableBlocks.add((Object)Blocks.red_flower);
        carriableBlocks.add((Object)Blocks.brown_mushroom);
        carriableBlocks.add((Object)Blocks.red_mushroom);
        carriableBlocks.add((Object)Blocks.tnt);
        carriableBlocks.add((Object)Blocks.cactus);
        carriableBlocks.add((Object)Blocks.clay);
        carriableBlocks.add((Object)Blocks.pumpkin);
        carriableBlocks.add((Object)Blocks.melon_block);
        carriableBlocks.add((Object)Blocks.mycelium);
    }
}
