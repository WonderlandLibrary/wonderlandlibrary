package net.minecraft.client.multiplayer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
  
  private float curBlockDamageMP;
  
  private float stepSoundTickCounter;
  
  private int blockHitDelay;
  
  private boolean isHittingBlock;
  
  private WorldSettings.GameType currentGameType = WorldSettings.GameType.SURVIVAL;
  
  private int currentPlayerItem;
  
  public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_) {
    this.mc = mcIn;
    this.netClientHandler = p_i45062_2_;
  }
  
  public static void clickBlockCreative(Minecraft mcIn, PlayerControllerMP p_178891_1_, BlockPos p_178891_2_, EnumFacing p_178891_3_) {
    if (!mcIn.theWorld.extinguishFire((EntityPlayer)mcIn.thePlayer, p_178891_2_, p_178891_3_))
      p_178891_1_.onPlayerDestroyBlock(p_178891_2_, p_178891_3_); 
  }
  
  public void setPlayerCapabilities(EntityPlayer p_78748_1_) {
    this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
  }
  
  public boolean isSpectator() {
    return (this.currentGameType == WorldSettings.GameType.SPECTATOR);
  }
  
  public void setGameType(WorldSettings.GameType p_78746_1_) {
    this.currentGameType = p_78746_1_;
    this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
  }
  
  public void flipPlayer(EntityPlayer playerIn) {
    playerIn.rotationYaw = -180.0F;
  }
  
  public boolean shouldDrawHUD() {
    return this.currentGameType.isSurvivalOrAdventure();
  }
  
  public boolean onPlayerDestroyBlock(BlockPos pos, EnumFacing side) {
    if (this.currentGameType.isAdventure()) {
      if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
        return false; 
      if (!this.mc.thePlayer.isAllowEdit()) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
        if (itemstack == null)
          return false; 
        if (!itemstack.canDestroy(block))
          return false; 
      } 
    } 
    if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemSword)
      return false; 
    World world = this.mc.theWorld;
    IBlockState iblockstate = world.getBlockState(pos);
    Block block1 = iblockstate.getBlock();
    if (block1.getMaterial() == Material.air)
      return false; 
    world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
    boolean flag = world.setBlockToAir(pos);
    if (flag)
      block1.onBlockDestroyedByPlayer(world, pos, iblockstate); 
    this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
    if (!this.currentGameType.isCreative()) {
      ItemStack itemstack1 = this.mc.thePlayer.getCurrentEquippedItem();
      if (itemstack1 != null) {
        itemstack1.onBlockDestroyed(world, block1, pos, (EntityPlayer)this.mc.thePlayer);
        if (itemstack1.stackSize == 0)
          this.mc.thePlayer.destroyCurrentEquippedItem(); 
      } 
    } 
    return flag;
  }
  
  public boolean clickBlock(BlockPos loc, EnumFacing face) {
    if (this.currentGameType.isAdventure()) {
      if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
        return false; 
      if (!this.mc.thePlayer.isAllowEdit()) {
        Block block = this.mc.theWorld.getBlockState(loc).getBlock();
        ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
        if (itemstack == null)
          return false; 
        if (!itemstack.canDestroy(block))
          return false; 
      } 
    } 
    if (!this.mc.theWorld.getWorldBorder().contains(loc))
      return false; 
    if (this.currentGameType.isCreative()) {
      this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
      clickBlockCreative(this.mc, this, loc, face);
      this.blockHitDelay = 5;
    } else if (!this.isHittingBlock || !isHittingPosition(loc)) {
      if (this.isHittingBlock)
        this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face)); 
      this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
      Block block1 = this.mc.theWorld.getBlockState(loc).getBlock();
      boolean flag = (block1.getMaterial() != Material.air);
      if (flag && this.curBlockDamageMP == 0.0F)
        block1.onBlockClicked(this.mc.theWorld, loc, (EntityPlayer)this.mc.thePlayer); 
      if (flag && block1.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.thePlayer, this.mc.thePlayer.worldObj, loc) >= 1.0F) {
        onPlayerDestroyBlock(loc, face);
      } else {
        this.isHittingBlock = true;
        this.currentBlock = loc;
        this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
        this.curBlockDamageMP = 0.0F;
        this.stepSoundTickCounter = 0.0F;
        this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
      } 
    } 
    return true;
  }
  
  public void resetBlockRemoving() {
    if (this.isHittingBlock) {
      this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
      this.isHittingBlock = false;
      this.curBlockDamageMP = 0.0F;
      this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, -1);
    } 
  }
  
  public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing) {
    syncCurrentPlayItem();
    if (this.blockHitDelay > 0) {
      this.blockHitDelay--;
      return true;
    } 
    if (this.currentGameType.isCreative() && this.mc.theWorld.getWorldBorder().contains(posBlock)) {
      this.blockHitDelay = 5;
      this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
      clickBlockCreative(this.mc, this, posBlock, directionFacing);
      return true;
    } 
    if (isHittingPosition(posBlock)) {
      Block block = this.mc.theWorld.getBlockState(posBlock).getBlock();
      if (block.getMaterial() == Material.air) {
        this.isHittingBlock = false;
        return false;
      } 
      this.curBlockDamageMP += block.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.thePlayer, this.mc.thePlayer.worldObj, posBlock);
      if (this.stepSoundTickCounter % 4.0F == 0.0F)
        this.mc.getSoundHandler().playSound((ISound)new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepSound()), (block.stepSound.getVolume() + 1.0F) / 8.0F, block.stepSound.getFrequency() * 0.5F, posBlock.getX() + 0.5F, posBlock.getY() + 0.5F, posBlock.getZ() + 0.5F)); 
      this.stepSoundTickCounter++;
      if (this.curBlockDamageMP >= 1.0F) {
        this.isHittingBlock = false;
        this.netClientHandler.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
        onPlayerDestroyBlock(posBlock, directionFacing);
        this.curBlockDamageMP = 0.0F;
        this.stepSoundTickCounter = 0.0F;
        this.blockHitDelay = 5;
      } 
      this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
      return true;
    } 
    return clickBlock(posBlock, directionFacing);
  }
  
  public float getBlockReachDistance() {
    return this.currentGameType.isCreative() ? 5.0F : 4.5F;
  }
  
  public void updateController() {
    syncCurrentPlayItem();
    if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
      this.netClientHandler.getNetworkManager().processReceivedPackets();
    } else {
      this.netClientHandler.getNetworkManager().checkDisconnected();
    } 
  }
  
  private boolean isHittingPosition(BlockPos pos) {
    ItemStack itemstack = this.mc.thePlayer.getHeldItem();
    boolean flag = (this.currentItemHittingBlock == null && itemstack == null);
    if (this.currentItemHittingBlock != null && itemstack != null)
      flag = (itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata())); 
    return (pos.equals(this.currentBlock) && flag);
  }
  
  public void syncCurrentPlayItem() {
    int i = this.mc.thePlayer.inventory.currentItem;
    if (i != this.currentPlayerItem) {
      this.currentPlayerItem = i;
      this.netClientHandler.addToSendQueue((Packet)new C09PacketHeldItemChange(this.currentPlayerItem));
    } 
  }
  
  public boolean onPlayerRightClick(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec) {
    syncCurrentPlayItem();
    float f = (float)(hitVec.xCoord - hitPos.getX());
    float f1 = (float)(hitVec.yCoord - hitPos.getY());
    float f2 = (float)(hitVec.zCoord - hitPos.getZ());
    boolean flag = false;
    if (!this.mc.theWorld.getWorldBorder().contains(hitPos))
      return false; 
    if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
      IBlockState iblockstate = worldIn.getBlockState(hitPos);
      if ((!player.isSneaking() || player.getHeldItem() == null) && iblockstate.getBlock().onBlockActivated(worldIn, hitPos, iblockstate, (EntityPlayer)player, side, f, f1, f2))
        flag = true; 
      if (!flag && heldStack != null && heldStack.getItem() instanceof ItemBlock) {
        ItemBlock itemblock = (ItemBlock)heldStack.getItem();
        if (!itemblock.canPlaceBlockOnSide(worldIn, hitPos, side, (EntityPlayer)player, heldStack))
          return false; 
      } 
    } 
    this.netClientHandler.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f1, f2));
    if (!flag && this.currentGameType != WorldSettings.GameType.SPECTATOR) {
      if (heldStack == null)
        return false; 
      if (this.currentGameType.isCreative()) {
        int i = heldStack.getMetadata();
        int j = heldStack.stackSize;
        boolean flag1 = heldStack.onItemUse((EntityPlayer)player, worldIn, hitPos, side, f, f1, f2);
        heldStack.setItemDamage(i);
        heldStack.stackSize = j;
        return flag1;
      } 
      return heldStack.onItemUse((EntityPlayer)player, worldIn, hitPos, side, f, f1, f2);
    } 
    return true;
  }
  
  public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
    if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
      return false; 
    syncCurrentPlayItem();
    this.netClientHandler.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
    int i = itemStackIn.stackSize;
    ItemStack itemstack = itemStackIn.useItemRightClick(worldIn, playerIn);
    if (itemstack != itemStackIn || (itemstack != null && itemstack.stackSize != i)) {
      playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;
      if (itemstack.stackSize == 0)
        playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null; 
      return true;
    } 
    return false;
  }
  
  public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter p_178892_2_) {
    return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, p_178892_2_);
  }
  
  public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
    syncCurrentPlayItem();
    this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
    if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
      playerIn.attackTargetEntityWithCurrentItem(targetEntity); 
  }
  
  public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity) {
    syncCurrentPlayItem();
    this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
    return (this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity));
  }
  
  public boolean func_178894_a(EntityPlayer p_178894_1_, Entity p_178894_2_, MovingObjectPosition p_178894_3_) {
    syncCurrentPlayItem();
    Vec3 vec3 = new Vec3(p_178894_3_.hitVec.xCoord - p_178894_2_.posX, p_178894_3_.hitVec.yCoord - p_178894_2_.posY, p_178894_3_.hitVec.zCoord - p_178894_2_.posZ);
    this.netClientHandler.addToSendQueue((Packet)new C02PacketUseEntity(p_178894_2_, vec3));
    return (this.currentGameType != WorldSettings.GameType.SPECTATOR && p_178894_2_.interactAt(p_178894_1_, vec3));
  }
  
  public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn) {
    short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
    ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, mode, playerIn);
    this.netClientHandler.addToSendQueue((Packet)new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, short1));
    return itemstack;
  }
  
  public void sendEnchantPacket(int p_78756_1_, int p_78756_2_) {
    this.netClientHandler.addToSendQueue((Packet)new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
  }
  
  public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
    if (this.currentGameType.isCreative())
      this.netClientHandler.addToSendQueue((Packet)new C10PacketCreativeInventoryAction(slotId, itemStackIn)); 
  }
  
  public void sendPacketDropItem(ItemStack itemStackIn) {
    if (this.currentGameType.isCreative() && itemStackIn != null)
      this.netClientHandler.addToSendQueue((Packet)new C10PacketCreativeInventoryAction(-1, itemStackIn)); 
  }
  
  public void onStoppedUsingItem(EntityPlayer playerIn) {
    syncCurrentPlayItem();
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
    return (this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof net.minecraft.entity.passive.EntityHorse);
  }
  
  public boolean isSpectatorMode() {
    return (this.currentGameType == WorldSettings.GameType.SPECTATOR);
  }
  
  public WorldSettings.GameType getCurrentGameType() {
    return this.currentGameType;
  }
  
  public boolean func_181040_m() {
    return this.isHittingBlock;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\multiplayer\PlayerControllerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */