package net.minecraft.network;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C16PacketClientStatus;

static class NetHandlerPlayServer.4 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action;
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action;
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$network$play$client$C16PacketClientStatus$EnumState;

    static {
        $SwitchMap$net$minecraft$network$play$client$C16PacketClientStatus$EnumState = new int[C16PacketClientStatus.EnumState.values().length];
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C16PacketClientStatus$EnumState[C16PacketClientStatus.EnumState.PERFORM_RESPAWN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C16PacketClientStatus$EnumState[C16PacketClientStatus.EnumState.REQUEST_STATS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C16PacketClientStatus$EnumState[C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action = new int[C0BPacketEntityAction.Action.values().length];
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action[C0BPacketEntityAction.Action.START_SNEAKING.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action[C0BPacketEntityAction.Action.STOP_SNEAKING.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action[C0BPacketEntityAction.Action.START_SPRINTING.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action[C0BPacketEntityAction.Action.STOP_SPRINTING.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action[C0BPacketEntityAction.Action.STOP_SLEEPING.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action[C0BPacketEntityAction.Action.RIDING_JUMP.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C0BPacketEntityAction$Action[C0BPacketEntityAction.Action.OPEN_INVENTORY.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action = new int[C07PacketPlayerDigging.Action.values().length];
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action[C07PacketPlayerDigging.Action.DROP_ITEM.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action[C07PacketPlayerDigging.Action.DROP_ALL_ITEMS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action[C07PacketPlayerDigging.Action.RELEASE_USE_ITEM.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action[C07PacketPlayerDigging.Action.START_DESTROY_BLOCK.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action[C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerPlayServer.4.$SwitchMap$net$minecraft$network$play$client$C07PacketPlayerDigging$Action[C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
