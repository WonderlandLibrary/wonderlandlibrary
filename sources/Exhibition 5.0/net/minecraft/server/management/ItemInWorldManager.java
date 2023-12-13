// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import net.minecraft.world.WorldServer;
import net.minecraft.inventory.IInventory;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.ILockableContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.item.ItemSword;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class ItemInWorldManager
{
    public World theWorld;
    public EntityPlayerMP thisPlayerMP;
    private WorldSettings.GameType gameType;
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos field_180240_f;
    private int curblockDamage;
    private boolean receivedFinishDiggingPacket;
    private BlockPos field_180241_i;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock;
    private static final String __OBFID = "CL_00001442";
    
    public ItemInWorldManager(final World worldIn) {
        this.gameType = WorldSettings.GameType.NOT_SET;
        this.field_180240_f = BlockPos.ORIGIN;
        this.field_180241_i = BlockPos.ORIGIN;
        this.durabilityRemainingOnBlock = -1;
        this.theWorld = worldIn;
    }
    
    public void setGameType(final WorldSettings.GameType p_73076_1_) {
        (this.gameType = p_73076_1_).configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
        this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.thisPlayerMP }));
    }
    
    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }
    
    public boolean func_180239_c() {
        return this.gameType.isSurvivalOrAdventure();
    }
    
    public boolean isCreative() {
        return this.gameType.isCreative();
    }
    
    public void initializeGameType(final WorldSettings.GameType p_73077_1_) {
        if (this.gameType == WorldSettings.GameType.NOT_SET) {
            this.gameType = p_73077_1_;
        }
        this.setGameType(this.gameType);
    }
    
    public void updateBlockRemoving() {
        ++this.curblockDamage;
        if (this.receivedFinishDiggingPacket) {
            final int var1 = this.curblockDamage - this.initialBlockDamage;
            final Block var2 = this.theWorld.getBlockState(this.field_180241_i).getBlock();
            if (var2.getMaterial() == Material.air) {
                this.receivedFinishDiggingPacket = false;
            }
            else {
                final float var3 = var2.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (var1 + 1);
                final int var4 = (int)(var3 * 10.0f);
                if (var4 != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, var4);
                    this.durabilityRemainingOnBlock = var4;
                }
                if (var3 >= 1.0f) {
                    this.receivedFinishDiggingPacket = false;
                    this.func_180237_b(this.field_180241_i);
                }
            }
        }
        else if (this.isDestroyingBlock) {
            final Block var5 = this.theWorld.getBlockState(this.field_180240_f).getBlock();
            if (var5.getMaterial() == Material.air) {
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else {
                final int var6 = this.curblockDamage - this.initialDamage;
                final float var3 = var5.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (var6 + 1);
                final int var4 = (int)(var3 * 10.0f);
                if (var4 != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, var4);
                    this.durabilityRemainingOnBlock = var4;
                }
            }
        }
    }
    
    public void func_180784_a(final BlockPos p_180784_1_, final EnumFacing p_180784_2_) {
        if (this.isCreative()) {
            if (!this.theWorld.func_175719_a(null, p_180784_1_, p_180784_2_)) {
                this.func_180237_b(p_180784_1_);
            }
        }
        else {
            final Block var3 = this.theWorld.getBlockState(p_180784_1_).getBlock();
            if (this.gameType.isAdventure()) {
                if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                    return;
                }
                if (!this.thisPlayerMP.func_175142_cm()) {
                    final ItemStack var4 = this.thisPlayerMP.getCurrentEquippedItem();
                    if (var4 == null) {
                        return;
                    }
                    if (!var4.canDestroy(var3)) {
                        return;
                    }
                }
            }
            this.theWorld.func_175719_a(null, p_180784_1_, p_180784_2_);
            this.initialDamage = this.curblockDamage;
            float var5 = 1.0f;
            if (var3.getMaterial() != Material.air) {
                var3.onBlockClicked(this.theWorld, p_180784_1_, this.thisPlayerMP);
                var5 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, p_180784_1_);
            }
            if (var3.getMaterial() != Material.air && var5 >= 1.0f) {
                this.func_180237_b(p_180784_1_);
            }
            else {
                this.isDestroyingBlock = true;
                this.field_180240_f = p_180784_1_;
                final int var6 = (int)(var5 * 10.0f);
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), p_180784_1_, var6);
                this.durabilityRemainingOnBlock = var6;
            }
        }
    }
    
    public void func_180785_a(final BlockPos p_180785_1_) {
        if (p_180785_1_.equals(this.field_180240_f)) {
            final int var2 = this.curblockDamage - this.initialDamage;
            final Block var3 = this.theWorld.getBlockState(p_180785_1_).getBlock();
            if (var3.getMaterial() != Material.air) {
                final float var4 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, p_180785_1_) * (var2 + 1);
                if (var4 >= 0.7f) {
                    this.isDestroyingBlock = false;
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), p_180785_1_, -1);
                    this.func_180237_b(p_180785_1_);
                }
                else if (!this.receivedFinishDiggingPacket) {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.field_180241_i = p_180785_1_;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }
    
    public void func_180238_e() {
        this.isDestroyingBlock = false;
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
    }
    
    private boolean func_180235_c(final BlockPos p_180235_1_) {
        final IBlockState var2 = this.theWorld.getBlockState(p_180235_1_);
        var2.getBlock().onBlockHarvested(this.theWorld, p_180235_1_, var2, this.thisPlayerMP);
        final boolean var3 = this.theWorld.setBlockToAir(p_180235_1_);
        if (var3) {
            var2.getBlock().onBlockDestroyedByPlayer(this.theWorld, p_180235_1_, var2);
        }
        return var3;
    }
    
    public boolean func_180237_b(final BlockPos p_180237_1_) {
        if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null && this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        }
        final IBlockState var2 = this.theWorld.getBlockState(p_180237_1_);
        final TileEntity var3 = this.theWorld.getTileEntity(p_180237_1_);
        if (this.gameType.isAdventure()) {
            if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!this.thisPlayerMP.func_175142_cm()) {
                final ItemStack var4 = this.thisPlayerMP.getCurrentEquippedItem();
                if (var4 == null) {
                    return false;
                }
                if (!var4.canDestroy(var2.getBlock())) {
                    return false;
                }
            }
        }
        this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, p_180237_1_, Block.getStateId(var2));
        final boolean var5 = this.func_180235_c(p_180237_1_);
        if (this.isCreative()) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, p_180237_1_));
        }
        else {
            final ItemStack var6 = this.thisPlayerMP.getCurrentEquippedItem();
            final boolean var7 = this.thisPlayerMP.canHarvestBlock(var2.getBlock());
            if (var6 != null) {
                var6.onBlockDestroyed(this.theWorld, var2.getBlock(), p_180237_1_, this.thisPlayerMP);
                if (var6.stackSize == 0) {
                    this.thisPlayerMP.destroyCurrentEquippedItem();
                }
            }
            if (var5 && var7) {
                var2.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, p_180237_1_, var2, var3);
            }
        }
        return var5;
    }
    
    public boolean tryUseItem(final EntityPlayer p_73085_1_, final World worldIn, final ItemStack p_73085_3_) {
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        final int var4 = p_73085_3_.stackSize;
        final int var5 = p_73085_3_.getMetadata();
        final ItemStack var6 = p_73085_3_.useItemRightClick(worldIn, p_73085_1_);
        if (var6 == p_73085_3_ && (var6 == null || (var6.stackSize == var4 && var6.getMaxItemUseDuration() <= 0 && var6.getMetadata() == var5))) {
            return false;
        }
        p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = var6;
        if (this.isCreative()) {
            var6.stackSize = var4;
            if (var6.isItemStackDamageable()) {
                var6.setItemDamage(var5);
            }
        }
        if (var6.stackSize == 0) {
            p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = null;
        }
        if (!p_73085_1_.isUsingItem()) {
            ((EntityPlayerMP)p_73085_1_).sendContainerToPlayer(p_73085_1_.inventoryContainer);
        }
        return true;
    }
    
    public boolean func_180236_a(final EntityPlayer p_180236_1_, final World worldIn, final ItemStack p_180236_3_, final BlockPos p_180236_4_, final EnumFacing p_180236_5_, final float p_180236_6_, final float p_180236_7_, final float p_180236_8_) {
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            final TileEntity var13 = worldIn.getTileEntity(p_180236_4_);
            if (var13 instanceof ILockableContainer) {
                final Block var14 = worldIn.getBlockState(p_180236_4_).getBlock();
                ILockableContainer var15 = (ILockableContainer)var13;
                if (var15 instanceof TileEntityChest && var14 instanceof BlockChest) {
                    var15 = ((BlockChest)var14).getLockableContainer(worldIn, p_180236_4_);
                }
                if (var15 != null) {
                    p_180236_1_.displayGUIChest(var15);
                    return true;
                }
            }
            else if (var13 instanceof IInventory) {
                p_180236_1_.displayGUIChest((IInventory)var13);
                return true;
            }
            return false;
        }
        if (!p_180236_1_.isSneaking() || p_180236_1_.getHeldItem() == null) {
            final IBlockState var16 = worldIn.getBlockState(p_180236_4_);
            if (var16.getBlock().onBlockActivated(worldIn, p_180236_4_, var16, p_180236_1_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_)) {
                return true;
            }
        }
        if (p_180236_3_ == null) {
            return false;
        }
        if (this.isCreative()) {
            final int var17 = p_180236_3_.getMetadata();
            final int var18 = p_180236_3_.stackSize;
            final boolean var19 = p_180236_3_.onItemUse(p_180236_1_, worldIn, p_180236_4_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_);
            p_180236_3_.setItemDamage(var17);
            p_180236_3_.stackSize = var18;
            return var19;
        }
        return p_180236_3_.onItemUse(p_180236_1_, worldIn, p_180236_4_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_);
    }
    
    public void setWorld(final WorldServer p_73080_1_) {
        this.theWorld = p_73080_1_;
    }
}
