/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.player;

import baritone.api.utils.Helper;
import baritone.api.utils.IPlayerController;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public enum PrimaryPlayerController implements IPlayerController,
Helper
{
    INSTANCE;


    @Override
    public void syncHeldItem() {
        PrimaryPlayerController.mc.playerController.callSyncCurrentPlayItem();
    }

    @Override
    public boolean hasBrokenBlock() {
        return PrimaryPlayerController.mc.playerController.getCurrentBlock().getY() == -1;
    }

    @Override
    public boolean onPlayerDamageBlock(BlockPos pos, EnumFacing side) {
        return PrimaryPlayerController.mc.playerController.onPlayerDamageBlock(pos, side);
    }

    @Override
    public void resetBlockRemoving() {
        PrimaryPlayerController.mc.playerController.resetBlockRemoving();
    }

    @Override
    public ItemStack windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player) {
        return PrimaryPlayerController.mc.playerController.windowClick(windowId, slotId, mouseButton, type, player);
    }

    @Override
    public GameType getGameType() {
        return PrimaryPlayerController.mc.playerController.getCurrentGameType();
    }

    @Override
    public EnumActionResult processRightClickBlock(EntityPlayerSP player, World world, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand) {
        return PrimaryPlayerController.mc.playerController.processRightClickBlock(player, (WorldClient)world, pos, direction, vec, hand);
    }

    @Override
    public EnumActionResult processRightClick(EntityPlayerSP player, World world, EnumHand hand) {
        return PrimaryPlayerController.mc.playerController.processRightClick(player, world, hand);
    }

    @Override
    public boolean clickBlock(BlockPos loc, EnumFacing face) {
        return PrimaryPlayerController.mc.playerController.clickBlock(loc, face);
    }

    @Override
    public void setHittingBlock(boolean hittingBlock) {
        PrimaryPlayerController.mc.playerController.setIsHittingBlock(hittingBlock);
    }
}

