// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.module;

import net.minecraft.client.Minecraft;
import moonsense.features.SettingsManager;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.MoonsenseClient;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.features.SCModule;
import moonsense.ui.elements.Element;

public class ElementModuleDetails extends Element
{
    private final SCModule renderModule;
    private final GuiCustomButton overworldScene;
    private final GuiCustomButton netherScene;
    private final GuiCustomButton endScene;
    
    public ElementModuleDetails(final SCModule module, final int x, final int y, final boolean shouldScissor, final AbstractGuiScreen parent) {
        super(x, y, 200, 50, shouldScissor, null, parent);
        this.renderModule = module;
        final int buttonId = 0;
        final int x2 = this.getX();
        final int n = this.getY() + this.getHeight();
        MoonsenseClient.titleRenderer.getClass();
        this.overworldScene = new GuiCustomButton(buttonId, x2, n + 9 + 2, 55, 12, "Overworld", false);
        final int buttonId2 = 1;
        final int x3 = this.getX() + 5 + 55;
        final int n2 = this.getY() + this.getHeight();
        MoonsenseClient.titleRenderer.getClass();
        this.netherScene = new GuiCustomButton(buttonId2, x3, n2 + 9 + 2, 55, 12, "Nether", false);
        final int buttonId3 = 2;
        final int x4 = this.getX() + 5 + 5 + 55 + 55;
        final int n3 = this.getY() + this.getHeight();
        MoonsenseClient.titleRenderer.getClass();
        this.endScene = new GuiCustomButton(buttonId3, x4, n3 + 9 + 2, 55, 12, "End", false);
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        this.hovered = this.hovered(mouseX, mouseY);
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 3.0f, 2.0f, this.enabled ? new Color(105, 116, 122, 100).getRGB() : new Color(85, 96, 102, 100).getRGB());
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 3.0f, this.enabled ? (this.hovered ? new Color(100, 111, 117, 100).getRGB() : new Color(42, 50, 55, 100).getRGB()) : new Color(70, 70, 70, 50).getRGB());
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.overworldScene.isMouseOver()) {
            SettingsManager.INSTANCE.previewBG.setValue(0);
            this.overworldScene.playPressSound(Minecraft.getMinecraft().getSoundHandler());
        }
        if (this.netherScene.isMouseOver()) {
            SettingsManager.INSTANCE.previewBG.setValue(1);
            this.netherScene.playPressSound(Minecraft.getMinecraft().getSoundHandler());
        }
        if (this.endScene.isMouseOver()) {
            SettingsManager.INSTANCE.previewBG.setValue(2);
            this.endScene.playPressSound(Minecraft.getMinecraft().getSoundHandler());
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
