/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
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
 *  net.minecraftforge.common.ForgeHooks
 *  net.minecraftforge.event.ForgeEventFactory
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0016J\u0013\u0010#\u001a\u00020\u00192\b\u0010$\u001a\u0004\u0018\u00010%H\u0096\u0002J\b\u0010&\u001a\u00020\u0019H\u0016J\u0018\u0010'\u001a\u00020\u00192\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0016J:\u0010(\u001a\u00020\u00192\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010.2\u0006\u0010/\u001a\u00020 2\u0006\u00100\u001a\u00020\"2\u0006\u00101\u001a\u000202H\u0016J\u0010\u00103\u001a\u0002042\u0006\u00105\u001a\u00020*H\u0016J \u00106\u001a\u00020\u00192\u0006\u00107\u001a\u0002082\u0006\u0010+\u001a\u0002092\u0006\u0010-\u001a\u00020.H\u0016J\b\u0010:\u001a\u000204H\u0016J0\u0010;\u001a\u0002042\u0006\u0010<\u001a\u00020\u00062\u0006\u0010=\u001a\u00020\u00062\u0006\u0010>\u001a\u00020\u00062\u0006\u0010?\u001a\u00020\u00062\u0006\u0010@\u001a\u00020*H\u0016R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR$\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0005\u001a\u00020\r8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0011\u0010\u000f\"\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0018\u001a\u00020\u00198VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u001aR\u0014\u0010\u001b\u001a\u00020\u00198VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001d\u00a8\u0006A"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/PlayerControllerMPImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "wrapped", "Lnet/minecraft/client/multiplayer/PlayerControllerMP;", "(Lnet/minecraft/client/multiplayer/PlayerControllerMP;)V", "value", "", "blockHitDelay", "getBlockHitDelay", "()I", "setBlockHitDelay", "(I)V", "blockReachDistance", "", "getBlockReachDistance", "()F", "curBlockDamageMP", "getCurBlockDamageMP", "setCurBlockDamageMP", "(F)V", "currentGameType", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings$WGameType;", "getCurrentGameType", "()Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings$WGameType;", "isInCreativeMode", "", "()Z", "isNotCreative", "getWrapped", "()Lnet/minecraft/client/multiplayer/PlayerControllerMP;", "clickBlock", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "enumFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "equals", "other", "", "extendedReach", "onPlayerDestroyBlock", "onPlayerRightClick", "playerSP", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "wWorld", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "wItemStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "wPosition", "wSideOpposite", "wHitVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "onStoppedUsingItem", "", "thePlayer", "sendUseItem", "wPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "updateController", "windowClick", "windowId", "slot", "mouseButton", "mode", "player", "LiKingSense"})
public final class PlayerControllerMPImpl
implements IPlayerControllerMP {
    @NotNull
    private final PlayerControllerMP wrapped;

    @Override
    public boolean isNotCreative() {
        return this.wrapped.func_78762_g();
    }

    @Override
    public float getBlockReachDistance() {
        return this.wrapped.func_78757_d();
    }

    @Override
    @NotNull
    public IWorldSettings.WGameType getCurrentGameType() {
        IWorldSettings.WGameType wGameType;
        GameType gameType = this.wrapped.func_178889_l();
        Intrinsics.checkExpressionValueIsNotNull((Object)gameType, (String)"wrapped.currentGameType");
        GameType $this$wrap$iv = gameType;
        boolean $i$f$wrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[$this$wrap$iv.ordinal()]) {
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
    public boolean isInCreativeMode() {
        return this.wrapped.func_78758_h();
    }

    @Override
    public float getCurBlockDamageMP() {
        return this.wrapped.field_78770_f;
    }

    @Override
    public void setCurBlockDamageMP(float value) {
        this.wrapped.field_78770_f = value;
    }

    @Override
    public int getBlockHitDelay() {
        return this.wrapped.field_78781_i;
    }

    @Override
    public void setBlockHitDelay(int value) {
        this.wrapped.field_78781_i = value;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void windowClick(int windowId, int slot, int mouseButton, int mode, @NotNull IEntityPlayerSP player) {
        void $this$unwrap$iv;
        ClickType clickType;
        void $this$toClickType$iv22;
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
        int n = mode;
        int n2 = mouseButton;
        int n3 = slot;
        int n4 = windowId;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean $i$f$toClickType = false;
        switch ($this$toClickType$iv22) {
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
                throw (Throwable)new IllegalArgumentException("Invalid mode " + (int)$this$toClickType$iv22);
            }
        }
        ClickType clickType2 = clickType;
        IEntityPlayerSP $this$toClickType$iv22 = player;
        boolean $i$f$unwrap = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap$iv).getWrapped();
        playerControllerMP.func_187098_a(n4, n3, n2, clickType2, (EntityPlayer)entityPlayerSP);
    }

    @Override
    public void updateController() {
        this.wrapped.func_78765_e();
    }

    @Override
    public boolean sendUseItem(@NotNull IEntityPlayer wPlayer, @NotNull IWorld wWorld, @NotNull IItemStack wItemStack) {
        ActionResult result;
        Intrinsics.checkParameterIsNotNull((Object)wPlayer, (String)"wPlayer");
        Intrinsics.checkParameterIsNotNull((Object)wWorld, (String)"wWorld");
        Intrinsics.checkParameterIsNotNull((Object)wItemStack, (String)"wItemStack");
        IEntityPlayer $this$unwrap$iv = wPlayer;
        boolean $i$f$unwrap = false;
        EntityPlayer player = (EntityPlayer)((EntityPlayerImpl)$this$unwrap$iv).getWrapped();
        IWorld $this$unwrap$iv2 = wWorld;
        boolean $i$f$unwrap2 = false;
        Object world = ((WorldImpl)$this$unwrap$iv2).getWrapped();
        IItemStack $this$unwrap$iv3 = wItemStack;
        boolean $i$f$unwrap3 = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv3).getWrapped();
        if (this.wrapped.func_178889_l() == GameType.SPECTATOR) {
            return false;
        }
        this.wrapped.func_78750_j();
        Minecraft minecraft = Minecraft.func_71410_x();
        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"Minecraft.getMinecraft()");
        minecraft.func_147114_u().func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        if (player.func_184811_cZ().func_185141_a(itemStack.func_77973_b())) {
            return false;
        }
        EnumActionResult cancelResult = ForgeHooks.onItemRightClick((EntityPlayer)player, (EnumHand)EnumHand.MAIN_HAND);
        if (cancelResult != null) {
            return cancelResult == EnumActionResult.SUCCESS;
        }
        int i = itemStack.func_190916_E();
        ActionResult actionResult = result = itemStack.func_77957_a(world, player, EnumHand.MAIN_HAND);
        Intrinsics.checkExpressionValueIsNotNull((Object)actionResult, (String)"result");
        ItemStack resultStack = (ItemStack)actionResult.func_188398_b();
        if (Intrinsics.areEqual((Object)resultStack, (Object)itemStack) ^ true || resultStack.func_190916_E() != i) {
            player.func_184611_a(EnumHand.MAIN_HAND, resultStack);
            ItemStack itemStack2 = resultStack;
            Intrinsics.checkExpressionValueIsNotNull((Object)itemStack2, (String)"resultStack");
            if (itemStack2.func_190926_b()) {
                ForgeEventFactory.onPlayerDestroyItem((EntityPlayer)player, (ItemStack)itemStack, (EnumHand)EnumHand.MAIN_HAND);
            }
        }
        return result.func_188397_a() == EnumActionResult.SUCCESS;
    }

    @Override
    public boolean onPlayerRightClick(@NotNull IEntityPlayerSP playerSP, @NotNull IWorldClient wWorld, @Nullable IItemStack wItemStack, @NotNull WBlockPos wPosition, @NotNull IEnumFacing wSideOpposite, @NotNull WVec3 wHitVec) {
        Object $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)playerSP, (String)"playerSP");
        Intrinsics.checkParameterIsNotNull((Object)wWorld, (String)"wWorld");
        Intrinsics.checkParameterIsNotNull((Object)wPosition, (String)"wPosition");
        Intrinsics.checkParameterIsNotNull((Object)wSideOpposite, (String)"wSideOpposite");
        Intrinsics.checkParameterIsNotNull((Object)wHitVec, (String)"wHitVec");
        IEntityPlayerSP iEntityPlayerSP = playerSP;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean $i$f$unwrap = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = wWorld;
        $i$f$unwrap = false;
        WorldClient worldClient = (WorldClient)((WorldClientImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = wPosition;
        $i$f$unwrap = false;
        BlockPos blockPos = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        $this$unwrap$iv = wSideOpposite;
        $i$f$unwrap = false;
        EnumFacing enumFacing = ((EnumFacingImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = wHitVec;
        $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d(((WVec3)$this$unwrap$iv).getXCoord(), ((WVec3)$this$unwrap$iv).getYCoord(), ((WVec3)$this$unwrap$iv).getZCoord());
        return playerControllerMP.func_187099_a(entityPlayerSP, worldClient, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onStoppedUsingItem(@NotNull IEntityPlayerSP thePlayer) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)thePlayer, (String)"thePlayer");
        IEntityPlayerSP iEntityPlayerSP = thePlayer;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean $i$f$unwrap = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap$iv).getWrapped();
        playerControllerMP.func_78766_c((EntityPlayer)entityPlayerSP);
    }

    @Override
    public boolean clickBlock(@NotNull WBlockPos blockPos, @NotNull IEnumFacing enumFacing) {
        IEnumFacing $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        Intrinsics.checkParameterIsNotNull((Object)enumFacing, (String)"enumFacing");
        WBlockPos wBlockPos = blockPos;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos(((WVec3i)((Object)$this$unwrap$iv)).getX(), ((WVec3i)((Object)$this$unwrap$iv)).getY(), ((WVec3i)((Object)$this$unwrap$iv)).getZ());
        $this$unwrap$iv = enumFacing;
        $i$f$unwrap = false;
        EnumFacing enumFacing2 = ((EnumFacingImpl)$this$unwrap$iv).getWrapped();
        return playerControllerMP.func_180511_b(blockPos2, enumFacing2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean onPlayerDestroyBlock(@NotNull WBlockPos blockPos, @NotNull IEnumFacing enumFacing) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        Intrinsics.checkParameterIsNotNull((Object)enumFacing, (String)"enumFacing");
        WBlockPos wBlockPos = blockPos;
        PlayerControllerMP playerControllerMP = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        return playerControllerMP.func_187103_a(blockPos2);
    }

    @Override
    public boolean extendedReach() {
        return this.wrapped.func_78749_i();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof PlayerControllerMPImpl && Intrinsics.areEqual((Object)((PlayerControllerMPImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final PlayerControllerMP getWrapped() {
        return this.wrapped;
    }

    public PlayerControllerMPImpl(@NotNull PlayerControllerMP wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

