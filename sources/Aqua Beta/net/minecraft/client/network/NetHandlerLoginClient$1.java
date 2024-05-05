package net.minecraft.client.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import javax.crypto.SecretKey;
import net.minecraft.client.network.NetHandlerLoginClient;

/*
 * Exception performing whole class analysis ignored.
 */
class NetHandlerLoginClient.1
implements GenericFutureListener<Future<? super Void>> {
    final /* synthetic */ SecretKey val$secretkey;

    NetHandlerLoginClient.1(SecretKey secretKey) {
        this.val$secretkey = secretKey;
    }

    public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception {
        NetHandlerLoginClient.access$000((NetHandlerLoginClient)NetHandlerLoginClient.this).enableEncryption(this.val$secretkey);
    }
}
