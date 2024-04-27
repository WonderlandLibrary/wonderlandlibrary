/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.client.render;

import java.awt.image.BufferedImage;
import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/render/WIImageBuffer;", "", "parseUserSkin", "Ljava/awt/image/BufferedImage;", "image", "skinAvailable", "", "LiKingSense"})
public interface WIImageBuffer {
    @Nullable
    public BufferedImage parseUserSkin(@Nullable BufferedImage var1);

    public void skinAvailable();
}

