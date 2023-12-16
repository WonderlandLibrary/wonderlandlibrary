// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.EntityCreature;
import net.minecraft.client.gui.GuiIngame;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class CrosshairModule extends SCModule
{
    public static CrosshairModule INSTANCE;
    private final Setting crosshair;
    private final Setting color;
    private final Setting size;
    private final Setting gap;
    private final Setting thickness;
    private final Setting dot;
    private final Setting dotThickness;
    private final Setting dynamicDotColor;
    private final Setting dotColor;
    public final Setting showInThird;
    private final Setting highlightEntities;
    private final Setting dynamicHighlightColors;
    private final Setting defaultHighlightColor;
    private final Setting highlightPassiveEntities;
    private final Setting passiveEntityColor;
    private final Setting highlightHostileEntities;
    private final Setting hostileEntityColor;
    private final Setting highlightPlayers;
    private final Setting playerColor;
    
    public CrosshairModule() {
        super("Crosshair", "Allows you to customize the default Minecraft crosshair.");
        new Setting(this, "Style Options");
        this.crosshair = new Setting(this, "Mode").setDefault(0).setRange("Vanilla", "Cross", "Circle", "Arrow");
        this.color = new Setting(this, "Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.size = new Setting(this, "Size").setDefault(16).setRange(2, 24, 1);
        this.gap = new Setting(this, "Gap").setDefault(4).setRange(0, 32, 1);
        this.thickness = new Setting(this, "Thickness").setDefault(2.0f).setRange(0.5f, 5.0f, 0.1f);
        new Setting(this, "Dot Options");
        this.dot = new Setting(this, "Dot").setDefault(false);
        this.dotThickness = new Setting(this, "Dot Thickness").setDefault(3.0f).setRange(1.0f, 10.0f, 0.01f);
        this.dynamicDotColor = new Setting(this, "Dynamic Dot Color").setDefault(true);
        this.dotColor = new Setting(this, "Dot Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        new Setting(this, "Entity Highlighting Options");
        this.highlightEntities = new Setting(this, "Highlight Entities").setDefault(false);
        this.dynamicHighlightColors = new Setting(this, "Dynamic Highlighting").setDefault(true);
        this.defaultHighlightColor = new Setting(this, "Default Highlight Color").setDefault(Color.red.getRGB(), 0);
        this.highlightPassiveEntities = new Setting(this, "Highlight Passive Entities").setDefault(true);
        this.passiveEntityColor = new Setting(this, "Passive Entity Color").setDefault(Color.green.getRGB(), 0);
        this.highlightHostileEntities = new Setting(this, "Highlight Hostile Entities").setDefault(true);
        this.hostileEntityColor = new Setting(this, "Hostile Entity Color").setDefault(Color.red.getRGB(), 0);
        this.highlightPlayers = new Setting(this, "Highlight Players").setDefault(true);
        this.playerColor = new Setting(this, "Player Color").setDefault(Color.yellow.getRGB(), 0);
        new Setting(this, "Other Options");
        this.showInThird = new Setting(this, "Show in third person view").setDefault(false);
        CrosshairModule.INSTANCE = this;
    }
    
    public void render(final GuiIngame gui, final int x, final int y, final int i, final int j) {
        int color = this.color.getColor();
        if (this.highlightEntities.getBoolean()) {
            final Entity e = this.mc.pointedEntity;
            if (this.dynamicHighlightColors.getBoolean()) {
                if (e instanceof EntityCreature && this.highlightPassiveEntities.getBoolean()) {
                    color = this.passiveEntityColor.getColor();
                }
                if (e instanceof EntityMob && this.highlightHostileEntities.getBoolean()) {
                    color = this.hostileEntityColor.getColor();
                }
                if (e instanceof EntityPlayer && this.highlightPlayers.getBoolean()) {
                    color = this.playerColor.getColor();
                }
            }
            else if (e != null) {
                color = this.defaultHighlightColor.getColor();
            }
        }
        final int size = this.size.getInt();
        final int gap = this.gap.getInt();
        float thickness = this.thickness.getFloat();
        if (this.dot.getBoolean() && this.crosshair.getInt() != 0) {
            int dc = this.dotColor.getColor();
            if (this.highlightEntities.getBoolean() && this.dynamicDotColor.getBoolean()) {
                final Entity e2 = this.mc.pointedEntity;
                if (this.dynamicHighlightColors.getBoolean()) {
                    if (e2 instanceof EntityCreature && this.highlightPassiveEntities.getBoolean()) {
                        dc = this.passiveEntityColor.getColor();
                    }
                    if (e2 instanceof EntityMob && this.highlightHostileEntities.getBoolean()) {
                        dc = this.hostileEntityColor.getColor();
                    }
                    if (e2 instanceof EntityPlayer && this.highlightPlayers.getBoolean()) {
                        dc = this.playerColor.getColor();
                    }
                }
                else if (e2 != null) {
                    dc = this.defaultHighlightColor.getColor();
                }
            }
            GuiUtils.drawDot(x - 0.1f, (float)y, this.dotThickness.getFloat(), dc);
        }
        switch (this.crosshair.getInt()) {
            case 0: {
                GuiUtils.setGlColor(color);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
                GlStateManager.enableAlpha();
                gui.drawTexturedModalRect(i - 7, j - 7, 0, 0, 16, 16);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.disableBlend();
                break;
            }
            case 1: {
                thickness /= 2.0f;
                GuiUtils.drawFilledRectangle(x - thickness, (float)(y - gap - size), x + thickness, (float)(y - gap), color, true);
                GuiUtils.drawFilledRectangle(x - thickness, (float)(y + gap), x + thickness, (float)(y + gap + size), color, true);
                GuiUtils.drawFilledRectangle((float)(x - gap - size), y - thickness, (float)(x - gap), y + thickness, color, true);
                GuiUtils.drawFilledRectangle((float)(x + gap), y - thickness, (float)(x + gap + size), y + thickness, color, true);
                break;
            }
            case 2: {
                GuiUtils.drawTorus(x, y, (float)gap, gap + thickness, color, true);
                break;
            }
            case 3: {
                GuiUtils.drawLines(new float[] { (float)(x - size), (float)(y + size), (float)x, (float)y, (float)x, (float)y, (float)(x + size), (float)(y + size) }, thickness, color, true);
                break;
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
}
