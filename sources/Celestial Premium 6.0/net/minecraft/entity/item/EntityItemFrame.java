/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame
extends EntityHanging {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private static final DataParameter<Integer> ROTATION = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.VARINT);
    private float itemDropChance = 1.0f;

    public EntityItemFrame(World worldIn) {
        super(worldIn);
    }

    public EntityItemFrame(World worldIn, BlockPos p_i45852_2_, EnumFacing p_i45852_3_) {
        super(worldIn, p_i45852_2_);
        this.updateFacingWithBoundingBox(p_i45852_3_);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(ITEM, ItemStack.field_190927_a);
        this.getDataManager().register(ROTATION, 0);
    }

    @Override
    public float getCollisionBorderSize() {
        return 0.0f;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!source.isExplosion() && !this.getDisplayedItem().isEmpty()) {
            if (!this.world.isRemote) {
                this.dropItemOrSelf(source.getEntity(), false);
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0f, 1.0f);
                this.setDisplayedItem(ItemStack.field_190927_a);
            }
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public int getWidthPixels() {
        return 12;
    }

    @Override
    public int getHeightPixels() {
        return 12;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = 16.0;
        return distance < (d0 = d0 * 64.0 * EntityItemFrame.getRenderDistanceWeight()) * d0;
    }

    @Override
    public void onBroken(@Nullable Entity brokenEntity) {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0f, 1.0f);
        this.dropItemOrSelf(brokenEntity, true);
    }

    @Override
    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_PLACE, 1.0f, 1.0f);
    }

    public void dropItemOrSelf(@Nullable Entity entityIn, boolean p_146065_2_) {
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = this.getDisplayedItem();
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                if (entityplayer.capabilities.isCreativeMode) {
                    this.removeFrameFromMap(itemstack);
                    return;
                }
            }
            if (p_146065_2_) {
                this.entityDropItem(new ItemStack(Items.ITEM_FRAME), 0.0f);
            }
            if (!itemstack.isEmpty() && this.rand.nextFloat() < this.itemDropChance) {
                itemstack = itemstack.copy();
                this.removeFrameFromMap(itemstack);
                this.entityDropItem(itemstack, 0.0f);
            }
        }
    }

    private void removeFrameFromMap(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.FILLED_MAP) {
                MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, this.world);
                mapdata.mapDecorations.remove("frame-" + this.getEntityId());
            }
            stack.setItemFrame(null);
        }
    }

    public ItemStack getDisplayedItem() {
        return this.getDataManager().get(ITEM);
    }

    public void setDisplayedItem(ItemStack stack) {
        this.setDisplayedItemWithUpdate(stack, true);
    }

    private void setDisplayedItemWithUpdate(ItemStack stack, boolean p_174864_2_) {
        if (!stack.isEmpty()) {
            stack = stack.copy();
            stack.func_190920_e(1);
            stack.setItemFrame(this);
        }
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
        if (!stack.isEmpty()) {
            this.playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0f, 1.0f);
        }
        if (p_174864_2_ && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        ItemStack itemstack;
        if (key.equals(ITEM) && !(itemstack = this.getDisplayedItem()).isEmpty() && itemstack.getItemFrame() != this) {
            itemstack.setItemFrame(this);
        }
    }

    public int getRotation() {
        return this.getDataManager().get(ROTATION);
    }

    public void setItemRotation(int rotationIn) {
        this.setRotation(rotationIn, true);
    }

    private void setRotation(int rotationIn, boolean p_174865_2_) {
        this.getDataManager().set(ROTATION, rotationIn % 8);
        if (p_174865_2_ && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }
    }

    public static void registerFixesItemFrame(DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItemFrame.class, "Item"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        if (!this.getDisplayedItem().isEmpty()) {
            compound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
            compound.setByte("ItemRotation", (byte)this.getRotation());
            compound.setFloat("ItemDropChance", this.itemDropChance);
        }
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
        if (nbttagcompound != null && !nbttagcompound.hasNoTags()) {
            this.setDisplayedItemWithUpdate(new ItemStack(nbttagcompound), false);
            this.setRotation(compound.getByte("ItemRotation"), false);
            if (compound.hasKey("ItemDropChance", 99)) {
                this.itemDropChance = compound.getFloat("ItemDropChance");
            }
        }
        super.readEntityFromNBT(compound);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        ItemStack itemstack = player.getHeldItem(stack);
        if (!this.world.isRemote) {
            if (this.getDisplayedItem().isEmpty()) {
                if (!itemstack.isEmpty()) {
                    this.setDisplayedItem(itemstack);
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                }
            } else {
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, 1.0f, 1.0f);
                this.setItemRotation(this.getRotation() + 1);
            }
        }
        return true;
    }

    public int getAnalogOutput() {
        return this.getDisplayedItem().isEmpty() ? 0 : this.getRotation() % 8 + 1;
    }
}

