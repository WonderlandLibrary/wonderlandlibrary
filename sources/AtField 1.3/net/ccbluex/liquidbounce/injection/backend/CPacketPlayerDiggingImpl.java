/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NoWhenBranchMatchedException;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;

public final class CPacketPlayerDiggingImpl
extends PacketImpl
implements ICPacketPlayerDigging {
    public CPacketPlayerDiggingImpl(CPacketPlayerDigging cPacketPlayerDigging) {
        super((Packet)cPacketPlayerDigging);
    }

    @Override
    public IEnumFacing getFacing() {
        EnumFacing enumFacing = ((CPacketPlayerDigging)this.getWrapped()).func_179714_b();
        boolean bl = false;
        return new EnumFacingImpl(enumFacing);
    }

    @Override
    public ICPacketPlayerDigging.WAction getAction() {
        ICPacketPlayerDigging.WAction wAction;
        CPacketPlayerDigging.Action action = ((CPacketPlayerDigging)this.getWrapped()).func_180762_c();
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[action.ordinal()]) {
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

    @Override
    public WBlockPos getPosition() {
        return new WBlockPos(((CPacketPlayerDigging)this.getWrapped()).func_179715_a().func_177958_n(), ((CPacketPlayerDigging)this.getWrapped()).func_179715_a().func_177956_o(), ((CPacketPlayerDigging)this.getWrapped()).func_179715_a().func_177952_p());
    }
}

