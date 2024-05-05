package net.minecraft.network;

class NetHandlerPlayServer.2
implements Runnable {
    NetHandlerPlayServer.2() {
    }

    public void run() {
        NetHandlerPlayServer.this.netManager.checkDisconnected();
    }
}
