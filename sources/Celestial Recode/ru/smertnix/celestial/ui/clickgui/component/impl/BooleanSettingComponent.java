package ru.smertnix.celestial.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.impl.combat.ShieldBreaker;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.feature.impl.misc.Optimizer;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.PropertyComponent;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.utils.inventory.InvenotryUtil;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

import java.awt.*;

import org.lwjgl.opengl.GL11;


public class BooleanSettingComponent extends Component implements PropertyComponent {
    public float textHoverAnimate = 0f;
    public float leftRectAnimation = 0;
    public float rightRectAnimation = 0;
    public BooleanSetting booleanSetting;
    Minecraft mc = Minecraft.getMinecraft();

    public BooleanSettingComponent(Component parent, BooleanSetting booleanSetting, int x, int y, int width, int height) {
        super(parent, booleanSetting.getName(), x, y, width, height);
        this.booleanSetting = booleanSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (booleanSetting.isVisible()) {
        	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            GlStateManager.pushMatrix();
        	GL11.glEnable(GL11.GL_SCISSOR_TEST);
        	RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            int middleHeight = getHeight() / 2;
            boolean hovered = isHovered(mouseX, mouseY);
            mc.mntsb_13.drawString(getName(), x - 1, y + middleHeight + 1, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
            textHoverAnimate = AnimationHelper.animation(textHoverAnimate, hovered ? 2.3f : 2, 0);
            leftRectAnimation = AnimationHelper.animation(leftRectAnimation, booleanSetting.getBoolValue() ? 10 : 19, 0);
            rightRectAnimation = AnimationHelper.animation(rightRectAnimation, (booleanSetting.getBoolValue() ? 3 : 12), 0);
            if (ClickGUI.shadow.getBoolValue()) {
            RenderUtils.drawBlurredShadow(x + 2+ width - 18 + 2.5f - 7, y + 2 + 8, x + width + 1 - (x + width - 18 + 1) - 1, y + height - 5 - 3.5f - (y + 8.5f), 7, new Color(0, 0, 0, 150));
        }
            RoundedUtil.drawRound(x +2 + width - 18 + 2 - 7, y + 2 + 8, x + width + 1 - (x + width - 18 + 1), y + height - 5 - 3.5f - (y + 8), 4, booleanSetting.getBoolValue() ? new Color(ClickGUI.color.getColorValue()) : new Color(124, 147, 155));
            RoundedUtil.drawRound(x +2.5f+ width - leftRectAnimation + 3 - 6, y + 1.5f + 2 + 8f , x + width - rightRectAnimation - (x + width - leftRectAnimation) - 1, y + height - 7 - 5.5f- (y + 7.5f)+ 0.5f, 2.5f, Color.WHITE);
            /*
            if (hovered) {
                if (booleanSetting.getDesc() != null) {
                    RenderUtils.drawSmoothRect(x + width + 20, y + height / 1.5F + 4.5F, x + width + 25 + mc.rubik_18.getStringWidth(booleanSetting.getDesc()), y + 6.5F, new Color(0, 0, 0, 80).getRGB());
                    mc.rubik_18.drawString(booleanSetting.getDesc(), x + width + 22, y + height / 1.35F - 5F, 0);
                }
            }*/
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
      		GlStateManager.popMatrix();
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY) && booleanSetting.isVisible()) {
            booleanSetting.setBoolValue(!booleanSetting.getBoolValue());
        }
    }

    @Override
    public Setting getSetting() {
        return booleanSetting;
    }
}
