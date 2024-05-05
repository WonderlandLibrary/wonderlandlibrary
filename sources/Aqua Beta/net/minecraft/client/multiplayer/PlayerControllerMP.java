package net.minecraft.client.multiplayer;

import events.Event;
import events.listeners.EventSycItem;
import intent.AquaDev.aqua.Aqua;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerControllerMP {
    private final Minecraft mc;
    private final NetHandlerPlayClient netClientHandler;
    private BlockPos currentBlock = new BlockPos(-1, -1, -1);
    private ItemStack currentItemHittingBlock;
    public static float curBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private WorldSettings.GameType currentGameType = WorldSettings.GameType.SURVIVAL;
    private int currentPlayerItem;

    public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient netHandler) {
        this.mc = mcIn;
        this.netClientHandler = netHandler;
    }

    public static void clickBlockCreative(Minecraft mcIn, PlayerControllerMP playerController, BlockPos pos, EnumFacing facing) {
        if (!mcIn.theWorld.extinguishFire((EntityPlayer)mcIn.thePlayer, pos, facing)) {
            playerController.onPlayerDestroyBlock(pos, facing);
        }
    }

    public void setPlayerCapabilities(EntityPlayer player) {
        this.currentGameType.configurePlayerCapabilities(player.capabilities);
    }

    public boolean isSpectator() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public void setGameType(WorldSettings.GameType type) {
        this.currentGameType = type;
        this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
    }

    public void flipPlayer(EntityPlayer playerIn) {
        playerIn.rotationYaw = -180.0f;
    }

    public boolean shouldDrawHUD() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean onPlayerDestroyBlock(BlockPos pos, EnumFacing side) {
        ItemStack itemstack1;
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!this.mc.thePlayer.isAllowEdit()) {
                Block block = this.mc.theWorld.getBlockState(pos).getBlock();
                ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
                if (itemstack == null) {
                    return false;
                }
                if (!itemstack.canDestroy(block)) {
                    return false;
                }
            }
        }
        if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        }
        WorldClient world = this.mc.theWorld;
        IBlockState iblockstate = world.getBlockState(pos);
        Block block1 = iblockstate.getBlock();
        if (block1.getMaterial() == Material.air) {
            return false;
        }
        world.playAuxSFX(2001, pos, Block.getStateId((IBlockState)iblockstate));
        boolean flag = world.setBlockToAir(pos);
        if (flag) {
            block1.onBlockDestroyedByPlayer((World)world, pos, iblockstate);
        }
        this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
        if (!this.currentGameType.isCreative() && (itemstack1 = this.mc.thePlayer.getCurrentEquippedItem()) != null) {
            itemstack1.onBlockDestroyed((World)world, block1, pos, (EntityPlayer)this.mc.thePlayer);
            if (itemstack1.stackSize == 0) {
                this.mc.thePlayer.destroyCurrentEquippedItem();
            }
        }
        return flag;
    }

    public boolean clickBlock(BlockPos loc, EnumFacing face) {
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!this.mc.thePlayer.isAllowEdit()) {
                Block block = this.mc.theWorld.getBlockState(loc).getBlock();
                ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
                if (itemstack == null) {
                    return false;
                }
                if (!itemstack.canDestroy(block)) {
                    return false;
                }
            }
        }
        if (!this.mc.theWorld.getWorldBorder().contains(loc)) {
            return false;
        }
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
            PlayerControllerMP.clickBlockCreative(this.mc, this, loc, face);
            this.blockHitDelay = 5;
        } else if (!this.isHittingBlock || !this.isHittingPosition(loc)) {
            boolean flag;
            if (this.isHittingBlock) {
                this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face));
            }
            this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
            Block block1 = this.mc.theWorld.getBlockState(loc).getBlock();
            boolean bl = flag = block1.getMaterial() != Material.air;
            if (flag) {
                if (curBlockDamageMP == 0.0f) {
                    block1.onBlockClicked((World)this.mc.theWorld, loc, (EntityPlayer)this.mc.thePlayer);
                }
            }
            if (flag && block1.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.thePlayer, this.mc.thePlayer.worldObj, loc) >= 1.0f) {
                this.onPlayerDestroyBlock(loc, face);
            } else {
                this.isHittingBlock = true;
                this.currentBlock = loc;
                this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
                curBlockDamageMP = 0.0f;
                this.stepSoundTickCounter = 0.0f;
                this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(curBlockDamageMP * 10.0f) - 1);
            }
        }
        return true;
    }

    public void resetBlockRemoving() {
        if (this.isHittingBlock) {
            this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
            this.isHittingBlock = false;
            curBlockDamageMP = 0.0f;
            this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, -1);
        }
    }

    public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing) {
        this.syncCurrentPlayItem();
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return true;
        }
        if (this.currentGameType.isCreative() && this.mc.theWorld.getWorldBorder().contains(posBlock)) {
            this.blockHitDelay = 5;
            this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
            PlayerControllerMP.clickBlockCreative(this.mc, this, posBlock, directionFacing);
            return true;
        }
        if (this.isHittingPosition(posBlock)) {
            Block block = this.mc.theWorld.getBlockState(posBlock).getBlock();
            if (block.getMaterial() == Material.air) {
                this.isHittingBlock = false;
                return false;
            }
            curBlockDamageMP += block.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.thePlayer, this.mc.thePlayer.worldObj, posBlock);
            if (this.stepSoundTickCounter % 4.0f == 0.0f) {
                this.mc.getSoundHandler().playSound((ISound)new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepSound()), (block.stepSound.getVolume() + 1.0f) / 8.0f, block.stepSound.getFrequency() * 0.5f, (float)posBlock.getX() + 0.5f, (float)posBlock.getY() + 0.5f, (float)posBlock.getZ() + 0.5f));
            }
            this.stepSoundTickCounter += 1.0f;
            if (curBlockDamageMP >= 1.0f) {
                this.isHittingBlock = false;
                this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
                this.onPlayerDestroyBlock(posBlock, directionFacing);
                curBlockDamageMP = 0.0f;
                this.stepSoundTickCounter = 0.0f;
                this.blockHitDelay = 5;
            }
            this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(curBlockDamageMP * 10.0f) - 1);
            return true;
        }
        return this.clickBlock(posBlock, directionFacing);
    }

    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0f : 4.5f;
    }

    public void updateController() {
        this.syncCurrentPlayItem();
        if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        } else {
            this.netClientHandler.getNetworkManager().checkDisconnected();
        }
    }

    private boolean isHittingPosition(BlockPos pos) {
        boolean flag;
        ItemStack itemstack = this.mc.thePlayer.getHeldItem();
        boolean bl = flag = this.currentItemHittingBlock == null && itemstack == null;
        if (this.currentItemHittingBlock != null && itemstack != null) {
            flag = itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual((ItemStack)itemstack, (ItemStack)this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata());
        }
        return pos.equals((Object)this.currentBlock) && flag;
    }

    public void syncCurrentPlayItem() {
        int i = this.mc.thePlayer.inventory.currentItem;
        EventSycItem eventSyncItem = new EventSycItem(i);
        Aqua.INSTANCE.onEvent((Event)eventSyncItem);
        i = eventSyncItem.slot;
        if (i != this.currentPlayerItem) {
            this.currentPlayerItem = i;
            this.netClientHandler.addToSendQueue((Packet)new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }

    public boolean onPlayerRightClick(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec) {
        this.syncCurrentPlayItem();
        float f = (float)(hitVec.xCoord - (double)hitPos.getX());
        float f1 = (float)(hitVec.yCoord - (double)hitPos.getY());
        float f2 = (float)(hitVec.zCoord - (double)hitPos.getZ());
        boolean flag = false;
        if (!this.mc.theWorld.getWorldBorder().contains(hitPos)) {
            return false;
        }
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            ItemBlock itemblock;
            IBlockState iblockstate = worldIn.getBlockState(hitPos);
            if ((!player.isSneaking() || player.getHeldItem() == null) && iblockstate.getBlock().onBlockActivated((World)worldIn, hitPos, iblockstate, (EntityPlayer)player, side, f, f1, f2)) {
                flag = true;
            }
            if (!flag && heldStack != null && heldStack.getItem() instanceof ItemBlock && !(itemblock = (ItemBlock)heldStack.getItem()).canPlaceBlockOnSide((World)worldIn, hitPos, side, (EntityPlayer)player, heldStack)) {
                return false;
            }
        }
        this.netClientHandler.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f1, f2));
        if (!flag && this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            if (heldStack == null) {
                return false;
            }
            if (this.currentGameType.isCreative()) {
                int i = heldStack.getMetadata();
                int j = heldStack.stackSize;
                boolean flag1 = heldStack.onItemUse((EntityPlayer)player, (World)worldIn, hitPos, side, f, f1, f2);
                heldStack.setItemDamage(i);
                heldStack.stackSize = j;
                return flag1;
            }
            return heldStack.onItemUse((EntityPlayer)player, (World)worldIn, hitPos, side, f, f1, f2);
        }
        return true;
    }

    public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
        if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
        int i = itemStackIn.stackSize;
        ItemStack itemstack = itemStackIn.useItemRightClick(worldIn, playerIn);
        if (itemstack != itemStackIn || itemstack != null && itemstack.stackSize != i) {
            playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;
            if (itemstack.stackSize == 0) {
                playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
            }
            return true;
        }
        return false;
    }

    public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter statWriter) {
        return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, statWriter);
    }

    public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }

    public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity);
    }

    public boolean isPlayerRightClickingOnEntity(EntityPlayer player, Entity entityIn, MovingObjectPosition movingObject) {
        this.syncCurrentPlayItem();
        Vec3 vec3 = new Vec3(movingObject.hitVec.xCoord - entityIn.posX, movingObject.hitVec.yCoord - entityIn.posY, movingObject.hitVec.zCoord - entityIn.posZ);
        this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(entityIn, vec3));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && entityIn.interactAt(player, vec3);
    }

    public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn) {
        short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
        ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, mode, playerIn);
        this.netClientHandler.addToSendQueue((Packet)new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, short1));
        return itemstack;
    }

    public void sendEnchantPacket(int windowID, int button) {
        this.netClientHandler.addToSendQueue((Packet)new C11PacketEnchantItem(windowID, button));
    }

    public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue((Packet)new C10PacketCreativeInventoryAction(slotId, itemStackIn));
        }
    }

    public void sendPacketDropItem(ItemStack itemStackIn) {
        if (this.currentGameType.isCreative() && itemStackIn != null) {
            this.netClientHandler.addToSendQueue((Packet)new C10PacketCreativeInventoryAction(-1, itemStackIn));
        }
    }

    public void onStoppedUsingItem(EntityPlayer playerIn) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        playerIn.stopUsingItem();
    }

    public boolean gameIsSurvivalOrAdventure() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean isNotCreative() {
        return !this.currentGameType.isCreative();
    }

    public boolean isInCreativeMode() {
        return this.currentGameType.isCreative();
    }

    public boolean extendedReach() {
        return this.currentGameType.isCreative();
    }

    public boolean isRidingHorse() {
        return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
    }

    public boolean isSpectatorMode() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public WorldSettings.GameType getCurrentGameType() {
        return this.currentGameType;
    }

    public boolean getIsHittingBlock() {
        return this.isHittingBlock;
    }
}
