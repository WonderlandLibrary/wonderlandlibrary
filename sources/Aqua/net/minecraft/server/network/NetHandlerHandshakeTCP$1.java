package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;

static class NetHandlerHandshakeTCP.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$network$EnumConnectionState;

    static {
        $SwitchMap$net$minecraft$network$EnumConnectionState = new int[EnumConnectionState.values().length];
        try {
            NetHandlerHandshakeTCP.1.$SwitchMap$net$minecraft$network$EnumConnectionState[EnumConnectionState.LOGIN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            NetHandlerHandshakeTCP.1.$SwitchMap$net$minecraft$network$EnumConnectionState[EnumConnectionState.STATUS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
