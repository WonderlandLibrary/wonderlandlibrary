package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@ModuleInfo(name = "HeadLogo", description = "HeadLogo", category = ModuleCategory.RENDER)
public class HeadLogo extends Module {

    private final IntegerValue xx = new IntegerValue("X", 300, 0, 3000);
    private final IntegerValue y = new IntegerValue("Y ", 500, 0, 3000);
    public final BoolValue dis = new BoolValue("disable", true);
    private ResourceLocation rl;

    public BoolValue getBoolValue() {
        return dis;
    }

    @Override
    public void onEnable() {
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
    }

    public static BufferedImage convertCircular(BufferedImage bi1, int min) {
        BufferedImage bi2 = new BufferedImage(min, min, BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, min, min);
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(bi1, 0, 0, null);
        g2.setBackground(Color.green);
        g2.dispose();
/*
        g2 = bi2.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int border1 = 3;
        //使画笔时基本会像外延伸一定像素
        Stroke s = new BasicStroke(6F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(s);
        g2.setColor(new Color(45,45,45));
        g2.drawOval(border1, border1, min - border1 * 2, min - border1 * 2);
        g2.dispose();
 */
        return bi2;
    }

    public static BufferedImage compress(BufferedImage inputImage, int newWidth, int newHeight) {
        int type = inputImage.getColorModel().getTransparency();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage img = new BufferedImage(newWidth, newHeight, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
        graphics2d.dispose();
        return img;
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        int picture = 48;
        //头像
        RenderUtils.drawImage(rl, xx.get(), y.get(), picture, picture);
        //整圆
        RenderUtils.drawCircleFull(xx.get() + picture / 2f, y.get() + picture / 2f, picture / 2f, 10, 2F, new Color(45, 45, 45));
        //护甲值
        RenderUtils.drawSimpleArc(xx.get() + picture / 2f, y.get() + picture / 2f, picture / 2f + 1, mc.thePlayer.getTotalArmorValue() * 18f, new Color(55, 150, 218));
        //分割黑线
        RenderUtils.drawLogoArc(xx.get() + picture / 2f, y.get() + picture / 2f, 34, 0, 400, Color.BLACK);
        GlStateManager.resetColor();

        //小矩形
        RenderUtils.drawGradientSideway(xx.get() + picture / 2f + 25.5f, y.get() - picture / 2f + 45, xx.get() + picture / 2f + 100, y.get() - picture / 2f + 60, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        //血量值
        RenderUtils.drawLogoArc(xx.get() + picture / 2f, y.get() + picture / 2f, 34, -10, (int) (mc.thePlayer.getHealth() * 16.5f - 10), Colors.getHealthColor(mc.thePlayer));
        //大矩形
        RenderUtils.drawGradientSideway(xx.get() + picture / 2f + 25.5f, y.get() - picture / 2f + 26, xx.get() + picture / 2f + 150, y.get() - picture / 2f + 43, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        Fonts.font25.drawString(mc.thePlayer.getName() + "  |  " + mc.thePlayer.getHealth() + "hp", xx.get() + picture / 2f + 43, y.get() - picture / 2f + 33, -1);

        Fonts.font25.drawString(mc.thePlayer.getFoodStats().getFoodLevel() + "   §6Food", xx.get() + picture / 2f + 43, y.get() - picture / 2f + 51, -1);


    }
}
