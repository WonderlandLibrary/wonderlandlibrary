/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartHopper
extends EntityMinecartContainer
implements IHopper {
    private boolean isBlocked = true;
    private int transferTicker = -1;
    private final BlockPos lastPosition = BlockPos.ORIGIN;

    public EntityMinecartHopper(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartHopper(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.HOPPER;
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.HOPPER.getDefaultState();
    }

    @Override
    public int getDefaultDisplayTileOffset() {
        return 1;
    }

    @Override
    public int getSizeInventory() {
        return 5;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        if (!this.world.isRemote) {
            player.displayGUIChest(this);
        }
        return true;
    }

    @Override
    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
        boolean flag;
        boolean bl = flag = !receivingPower;
        if (flag != this.getBlocked()) {
            this.setBlocked(flag);
        }
    }

    public boolean getBlocked() {
        return this.isBlocked;
    }

    public void setBlocked(boolean p_96110_1_) {
        this.isBlocked = p_96110_1_;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public double getXPos() {
        return this.posX;
    }

    @Override
    public double getYPos() {
        return this.posY + 0.5;
    }

    @Override
    public double getZPos() {
        return this.posZ;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.isEntityAlive() && this.getBlocked()) {
            BlockPos blockpos = new BlockPos(this);
            if (blockpos.equals(this.lastPosition)) {
                --this.transferTicker;
            } else {
                this.setTransferTicker(0);
            }
            if (!this.canTransfer()) {
                this.setTransferTicker(0);
                if (this.captureDroppedItems()) {
                    this.setTransferTicker(4);
                    this.markDirty();
                }
            }
        }
    }

    public boolean captureDroppedItems() {
        if (TileEntityHopper.captureDroppedItems(this)) {
            return true;
        }
        List<Entity> list = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(0.25, 0.0, 0.25), EntitySelectors.IS_ALIVE);
        if (!list.isEmpty()) {
            TileEntityHopper.putDropInInventoryAllSlots(null, this, (EntityItem)list.get(0));
        }
        return false;
    }

    @Override
    public void killMinecart(DamageSource source) {
        super.killMinecart(source);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.HOPPER), 1, 0.0f);
        }
    }

    public static void registerFixesMinecartHopper(DataFixer fixer) {
        EntityMinecartContainer.func_190574_b(fixer, EntityMinecartHopper.class);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("TransferCooldown", this.transferTicker);
        compound.setBoolean("Enabled", this.isBlocked);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.transferTicker = compound.getInteger("TransferCooldown");
        this.isBlocked = compound.hasKey("Enabled") ? compound.getBoolean("Enabled") : true;
    }

    public void setTransferTicker(int p_98042_1_) {
        this.transferTicker = p_98042_1_;
    }

    public boolean canTransfer() {
        return this.transferTicker > 0;
    }

    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerHopper(playerInventory, this, playerIn);
    }
}

