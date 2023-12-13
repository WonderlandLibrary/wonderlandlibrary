package com.ohare.client.gui.hudsettings;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import com.ohare.client.gui.GuiHud;
import com.ohare.client.gui.hudsettings.component.Component;
import com.ohare.client.gui.hudsettings.component.impl.BooleanButton;
import com.ohare.client.gui.hudsettings.component.impl.ColorButton;
import com.ohare.client.gui.hudsettings.component.impl.EnumButton;
import com.ohare.client.gui.hudsettings.component.impl.NumberButton;
import com.ohare.client.gui.hudsettings.component.impl.StringButton;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.Value;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.ColorValue;
import com.ohare.client.utils.value.impl.EnumValue;
import com.ohare.client.utils.value.impl.NumberValue;
import com.ohare.client.utils.value.impl.StringValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by Xen for OhareWare
 *
 * @since 7/26/2019
 **/
public class HudSettings extends GuiScreen {
    private HudComp hudComp;
    private ArrayList<Component> components = new ArrayList<>();

    public HudSettings(HudComp hudComp) {
        this.hudComp = hudComp;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        components.clear();
        int y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2 - 80;
        for (Value value : hudComp.getValues()) {
            if (value instanceof EnumValue) {
                EnumValue enumValue = (EnumValue) value;
                components.add(new EnumButton(enumValue, this.width / 2 - 90, y));
                y += 16;
            }
            if (value instanceof BooleanValue) {
                BooleanValue booleanValue = (BooleanValue) value;
                components.add(new BooleanButton(booleanValue, this.width / 2 - 90, y));
                y += 16;
            }
            if (value instanceof NumberValue) {
                NumberValue numberValue = (NumberValue) value;
                components.add(new NumberButton(numberValue, this.width / 2 - 90, y));
                y += 16;
            }
            if(value instanceof StringValue) {
                StringValue stringValue = (StringValue) value;
                components.add(new StringButton(stringValue,width / 2 - 90,y));
                y += 16;
            }
            if(value instanceof ColorValue) {
                ColorValue colorValue = (ColorValue) value;
                components.add(new ColorButton(colorValue,width /2-90,y));
                y += 48;
            }
        }

        components.forEach(component -> component.init());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        hudComp.onRender(scaledResolution);
        RenderUtil.drawBordered(scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 2 - 100, scaledResolution.getScaledWidth() / 2 + 100, scaledResolution.getScaledHeight() / 2 + 100, 0.5, new Color(15, 15, 15, 255).getRGB(), new Color(61, 156, 255, 255).getRGB());
        RenderUtil.drawRect(scaledResolution.getScaledWidth() / 2 + 86,scaledResolution.getScaledHeight() / 2 - 96,10,10,new Color(0xFF4548).getRGB());
        Fonts.hudfont.drawStringWithShadow("x",scaledResolution.getScaledWidth() / 2 + 88.5,scaledResolution.getScaledHeight() / 2 - 95, -1);
        Fonts.hudfont.drawStringWithShadow(hudComp.getLabel().replaceAll("Comp",""), scaledResolution.getScaledWidth() / 2 - Fonts.hudfont.getStringWidth(hudComp.getLabel().replaceAll("Comp","")) / 2, scaledResolution.getScaledHeight() / 2 - 95, -1);
        components.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        if (mouseWithinBounds(mouseX,mouseY,scaledResolution.getScaledWidth() / 2 + 86,scaledResolution.getScaledHeight() / 2 - 96,10,10)) {
            mc.displayGuiScreen(new GuiHud());
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        components.forEach(component -> component.keyTyped(typedChar,keyCode));
    }

    public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }
}
