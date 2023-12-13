/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySheep
extends EntityAnimal {
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container(){
        private static final String __OBFID = "CL_00001649";

        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return false;
        }
    }, 2, 1);
    private static final Map field_175514_bm = Maps.newEnumMap(EnumDyeColor.class);
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass(this);
    private static final String __OBFID = "CL_00001648";

    static {
        field_175514_bm.put(EnumDyeColor.WHITE, new float[]{1.0f, 1.0f, 1.0f});
        field_175514_bm.put(EnumDyeColor.ORANGE, new float[]{0.85f, 0.5f, 0.2f});
        field_175514_bm.put(EnumDyeColor.MAGENTA, new float[]{0.7f, 0.3f, 0.85f});
        field_175514_bm.put(EnumDyeColor.LIGHT_BLUE, new float[]{0.4f, 0.6f, 0.85f});
        field_175514_bm.put(EnumDyeColor.YELLOW, new float[]{0.9f, 0.9f, 0.2f});
        field_175514_bm.put(EnumDyeColor.LIME, new float[]{0.5f, 0.8f, 0.1f});
        field_175514_bm.put(EnumDyeColor.PINK, new float[]{0.95f, 0.5f, 0.65f});
        field_175514_bm.put(EnumDyeColor.GRAY, new float[]{0.3f, 0.3f, 0.3f});
        field_175514_bm.put(EnumDyeColor.SILVER, new float[]{0.6f, 0.6f, 0.6f});
        field_175514_bm.put(EnumDyeColor.CYAN, new float[]{0.3f, 0.5f, 0.6f});
        field_175514_bm.put(EnumDyeColor.PURPLE, new float[]{0.5f, 0.25f, 0.7f});
        field_175514_bm.put(EnumDyeColor.BLUE, new float[]{0.2f, 0.3f, 0.7f});
        field_175514_bm.put(EnumDyeColor.BROWN, new float[]{0.4f, 0.3f, 0.2f});
        field_175514_bm.put(EnumDyeColor.GREEN, new float[]{0.4f, 0.5f, 0.2f});
        field_175514_bm.put(EnumDyeColor.RED, new float[]{0.6f, 0.2f, 0.2f});
        field_175514_bm.put(EnumDyeColor.BLACK, new float[]{0.1f, 0.1f, 0.1f});
    }

    public static float[] func_175513_a(EnumDyeColor p_175513_0_) {
        return (float[])field_175514_bm.get(p_175513_0_);
    }

    public EntitySheep(World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 1.3f);
        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1, Items.wheat, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, 1.0));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
        this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
    }

    @Override
    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
        super.onLivingUpdate();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        if (!this.getSheared()) {
            this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.func_175509_cj().func_176765_a()), 0.0f);
        }
        int var3 = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + p_70628_2_);
        for (int var4 = 0; var4 < var3; ++var4) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_mutton, 1);
                continue;
            }
            this.dropItem(Items.mutton, 1);
        }
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.wool);
    }

    @Override
    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public float getHeadRotationPointY(float p_70894_1_) {
        return this.sheepTimer <= 0 ? 0.0f : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0f : (this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0f : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0f));
    }

    public float getHeadRotationAngleX(float p_70890_1_) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float var2 = ((float)(this.sheepTimer - 4) - p_70890_1_) / 32.0f;
            return 0.62831855f + 0.2199115f * MathHelper.sin(var2 * 28.7f);
        }
        return this.sheepTimer > 0 ? 0.62831855f : this.rotationPitch / 57.295776f;
    }

    @Override
    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.shears && !this.getSheared() && !this.isChild()) {
            if (!this.worldObj.isRemote) {
                this.setSheared(true);
                int var3 = 1 + this.rand.nextInt(3);
                for (int var4 = 0; var4 < var3; ++var4) {
                    EntityItem var5 = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.func_175509_cj().func_176765_a()), 1.0f);
                    var5.motionY += (double)(this.rand.nextFloat() * 0.05f);
                    var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
                    var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
                }
            }
            var2.damageItem(1, p_70085_1_);
            this.playSound("mob.sheep.shear", 1.0f, 1.0f);
        }
        return super.interact(p_70085_1_);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Sheared", this.getSheared());
        tagCompound.setByte("Color", (byte)this.func_175509_cj().func_176765_a());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setSheared(tagCompund.getBoolean("Sheared"));
        this.func_175512_b(EnumDyeColor.func_176764_b(tagCompund.getByte("Color")));
    }

    @Override
    protected String getLivingSound() {
        return "mob.sheep.say";
    }

    @Override
    protected String getHurtSound() {
        return "mob.sheep.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.sheep.say";
    }

    @Override
    protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
        this.playSound("mob.sheep.step", 0.15f, 1.0f);
    }

    public EnumDyeColor func_175509_cj() {
        return EnumDyeColor.func_176764_b(this.dataWatcher.getWatchableObjectByte(16) & 0xF);
    }

    public void func_175512_b(EnumDyeColor p_175512_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, (byte)(var2 & 0xF0 | p_175512_1_.func_176765_a() & 0xF));
    }

    public boolean getSheared() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x10) != 0;
    }

    public void setSheared(boolean p_70893_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_70893_1_) {
            this.dataWatcher.updateObject(16, (byte)(var2 | 0x10));
        } else {
            this.dataWatcher.updateObject(16, (byte)(var2 & 0xFFFFFFEF));
        }
    }

    public static EnumDyeColor func_175510_a(Random p_175510_0_) {
        int var1 = p_175510_0_.nextInt(100);
        return var1 < 5 ? EnumDyeColor.BLACK : (var1 < 10 ? EnumDyeColor.GRAY : (var1 < 15 ? EnumDyeColor.SILVER : (var1 < 18 ? EnumDyeColor.BROWN : (p_175510_0_.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }

    public EntitySheep func_180491_b(EntityAgeable p_180491_1_) {
        EntitySheep var2 = (EntitySheep)p_180491_1_;
        EntitySheep var3 = new EntitySheep(this.worldObj);
        var3.func_175512_b(this.func_175511_a(this, var2));
        return var3;
    }

    @Override
    public void eatGrassBonus() {
        this.setSheared(false);
        if (this.isChild()) {
            this.addGrowth(60);
        }
    }

    @Override
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
        this.func_175512_b(EntitySheep.func_175510_a(this.worldObj.rand));
        return p_180482_2_;
    }

    private EnumDyeColor func_175511_a(EntityAnimal p_175511_1_, EntityAnimal p_175511_2_) {
        int var3 = ((EntitySheep)p_175511_1_).func_175509_cj().getDyeColorDamage();
        int var4 = ((EntitySheep)p_175511_2_).func_175509_cj().getDyeColorDamage();
        this.inventoryCrafting.getStackInSlot(0).setItemDamage(var3);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(var4);
        ItemStack var5 = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)p_175511_1_).worldObj);
        int var6 = var5 != null && var5.getItem() == Items.dye ? var5.getMetadata() : (this.worldObj.rand.nextBoolean() ? var3 : var4);
        return EnumDyeColor.func_176766_a(var6);
    }

    @Override
    public float getEyeHeight() {
        return 0.95f * this.height;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable p_90011_1_) {
        return this.func_180491_b(p_90011_1_);
    }
}

