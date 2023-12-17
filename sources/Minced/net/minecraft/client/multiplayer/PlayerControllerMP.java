// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ActionResult;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventInteractBlock;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.PLAYER.AutoTool;
import ru.tuskevich.Minced;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.item.ItemSword;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.GameType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.Minecraft;

public class PlayerControllerMP
{
    private final Minecraft mc;
    private final NetHandlerPlayClient connection;
    private BlockPos currentBlock;
    private ItemStack currentItemHittingBlock;
    private float curBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private GameType currentGameType;
    private int currentPlayerItem;
    
    public PlayerControllerMP(final Minecraft mcIn, final NetHandlerPlayClient netHandler) {
        this.currentBlock = new BlockPos(-1, -1, -1);
        this.currentItemHittingBlock = ItemStack.EMPTY;
        this.currentGameType = GameType.SURVIVAL;
        this.mc = mcIn;
        this.connection = netHandler;
    }
    
    public static void clickBlockCreative(final Minecraft mcIn, final PlayerControllerMP playerController, final BlockPos pos, final EnumFacing facing) {
        if (!mcIn.world.extinguishFire(Minecraft.player, pos, facing)) {
            playerController.onPlayerDestroyBlock(pos);
        }
    }
    
    public void setPlayerCapabilities(final EntityPlayer player) {
        this.currentGameType.configurePlayerCapabilities(player.capabilities);
    }
    
    public boolean isSpectator() {
        return this.currentGameType == GameType.SPECTATOR;
    }
    
    public void setGameType(final GameType type) {
        this.currentGameType = type;
        final GameType currentGameType = this.currentGameType;
        final Minecraft mc = this.mc;
        currentGameType.configurePlayerCapabilities(Minecraft.player.capabilities);
    }
    
    public void flipPlayer(final EntityPlayer playerIn) {
        playerIn.rotationYaw = -180.0f;
    }
    
    public boolean shouldDrawHUD() {
        return this.currentGameType.isSurvivalOrAdventure();
    }
    
    public boolean onPlayerDestroyBlock(final BlockPos pos) {
        if (this.currentGameType.hasLimitedInteractions()) {
            if (this.currentGameType == GameType.SPECTATOR) {
                return false;
            }
            final Minecraft mc = this.mc;
            if (!Minecraft.player.isAllowEdit()) {
                final Minecraft mc2 = this.mc;
                final ItemStack itemstack = Minecraft.player.getHeldItemMainhand();
                if (itemstack.isEmpty()) {
                    return false;
                }
                if (!itemstack.canDestroy(this.mc.world.getBlockState(pos).getBlock())) {
                    return false;
                }
            }
        }
        if (this.currentGameType.isCreative()) {
            final Minecraft mc3 = this.mc;
            if (!Minecraft.player.getHeldItemMainhand().isEmpty()) {
                final Minecraft mc4 = this.mc;
                if (Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        final World world = this.mc.world;
        final IBlockState iblockstate = world.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (block instanceof BlockCommandBlock || block instanceof BlockStructure) {
            final Minecraft mc5 = this.mc;
            if (!Minecraft.player.canUseCommandBlock()) {
                return false;
            }
        }
        if (iblockstate.getMaterial() == Material.AIR) {
            return false;
        }
        world.playEvent(2001, pos, Block.getStateId(iblockstate));
        final Block block2 = block;
        final World worldIn = world;
        final IBlockState state = iblockstate;
        final Minecraft mc6 = this.mc;
        block2.onBlockHarvested(worldIn, pos, state, Minecraft.player);
        final boolean flag = world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
        if (flag) {
            block.onPlayerDestroy(world, pos, iblockstate);
        }
        this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
        if (!this.currentGameType.isCreative()) {
            final Minecraft mc7 = this.mc;
            final ItemStack itemstack2 = Minecraft.player.getHeldItemMainhand();
            if (!itemstack2.isEmpty()) {
                final ItemStack itemStack = itemstack2;
                final World worldIn2 = world;
                final IBlockState blockIn = iblockstate;
                final Minecraft mc8 = this.mc;
                itemStack.onBlockDestroyed(worldIn2, blockIn, pos, Minecraft.player);
                if (itemstack2.isEmpty()) {
                    final Minecraft mc9 = this.mc;
                    Minecraft.player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                }
            }
        }
        return flag;
    }
    
    public boolean clickBlock(final BlockPos loc, final EnumFacing face) {
        if (this.currentGameType.hasLimitedInteractions()) {
            if (this.currentGameType == GameType.SPECTATOR) {
                return false;
            }
            final Minecraft mc = this.mc;
            if (!Minecraft.player.isAllowEdit()) {
                final Minecraft mc2 = this.mc;
                final ItemStack itemstack = Minecraft.player.getHeldItemMainhand();
                if (itemstack.isEmpty()) {
                    return false;
                }
                if (!itemstack.canDestroy(this.mc.world.getBlockState(loc).getBlock())) {
                    return false;
                }
            }
        }
        if (!this.mc.world.getWorldBorder().contains(loc)) {
            return false;
        }
        if (this.currentGameType.isCreative()) {
            this.mc.getTutorial().onHitBlock(this.mc.world, loc, this.mc.world.getBlockState(loc), 1.0f);
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
            clickBlockCreative(this.mc, this, loc, face);
            this.blockHitDelay = 5;
        }
        else if (!this.isHittingBlock || !this.isHittingPosition(loc)) {
            if (this.isHittingBlock) {
                this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face));
            }
            final IBlockState iblockstate = this.mc.world.getBlockState(loc);
            this.mc.getTutorial().onHitBlock(this.mc.world, loc, iblockstate, 0.0f);
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
            final boolean flag = iblockstate.getMaterial() != Material.AIR;
            if (flag && this.curBlockDamageMP == 0.0f) {
                final Block block = iblockstate.getBlock();
                final WorldClient world = this.mc.world;
                final Minecraft mc3 = this.mc;
                block.onBlockClicked(world, loc, Minecraft.player);
            }
            if (flag) {
                final IBlockState blockState = iblockstate;
                final Minecraft mc4 = this.mc;
                final EntityPlayerSP player = Minecraft.player;
                final Minecraft mc5 = this.mc;
                if (blockState.getPlayerRelativeBlockHardness(player, Minecraft.player.world, loc) >= 1.0f) {
                    this.onPlayerDestroyBlock(loc);
                    return true;
                }
            }
            this.isHittingBlock = true;
            this.currentBlock = loc;
            final Minecraft mc6 = this.mc;
            this.currentItemHittingBlock = Minecraft.player.getHeldItemMainhand();
            this.curBlockDamageMP = 0.0f;
            this.stepSoundTickCounter = 0.0f;
            final WorldClient world2 = this.mc.world;
            final Minecraft mc7 = this.mc;
            world2.sendBlockBreakProgress(Minecraft.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0f) - 1);
        }
        return true;
    }
    
