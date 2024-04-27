package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;

import static net.ccbluex.liquidbounce.utils.render.RenderUtils.convertCircular;

@ElementInfo(name = "HeadLogo")
public class HeadLogo extends Element {

    private ResourceLocation rl;

    @Override
    public boolean createElement() {
        if (rl == null) {
            rl = new ResourceLocation("liquidbounce" + "/playerface/head.png");
            IImageBuffer iib = new IImageBuffer() {
                public BufferedImage parseUserSkin(BufferedImage img) {
                    img = convertCircular(img, img.getWidth());
                    return img;
                }

                @Override
                public void skinAvailable() {

                }
            };

            ThreadDownloadImageData tex = new ThreadDownloadImageData(null, "https://cdn.jsdelivr.net/gh/ImageHelper/image_repository@main/headlogo.png", (ResourceLocation) null, iib);
            mc.getTextureManager().loadTexture(rl, tex);
        }

        return true;
    }

    @Nullable
    @Override
    public Border drawElement() {
        int picture = 48;
        //头像
        RenderUtils.drawImage(rl, 0, 0, picture, picture);
        //整圆
        RenderUtils.drawCircleFull(picture / 2f, picture / 2f, picture / 2f, 10, 2F, new Color(45, 45, 45));
        //护甲值
        RenderUtils.drawSimpleArc(picture / 2f, picture / 2f, picture / 2f + 1, mc.thePlayer.getTotalArmorValue() * 18f, new Color(55, 150, 218));
        //分割黑线
        RenderUtils.drawLogoArc(picture / 2f, picture / 2f, 34, 0, 400, Color.BLACK);
        GlStateManager.resetColor();

        //小矩形
        RenderUtils.drawGradientSideway(picture / 2f + 25.5f, 0 - picture / 2f + 45, picture / 2f + 100, 0 - picture / 2f + 60, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        //血量值
        RenderUtils.drawLogoArc(picture / 2f, picture / 2f, 34, -10, (int) (mc.thePlayer.getHealth() * 16.5f - 10), Colors.getHealthColor(mc.thePlayer));
        //大矩形
        RenderUtils.drawGradientSideway(picture / 2f + 25.5f, 0 - picture / 2f + 26, picture / 2f + 150, 0 - picture / 2f + 43, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        Fonts.font25.drawString(mc.thePlayer.getName() + "  |  " + (int)mc.thePlayer.getHealth() + "hp", picture / 2f + 43, 0 - picture / 2f + 33, -1);

        Fonts.font25.drawString(mc.thePlayer.getFoodStats().getFoodLevel() + "   §6Food", picture / 2f + 43, 0 - picture / 2f + 51, -1);

        return new Border(20, 20, 120, 80);
    }

}
