/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;

public class ItemInWorldManager {
    private int durabilityRemainingOnBlock = -1;
    private int initialDamage;
    private boolean isDestroyingBlock;
    private BlockPos field_180240_f;
    public World theWorld;
    private int initialBlockDamage;
    private int curblockDamage;
    private BlockPos field_180241_i;
    private boolean receivedFinishDiggingPacket;
    private WorldSettings.GameType gameType = WorldSettings.GameType.NOT_SET;
    public EntityPlayerMP thisPlayerMP;

    public boolean tryUseItem(EntityPlayer entityPlayer, World world, ItemStack itemStack) {
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        int n = itemStack.stackSize;
        int n2 = itemStack.getMetadata();
        ItemStack itemStack2 = itemStack.useItemRightClick(world, entityPlayer);
        if (itemStack2 != itemStack || itemStack2 != null && (itemStack2.stackSize != n || itemStack2.getMaxItemUseDuration() > 0 || itemStack2.getMetadata() != n2)) {
            entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = itemStack2;
            if (this.isCreative()) {
                itemStack2.stackSize = n;
                if (itemStack2.isItemStackDamageable()) {
                    itemStack2.setItemDamage(n2);
                }
            }
            if (itemStack2.stackSize == 0) {
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = null;
            }
            if (!entityPlayer.isUsingItem()) {
                ((EntityPlayerMP)entityPlayer).sendContainerToPlayer(entityPlayer.inventoryContainer);
            }
            return true;
        }
        return false;
    }

