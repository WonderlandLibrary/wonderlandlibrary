/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData;
import net.ccbluex.liquidbounce.injection.backend.AbstractTextureImpl;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0096\u0002\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ThreadDownloadImageDataImpl;", "T", "Lnet/minecraft/client/renderer/ThreadDownloadImageData;", "Lnet/ccbluex/liquidbounce/injection/backend/AbstractTextureImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IThreadDownloadImageData;", "wrapped", "(Lnet/minecraft/client/renderer/ThreadDownloadImageData;)V", "equals", "", "other", "", "LiKingSense"})
public final class ThreadDownloadImageDataImpl<T extends ThreadDownloadImageData>
extends AbstractTextureImpl<T>
implements IThreadDownloadImageData {
    @Override
    public boolean equals(@Nullable Object other) {
        return other instanceof ThreadDownloadImageDataImpl && Intrinsics.areEqual((Object)((ThreadDownloadImageData)((ThreadDownloadImageDataImpl)other).getWrapped()), (Object)((ThreadDownloadImageData)this.getWrapped()));
    }

    public ThreadDownloadImageDataImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((AbstractTexture)wrapped);
    }
}

