// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.module;

import net.minecraft.client.Minecraft;
import moonsense.features.SCAbstractRenderModule;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import moonsense.features.SettingsManager;
import org.lwjgl.opengl.GL11;
import moonsense.MoonsenseClient;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.features.SCModule;
import moonsense.ui.elements.Element;

public class ElementModuleRenderPreview extends Element
{
    private final SCModule renderModule;
    private final GuiCustomButton overworldScene;
    private final GuiCustomButton netherScene;
    private final GuiCustomButton endScene;
    
    public ElementModuleRenderPreview(final SCModule module, final int x, final int y, final int width, final int height, final boolean shouldScissor, final AbstractGuiScreen parent) {
        super(x, y, width, height, shouldScissor, null, parent);
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
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (SettingsManager.INSTANCE.previewBG.getInt() == 0) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/preview 1.png"));
        }
        if (SettingsManager.INSTANCE.previewBG.getInt() == 1) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/preview 2.png"));
        }
        if (SettingsManager.INSTANCE.previewBG.getInt() == 2) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/preview 3.png"));
        }
        int offX = 0;
        int offY = 0;
        if (this.hovered) {
            offX = -(this.getX() + this.width / 2 - mouseX);
            offY = -(this.getY() + this.height / 2 - mouseY);
        }
        Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 1050.0f - offX, 50.0f - offY, this.width, this.height, 480.0f, 252.0f);
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0.0f, 2.0f, Color.black.getRGB());
        ((SCAbstractRenderModule)this.renderModule).renderDummy((float)(this.getX() + this.width / 2 - ((SCAbstractRenderModule)this.renderModule).getWidth() / 2), (float)(this.getY() + this.height / 2 - ((SCAbstractRenderModule)this.renderModule).getHeight() / 2));
        MoonsenseClient.titleRenderer.drawString("Preview Scene", this.getX(), this.getY() + 2 + this.getHeight(), -1);
        final GuiCustomButton overworldScene = this.overworldScene;
        final int n = this.getY() + this.getHeight();
        MoonsenseClient.titleRenderer.getClass();
        overworldScene.yPosition = n + 9 + 4;
        final GuiCustomButton netherScene = this.netherScene;
        final int n2 = this.getY() + this.getHeight();
        MoonsenseClient.titleRenderer.getClass();
        netherScene.yPosition = n2 + 9 + 4;
        final GuiCustomButton endScene = this.endScene;
        final int n3 = this.getY() + this.getHeight();
        MoonsenseClient.titleRenderer.getClass();
        endScene.yPosition = n3 + 9 + 4;
        this.overworldScene.drawButton(this.mc, mouseX, mouseY);
        this.netherScene.drawButton(this.mc, mouseX, mouseY);
        this.endScene.drawButton(this.mc, mouseX, mouseY);
        final float x = (float)(this.getX() - 2);
        final int n4 = this.getY() + this.height;
        MoonsenseClient.titleRenderer.getClass();
        final float y = n4 + 9 + 22.0f;
        final float x2 = (float)(this.getX() + this.width);
        final int n5 = this.getY() + this.height;
        MoonsenseClient.titleRenderer.getClass();
        GuiUtils.drawRoundedOutline(x, y, x2, n5 + 9 + 22.5f, 0.0f, 1.0f, -1);
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
