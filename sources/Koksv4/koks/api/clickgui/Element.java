package koks.api.clickgui;

import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.utils.RenderUtil;
import koks.event.ValueChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

public abstract class Element {

    public final Minecraft mc = Minecraft.getMinecraft();
    public DirtyFontRenderer fr;
    public Value<?> value;
    public int x, y, width, height;
    public boolean extended, hasResetButton;
    public int resetButtonDistance, resetButtonHeight;

    public Element(DirtyFontRenderer fr) {
        this.fr = fr;
    }

    public void updatePosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (hasResetButton) {
            final RenderUtil renderUtil = RenderUtil.getInstance();
            renderUtil.drawPicture(x + resetButtonDistance, y + resetButtonHeight, 10, 10, new ResourceLocation("koks/icons/clickgui/elements/reset.png"));
        }
    }

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract void keyTyped(char typedChar, int keyCode);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public void handleResetButton(int mouseX, int mouseY, int state) {
        if(hasResetButton) {
            if(mouseX >= x + resetButtonDistance && mouseX <= x + resetButtonDistance + 10 && mouseY >= y + resetButtonHeight && mouseY <= y + resetButtonHeight + 10) {
                value.setToDefault();
                new ValueChangeEvent(value).onFire();
            }
        }
    }

}