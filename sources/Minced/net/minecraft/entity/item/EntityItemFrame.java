// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumHand;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.Blocks;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemMap;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.EntityHanging;

public class EntityItemFrame extends EntityHanging
{
    private static final DataParameter<ItemStack> ITEM;
    private static final DataParameter<Integer> ROTATION;
    private float itemDropChance;
    
    public EntityItemFrame(final World worldIn) {
        super(worldIn);
        this.itemDropChance = 1.0f;
    }
    
    public EntityItemFrame(final World worldIn, final BlockPos p_i45852_2_, final EnumFacing p_i45852_3_) {
        super(worldIn, p_i45852_2_);
        this.itemDropChance = 1.0f;
        this.updateFacingWithBoundingBox(p_i45852_3_);
    }
    
    @Override
    protected void entityInit() {
        this.getDataManager().register(EntityItemFrame.ITEM, ItemStack.EMPTY);
        this.getDataManager().register(EntityItemFrame.ROTATION, 0);
    }
    
    @Override
    public float getCollisionBorderSize() {
        return 0.0f;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!source.isExplosion() && !this.getDisplayedItem().isEmpty()) {
            if (!this.world.isRemote) {
                this.dropItemOrSelf(source.getTrueSource(), false);
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0f, 1.0f);
                this.setDisplayedItem(ItemStack.EMPTY);
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
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = 16.0;
        d0 = d0 * 64.0 * getRenderDistanceWeight();
        return distance < d0 * d0;
    }
    
    @Override
    public void onBroken(@Nullable final Entity brokenEntity) {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0f, 1.0f);
        this.dropItemOrSelf(brokenEntity, true);
    }
    
    @Override
    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_PLACE, 1.0f, 1.0f);
    }
    
    public void dropItemOrSelf(@Nullable final Entity entityIn, final boolean p_146065_2_) {
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = this.getDisplayedItem();
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)entityIn;
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
    
    private void removeFrameFromMap(final ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.FILLED_MAP) {
                final MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, this.world);
                mapdata.mapDecorations.remove("frame-" + this.getEntityId());
            }
            stack.setItemFrame(null);
        }
    }
    
    public ItemStack getDisplayedItem() {
        return this.getDataManager().get(EntityItemFrame.ITEM);
    }
    
    public void setDisplayedItem(final ItemStack stack) {
        this.setDisplayedItemWithUpdate(stack, true);
    }
    
    private void setDisplayedItemWithUpdate(ItemStack stack, final boolean p_174864_2_) {
        if (!stack.isEmpty()) {
            stack = stack.copy();
            stack.setCount(1);
            stack.setItemFrame(this);
        }
        this.getDataManager().set(EntityItemFrame.ITEM, stack);
        this.getDataManager().setDirty(EntityItemFrame.ITEM);
        if (!stack.isEmpty()) {
            this.playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0f, 1.0f);
        }
        if (p_174864_2_ && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (key.equals(EntityItemFrame.ITEM)) {
            final ItemStack itemstack = this.getDisplayedItem();
            if (!itemstack.isEmpty() && itemstack.getItemFrame() != this) {
                itemstack.setItemFrame(this);
            }
        }
    }
    
    public int getRotation() {
        return this.getDataManager().get(EntityItemFrame.ROTATION);
    }
    
    public void setItemRotation(final int rotationIn) {
        this.setRotation(rotationIn, true);
    }
    
    private void setRotation(final int rotationIn, final boolean p_174865_2_) {
        this.getDataManager().set(EntityItemFrame.ROTATION, rotationIn % 8);
        if (p_174865_2_ && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }
    }
    
    public static void registerFixesItemFrame(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItemFrame.class, new String[] { "Item" }));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        if (!this.getDisplayedItem().isEmpty()) {
            compound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
            compound.setByte("ItemRotation", (byte)this.getRotation());
            compound.setFloat("ItemDropChance", this.itemDropChance);
        }
        super.writeEntityToNBT(compound);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        final NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
        if (nbttagcompound != null && !nbttagcompound.isEmpty()) {
            this.setDisplayedItemWithUpdate(new ItemStack(nbttagcompound), false);
            this.setRotation(compound.getByte("ItemRotation"), false);
            if (compound.hasKey("ItemDropChance", 99)) {
                this.itemDropChance = compound.getFloat("ItemDropChance");
            }
        }
        super.readEntityFromNBT(compound);
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!this.world.isRemote) {
            if (this.getDisplayedItem().isEmpty()) {
                if (!itemstack.isEmpty()) {
                    this.setDisplayedItem(itemstack);
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                }
            }
            else {
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, 1.0f, 1.0f);
                this.setItemRotation(this.getRotation() + 1);
            }
        }
        return true;
    }
    
    public int getAnalogOutput() {
        return this.getDisplayedItem().isEmpty() ? 0 : (this.getRotation() % 8 + 1);
    }
    
    static {
        ITEM = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.ITEM_STACK);
        ROTATION = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.VARINT);
    }
}
