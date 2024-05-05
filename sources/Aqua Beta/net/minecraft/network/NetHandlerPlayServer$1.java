package net.minecraft.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

class NetHandlerPlayServer.1
implements GenericFutureListener<Future<? super Void>> {
    final /* synthetic */ ChatComponentText val$chatcomponenttext;

    NetHandlerPlayServer.1(ChatComponentText chatComponentText) {
        this.val$chatcomponenttext = chatComponentText;
    }

    public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception {
        NetHandlerPlayServer.this.netManager.closeChannel((IChatComponent)this.val$chatcomponenttext);
    }
}