    public void blockRemoving(BlockPos blockPos) {
        if (blockPos.equals(this.field_180240_f)) {
            int n = this.curblockDamage - this.initialDamage;
            Block block = this.theWorld.getBlockState(blockPos).getBlock();
            if (block.getMaterial() != Material.air) {
                float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, blockPos) * (float)(n + 1);
                if (f >= 0.7f) {
                    this.isDestroyingBlock = false;
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), blockPos, -1);
                    this.tryHarvestBlock(blockPos);
                } else if (!this.receivedFinishDiggingPacket) {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.field_180241_i = blockPos;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public boolean activateBlockOrUseItem(EntityPlayer entityPlayer, World world, ItemStack itemStack, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        IBlockState iBlockState;
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof ILockableContainer) {
                Block block = world.getBlockState(blockPos).getBlock();
                ILockableContainer iLockableContainer = (ILockableContainer)((Object)tileEntity);
                if (iLockableContainer instanceof TileEntityChest && block instanceof BlockChest) {
                    iLockableContainer = ((BlockChest)block).getLockableContainer(world, blockPos);
                }
                if (iLockableContainer != null) {
                    entityPlayer.displayGUIChest(iLockableContainer);
                    return true;
                }
            } else if (tileEntity instanceof IInventory) {
                entityPlayer.displayGUIChest((IInventory)((Object)tileEntity));
                return true;
            }
            return false;
        }
        if ((!entityPlayer.isSneaking() || entityPlayer.getHeldItem() == null) && (iBlockState = world.getBlockState(blockPos)).getBlock().onBlockActivated(world, blockPos, iBlockState, entityPlayer, enumFacing, f, f2, f3)) {
            return true;
        }
        if (itemStack == null) {
            return false;
        }
        if (this.isCreative()) {
            int n = itemStack.getMetadata();
            int n2 = itemStack.stackSize;
            boolean bl = itemStack.onItemUse(entityPlayer, world, blockPos, enumFacing, f, f2, f3);
            itemStack.setItemDamage(n);
            itemStack.stackSize = n2;
            return bl;
        }
        return itemStack.onItemUse(entityPlayer, world, blockPos, enumFacing, f, f2, f3);
    }

    public ItemInWorldManager(World world) {
        this.field_180240_f = BlockPos.ORIGIN;
        this.field_180241_i = BlockPos.ORIGIN;
        this.theWorld = world;
    }

    public void setWorld(WorldServer worldServer) {
        this.theWorld = worldServer;
    }

    public void onBlockClicked(BlockPos blockPos, EnumFacing enumFacing) {
        if (this.isCreative()) {
            if (!this.theWorld.extinguishFire(null, blockPos, enumFacing)) {
                this.tryHarvestBlock(blockPos);
            }
        } else {
            Block block = this.theWorld.getBlockState(blockPos).getBlock();
            if (this.gameType.isAdventure()) {
                if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                    return;
                }
                if (!this.thisPlayerMP.isAllowEdit()) {
                    ItemStack itemStack = this.thisPlayerMP.getCurrentEquippedItem();
                    if (itemStack == null) {
                        return;
                    }
                    if (!itemStack.canDestroy(block)) {
                        return;
                    }
                }
            }
            this.theWorld.extinguishFire(null, blockPos, enumFacing);
            this.initialDamage = this.curblockDamage;
            float f = 1.0f;
            if (block.getMaterial() != Material.air) {
                block.onBlockClicked(this.theWorld, blockPos, this.thisPlayerMP);
                f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, blockPos);
            }
            if (block.getMaterial() != Material.air && f >= 1.0f) {
                this.tryHarvestBlock(blockPos);
            } else {
                this.isDestroyingBlock = true;
                this.field_180240_f = blockPos;
                int n = (int)(f * 10.0f);
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), blockPos, n);
                this.durabilityRemainingOnBlock = n;
            }
        }
    }

    public boolean survivalOrAdventure() {
        return this.gameType.isSurvivalOrAdventure();
    }

    public boolean tryHarvestBlock(BlockPos blockPos) {
        if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null && this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        }
        IBlockState iBlockState = this.theWorld.getBlockState(blockPos);
        TileEntity tileEntity = this.theWorld.getTileEntity(blockPos);
        if (this.gameType.isAdventure()) {
            if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!this.thisPlayerMP.isAllowEdit()) {
                ItemStack itemStack = this.thisPlayerMP.getCurrentEquippedItem();
                if (itemStack == null) {
                    return false;
                }
                if (!itemStack.canDestroy(iBlockState.getBlock())) {
                    return false;
                }
            }
        }
        this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, blockPos, Block.getStateId(iBlockState));
        boolean bl = this.removeBlock(blockPos);
        if (this.isCreative()) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, blockPos));
        } else {
            ItemStack itemStack = this.thisPlayerMP.getCurrentEquippedItem();
            boolean bl2 = this.thisPlayerMP.canHarvestBlock(iBlockState.getBlock());
            if (itemStack != null) {
                itemStack.onBlockDestroyed(this.theWorld, iBlockState.getBlock(), blockPos, this.thisPlayerMP);
                if (itemStack.stackSize == 0) {
                    this.thisPlayerMP.destroyCurrentEquippedItem();
                }
            }
            if (bl && bl2) {
                iBlockState.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, blockPos, iBlockState, tileEntity);
            }
        }
        return bl;
    }

    private boolean removeBlock(BlockPos blockPos) {
        IBlockState iBlockState = this.theWorld.getBlockState(blockPos);
        iBlockState.getBlock().onBlockHarvested(this.theWorld, blockPos, iBlockState, this.thisPlayerMP);
        boolean bl = this.theWorld.setBlockToAir(blockPos);
        if (bl) {
            iBlockState.getBlock().onBlockDestroyedByPlayer(this.theWorld, blockPos, iBlockState);
        }
        return bl;
    }

    public void setGameType(WorldSettings.GameType gameType) {
        this.gameType = gameType;
        gameType.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
        this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, this.thisPlayerMP));
    }

    public void initializeGameType(WorldSettings.GameType gameType) {
        if (this.gameType == WorldSettings.GameType.NOT_SET) {
            this.gameType = gameType;
        }
        this.setGameType(this.gameType);
    }

    public void cancelDestroyingBlock() {
        this.isDestroyingBlock = false;
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
    }

    public void updateBlockRemoving() {
        ++this.curblockDamage;
        if (this.receivedFinishDiggingPacket) {
            int n = this.curblockDamage - this.initialBlockDamage;
            Block block = this.theWorld.getBlockState(this.field_180241_i).getBlock();
            if (block.getMaterial() == Material.air) {
                this.receivedFinishDiggingPacket = false;
            } else {
                float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (float)(n + 1);
                int n2 = (int)(f * 10.0f);
                if (n2 != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, n2);
                    this.durabilityRemainingOnBlock = n2;
                }
                if (f >= 1.0f) {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.field_180241_i);
                }
            }
        } else if (this.isDestroyingBlock) {
            Block block = this.theWorld.getBlockState(this.field_180240_f).getBlock();
            if (block.getMaterial() == Material.air) {
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            } else {
                int n = this.curblockDamage - this.initialDamage;
                float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (float)(n + 1);
                int n3 = (int)(f * 10.0f);
                if (n3 != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, n3);
                    this.durabilityRemainingOnBlock = n3;
                }
            }
        }
    }

    public boolean isCreative() {
        return this.gameType.isCreative();
    }
}

