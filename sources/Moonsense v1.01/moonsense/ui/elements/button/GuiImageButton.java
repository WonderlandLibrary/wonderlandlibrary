// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.button;

import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import moonsense.utils.Colors;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class GuiImageButton extends GuiButton
{
    private final ResourceLocation img;
    
    public GuiImageButton(final int buttonId, final int x, final int y, final String buttonText, final ResourceLocation img) {
        super(buttonId, x, y, buttonText);
        this.img = img;
    }
    
    public GuiImageButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText, final ResourceLocation img) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.img = img;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.img);
        Gui.drawModalRectWithCustomSizedTexture(this.xPosition + 2, this.yPosition + 2, 0.0f, 0.0f, 16, 16, 16.0f, 16.0f);
        GuiUtils.drawRoundedRect((float)this.xPosition, (float)this.yPosition, (float)(this.xPosition + this.width), (float)(this.yPosition + this.height), 5.0f, this.hovered ? Colors.CLIENT_MAIN_MENU_BACKGROUND_CENTER_GRAD3.getRGB() : Colors.CLIENT_MAIN_MENU_BACKGROUND_CENTER_GRAD2.getRGB());
        GuiUtils.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 5.0f, 3.0f, Colors.CLIENT_MAIN_MENU_BACKGROUND_CENTER_GRAD4.getRGB());
        MoonsenseClient.textRenderer.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2 + 8), (float)(this.yPosition + (this.height - 3) / 2 + 2), -1);
    }
}
