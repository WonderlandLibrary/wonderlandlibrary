package net.optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerControllerOF extends PlayerControllerMP {

    private boolean acting = false;

    public PlayerControllerOF(Minecraft minecraft,
                              NetHandlerPlayClient netHandlerPlayClient) {
        super(minecraft, netHandlerPlayClient);
    }

    /**
     * Called when the player is hitting a block with an item.
     */
    @Override
    public boolean clickBlock(BlockPos loc,
                              EnumFacing face) {
        this.acting = true;
        boolean flag = super.clickBlock(loc, face);
        this.acting = false;
        return flag;
    }

    @Override
    public boolean onPlayerDamageBlock(BlockPos posBlock,
                                       EnumFacing directionFacing) {
        this.acting = true;
        boolean flag = super.onPlayerDamageBlock(posBlock, directionFacing);
        this.acting = false;
        return flag;
    }

    /**
     * Notifies the server of things like consuming food, etc...
     */
    @Override
    public boolean sendUseItem(EntityPlayer playerIn,
                               World worldIn,
                               ItemStack itemStackIn) {
        this.acting = true;
        boolean flag = super.sendUseItem(playerIn, worldIn, itemStackIn);
        this.acting = false;
        return flag;
    }

    @Override
    public boolean onPlayerRightClick(EntityPlayerSP player,
                                      WorldClient world,
                                      ItemStack heldStack,
                                      BlockPos hitPos,
                                      EnumFacing side,
                                      Vec3 hitVec) {
        this.acting = true;
        boolean flag = super.onPlayerRightClick(player, world, heldStack, hitPos, side, hitVec);
        this.acting = false;
        return flag;
    }

    public boolean isActing() {
        return acting;
    }
}
