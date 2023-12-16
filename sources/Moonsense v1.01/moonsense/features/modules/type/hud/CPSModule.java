// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import java.awt.Color;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class CPSModule extends SCDefaultRenderModule
{
    private final Setting rightClickCPS;
    private final Setting showCPSText;
    private final Setting lineColor;
    private int width;
    private int height;
    
    public CPSModule() {
        super("CPS Display", "Displays your clicking speed on the HUD.");
        new Setting(this, "Settings");
        this.rightClickCPS = new Setting(this, "Right Click CPS").setDefault(true);
        this.showCPSText = new Setting(this, "Show CPS Text").setDefault(true);
        this.lineColor = new Setting(this, "Line Color").setDefault(new Color(145, 145, 145, 255).getRGB(), 0);
        this.settings.remove(this.brackets);
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.rightClickCPS.getBoolean()) {
            String renderText = String.valueOf(MoonsenseClient.left.getCPS()) + "   " + MoonsenseClient.right.getCPS();
            if (this.showCPSText.getBoolean()) {
                renderText = String.valueOf(renderText) + " CPS";
            }
            this.calculateDimensionsAndRender(renderText, x, y);
            if (this.background.getBoolean()) {
                GuiUtils.drawRect(x + this.mc.fontRendererObj.getStringWidth(String.valueOf(MoonsenseClient.left.getCPS()) + " ") + this.backgroundWidth.getInt() + 1.0f, y + this.backgroundHeight.getInt() - 1.0f, x + this.mc.fontRendererObj.getStringWidth(String.valueOf(MoonsenseClient.left.getCPS()) + " ") + this.backgroundWidth.getInt() + 2.0f, y + this.height - this.backgroundHeight.getInt() + 1.0f, this.lineColor.getColorObject());
            }
            else {
                GuiUtils.drawRect(x + this.mc.fontRendererObj.getStringWidth(String.valueOf(MoonsenseClient.left.getCPS()) + " ") + 2.0f, y, x + this.mc.fontRendererObj.getStringWidth(String.valueOf(MoonsenseClient.left.getCPS()) + " ") + 3.0f, y + this.height, this.lineColor.getColorObject());
            }
        }
        else {
            String renderText = new StringBuilder().append(MoonsenseClient.left.getCPS()).toString();
            if (this.showCPSText.getBoolean()) {
                renderText = String.valueOf(renderText) + " CPS";
            }
            this.calculateDimensionsAndRender(renderText, x, y);
        }
    }
    
    private void calculateDimensionsAndRender(final String text, final float x, final float y) {
        this.width = this.mc.fontRendererObj.getStringWidth(text);
        this.height = this.mc.fontRendererObj.FONT_HEIGHT;
        if (this.background.getBoolean()) {
            this.width += this.backgroundWidth.getInt() * 2;
            this.height += this.backgroundHeight.getInt() * 2 - 2;
            this.renderBackground(x + this.width / 2.0f, y + this.height / 2.0f - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f);
            this.drawCenteredString(text, x + this.width / 2.0f, y + this.height / 2.0f - (this.mc.fontRendererObj.FONT_HEIGHT - 2) / 2.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
        }
        else {
            ++this.width;
            this.drawString(text, x + 1.0f, y + 1.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
        }
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        if (this.rightClickCPS.getBoolean()) {
            String renderText = "0   0";
            if (this.showCPSText.getBoolean()) {
                renderText = String.valueOf(renderText) + " CPS";
            }
            this.calculateDimensionsAndRender(renderText, x, y);
            if (this.background.getBoolean()) {
                GuiUtils.drawRect(x + this.mc.fontRendererObj.getStringWidth("0 ") + this.backgroundWidth.getInt() + 1.0f, y + this.backgroundHeight.getInt() - 1.0f, x + this.mc.fontRendererObj.getStringWidth("0 ") + this.backgroundWidth.getInt() + 2.0f, y + this.height - this.backgroundHeight.getInt() + 1.0f, this.lineColor.getColorObject());
            }
            else {
                GuiUtils.drawRect(x + this.mc.fontRendererObj.getStringWidth("0 ") + 2.0f, y, x + this.mc.fontRendererObj.getStringWidth("0 ") + 3.0f, y + this.height, this.lineColor.getColorObject());
            }
        }
        else {
            String renderText = "0";
            if (this.showCPSText.getBoolean()) {
                renderText = String.valueOf(renderText) + " CPS";
            }
            this.calculateDimensionsAndRender(renderText, x, y);
        }
    }
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    protected void renderBackground(final float x, final float y) {
        if (this.border.getBoolean()) {
            GuiUtils.drawRoundedOutline(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, 0.0f, this.borderWidth.getFloat(), SCModule.getColor(this.borderColor.getColorObject()));
        }
        GuiUtils.drawRect(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, SCModule.getColor(this.backgroundColor.getColorObject()));
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_RIGHT;
    }
}
