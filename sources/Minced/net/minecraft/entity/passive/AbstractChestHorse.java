// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public abstract class AbstractChestHorse extends AbstractHorse
{
    private static final DataParameter<Boolean> DATA_ID_CHEST;
    
    public AbstractChestHorse(final World worldIn) {
        super(worldIn);
        this.canGallop = false;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractChestHorse.DATA_ID_CHEST, false);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getModifiedMaxHealth());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.17499999701976776);
        this.getEntityAttribute(AbstractChestHorse.JUMP_STRENGTH).setBaseValue(0.5);
    }
    
    public boolean hasChest() {
        return this.dataManager.get(AbstractChestHorse.DATA_ID_CHEST);
    }
    
    public void setChested(final boolean chested) {
        this.dataManager.set(AbstractChestHorse.DATA_ID_CHEST, chested);
    }
    
    @Override
    protected int getInventorySize() {
        return this.hasChest() ? 17 : super.getInventorySize();
    }
    
    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() - 0.25;
    }
    
    @Override
    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.ENTITY_DONKEY_ANGRY;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (this.hasChest()) {
            if (!this.world.isRemote) {
                this.dropItem(Item.getItemFromBlock(Blocks.CHEST), 1);
            }
            this.setChested(false);
        }
    }
    
    public static void registerFixesAbstractChestHorse(final DataFixer fixer, final Class<?> entityClass) {
        AbstractHorse.registerFixesAbstractHorse(fixer, entityClass);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(entityClass, new String[] { "Items" }));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("ChestedHorse", this.hasChest());
        if (this.hasChest()) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (int i = 2; i < this.horseChest.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.horseChest.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    final NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte)i);
                    itemstack.writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }
            compound.setTag("Items", nbttaglist);
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setChested(compound.getBoolean("ChestedHorse"));
        if (this.hasChest()) {
            final NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initHorseChest();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final int j = nbttagcompound.getByte("Slot") & 0xFF;
                if (j >= 2 && j < this.horseChest.getSizeInventory()) {
                    this.horseChest.setInventorySlotContents(j, new ItemStack(nbttagcompound));
                }
            }
        }
        this.updateHorseSlots();
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        if (inventorySlot == 499) {
            if (this.hasChest() && itemStackIn.isEmpty()) {
                this.setChested(false);
                this.initHorseChest();
                return true;
            }
            if (!this.hasChest() && itemStackIn.getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
                this.setChested(true);
                this.initHorseChest();
                return true;
            }
        }
        return super.replaceItemInInventory(inventorySlot, itemStackIn);
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(player, hand);
        }
        if (!this.isChild()) {
            if (this.isTame() && player.isSneaking()) {
                this.openGUI(player);
                return true;
            }
            if (this.isBeingRidden()) {
                return super.processInteract(player, hand);
            }
        }
        if (!itemstack.isEmpty()) {
            boolean flag = this.handleEating(player, itemstack);
            if (!flag && !this.isTame()) {
                if (itemstack.interactWithEntity(player, this, hand)) {
                    return true;
                }
                this.makeMad();
                return true;
            }
            else {
                if (!flag && !this.hasChest() && itemstack.getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
                    this.setChested(true);
                    this.playChestEquipSound();
                    flag = true;
                    this.initHorseChest();
                }
                if (!flag && !this.isChild() && !this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE) {
                    this.openGUI(player);
                    return true;
                }
                if (flag) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    return true;
                }
            }
        }
        if (this.isChild()) {
            return super.processInteract(player, hand);
        }
        if (itemstack.interactWithEntity(player, this, hand)) {
            return true;
        }
        this.mountTo(player);
        return true;
    }
    
    protected void playChestEquipSound() {
        this.playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }
    
    public int getInventoryColumns() {
        return 5;
    }
    
    static {
        DATA_ID_CHEST = EntityDataManager.createKey(AbstractChestHorse.class, DataSerializers.BOOLEAN);
    }
}
