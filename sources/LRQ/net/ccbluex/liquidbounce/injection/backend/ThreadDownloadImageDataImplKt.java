/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData;
import net.ccbluex.liquidbounce.injection.backend.ThreadDownloadImageDataImpl;
import net.minecraft.client.renderer.ThreadDownloadImageData;

public final class ThreadDownloadImageDataImplKt {
    public static final ThreadDownloadImageData unwrap(IThreadDownloadImageData $this$unwrap) {
        int $i$f$unwrap = 0;
        return (ThreadDownloadImageData)((ThreadDownloadImageDataImpl)$this$unwrap).getWrapped();
    }

    public static final IThreadDownloadImageData wrap(ThreadDownloadImageData $this$wrap) {
        int $i$f$wrap = 0;
        return new ThreadDownloadImageDataImpl<ThreadDownloadImageData>($this$wrap);
    }
}

