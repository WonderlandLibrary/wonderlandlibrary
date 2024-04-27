/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NoWhenBranchMatchedException;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;

public final class CPacketUseEntityImpl
extends PacketImpl
implements ICPacketUseEntity {
    public CPacketUseEntityImpl(CPacketUseEntity cPacketUseEntity) {
        super((Packet)cPacketUseEntity);
    }

    @Override
    public ICPacketUseEntity.WAction getAction() {
        ICPacketUseEntity.WAction wAction;
        CPacketUseEntity.Action action = ((CPacketUseEntity)this.getWrapped()).func_149565_c();
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$7[action.ordinal()]) {
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
}

