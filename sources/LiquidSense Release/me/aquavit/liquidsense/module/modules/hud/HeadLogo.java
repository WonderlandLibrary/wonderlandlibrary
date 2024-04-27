package me.aquavit.liquidsense.module.modules.hud;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.login.UserUtils;
import me.aquavit.liquidsense.utils.render.Colors;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@ModuleInfo(name = "HeadLogo", description = "Drawing 2D HeadLogo", category = ModuleCategory.HUD, array = false)
public class HeadLogo extends Module {

    public HeadLogo(double x, double y){
        this.setDefaultx(x);
        this.setDefaulty(y);

    }

    public HeadLogo(){
        this(68,  92);
    }

    private final ListValue horizontal = new ListValue("Horizontal", new String[] {"Left", "Middle", "Right"}, "Left") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "left":
                    setRenderx(x);
                    break;
                case "middle":
                    setRenderx((float) new ScaledResolution(mc).getScaledWidth() / 2 - x);
                    break;
                case "right":
                    setRenderx(new ScaledResolution(mc).getScaledWidth() - x);
                    break;
            }
        }
    };
    private final ListValue vertical = new ListValue("Vertical", new String[] {"Up", "Middle", "Down"}, "Up") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "up":
                    setRendery(y);
                    break;
                case "middle":
                    setRendery((float) new ScaledResolution(mc).getScaledHeight() / 2 - y);
                    break;
                case "down":
                    setRendery(new ScaledResolution(mc).getScaledHeight() - y);
                    break;
            }
        }
    };

    public final BoolValue health = new BoolValue("CancelHealth", true);
    public final BoolValue food = new BoolValue("CancelFood", true);
    public final BoolValue armor = new BoolValue("CancelArmor", true);
    int x,y;
    private final Translate translate = new Translate(0f , 0f);
    private ResourceLocation rl;

    @Override
    public void onEnable() {
        if (rl == null) {
            rl = new ResourceLocation("sb.png");
            try {
                UserUtils.getWebImageResource(rl, "https://cdn.jsdelivr.net/gh/ImageHelper/image_repository@main/headlogo.png");
            } catch (Exception e) {
                ClientUtils.displayChatMessage("Couldn't download http texture");
                this.setState(false);
            }
        }
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        isLeft = horizontal.get().equalsIgnoreCase("Left");
        isUp = vertical.get().equalsIgnoreCase("Up");
        x = (int) getHUDX(horizontal.get().equalsIgnoreCase("Left"),
                horizontal.get().equalsIgnoreCase("Middle"),
                horizontal.get().equalsIgnoreCase("Right"));
        y = (int) getHUDY(vertical.get().equalsIgnoreCase("Up"),
                vertical.get().equalsIgnoreCase("Middle"),
                vertical.get().equalsIgnoreCase("Down"));

        translate.translate((mc.thePlayer.getHealth() * 16.5f - 10) , 0f );

        int picture = 48;
        //头像
        RenderUtils.drawImage(rl, x, y, picture, picture);
        //整圆
        RenderUtils.drawFullCircle(x+picture / 2f, y+picture / 2f, picture / 2f, 10, 2F, new Color(45, 45, 45));
        //护甲值
        RenderUtils.drawSimpleArc(x+picture / 2f, y+picture / 2f, picture / 2f + 1, mc.thePlayer.getTotalArmorValue() * 18f, new Color(55, 150, 218));
        //分割黑线
        RenderUtils.drawLogoArc(x+picture / 2f, y+picture / 2f, 34, 0, 400, Color.BLACK);
        GlStateManager.resetColor();

        //小矩形
        RenderUtils.drawGradientSideway(x+picture / 2f + 25.5f, y - picture / 2f + 45, x+picture / 2f + Fonts.font14.getStringWidth(mc.thePlayer.getFoodStats().getFoodLevel() + "   §6Food") * 2.8f, y - picture / 2f + 60, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        //血量值
        RenderUtils.drawLogoArc(x+picture / 2f, y+picture / 2f, 34, -10, (int) translate.getX(), Colors.getHealthColor(mc.thePlayer));
        //大矩形
        RenderUtils.drawGradientSideway(x+picture / 2f + 25.5f, y - picture / 2f + 26, x+picture / 2f + Fonts.font14.getStringWidth(mc.thePlayer.getName() + "  |  " + (int)mc.thePlayer.getHealth() + "hp") * 2.2f, y - picture / 2f + 43, new Color(45, 45, 45, 255).getRGB(), new Color(45, 45, 45, 0).getRGB());
        Fonts.font14.drawString(mc.thePlayer.getName() + "  |  " + (int)mc.thePlayer.getHealth() + "hp", x+picture / 2f + 43, y - picture / 2f + 33, -1);

        Fonts.font14.drawString(mc.thePlayer.getFoodStats().getFoodLevel() + "   §6Food", x+picture / 2f + 43, y - picture / 2f + 51, -1);

        drawBorder((float) x, (float) y,
                (float)picture, (float)picture);
    }

}
