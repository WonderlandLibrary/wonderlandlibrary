/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u00108VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/CPacketPlayerDiggingImpl;", "T", "Lnet/minecraft/network/play/client/CPacketPlayerDigging;", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging;", "wrapped", "(Lnet/minecraft/network/play/client/CPacketPlayerDigging;)V", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "getAction", "()Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "facing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "position", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPosition", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "LiKingSense"})
public final class CPacketPlayerDiggingImpl<T extends CPacketPlayerDigging>
extends PacketImpl<T>
implements ICPacketPlayerDigging {
    @Override
    @NotNull
    public WBlockPos getPosition() {
        BlockPos blockPos = ((CPacketPlayerDigging)this.getWrapped()).func_179715_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)blockPos, (String)"wrapped.position");
        int n = blockPos.func_177958_n();
        BlockPos blockPos2 = ((CPacketPlayerDigging)this.getWrapped()).func_179715_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)blockPos2, (String)"wrapped.position");
        int n2 = blockPos2.func_177956_o();
        BlockPos blockPos3 = ((CPacketPlayerDigging)this.getWrapped()).func_179715_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)blockPos3, (String)"wrapped.position");
        return new WBlockPos(n, n2, blockPos3.func_177952_p());
    }

    @Override
    @NotNull
    public IEnumFacing getFacing() {
        EnumFacing enumFacing = ((CPacketPlayerDigging)this.getWrapped()).func_179714_b();
        Intrinsics.checkExpressionValueIsNotNull((Object)enumFacing, (String)"wrapped.facing");
        EnumFacing $this$wrap$iv = enumFacing;
        boolean $i$f$wrap = false;
        return new EnumFacingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public ICPacketPlayerDigging.WAction getAction() {
        ICPacketPlayerDigging.WAction wAction;
        CPacketPlayerDigging.Action action = ((CPacketPlayerDigging)this.getWrapped()).func_180762_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)action, (String)"wrapped.action");
        CPacketPlayerDigging.Action $this$wrap$iv = action;
        boolean $i$f$wrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[$this$wrap$iv.ordinal()]) {
            case 1: {
                wAction = ICPacketPlayerDigging.WAction.ABORT_DESTROY_BLOCK;
                break;
            }
            case 2: {
                wAction = ICPacketPlayerDigging.WAction.DROP_ALL_ITEMS;
                break;
            }
            case 3: {
                wAction = ICPacketPlayerDigging.WAction.DROP_ITEM;
                break;
            }
            case 4: {
                wAction = ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM;
                break;
            }
            case 5: {
                wAction = ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK;
                break;
            }
            case 6: {
                wAction = ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK;
                break;
            }
            case 7: {
                wAction = ICPacketPlayerDigging.WAction.SWAP_HELD_ITEMS;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wAction;
    }

    public CPacketPlayerDiggingImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Packet)wrapped);
    }
}

