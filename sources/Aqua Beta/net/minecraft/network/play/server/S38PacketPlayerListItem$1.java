package net.minecraft.network.play.server;

import net.minecraft.network.play.server.S38PacketPlayerListItem;

/*
 * Exception performing whole class analysis ignored.
 */
static class S38PacketPlayerListItem.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action;

    static {
        $SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action = new int[S38PacketPlayerListItem.Action.values().length];
        try {
            S38PacketPlayerListItem.1.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[S38PacketPlayerListItem.Action.ADD_PLAYER.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            S38PacketPlayerListItem.1.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[S38PacketPlayerListItem.Action.UPDATE_GAME_MODE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            S38PacketPlayerListItem.1.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[S38PacketPlayerListItem.Action.UPDATE_LATENCY.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            S38PacketPlayerListItem.1.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            S38PacketPlayerListItem.1.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[S38PacketPlayerListItem.Action.REMOVE_PLAYER.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
