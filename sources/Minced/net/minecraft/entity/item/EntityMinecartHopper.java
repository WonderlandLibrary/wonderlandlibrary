// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import java.util.List;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.IHopper;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper
{
    private boolean isBlocked;
    private int transferTicker;
    private final BlockPos lastPosition;
    
    public EntityMinecartHopper(final World worldIn) {
        super(worldIn);
        this.isBlocked = true;
        this.transferTicker = -1;
        this.lastPosition = BlockPos.ORIGIN;
    }
    
    public EntityMinecartHopper(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
        this.isBlocked = true;
        this.transferTicker = -1;
        this.lastPosition = BlockPos.ORIGIN;
    }
    
    @Override
    public Type getType() {
        return Type.HOPPER;
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
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        if (!this.world.isRemote) {
            player.displayGUIChest(this);
        }
        return true;
    }
    
    @Override
    public void onActivatorRailPass(final int x, final int y, final int z, final boolean receivingPower) {
        final boolean flag = !receivingPower;
        if (flag != this.getBlocked()) {
            this.setBlocked(flag);
        }
    }
    
    public boolean getBlocked() {
        return this.isBlocked;
    }
    
    public void setBlocked(final boolean p_96110_1_) {
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
            final BlockPos blockpos = new BlockPos(this);
            if (blockpos.equals(this.lastPosition)) {
                --this.transferTicker;
            }
            else {
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
        if (TileEntityHopper.pullItems(this)) {
            return true;
        }
        final List<EntityItem> list = this.world.getEntitiesWithinAABB((Class<? extends EntityItem>)EntityItem.class, this.getEntityBoundingBox().grow(0.25, 0.0, 0.25), (com.google.common.base.Predicate<? super EntityItem>)EntitySelectors.IS_ALIVE);
        if (!list.isEmpty()) {
            TileEntityHopper.putDropInInventoryAllSlots(null, this, list.get(0));
        }
        return false;
    }
    
    @Override
    public void killMinecart(final DamageSource source) {
        super.killMinecart(source);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.HOPPER), 1, 0.0f);
        }
    }
    
    public static void registerFixesMinecartHopper(final DataFixer fixer) {
        EntityMinecartContainer.addDataFixers(fixer, EntityMinecartHopper.class);
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("TransferCooldown", this.transferTicker);
        compound.setBoolean("Enabled", this.isBlocked);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.transferTicker = compound.getInteger("TransferCooldown");
        this.isBlocked = (!compound.hasKey("Enabled") || compound.getBoolean("Enabled"));
    }
    
    public void setTransferTicker(final int p_98042_1_) {
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
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerHopper(playerInventory, this, playerIn);
    }
}
