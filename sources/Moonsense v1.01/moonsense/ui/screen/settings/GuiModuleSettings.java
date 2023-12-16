// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen.settings;

import moonsense.settings.Setting;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import moonsense.config.ModuleConfig;
import moonsense.ui.elements.Element;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import org.lwjgl.input.Keyboard;
import moonsense.ui.screen.AbstractGuiScrolling;
import java.util.Set;
import moonsense.ui.screen.AbstractGuiScreen;
import moonsense.ui.elements.module.ElementCrosshairPreview;
import moonsense.features.modules.type.hud.CrosshairModule;
import net.minecraft.client.gui.GuiScreen;
import moonsense.features.SCModule;

public class GuiModuleSettings extends AbstractSettingsGui
{
    private final SCModule module;
    private int row;
    private int column;
    
    public GuiModuleSettings(final SCModule module, final GuiScreen parentScreen) {
        super(parentScreen);
        this.module = module;
    }
    
    @Override
    public void initGui() {
        this.row = ((this.module instanceof CrosshairModule) ? 8 : 1);
        final int extra = (this.module instanceof CrosshairModule) ? 7 : 0;
        this.column = 1;
        this.elements.clear();
        this.components.clear();
        if (this.module instanceof CrosshairModule) {
            this.elements.add(new ElementCrosshairPreview(this.width / 2 - this.getWidth() / 2 + 15, (int)(this.height / 2.0f - this.getHeight() / 2.0f + 25.0f) + 4, 310, 110, true, this));
        }
        this.module.settings.forEach(setting -> {
            if (setting.isHidden()) {
                return;
            }
            else {
                this.addSetting(setting, this.width / 2 - this.getWidth() / 2 + 15, (int)this.getRowHeight(this.row, 17));
                ++this.column;
                if (this.column > 1) {
                    this.column = 1;
                    ++this.row;
                }
                return;
            }
        });
        super.initGui();
        this.registerScroll(new GuiModules.Scroll(this.module.settings, this.width, this.height, this.height / 2 - this.getHeight() / 2 + 20, this.height / 2 + this.getHeight() / 2 + 17, 17, this.width / 2 + this.getWidth() / 2 - 4, 1, extra));
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        int offset = 10;
        if (this.module.hasIcon()) {
            this.mc.getTextureManager().bindTexture(this.module.getIcon());
            final int textureSize = ((this.module.getTextureIndex() != -1) ? this.module.getTextureIndex() : 20) - 4;
            if (this.module.displayName.equalsIgnoreCase("Hypixel Mods")) {
                GuiUtils.setGlColor(-1);
            }
            else {
                GuiUtils.setGlColor(MoonsenseClient.getMainColor(255));
            }
            GuiUtils.drawModalRectWithCustomSizedTexture(this.width / 2 - this.getWidth() / 2 + (20 - textureSize) / 2 + 4.0f, (float)(this.height / 2 - this.getHeight() / 2 + (20 - textureSize) / 2 - 20), 0.0f, 0.0f, textureSize, textureSize, (float)textureSize, (float)textureSize);
            offset = 26;
        }
        MoonsenseClient.textRenderer.drawString(String.valueOf(this.module.displayName.toUpperCase()) + " - ", this.width / 2.0f - this.getWidth() / 2.0f + offset, this.height / 2.0f - this.getHeight() / 2.0f - 15.0f, new Color(200, 200, 200, 200).getRGB());
        MoonsenseClient.textRenderer.drawString(this.module.description.toUpperCase(), this.width / 2.0f - this.getWidth() / 2.0f + offset + MoonsenseClient.titleRenderer2.getWidth(String.valueOf(this.module.displayName.toUpperCase()) + " - "), this.height / 2.0f - this.getHeight() / 2.0f - 15.0f, new Color(150, 150, 150, 200).getRGB());
        MoonsenseClient.textRenderer.drawString("Author(s): " + this.module.getAuthor(), this.width / 2.0f - this.getWidth() / 2.0f + 9.0f, this.height / 2.0f - this.getHeight() / 2.0f - 1.0f, new Color(200, 200, 200, 200).getRGB());
        Gui.drawRect(this.width / 2 - this.getWidth() / 2, this.height / 2 - this.getHeight() / 2 + 15, this.width / 2 + this.getWidth() / 2, this.height / 2 - this.getHeight() / 2 + 16, new Color(100, 100, 100, 100).getRGB());
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int x = (this.width / 2 - this.getWidth() / 2) * sr.getScaleFactor();
        final int y = (this.height / 2 - this.getHeight() / 2 + 1) * sr.getScaleFactor();
        final int xWidth = (this.width / 2 + this.getWidth() / 2) * sr.getScaleFactor() - x;
        final int yHeight = (this.height / 2 + this.getHeight() / 2 - 20) * sr.getScaleFactor() - y;
        if (this.module instanceof CrosshairModule) {
            GL11.glEnable(3089);
            this.scissorFunc(sr, x, y - 40, xWidth, yHeight - 230 + 50 - 25);
            GL11.glDisable(3089);
        }
        else {
            GL11.glEnable(3089);
            this.scissorFunc(sr, x, y - 40, xWidth, yHeight + 50);
            GL11.glDisable(3089);
        }
        if (this.module instanceof CrosshairModule) {
            this.elements.get(0).render(mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ModuleConfig.INSTANCE.saveConfig();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            }
        }
    }
    
    private double getRowHeight(double row, final int buttonHeight) {
        --row;
        return this.height / 2.0f - this.getHeight() / 2.0f + 25.0f + row * buttonHeight;
    }
}
