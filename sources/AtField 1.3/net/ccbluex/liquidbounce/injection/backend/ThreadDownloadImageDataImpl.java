/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData;
import net.ccbluex.liquidbounce.injection.backend.AbstractTextureImpl;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.jetbrains.annotations.Nullable;

public final class ThreadDownloadImageDataImpl
extends AbstractTextureImpl
implements IThreadDownloadImageData {
    @Override
    public boolean equals(@Nullable Object object) {
        return object instanceof ThreadDownloadImageDataImpl && ((ThreadDownloadImageData)((ThreadDownloadImageDataImpl)object).getWrapped()).equals((ThreadDownloadImageData)this.getWrapped());
    }

    public ThreadDownloadImageDataImpl(ThreadDownloadImageData threadDownloadImageData) {
        super((AbstractTexture)threadDownloadImageData);
    }
}

