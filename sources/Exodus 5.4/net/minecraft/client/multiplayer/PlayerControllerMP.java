/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.multiplayer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
    private ItemStack currentItemHittingBlock;
    private final NetHandlerPlayClient netClientHandler;
    public WorldSettings.GameType currentGameType;
    private boolean isHittingBlock;
    private BlockPos currentBlock = new BlockPos(-1, -1, -1);
    private int currentPlayerItem;
    private final Minecraft mc;
    private float stepSoundTickCounter;
    private float curBlockDamageMP;
    private int blockHitDelay;

    public void onStoppedUsingItem(EntityPlayer entityPlayer) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        entityPlayer.stopUsingItem();
    }

    public boolean onPlayerDestroyBlock(BlockPos blockPos, EnumFacing enumFacing) {
        ItemStack itemStack;
        Object object;
        Object object2;
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!Minecraft.thePlayer.isAllowEdit()) {
                object2 = Minecraft.theWorld.getBlockState(blockPos).getBlock();
                object = Minecraft.thePlayer.getCurrentEquippedItem();
                if (object == null) {
                    return false;
                }
                if (!((ItemStack)object).canDestroy((Block)object2)) {
                    return false;
                }
            }
        }
        if (this.currentGameType.isCreative() && Minecraft.thePlayer.getHeldItem() != null && Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        }
        object2 = Minecraft.theWorld;
        object = ((World)object2).getBlockState(blockPos);
        Block block = object.getBlock();
        if (block.getMaterial() == Material.air) {
            return false;
        }
        ((World)object2).playAuxSFX(2001, blockPos, Block.getStateId((IBlockState)object));
        boolean bl = ((World)object2).setBlockToAir(blockPos);
        if (bl) {
            block.onBlockDestroyedByPlayer((World)object2, blockPos, (IBlockState)object);
        }
        this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
        if (!this.currentGameType.isCreative() && (itemStack = Minecraft.thePlayer.getCurrentEquippedItem()) != null) {
            itemStack.onBlockDestroyed((World)object2, block, blockPos, Minecraft.thePlayer);
            if (itemStack.stackSize == 0) {
                Minecraft.thePlayer.destroyCurrentEquippedItem();
            }
        }
        return bl;
    }

    public boolean onPlayerRightClick(EntityPlayerSP entityPlayerSP, WorldClient worldClient, ItemStack itemStack, BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3) {
        this.syncCurrentPlayItem();
        float f = (float)(vec3.xCoord - (double)blockPos.getX());
        float f2 = (float)(vec3.yCoord - (double)blockPos.getY());
        float f3 = (float)(vec3.zCoord - (double)blockPos.getZ());
        boolean bl = false;
        if (!Minecraft.theWorld.getWorldBorder().contains(blockPos)) {
            return false;
        }
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            ItemBlock itemBlock;
            IBlockState iBlockState = worldClient.getBlockState(blockPos);
            if ((!entityPlayerSP.isSneaking() || entityPlayerSP.getHeldItem() == null) && iBlockState.getBlock().onBlockActivated(worldClient, blockPos, iBlockState, entityPlayerSP, enumFacing, f, f2, f3)) {
                bl = true;
            }
            if (!bl && itemStack != null && itemStack.getItem() instanceof ItemBlock && !(itemBlock = (ItemBlock)itemStack.getItem()).canPlaceBlockOnSide(worldClient, blockPos, enumFacing, entityPlayerSP, itemStack)) {
                return false;
            }
        }
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(blockPos, enumFacing.getIndex(), entityPlayerSP.inventory.getCurrentItem(), f, f2, f3));
        if (!bl && this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            if (itemStack == null) {
                return false;
            }
            if (this.currentGameType.isCreative()) {
                int n = itemStack.getMetadata();
                int n2 = itemStack.stackSize;
                boolean bl2 = itemStack.onItemUse(entityPlayerSP, worldClient, blockPos, enumFacing, f, f2, f3);
                itemStack.setItemDamage(n);
                itemStack.stackSize = n2;
                return bl2;
            }
            return itemStack.onItemUse(entityPlayerSP, worldClient, blockPos, enumFacing, f, f2, f3);
        }
        return true;
    }

    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0f : 4.5f;
    }

    public boolean isSpectator() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public void setGameType(WorldSettings.GameType gameType) {
        this.currentGameType = gameType;
        this.currentGameType.configurePlayerCapabilities(Minecraft.thePlayer.capabilities);
    }

    public boolean isInCreativeMode() {
        return this.currentGameType.isCreative();
    }

    public boolean isNotCreative() {
        return !this.currentGameType.isCreative();
    }

    private void syncCurrentPlayItem() {
        int n = Minecraft.thePlayer.inventory.currentItem;
        if (n != this.currentPlayerItem) {
            this.currentPlayerItem = n;
            this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }

    public boolean onPlayerDamageBlock(BlockPos blockPos, EnumFacing enumFacing) {
        this.syncCurrentPlayItem();
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return true;
        }
        if (this.currentGameType.isCreative() && Minecraft.theWorld.getWorldBorder().contains(blockPos)) {
            this.blockHitDelay = 5;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, enumFacing));
            PlayerControllerMP.clickBlockCreative(this.mc, this, blockPos, enumFacing);
            return true;
        }
        if (this.isHittingPosition(blockPos)) {
            Block block = Minecraft.theWorld.getBlockState(blockPos).getBlock();
            if (block.getMaterial() == Material.air) {
                this.isHittingBlock = false;
                return false;
            }
            this.curBlockDamageMP += block.getPlayerRelativeBlockHardness(Minecraft.thePlayer, Minecraft.thePlayer.worldObj, blockPos);
            if (this.stepSoundTickCounter % 4.0f == 0.0f) {
                this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepSound()), (block.stepSound.getVolume() + 1.0f) / 8.0f, block.stepSound.getFrequency() * 0.5f, (float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f));
            }
            this.stepSoundTickCounter += 1.0f;
            if (this.curBlockDamageMP >= 1.0f) {
                this.isHittingBlock = false;
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, enumFacing));
                this.onPlayerDestroyBlock(blockPos, enumFacing);
                this.curBlockDamageMP = 0.0f;
                this.stepSoundTickCounter = 0.0f;
                this.blockHitDelay = 5;
            }
            Minecraft.theWorld.sendBlockBreakProgress(Minecraft.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0f) - 1);
            return true;
        }
        return this.clickBlock(blockPos, enumFacing);
    }

    public boolean func_178894_a(EntityPlayer entityPlayer, Entity entity, MovingObjectPosition movingObjectPosition) {
        this.syncCurrentPlayItem();
        Vec3 vec3 = new Vec3(movingObjectPosition.hitVec.xCoord - entity.posX, movingObjectPosition.hitVec.yCoord - entity.posY, movingObjectPosition.hitVec.zCoord - entity.posZ);
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(entity, vec3));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && entity.interactAt(entityPlayer, vec3);
    }

    public PlayerControllerMP(Minecraft minecraft, NetHandlerPlayClient netHandlerPlayClient) {
        this.currentGameType = WorldSettings.GameType.SURVIVAL;
        this.mc = minecraft;
        this.netClientHandler = netHandlerPlayClient;
    }

    public boolean func_181040_m() {
        return this.isHittingBlock;
    }

    public void sendEnchantPacket(int n, int n2) {
        this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(n, n2));
    }

    public boolean isRidingHorse() {
        return Minecraft.thePlayer.isRiding() && Minecraft.thePlayer.ridingEntity instanceof EntityHorse;
    }

    public boolean extendedReach() {
        return this.currentGameType.isCreative();
    }

    public void setPlayerCapabilities(EntityPlayer entityPlayer) {
        this.currentGameType.configurePlayerCapabilities(entityPlayer.capabilities);
    }

    private boolean isHittingPosition(BlockPos blockPos) {
        boolean bl;
        ItemStack itemStack = Minecraft.thePlayer.getHeldItem();
        boolean bl2 = bl = this.currentItemHittingBlock == null && itemStack == null;
        if (this.currentItemHittingBlock != null && itemStack != null) {
            boolean bl3 = bl = itemStack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemStack, this.currentItemHittingBlock) && (itemStack.isItemStackDamageable() || itemStack.getMetadata() == this.currentItemHittingBlock.getMetadata());
        }
        return blockPos.equals(this.currentBlock) && bl;
    }

    public void flipPlayer(EntityPlayer entityPlayer) {
        entityPlayer.rotationYaw = -180.0f;
    }

    public boolean gameIsSurvivalOrAdventure() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public static void clickBlockCreative(Minecraft minecraft, PlayerControllerMP playerControllerMP, BlockPos blockPos, EnumFacing enumFacing) {
        if (!Minecraft.theWorld.extinguishFire(Minecraft.thePlayer, blockPos, enumFacing)) {
            playerControllerMP.onPlayerDestroyBlock(blockPos, enumFacing);
        }
    }

    public boolean sendUseItem(EntityPlayer entityPlayer, World world, ItemStack itemStack) {
        if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(entityPlayer.inventory.getCurrentItem()));
        int n = itemStack.stackSize;
        ItemStack itemStack2 = itemStack.useItemRightClick(world, entityPlayer);
        if (itemStack2 != itemStack || itemStack2 != null && itemStack2.stackSize != n) {
            entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = itemStack2;
            if (itemStack2.stackSize == 0) {
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = null;
            }
            return true;
        }
        return false;
    }

    public void updateController() {
        this.syncCurrentPlayItem();
        if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        } else {
            this.netClientHandler.getNetworkManager().checkDisconnected();
        }
    }

    public boolean clickBlock(BlockPos blockPos, EnumFacing enumFacing) {
        Block block;
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!Minecraft.thePlayer.isAllowEdit()) {
                block = Minecraft.theWorld.getBlockState(blockPos).getBlock();
                ItemStack itemStack = Minecraft.thePlayer.getCurrentEquippedItem();
                if (itemStack == null) {
                    return false;
                }
                if (!itemStack.canDestroy(block)) {
                    return false;
                }
            }
        }
        if (!Minecraft.theWorld.getWorldBorder().contains(blockPos)) {
            return false;
        }
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, enumFacing));
            PlayerControllerMP.clickBlockCreative(this.mc, this, blockPos, enumFacing);
            this.blockHitDelay = 5;
        } else if (!this.isHittingBlock || !this.isHittingPosition(blockPos)) {
            boolean bl;
            if (this.isHittingBlock) {
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, enumFacing));
            }
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, enumFacing));
            block = Minecraft.theWorld.getBlockState(blockPos).getBlock();
            boolean bl2 = bl = block.getMaterial() != Material.air;
            if (bl && this.curBlockDamageMP == 0.0f) {
                block.onBlockClicked(Minecraft.theWorld, blockPos, Minecraft.thePlayer);
            }
            if (bl && block.getPlayerRelativeBlockHardness(Minecraft.thePlayer, Minecraft.thePlayer.worldObj, blockPos) >= 1.0f) {
                this.onPlayerDestroyBlock(blockPos, enumFacing);
            } else {
                this.isHittingBlock = true;
                this.currentBlock = blockPos;
                this.currentItemHittingBlock = Minecraft.thePlayer.getHeldItem();
                this.curBlockDamageMP = 0.0f;
                this.stepSoundTickCounter = 0.0f;
                Minecraft.theWorld.sendBlockBreakProgress(Minecraft.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0f) - 1);
            }
        }
        return true;
    }

    public void sendPacketDropItem(ItemStack itemStack) {
        if (this.currentGameType.isCreative() && itemStack != null) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStack));
        }
    }

    public void sendSlotPacket(ItemStack itemStack, int n) {
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(n, itemStack));
        }
    }

    public ItemStack windowClick(int n, int n2, int n3, int n4, EntityPlayer entityPlayer) {
        short s = entityPlayer.openContainer.getNextTransactionID(entityPlayer.inventory);
        ItemStack itemStack = entityPlayer.openContainer.slotClick(n2, n3, n4, entityPlayer);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(n, n2, n3, n4, itemStack, s));
        return itemStack;
    }

    public void resetBlockRemoving() {
        if (this.isHittingBlock) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
            this.isHittingBlock = false;
            this.curBlockDamageMP = 0.0f;
            Minecraft.theWorld.sendBlockBreakProgress(Minecraft.thePlayer.getEntityId(), this.currentBlock, -1);
        }
    }

    public EntityPlayerSP func_178892_a(World world, StatFileWriter statFileWriter) {
        return new EntityPlayerSP(this.mc, world, this.netClientHandler, statFileWriter);
    }

    public WorldSettings.GameType getCurrentGameType() {
        return this.currentGameType;
    }

    public boolean isSpectatorMode() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public boolean shouldDrawHUD() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public void attackEntity(EntityPlayer entityPlayer, Entity entity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            entityPlayer.attackTargetEntityWithCurrentItem(entity);
        }
    }

    public boolean interactWithEntitySendPacket(EntityPlayer entityPlayer, Entity entity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.INTERACT));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && entityPlayer.interactWith(entity);
    }
}

