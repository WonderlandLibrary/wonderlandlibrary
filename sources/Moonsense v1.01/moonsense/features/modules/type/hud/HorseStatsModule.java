// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import moonsense.config.utils.AnchorPoint;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import moonsense.ui.screen.settings.GuiHUDEditor;
import java.awt.Color;
import moonsense.features.SCModule;
import java.text.DecimalFormat;
import moonsense.settings.Setting;
import moonsense.features.SCAbstractRenderModule;

public class HorseStatsModule extends SCAbstractRenderModule
{
    private int width;
    private int height;
    private final Setting background;
    private final Setting backgroundRadius;
    private final Setting backgroundColor;
    private final Setting backgroundWidth;
    private final Setting backgroundHeight;
    private final Setting textShadow;
    private final Setting textColor;
    private final Setting borderColor;
    private final Setting borderWidth;
    private final Setting border;
    private final DecimalFormat df;
    
    public HorseStatsModule() {
        super("Horse Stats", "No description set!");
        this.df = new DecimalFormat("0.0");
        this.background = new Setting(this, "Background").setDefault(true);
        this.backgroundRadius = new Setting(this, "Background Radius").setDefault(0.0f).setRange(0.0f, 5.0f, 0.01f);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(false);
        this.backgroundWidth = new Setting(this, "Background Width").setDefault(5).setRange(2, 20, 1);
        this.backgroundHeight = new Setting(this, "Background Height").setDefault(5).setRange(2, 12, 1);
        this.border = new Setting(this, "Border").setDefault(false);
        this.borderWidth = new Setting(this, "Border Thickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f);
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
        if (this.getValue() != null || (this.mc.currentScreen instanceof GuiHUDEditor && this.getDummyValue() != null)) {
            final String t = String.valueOf((this.getValue() != null) ? this.getValue() : this.getDummyValue());
            final String text1 = t.split("\n")[0];
            final String text2 = t.split("\n")[1];
            this.width = Math.max(this.mc.fontRendererObj.getStringWidth(text1), this.mc.fontRendererObj.getStringWidth(text2));
            this.height = this.mc.fontRendererObj.FONT_HEIGHT * 2;
            if (this.background.getBoolean()) {
                this.width += this.backgroundWidth.getInt() * 2;
                this.height += this.backgroundHeight.getInt() * 2 - 2;
                this.renderBackground(x + this.width / 2.0f, y + this.height / 2.0f - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f);
                this.drawCenteredString(text1, x + this.width / 2.0f, y + this.height / 2.0f - (this.mc.fontRendererObj.FONT_HEIGHT - 2) / 2.0f - this.mc.fontRendererObj.FONT_HEIGHT / 2, this.textColor.getColorObject(), this.textShadow.getBoolean());
                this.drawCenteredString(text2, x + this.width / 2.0f, y + this.height / 2.0f - (this.mc.fontRendererObj.FONT_HEIGHT - 2) / 2.0f + this.mc.fontRendererObj.FONT_HEIGHT / 2, this.textColor.getColorObject(), this.textShadow.getBoolean());
            }
            else {
                ++this.width;
                this.drawString(text1, x + 1.0f, y + 1.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
                this.drawString(text2, x + 1.0f, y + 1.0f + this.mc.fontRendererObj.FONT_HEIGHT, this.textColor.getColorObject(), this.textShadow.getBoolean());
            }
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    protected void renderBackground(final float x, final float y) {
        if (this.border.getBoolean()) {
            GuiUtils.drawRoundedOutline(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, this.backgroundRadius.getFloat(), this.borderWidth.getFloat(), SCModule.getColor(this.borderColor.getColorObject()));
        }
        GuiUtils.drawRoundedRect(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, this.backgroundRadius.getFloat(), SCModule.getColor(this.backgroundColor.getColorObject()));
    }
    
    private String getValue() {
        if (!(this.mc.objectMouseOver.entityHit instanceof EntityHorse)) {
            return null;
        }
        final EntityHorse horse = (EntityHorse)this.mc.objectMouseOver.entityHit;
        if (this.mc.playerController.isRidingHorse()) {
            return null;
        }
        return "Speed: " + this.getHorseSpeedRounded(horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()) + " Blocks / Second" + "\n" + "Jump: " + this.df.format(horse.getHorseJumpStrength() * 5.5) + " Blocks";
    }
    
    private String getDummyValue() {
        return "Speed: 5.3 Blocks / Second\nJump: 2.4 Blocks";
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.CENTER_RIGHT;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public boolean isNewModule() {
        return true;
    }
    
    private String getHorseSpeedRounded(final double baseSpeed) {
        final float factor = 43.170372f;
        final float speed = (float)(baseSpeed * 43.170372009277344);
        return this.df.format(speed);
    }
}
