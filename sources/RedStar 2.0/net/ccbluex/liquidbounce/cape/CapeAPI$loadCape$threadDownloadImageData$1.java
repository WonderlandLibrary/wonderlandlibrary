package net.ccbluex.liquidbounce.cape;

import java.awt.image.BufferedImage;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.render.WIImageBuffer;
import net.ccbluex.liquidbounce.cape.CapeInfo;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\u0000\n\n\u0000\n\n\b\n\n\u0000*\u0000\b\n\u000020J02\b0HJ\b0H¨"}, d2={"net/ccbluex/liquidbounce/cape/CapeAPI$loadCape$threadDownloadImageData$1", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/WIImageBuffer;", "parseUserSkin", "Ljava/awt/image/BufferedImage;", "image", "skinAvailable", "", "Pride"})
public static final class CapeAPI$loadCape$threadDownloadImageData$1
implements WIImageBuffer {
    final CapeInfo $capeInfo;

    @Override
    @Nullable
    public BufferedImage parseUserSkin(@Nullable BufferedImage image) {
        return image;
    }

    @Override
    public void skinAvailable() {
        this.$capeInfo.setCapeAvailable(true);
    }

    CapeAPI$loadCape$threadDownloadImageData$1(CapeInfo $captured_local_variable$0) {
        this.$capeInfo = $captured_local_variable$0;
    }
}
