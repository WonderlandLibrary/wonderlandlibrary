package net.minecraft.server.network;

static enum NetHandlerLoginServer.LoginState {
    HELLO,
    KEY,
    AUTHENTICATING,
    READY_TO_ACCEPT,
    DELAY_ACCEPT,
    ACCEPTED;

}
