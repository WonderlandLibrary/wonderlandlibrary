package net.ccbluex.liquidbounce.api.minecraft.client.render;

import java.awt.image.BufferedImage;
import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\n\n\u0000\bf\u000020J02\b0H&J\b0H&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/render/WIImageBuffer;", "", "parseUserSkin", "Ljava/awt/image/BufferedImage;", "image", "skinAvailable", "", "Pride"})
public interface WIImageBuffer {
    @Nullable
    public BufferedImage parseUserSkin(@Nullable BufferedImage var1);

    public void skinAvailable();
}
