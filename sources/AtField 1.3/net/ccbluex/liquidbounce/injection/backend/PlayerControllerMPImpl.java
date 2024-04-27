/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 *  net.minecraftforge.common.ForgeHooks
 *  net.minecraftforge.event.ForgeEventFactory
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerSPImpl;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public final class PlayerControllerMPImpl
implements IPlayerControllerMP {
    private final PlayerControllerMP wrapped;

    @Override
    public boolean onPlayerDestroyBlock(WBlockPos wBlockPos, IEnumFacing iEnumFacing) {
        WBlockPos wBlockPos2 = wBlockPos;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(wBlockPos2.getX(), wBlockPos2.getY(), wBlockPos2.getZ());
        return playerControllerMP.func_187103_a(blockPos);
    }

    @Override
    public IWorldSettings.WGameType getCurrentGameType() {
        IWorldSettings.WGameType wGameType;
        GameType gameType = this.wrapped.func_178889_l();
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[gameType.ordinal()]) {
            case 1: {
                wGameType = IWorldSettings.WGameType.NOT_SET;
                break;
            }
            case 2: {
                wGameType = IWorldSettings.WGameType.SURVIVAL;
                break;
            }
            case 3: {
                wGameType = IWorldSettings.WGameType.CREATIVE;
                break;
            }
            case 4: {
                wGameType = IWorldSettings.WGameType.ADVENTUR;
                break;
            }
            case 5: {
                wGameType = IWorldSettings.WGameType.SPECTATOR;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wGameType;
    }

    @Override
    public boolean extendedReach() {
        return this.wrapped.func_78749_i();
    }

    public PlayerControllerMPImpl(PlayerControllerMP playerControllerMP) {
        this.wrapped = playerControllerMP;
    }

    @Override
    public int getBlockHitDelay() {
        return this.wrapped.field_78781_i;
    }

    @Override
    public boolean isNotCreative() {
        return this.wrapped.func_78762_g();
    }

    @Override
    public boolean sendUseItem(IEntityPlayer iEntityPlayer, IWorld iWorld, IItemStack iItemStack) {
        IEntityPlayer iEntityPlayer2 = iEntityPlayer;
        boolean bl = false;
        EntityPlayer entityPlayer = (EntityPlayer)((EntityPlayerImpl)iEntityPlayer2).getWrapped();
        IWorld iWorld2 = iWorld;
        boolean bl2 = false;
        iEntityPlayer2 = ((WorldImpl)iWorld2).getWrapped();
        IItemStack iItemStack2 = iItemStack;
        int n = 0;
        iWorld2 = ((ItemStackImpl)iItemStack2).getWrapped();
        if (this.wrapped.func_178889_l() == GameType.SPECTATOR) {
            return false;
        }
        this.wrapped.func_78750_j();
        NetHandlerPlayClient netHandlerPlayClient = Minecraft.func_71410_x().func_147114_u();
        if (netHandlerPlayClient == null) {
            Intrinsics.throwNpe();
        }
        netHandlerPlayClient.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        if (entityPlayer.func_184811_cZ().func_185141_a(iWorld2.func_77973_b())) {
            return false;
        }
        iItemStack2 = ForgeHooks.onItemRightClick((EntityPlayer)entityPlayer, (EnumHand)EnumHand.MAIN_HAND);
        if (iItemStack2 != null) {
            return iItemStack2 == EnumActionResult.SUCCESS;
        }
        n = iWorld2.func_190916_E();
        ActionResult actionResult = iWorld2.func_77957_a((World)iEntityPlayer2, entityPlayer, EnumHand.MAIN_HAND);
        ItemStack itemStack = (ItemStack)actionResult.func_188398_b();
        if (itemStack.equals(iWorld2) ^ true || itemStack.func_190916_E() != n) {
            entityPlayer.func_184611_a(EnumHand.MAIN_HAND, itemStack);
            if (itemStack.func_190926_b()) {
                ForgeEventFactory.onPlayerDestroyItem((EntityPlayer)entityPlayer, (ItemStack)iWorld2, (EnumHand)EnumHand.MAIN_HAND);
            }
        }
        return actionResult.func_188397_a() == EnumActionResult.SUCCESS;
    }

    @Override
    public boolean isInCreativeMode() {
        return this.wrapped.func_78758_h();
    }

    @Override
    public void updateController() {
        this.wrapped.func_78765_e();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof PlayerControllerMPImpl && ((PlayerControllerMPImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void windowClick(int n, int n2, int n3, int n4, IEntityPlayerSP iEntityPlayerSP) {
        ClickType clickType;
        int n5 = n4;
        int n6 = n3;
        int n7 = n2;
        int n8 = n;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean bl = false;
        switch (n5) {
            case 0: {
                clickType = ClickType.PICKUP;
                break;
            }
            case 1: {
                clickType = ClickType.QUICK_MOVE;
                break;
            }
            case 2: {
                clickType = ClickType.SWAP;
                break;
            }
            case 3: {
                clickType = ClickType.CLONE;
                break;
            }
            case 4: {
                clickType = ClickType.THROW;
                break;
            }
            case 5: {
                clickType = ClickType.QUICK_CRAFT;
                break;
            }
            case 6: {
                clickType = ClickType.PICKUP_ALL;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid mode " + n5);
            }
        }
        ClickType clickType2 = clickType;
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        bl = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)iEntityPlayerSP2).getWrapped();
        playerControllerMP.func_187098_a(n8, n7, n6, clickType2, (EntityPlayer)entityPlayerSP);
    }

    @Override
    public void setBlockHitDelay(int n) {
        this.wrapped.field_78781_i = n;
    }

    @Override
    public float getBlockReachDistance() {
        return this.wrapped.func_78757_d();
    }

    @Override
    public boolean clickBlock(WBlockPos wBlockPos, IEnumFacing iEnumFacing) {
        Object object = wBlockPos;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        object = iEnumFacing;
        bl = false;
        EnumFacing enumFacing = ((EnumFacingImpl)object).getWrapped();
        return playerControllerMP.func_180511_b(blockPos, enumFacing);
    }

    @Override
    public void setCurBlockDamageMP(float f) {
        this.wrapped.field_78770_f = f;
    }

    @Override
    public float getCurBlockDamageMP() {
        return this.wrapped.field_78770_f;
    }

    public final PlayerControllerMP getWrapped() {
        return this.wrapped;
    }

    @Override
    public void onStoppedUsingItem(IEntityPlayerSP iEntityPlayerSP) {
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean bl = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)iEntityPlayerSP2).getWrapped();
        playerControllerMP.func_78766_c((EntityPlayer)entityPlayerSP);
    }

    @Override
    public boolean onPlayerRightClick(IEntityPlayerSP iEntityPlayerSP, IWorldClient iWorldClient, @Nullable IItemStack iItemStack, WBlockPos wBlockPos, IEnumFacing iEnumFacing, WVec3 wVec3) {
        Object object = iEntityPlayerSP;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean bl = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)object).getWrapped();
        object = iWorldClient;
        bl = false;
        WorldClient worldClient = (WorldClient)((WorldClientImpl)object).getWrapped();
        object = wBlockPos;
        bl = false;
        BlockPos blockPos = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        object = iEnumFacing;
        bl = false;
        EnumFacing enumFacing = ((EnumFacingImpl)object).getWrapped();
        object = wVec3;
        bl = false;
        Vec3d vec3d = new Vec3d(((WVec3)object).getXCoord(), ((WVec3)object).getYCoord(), ((WVec3)object).getZCoord());
        return playerControllerMP.func_187099_a(entityPlayerSP, worldClient, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
    }
}

