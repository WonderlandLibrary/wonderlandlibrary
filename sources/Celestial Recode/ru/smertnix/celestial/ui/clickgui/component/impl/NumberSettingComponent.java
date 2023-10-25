package ru.smertnix.celestial.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.PropertyComponent;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

import java.awt.*;

import org.lwjgl.opengl.GL11;


public class NumberSettingComponent extends ru.smertnix.celestial.ui.clickgui.component.Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate = 0f;
    private boolean sliding;
    Minecraft mc = Minecraft.getMinecraft();
    public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.pushMatrix();
    	GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
        int e = 10;
    	int x = getX() + e;
        int y = getY();
        int width = getWidth() - e;
        int height = getHeight();
        double min = numberSetting.getMinValue();
        double max = numberSetting.getMaxValue();
        double max2 = numberSetting.getMaxValue() * 1.5f;
        boolean hovered = isHovered(mouseX, mouseY);
        double selected = 0;
        if (this.sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x) * (max2 - min) / (double) getWidth() + min, numberSetting.getIncrement()));
            if (numberSetting.getNumberValue() > max) {
                numberSetting.setValueNumber((float) max);
            } else if (numberSetting.getNumberValue() < min) {
                numberSetting.setValueNumber((float) min);
            }
        }

        float amountWidth = (float) (((numberSetting.getNumberValue()) - (min)) / (max - (min)));
        
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        String valueString = "";

        NumberSetting.NumberType numberType = numberSetting.getType();


        switch (numberType) {
        case PERCENTAGE:
            valueString += '%';
            break;
        case MS:
            valueString += "ms";
            break;
        case DISTANCE:
            valueString += 'm';
        case SIZE:
            valueString += "SIZE";
        case APS:
            valueString += "APS";
            break;
        default:
            valueString = "";
    }
        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());
        
        
        RenderUtils.drawRect(x + 13 - e, y + 13.5, x + 5 + 1 * (width - 30), y + 15F, 0);

        mc.mntsb_13.drawString("" + String.format("%.1f", Float.valueOf("" + min)), x - 1 - e, y + height- 11, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);

        mc.mntsb_13.drawString("" + String.format("%.1f", Float.valueOf("" + max)), x + width - mc.mntsb_13.getStringWidth("" + String.format("%.1f", Float.valueOf("" + max))) - 4, y + height- 11, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);

        RenderUtils.drawGradientSideways(x - e + 13, y + 13.5, x + 5 + 5 + 1 * (width - 30), y + 15F, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB(), Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
        
        RenderUtils.drawGradientSideways(x - e + 13, y + 13.5, x + 5 + currentValueAnimate * (width - 30 + 5), y + 15F, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : new Color(ClickGUI.color.getColorValue()).getRGB(), Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : new Color(ClickGUI.color.getColorValue()).darker().getRGB());
        
        RenderUtils.drawRect(x - e, y, x, y, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : new Color(ClickGUI.color.getColorValue()).darker().getRGB());
        
        RenderUtils.drawBlurredShadow((int) (x + 3 + currentValueAnimate * (width - 32 + 5)), (int) (y + 11.7f), 5, 5, 5, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE : new Color(ClickGUI.color.getColorValue()).darker());
        
        RoundedUtil.drawRound((int) (x + 3 + currentValueAnimate * (width - 32 + 5)), (int) (y + 11.7f), 5, 5, 2f, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE : new Color(ClickGUI.color.getColorValue()).darker());
        
        RoundedUtil.drawRound((int) (x + 4 + currentValueAnimate * (width - 32 + 5)), (int) (y + 12.7f), 3, 3, 1, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? new Color(ClickGUI.color.getColorValue()).darker() : Color.WHITE);

        mc.mntsb_14.drawString(numberSetting.getName() + ":", x - e - 1, y + height / 2.5F - 6F, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);
        mc.mntsb_14.drawString(optionValue + " " + valueString, x + 5 + currentValueAnimate * (width - 30) - (mc.mntsb_14.getStringWidth(optionValue + " " + valueString)/ 2), y + height / 2.5F - 4F + 14, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? -1 : 0);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!sliding && button == 0 && isHovered(mouseX, mouseY)) {
            sliding = true;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        this.sliding = false;
    }

    @Override
    public Setting getSetting() {
        return numberSetting;
    }
}
