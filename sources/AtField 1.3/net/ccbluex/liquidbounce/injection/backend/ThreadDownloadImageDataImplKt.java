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
    public static final ThreadDownloadImageData unwrap(IThreadDownloadImageData iThreadDownloadImageData) {
        boolean bl = false;
        return (ThreadDownloadImageData)((ThreadDownloadImageDataImpl)iThreadDownloadImageData).getWrapped();
    }

    public static final IThreadDownloadImageData wrap(ThreadDownloadImageData threadDownloadImageData) {
        boolean bl = false;
        return new ThreadDownloadImageDataImpl(threadDownloadImageData);
    }
}

