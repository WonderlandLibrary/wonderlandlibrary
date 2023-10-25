package net.minecraft.network;

import java.util.concurrent.Callable;
import net.minecraft.network.NetworkManager;

class NetworkSystem.6
implements Callable<String> {
    final /* synthetic */ NetworkManager val$networkmanager;

    NetworkSystem.6(NetworkManager networkManager) {
        this.val$networkmanager = networkManager;
    }

    public String call() throws Exception {
        return this.val$networkmanager.toString();
    }
}
