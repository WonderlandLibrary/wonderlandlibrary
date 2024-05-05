package net.minecraft.client.resources;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.SettableFuture;
import java.io.File;

class ResourcePackRepository.3
implements FutureCallback<Object> {
    final /* synthetic */ File val$file1;
    final /* synthetic */ SettableFuture val$settablefuture;

    ResourcePackRepository.3(File file, SettableFuture settableFuture) {
        this.val$file1 = file;
        this.val$settablefuture = settableFuture;
    }

    public void onSuccess(Object p_onSuccess_1_) {
        ResourcePackRepository.this.setResourcePackInstance(this.val$file1);
        this.val$settablefuture.set(null);
    }

    public void onFailure(Throwable p_onFailure_1_) {
        this.val$settablefuture.setException(p_onFailure_1_);
    }
}
