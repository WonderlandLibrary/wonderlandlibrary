package net.minecraft.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

class NetworkSystem.7
implements GenericFutureListener<Future<? super Void>> {
    final /* synthetic */ NetworkManager val$networkmanager;
    final /* synthetic */ ChatComponentText val$chatcomponenttext;

    NetworkSystem.7(NetworkManager networkManager, ChatComponentText chatComponentText) {
        this.val$networkmanager = networkManager;
        this.val$chatcomponenttext = chatComponentText;
    }

    public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception {
        this.val$networkmanager.closeChannel((IChatComponent)this.val$chatcomponenttext);
    }
}
