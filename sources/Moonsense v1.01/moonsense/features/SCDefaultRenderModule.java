// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features;

import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import moonsense.ui.screen.settings.GuiHUDEditor;
import java.awt.Color;
import moonsense.settings.Setting;

public abstract class SCDefaultRenderModule extends SCAbstractRenderModule
{
    protected int width;
    protected int height;
    protected final Setting background;
    protected final Setting backgroundRadius;
    protected final Setting backgroundColor;
    protected final Setting backgroundWidth;
    protected final Setting backgroundHeight;
    protected final Setting dynamicBackgroundDimensions;
    protected final Setting textShadow;
    protected final Setting brackets;
    protected final Setting textColor;
    protected final Setting borderColor;
    protected final Setting borderWidth;
    protected final Setting border;
    
    public SCDefaultRenderModule(final String displayName, final String description) {
        this(displayName, description, -1);
    }
    
    public SCDefaultRenderModule(final String displayName, final String description, final int textureIndex) {
        super(displayName, description, textureIndex);
        this.background = new Setting(this, "Background").setDefault(true);
        this.backgroundRadius = new Setting(this, "Background Radius").setDefault(0.0f).setRange(0.0f, 5.0f, 0.01f);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(false);
        this.backgroundWidth = new Setting(this, "Background Width").setDefault(5).setRange(2, 20, 1);
        this.backgroundHeight = new Setting(this, "Background Height").setDefault(5).setRange(2, 12, 1);
        this.dynamicBackgroundDimensions = new Setting(this, "Dynamic Background Dimensions").setDefault(false);
        this.border = new Setting(this, "Border").setDefault(false);
        this.borderWidth = new Setting(this, "Border Thickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f);
        this.brackets = new Setting(this, "Brackets").setDefault(5).setRange("[]", "<>", "{}", "--", "||", "NONE");
        new Setting(this, "Color Options");
        this.textColor = new Setting(this, "Text Color").setDefault(new Color(255, 255, 255).getRGB(), 0);
        this.backgroundColor = new Setting(this, "Background Color").setDefault(new Color(0, 0, 0, 75).getRGB(), 0);
        this.borderColor = new Setting(this, "Border Color").setDefault(new Color(0, 0, 0, 75).getRGB(), 0);
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
    public void render(final float x, final float y) {
        if (this.getValue() == null) {
            return;
        }
        if (this.getValue() != null || (this.mc.currentScreen instanceof GuiHUDEditor && this.getDummyValue() != null)) {
            final String text = this.getFormat().replace("%value%", String.valueOf((this.getValue() != null) ? this.getValue() : this.getDummyValue()));
            this.width = this.mc.fontRendererObj.getStringWidth(text);
            this.height = this.mc.fontRendererObj.FONT_HEIGHT;
            if (this.background.getBoolean() || !this.dynamicBackgroundDimensions.getBoolean()) {
                this.width += this.backgroundWidth.getInt() * 2;
                this.height += this.backgroundHeight.getInt() * 2 - 2;
                if (this.background.getBoolean()) {
                    this.renderBackground(x + this.width / 2.0f, y + this.height / 2.0f - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f);
                }
                this.drawCenteredString(text, x + this.width / 2.0f, y + this.height / 2.0f - (this.mc.fontRendererObj.FONT_HEIGHT - 2) / 2.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
            }
            else {
                ++this.width;
                this.drawString(text, x + 1.0f, y + 1.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
            }
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        if (this.getValue() != null || (this.mc.currentScreen instanceof GuiHUDEditor && this.getDummyValue() != null)) {
            final String text = this.getFormat().replace("%value%", String.valueOf((this.getValue() != null) ? this.getValue() : this.getDummyValue()));
            this.width = this.mc.fontRendererObj.getStringWidth(text);
            this.height = this.mc.fontRendererObj.FONT_HEIGHT;
            if (this.background.getBoolean() || !this.dynamicBackgroundDimensions.getBoolean()) {
                this.width += this.backgroundWidth.getInt() * 2;
                this.height += this.backgroundHeight.getInt() * 2 - 2;
                if (this.background.getBoolean()) {
                    this.renderBackground(x + this.width / 2.0f, y + this.height / 2.0f - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f);
                }
                this.drawCenteredString(text, x + this.width / 2.0f, y + this.height / 2.0f - (this.mc.fontRendererObj.FONT_HEIGHT - 2) / 2.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
            }
            else {
                ++this.width;
                this.drawString(text, x + 1.0f, y + 1.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
            }
        }
    }
    
    protected void renderBackground(final float x, final float y) {
        if (this.border.getBoolean()) {
            GuiUtils.drawRoundedOutline(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, this.backgroundRadius.getFloat(), this.borderWidth.getFloat(), SCModule.getColor(this.borderColor.getColorObject()));
        }
        GuiUtils.drawRoundedRect(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, this.backgroundRadius.getFloat(), SCModule.getColor(this.backgroundColor.getColorObject()));
    }
    
    public String getFormat() {
        String bracketType = this.brackets.getValue().get(this.brackets.getInt() + 1);
        if (bracketType.equalsIgnoreCase("NONE")) {
            bracketType = "  ";
        }
        return String.valueOf(bracketType.charAt(0)) + "%value%" + bracketType.charAt(1);
    }
    
    public abstract Object getValue();
    
    public Object getDummyValue() {
        return null;
    }
}
