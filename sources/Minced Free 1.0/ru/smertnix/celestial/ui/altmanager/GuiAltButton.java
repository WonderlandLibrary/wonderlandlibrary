package ru.smertnix.celestial.ui.altmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

import java.awt.*;

public class GuiAltButton extends GuiButton {
    private int opacity = 40;

    public GuiAltButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiAltButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (hovered) {
                if (this.opacity < 40) {
                    this.opacity += 1;
                }
            } else if (this.opacity > 22) {
                this.opacity -= 1;
            }

            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height; // Flag, tells if your mouse is hovering the button
            Color color = new Color(25, 25, 25, 73);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width + 1, this.height, 1, new Color(0,0, 0, 50));
            if (displayString.contains("Files")) {
                mc.mntsb_15.drawCenteredStringWithShadow(displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - (int) 2) / 3 + 1, flag ? new Color(ClickGUI.color.getColorValue()).getRGB() : -1);
            } else if (displayString.contains("-")) {
                mc.mntsb_25.drawCenteredStringWithShadow(displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - (int) 2) / 3 - 3, flag ? new Color(ClickGUI.color.getColorValue()).getRGB() : -1);
            } else {
                mc.mntsb_25.drawCenteredStringWithShadow(displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - (int) 2) / 3 - 2, flag ? new Color(ClickGUI.color.getColorValue()).getRGB() : -1);
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}