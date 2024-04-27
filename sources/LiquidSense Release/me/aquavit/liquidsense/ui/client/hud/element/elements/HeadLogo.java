package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.utils.login.UserUtils;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.Colors;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@ElementInfo(name = "HeadLogo")
public class HeadLogo extends Element {

    private ResourceLocation rl;

    private final Translate translate = new Translate(0f , 0f);

    @Override
    public Border drawElement() {
        if (rl == null) {
            rl = new ResourceLocation("sb.png");
            UserUtils.getWebImageResource(rl,"https://cdn.jsdelivr.net/gh/ImageHelper/image_repository@main/headlogo.png");
        }

        translate.translate((mc.thePlayer.getHealth() * 16.5f - 10) , 0f );

        int picture = 48;
        //头像
        RenderUtils.drawImage(rl, 0, 0, picture, picture);
        //整圆
        RenderUtils.drawFullCircle(picture / 2f, picture / 2f, picture / 2f, 10, 2F, new Color(45, 45, 45));
        //护甲值
        RenderUtils.drawSimpleArc(picture / 2f, picture / 2f, picture / 2f + 1, mc.thePlayer.getTotalArmorValue() * 18f, new Color(55, 150, 218));
        //分割黑线
        RenderUtils.drawLogoArc(picture / 2f, picture / 2f, 34, 0, 400, Color.BLACK);
        GlStateManager.resetColor();

        //小矩形
        RenderUtils.drawGradientSideway(picture / 2f + 25.5f, 0 - picture / 2f + 45, picture / 2f + 100, 0 - picture / 2f + 60, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        //血量值
        RenderUtils.drawLogoArc(picture / 2f, picture / 2f, 34, -10, (int) translate.getX(), Colors.getHealthColor(mc.thePlayer));
        //大矩形
        RenderUtils.drawGradientSideway(picture / 2f + 25.5f, 0 - picture / 2f + 26, picture / 2f + 150, 0 - picture / 2f + 43, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        Fonts.font14.drawString(mc.thePlayer.getName() + "  |  " + (int)mc.thePlayer.getHealth() + "hp", picture / 2f + 43, 0 - picture / 2f + 33, -1);

        Fonts.font14.drawString(mc.thePlayer.getFoodStats().getFoodLevel() + "   §6Food", picture / 2f + 43, 0 - picture / 2f + 51, -1);

        return new Border(20, 20, 120, 80);
    }

}
