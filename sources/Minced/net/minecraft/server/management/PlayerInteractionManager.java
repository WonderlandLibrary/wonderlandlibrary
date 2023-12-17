// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import net.minecraft.world.WorldServer;
import net.minecraft.item.ItemBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.ILockableContainer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class PlayerInteractionManager
{
    public World world;
    public EntityPlayerMP player;
    private GameType gameType;
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos destroyPos;
    private int curblockDamage;
    private boolean receivedFinishDiggingPacket;
    private BlockPos delayedDestroyPos;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock;
    
    public PlayerInteractionManager(final World worldIn) {
        this.gameType = GameType.NOT_SET;
        this.destroyPos = BlockPos.ORIGIN;
        this.delayedDestroyPos = BlockPos.ORIGIN;
        this.durabilityRemainingOnBlock = -1;
        this.world = worldIn;
    }
    
    public void setGameType(final GameType type) {
        (this.gameType = type).configurePlayerCapabilities(this.player.capabilities);
        this.player.sendPlayerAbilities();
        this.player.server.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.player }));
        this.world.updateAllPlayersSleepingFlag();
    }
    
    public GameType getGameType() {
        return this.gameType;
    }
    
    public boolean survivalOrAdventure() {
        return this.gameType.isSurvivalOrAdventure();
    }
    
    public boolean isCreative() {
        return this.gameType.isCreative();
    }
    
    public void initializeGameType(final GameType type) {
        if (this.gameType == GameType.NOT_SET) {
            this.gameType = type;
        }
        this.setGameType(this.gameType);
    }
    
    public void updateBlockRemoving() {
        ++this.curblockDamage;
        if (this.receivedFinishDiggingPacket) {
            final int i = this.curblockDamage - this.initialBlockDamage;
            final IBlockState iblockstate = this.world.getBlockState(this.delayedDestroyPos);
            if (iblockstate.getMaterial() == Material.AIR) {
                this.receivedFinishDiggingPacket = false;
            }
            else {
                final float f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, this.delayedDestroyPos) * (i + 1);
                final int j = (int)(f * 10.0f);
                if (j != this.durabilityRemainingOnBlock) {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.delayedDestroyPos, j);
                    this.durabilityRemainingOnBlock = j;
                }
                if (f >= 1.0f) {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.delayedDestroyPos);
                }
            }
        }
        else if (this.isDestroyingBlock) {
            final IBlockState iblockstate2 = this.world.getBlockState(this.destroyPos);
            if (iblockstate2.getMaterial() == Material.AIR) {
                this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else {
                final int k = this.curblockDamage - this.initialDamage;
                final float f2 = iblockstate2.getPlayerRelativeBlockHardness(this.player, this.player.world, this.delayedDestroyPos) * (k + 1);
                final int l = (int)(f2 * 10.0f);
                if (l != this.durabilityRemainingOnBlock) {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, l);
                    this.durabilityRemainingOnBlock = l;
                }
            }
        }
    }
    
    public void onBlockClicked(final BlockPos pos, final EnumFacing side) {
        if (this.isCreative()) {
            if (!this.world.extinguishFire(null, pos, side)) {
                this.tryHarvestBlock(pos);
            }
        }
        else {
            final IBlockState iblockstate = this.world.getBlockState(pos);
            final Block block = iblockstate.getBlock();
            if (this.gameType.hasLimitedInteractions()) {
                if (this.gameType == GameType.SPECTATOR) {
                    return;
                }
                if (!this.player.isAllowEdit()) {
                    final ItemStack itemstack = this.player.getHeldItemMainhand();
                    if (itemstack.isEmpty()) {
                        return;
                    }
                    if (!itemstack.canDestroy(block)) {
                        return;
                    }
                }
            }
            this.world.extinguishFire(null, pos, side);
            this.initialDamage = this.curblockDamage;
            float f = 1.0f;
            if (iblockstate.getMaterial() != Material.AIR) {
                block.onBlockClicked(this.world, pos, this.player);
                f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, pos);
            }
            if (iblockstate.getMaterial() != Material.AIR && f >= 1.0f) {
                this.tryHarvestBlock(pos);
            }
            else {
                this.isDestroyingBlock = true;
                this.destroyPos = pos;
                final int i = (int)(f * 10.0f);
                this.world.sendBlockBreakProgress(this.player.getEntityId(), pos, i);
                this.durabilityRemainingOnBlock = i;
            }
        }
    }
    
    public void blockRemoving(final BlockPos pos) {
        if (pos.equals(this.destroyPos)) {
            final int i = this.curblockDamage - this.initialDamage;
            final IBlockState iblockstate = this.world.getBlockState(pos);
            if (iblockstate.getMaterial() != Material.AIR) {
                final float f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, pos) * (i + 1);
                if (f >= 0.7f) {
                    this.isDestroyingBlock = false;
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), pos, -1);
                    this.tryHarvestBlock(pos);
                }
                else if (!this.receivedFinishDiggingPacket) {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.delayedDestroyPos = pos;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }
    
    public void cancelDestroyingBlock() {
        this.isDestroyingBlock = false;
        this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
    }
    
    private boolean removeBlock(final BlockPos pos) {
        final IBlockState iblockstate = this.world.getBlockState(pos);
        iblockstate.getBlock().onBlockHarvested(this.world, pos, iblockstate, this.player);
        final boolean flag = this.world.setBlockToAir(pos);
        if (flag) {
            iblockstate.getBlock().onPlayerDestroy(this.world, pos, iblockstate);
        }
        return flag;
    }
    
    public boolean tryHarvestBlock(final BlockPos pos) {
        if (this.gameType.isCreative() && !this.player.getHeldItemMainhand().isEmpty() && this.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
            return false;
        }
        final IBlockState iblockstate = this.world.getBlockState(pos);
        final TileEntity tileentity = this.world.getTileEntity(pos);
        final Block block = iblockstate.getBlock();
        if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !this.player.canUseCommandBlock()) {
            this.world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
            return false;
        }
        if (this.gameType.hasLimitedInteractions()) {
            if (this.gameType == GameType.SPECTATOR) {
                return false;
            }
            if (!this.player.isAllowEdit()) {
                final ItemStack itemstack = this.player.getHeldItemMainhand();
                if (itemstack.isEmpty()) {
                    return false;
                }
                if (!itemstack.canDestroy(block)) {
                    return false;
                }
            }
        }
        this.world.playEvent(this.player, 2001, pos, Block.getStateId(iblockstate));
        final boolean flag1 = this.removeBlock(pos);
        if (this.isCreative()) {
            this.player.connection.sendPacket(new SPacketBlockChange(this.world, pos));
        }
        else {
            final ItemStack itemstack2 = this.player.getHeldItemMainhand();
            final ItemStack itemstack3 = itemstack2.isEmpty() ? ItemStack.EMPTY : itemstack2.copy();
            final boolean flag2 = this.player.canHarvestBlock(iblockstate);
            if (!itemstack2.isEmpty()) {
                itemstack2.onBlockDestroyed(this.world, iblockstate, pos, this.player);
            }
            if (flag1 && flag2) {
                iblockstate.getBlock().harvestBlock(this.world, this.player, pos, iblockstate, tileentity, itemstack3);
            }
        }
        return flag1;
    }
    
    public EnumActionResult processRightClick(final EntityPlayer player, final World worldIn, final ItemStack stack, final EnumHand hand) {
        if (this.gameType == GameType.SPECTATOR) {
            return EnumActionResult.PASS;
        }
        if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
            return EnumActionResult.PASS;
        }
        final int i = stack.getCount();
        final int j = stack.getMetadata();
        final ActionResult<ItemStack> actionresult = stack.useItemRightClick(worldIn, player, hand);
        final ItemStack itemstack = actionresult.getResult();
        if (itemstack == stack && itemstack.getCount() == i && itemstack.getMaxItemUseDuration() <= 0 && itemstack.getMetadata() == j) {
            return actionresult.getType();
        }
        if (actionresult.getType() == EnumActionResult.FAIL && itemstack.getMaxItemUseDuration() > 0 && !player.isHandActive()) {
            return actionresult.getType();
        }
        player.setHeldItem(hand, itemstack);
        if (this.isCreative()) {
            itemstack.setCount(i);
            if (itemstack.isItemStackDamageable()) {
                itemstack.setItemDamage(j);
            }
        }
        if (itemstack.isEmpty()) {
            player.setHeldItem(hand, ItemStack.EMPTY);
        }
        if (!player.isHandActive()) {
            ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
        }
        return actionresult.getType();
    }
    
    public EnumActionResult processRightClickBlock(final EntityPlayer player, final World worldIn, final ItemStack stack, final EnumHand hand, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (this.gameType == GameType.SPECTATOR) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ILockableContainer) {
                final Block block1 = worldIn.getBlockState(pos).getBlock();
                ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;
                if (ilockablecontainer instanceof TileEntityChest && block1 instanceof BlockChest) {
                    ilockablecontainer = ((BlockChest)block1).getLockableContainer(worldIn, pos);
                }
                if (ilockablecontainer != null) {
                    player.displayGUIChest(ilockablecontainer);
                    return EnumActionResult.SUCCESS;
                }
            }
            else if (tileentity instanceof IInventory) {
                player.displayGUIChest((IInventory)tileentity);
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
        if (!player.isSneaking() || (player.getHeldItemMainhand().isEmpty() && player.getHeldItemOffhand().isEmpty())) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, hand, facing, hitX, hitY, hitZ)) {
                return EnumActionResult.SUCCESS;
            }
        }
        if (stack.isEmpty()) {
            return EnumActionResult.PASS;
        }
        if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
            return EnumActionResult.PASS;
        }
        if (stack.getItem() instanceof ItemBlock && !player.canUseCommandBlock()) {
            final Block block2 = ((ItemBlock)stack.getItem()).getBlock();
            if (block2 instanceof BlockCommandBlock || block2 instanceof BlockStructure) {
                return EnumActionResult.FAIL;
            }
        }
        if (this.isCreative()) {
            final int j = stack.getMetadata();
            final int i = stack.getCount();
            final EnumActionResult enumactionresult = stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            stack.setItemDamage(j);
            stack.setCount(i);
            return enumactionresult;
        }
        return stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    public void setWorld(final WorldServer serverWorld) {
        this.world = serverWorld;
    }
}
