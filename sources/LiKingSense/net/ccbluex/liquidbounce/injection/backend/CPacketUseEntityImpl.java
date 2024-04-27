/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/CPacketUseEntityImpl;", "T", "Lnet/minecraft/network/play/client/CPacketUseEntity;", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "wrapped", "(Lnet/minecraft/network/play/client/CPacketUseEntity;)V", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "getAction", "()Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "LiKingSense"})
public final class CPacketUseEntityImpl<T extends CPacketUseEntity>
extends PacketImpl<T>
implements ICPacketUseEntity {
    @Override
    @NotNull
    public ICPacketUseEntity.WAction getAction() {
        ICPacketUseEntity.WAction wAction;
        CPacketUseEntity.Action action = ((CPacketUseEntity)this.getWrapped()).func_149565_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)action, (String)"wrapped.action");
        CPacketUseEntity.Action $this$wrap$iv = action;
        boolean $i$f$wrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$7[$this$wrap$iv.ordinal()]) {
            case 1: {
                wAction = ICPacketUseEntity.WAction.INTERACT;
                break;
            }
            case 2: {
                wAction = ICPacketUseEntity.WAction.ATTACK;
                break;
            }
            case 3: {
                wAction = ICPacketUseEntity.WAction.INTERACT_AT;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wAction;
    }

    public CPacketUseEntityImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Packet)wrapped);
    }
}

