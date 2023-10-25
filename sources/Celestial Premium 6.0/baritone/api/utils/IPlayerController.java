/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.BaritoneAPI;
import net.minecraft.client.entity.EntityPlayerSP;
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

public interface IPlayerController {
    public void syncHeldItem();

    public boolean hasBrokenBlock();

    public boolean onPlayerDamageBlock(BlockPos var1, EnumFacing var2);

    public void resetBlockRemoving();

    public ItemStack windowClick(int var1, int var2, int var3, ClickType var4, EntityPlayer var5);

    public GameType getGameType();

    public EnumActionResult processRightClickBlock(EntityPlayerSP var1, World var2, BlockPos var3, EnumFacing var4, Vec3d var5, EnumHand var6);

    public EnumActionResult processRightClick(EntityPlayerSP var1, World var2, EnumHand var3);

    public boolean clickBlock(BlockPos var1, EnumFacing var2);

    public void setHittingBlock(boolean var1);

    default public double getBlockReachDistance() {
        return this.getGameType().isCreative() ? 5.0 : (double)((Float)BaritoneAPI.getSettings().blockReachDistance.value).floatValue();
    }
}