    public void resetBlockRemoving() {
        if (this.isHittingBlock) {
            this.mc.getTutorial().onHitBlock(this.mc.world, this.currentBlock, this.mc.world.getBlockState(this.currentBlock), -1.0f);
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
            this.isHittingBlock = false;
            this.curBlockDamageMP = 0.0f;
            final WorldClient world = this.mc.world;
            final Minecraft mc = this.mc;
            world.sendBlockBreakProgress(Minecraft.player.getEntityId(), this.currentBlock, -1);
            final Minecraft mc2 = this.mc;
            Minecraft.player.resetCooldown();
        }
    }
    
    public boolean onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing) {
        this.syncCurrentPlayItem();
        if (Minced.getInstance().manager.getModule(AutoTool.class).state) {
            final EventInteractBlock event = new EventInteractBlock(posBlock, directionFacing);
            EventManager.call(event);
            if (event.isCanceled()) {
                return false;
            }
        }
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return true;
        }
        if (this.currentGameType.isCreative() && this.mc.world.getWorldBorder().contains(posBlock)) {
            this.blockHitDelay = 5;
            this.mc.getTutorial().onHitBlock(this.mc.world, posBlock, this.mc.world.getBlockState(posBlock), 1.0f);
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
            clickBlockCreative(this.mc, this, posBlock, directionFacing);
            return true;
        }
        if (!this.isHittingPosition(posBlock)) {
            return this.clickBlock(posBlock, directionFacing);
        }
        final IBlockState iblockstate = this.mc.world.getBlockState(posBlock);
        final Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() == Material.AIR) {
            return this.isHittingBlock = false;
        }
        final float curBlockDamageMP = this.curBlockDamageMP;
        final IBlockState blockState = iblockstate;
        final Minecraft mc = this.mc;
        final EntityPlayerSP player = Minecraft.player;
        final Minecraft mc2 = this.mc;
        this.curBlockDamageMP = curBlockDamageMP + blockState.getPlayerRelativeBlockHardness(player, Minecraft.player.world, posBlock);
        if (this.stepSoundTickCounter % 4.0f == 0.0f) {
            final SoundType soundtype = block.getSoundType();
            this.mc.getSoundHandler().playSound(new PositionedSoundRecord(soundtype.getHitSound(), SoundCategory.NEUTRAL, (soundtype.getVolume() + 1.0f) / 8.0f, soundtype.getPitch() * 0.5f, posBlock));
        }
        ++this.stepSoundTickCounter;
        this.mc.getTutorial().onHitBlock(this.mc.world, posBlock, iblockstate, MathHelper.clamp(this.curBlockDamageMP, 0.0f, 1.0f));
        if (this.curBlockDamageMP >= 1.0f) {
            this.isHittingBlock = false;
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
            this.onPlayerDestroyBlock(posBlock);
            this.curBlockDamageMP = 0.0f;
            this.stepSoundTickCounter = 0.0f;
            this.blockHitDelay = 5;
        }
        final WorldClient world = this.mc.world;
        final Minecraft mc3 = this.mc;
        world.sendBlockBreakProgress(Minecraft.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0f) - 1);
        return true;
    }
    
    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0f : 4.5f;
    }
    
    public void updateController() {
        this.syncCurrentPlayItem();
        if (this.connection.getNetworkManager().isChannelOpen()) {
            this.connection.getNetworkManager().processReceivedPackets();
        }
        else {
            this.connection.getNetworkManager().handleDisconnection();
        }
    }
    
    private boolean isHittingPosition(final BlockPos pos) {
        final Minecraft mc = this.mc;
        final ItemStack itemstack = Minecraft.player.getHeldItemMainhand();
        boolean flag = this.currentItemHittingBlock.isEmpty() && itemstack.isEmpty();
        if (!this.currentItemHittingBlock.isEmpty() && !itemstack.isEmpty()) {
            flag = (itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata()));
        }
        return pos.equals(this.currentBlock) && flag;
    }
    
    private void syncCurrentPlayItem() {
        final Minecraft mc = this.mc;
        final int i = Minecraft.player.inventory.currentItem;
        if (i != this.currentPlayerItem) {
            this.currentPlayerItem = i;
            this.connection.sendPacket(new CPacketHeldItemChange(this.currentPlayerItem));
        }
    }
    
    public EnumActionResult processRightClickBlock(final EntityPlayerSP player, final WorldClient worldIn, final BlockPos pos, final EnumFacing direction, final Vec3d vec, final EnumHand hand) {
        this.syncCurrentPlayItem();
        final ItemStack itemstack = player.getHeldItem(hand);
        final float f = (float)(vec.x - pos.getX());
        final float f2 = (float)(vec.y - pos.getY());
        final float f3 = (float)(vec.z - pos.getZ());
        boolean flag = false;
        if (!this.mc.world.getWorldBorder().contains(pos)) {
            return EnumActionResult.FAIL;
        }
        if (this.currentGameType != GameType.SPECTATOR) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if ((!player.isSneaking() || (player.getHeldItemMainhand().isEmpty() && player.getHeldItemOffhand().isEmpty())) && iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, hand, direction, f, f2, f3)) {
                flag = true;
            }
            if (!flag && itemstack.getItem() instanceof ItemBlock) {
                final ItemBlock itemblock = (ItemBlock)itemstack.getItem();
                if (!itemblock.canPlaceBlockOnSide(worldIn, pos, direction, player, itemstack)) {
                    return EnumActionResult.FAIL;
                }
            }
        }
        this.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        if (flag || this.currentGameType == GameType.SPECTATOR) {
            return EnumActionResult.SUCCESS;
        }
        if (itemstack.isEmpty()) {
            return EnumActionResult.PASS;
        }
        if (player.getCooldownTracker().hasCooldown(itemstack.getItem())) {
            return EnumActionResult.PASS;
        }
        if (itemstack.getItem() instanceof ItemBlock && !player.canUseCommandBlock()) {
            final Block block = ((ItemBlock)itemstack.getItem()).getBlock();
            if (block instanceof BlockCommandBlock || block instanceof BlockStructure) {
                return EnumActionResult.FAIL;
            }
        }
        if (this.currentGameType.isCreative()) {
            final int i = itemstack.getMetadata();
            final int j = itemstack.getCount();
            final EnumActionResult enumactionresult = itemstack.onItemUse(player, worldIn, pos, hand, direction, f, f2, f3);
            itemstack.setItemDamage(i);
            itemstack.setCount(j);
            return enumactionresult;
        }
        return itemstack.onItemUse(player, worldIn, pos, hand, direction, f, f2, f3);
    }
    
    public EnumActionResult processRightClick(final EntityPlayer player, final World worldIn, final EnumHand hand) {
        if (this.currentGameType == GameType.SPECTATOR) {
            return EnumActionResult.PASS;
        }
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketPlayerTryUseItem(hand));
        final ItemStack itemstack = player.getHeldItem(hand);
        if (player.getCooldownTracker().hasCooldown(itemstack.getItem())) {
            return EnumActionResult.PASS;
        }
        final int i = itemstack.getCount();
        final ActionResult<ItemStack> actionresult = itemstack.useItemRightClick(worldIn, player, hand);
        final ItemStack itemstack2 = actionresult.getResult();
        if (itemstack2 != itemstack || itemstack2.getCount() != i) {
            player.setHeldItem(hand, itemstack2);
        }
        return actionresult.getType();
    }
    
    public EntityPlayerSP createPlayer(final World p_192830_1_, final StatisticsManager p_192830_2_, final RecipeBook p_192830_3_) {
        return new EntityPlayerSP(this.mc, p_192830_1_, this.connection, p_192830_2_, p_192830_3_);
    }
    
    public void attackEntity(final EntityPlayer playerIn, final Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketUseEntity(targetEntity));
        if (this.currentGameType != GameType.SPECTATOR) {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
            playerIn.resetCooldown();
        }
    }
    
    public EnumActionResult interactWithEntity(final EntityPlayer player, final Entity target, final EnumHand hand) {
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketUseEntity(target, hand));
        return (this.currentGameType == GameType.SPECTATOR) ? EnumActionResult.PASS : player.interactOn(target, hand);
    }
    
    public EnumActionResult interactWithEntity(final EntityPlayer player, final Entity target, final RayTraceResult ray, final EnumHand hand) {
        this.syncCurrentPlayItem();
        final Vec3d vec3d = new Vec3d(ray.hitVec.x - target.posX, ray.hitVec.y - target.posY, ray.hitVec.z - target.posZ);
        this.connection.sendPacket(new CPacketUseEntity(target, hand, vec3d));
        return (this.currentGameType == GameType.SPECTATOR) ? EnumActionResult.PASS : target.applyPlayerInteraction(player, vec3d, hand);
    }
    
    public ItemStack windowClick(final int windowId, final int slotId, final int mouseButton, final ClickType type, final EntityPlayer player) {
        final short short1 = player.openContainer.getNextTransactionID(player.inventory);
        final ItemStack itemstack = player.openContainer.slotClick(slotId, mouseButton, type, player);
        this.connection.sendPacket(new CPacketClickWindow(windowId, slotId, mouseButton, type, itemstack, short1));
        return itemstack;
    }
    
    public void func_194338_a(final int p_194338_1_, final IRecipe p_194338_2_, final boolean p_194338_3_, final EntityPlayer p_194338_4_) {
        this.connection.sendPacket(new CPacketPlaceRecipe(p_194338_1_, p_194338_2_, p_194338_3_));
    }
    
    public void sendEnchantPacket(final int windowID, final int button) {
        this.connection.sendPacket(new CPacketEnchantItem(windowID, button));
    }
    
    public void sendSlotPacket(final ItemStack itemStackIn, final int slotId) {
        if (this.currentGameType.isCreative()) {
            this.connection.sendPacket(new CPacketCreativeInventoryAction(slotId, itemStackIn));
        }
    }
    
    public void sendPacketDropItem(final ItemStack itemStackIn) {
        if (this.currentGameType.isCreative() && !itemStackIn.isEmpty()) {
            this.connection.sendPacket(new CPacketCreativeInventoryAction(-1, itemStackIn));
        }
    }
    
    public void onStoppedUsingItem(final EntityPlayer playerIn) {
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        playerIn.stopActiveHand();
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
        final Minecraft mc = this.mc;
        if (Minecraft.player.isRiding()) {
            final Minecraft mc2 = this.mc;
            if (Minecraft.player.getRidingEntity() instanceof AbstractHorse) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSpectatorMode() {
        return this.currentGameType == GameType.SPECTATOR;
    }
    
    public GameType getCurrentGameType() {
        return this.currentGameType;
    }
    
    public boolean getIsHittingBlock() {
        return this.isHittingBlock;
    }
    
    public void pickItem(final int index) {
        this.connection.sendPacket(new CPacketCustomPayload("MC|PickItem", new PacketBuffer(Unpooled.buffer()).writeVarInt(index)));
    }
    
    public void attackEntityNoEvent(final EntityPlayer playerIn, final Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketUseEntity(targetEntity));
        if (this.currentGameType != GameType.SPECTATOR) {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
            playerIn.resetCooldown();
        }
    }
}
