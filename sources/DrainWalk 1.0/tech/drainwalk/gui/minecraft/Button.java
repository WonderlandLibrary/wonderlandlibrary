package tech.drainwalk.gui.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.RenderUtility;

public class Button extends GuiButton {
    public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }
    public Button(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 143, 28, buttonText);
    }

    public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float pt) {
        if (this.visible) {
            minecraft.getTextureManager().bindTexture(BUTTON_TEXTURES);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            getAnimation().animate(0, 1, 2f, EasingList.NONE, pt);

            this.mouseDragged(minecraft, mouseX, mouseY);
            int color = ColorUtility.rgba(255, 255, 255, (int) (255 - (55 * (1-getAnimation().getAnimationValue()))));

            GlStateManager.pushMatrix();
            float value = 0.025f;
            float anim = getAnimation().getAnimationValue() *value;
            GlStateManager.translate((x + width / 2f), (y + height / 2f), 0);
            GlStateManager.scale(1 + anim , 1 + anim, 1 + anim);
            GlStateManager.translate(-(x + width / 2f), -(y + height / 2f), 0);

            RenderUtility.drawRoundedRect(this.x, this.y, this.width, this.height, 8, ColorUtility.rgba(0, 0, 0, (int) (60 + (33 *  getAnimation().getAnimationValue()))));
            RenderUtility.drawRoundedOutlineRect(this.x, this.y, this.width, this.height,8, 1.5f, ColorUtility.rgba(255, 255, 255, 15));
            GlStateManager.popMatrix();
            FontManager.SEMI_BOLD_28.drawCenteredString(displayString, this.x + this.width / 2f, this.y + (FontManager.SEMI_BOLD_28.getStringHeight(displayString) / 2f) + 1, color);
        }
    }



}
