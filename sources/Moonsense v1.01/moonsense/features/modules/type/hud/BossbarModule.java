// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCAbstractRenderModule;

public class BossbarModule extends SCAbstractRenderModule
{
    private final Setting renderHealth;
    private final Setting useCustomBossBar;
    private final Setting textShadow;
    private final Setting bossNameColor;
    private final Setting bossBarColor;
    
    public BossbarModule() {
        super("Bossbar", "Customize and move the Minecraft bossbar.");
        this.renderHealth = new Setting(this, "Render Health").setDefault(true);
        this.useCustomBossBar = new Setting(this, "Use Custom Bossbar").setDefault(true);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(true);
        this.bossNameColor = new Setting(this, "Boss Name Color").setDefault(Color.white.getRGB(), 0);
        this.bossBarColor = new Setting(this, "Bossbar Color").setDefault(new Color(242, 73, 242, 255).getRGB(), 0);
    }
    
    @Override
    public int getWidth() {
        return 182;
    }
    
    @Override
    public int getHeight() {
        return 17;
    }
    
    @Override
    public void render(final float x, final float y) {
        this.renderBossHealth(x, y);
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final FontRenderer var1 = this.mc.fontRendererObj;
        final ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int var3 = var2.getScaledWidth();
        final short var4 = 182;
        final int var5 = var3 / 2 - var4 / 2;
        final int var6 = 182;
        final byte var7 = 12;
        if (this.useCustomBossBar.getBoolean()) {
            if (this.renderHealth.getBoolean()) {
                GuiUtils.setGlColor(SCModule.getColor(this.bossBarColor.getColorObject()));
                this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
                this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
            }
        }
        else if (this.renderHealth.getBoolean()) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
            this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
        }
        if (var6 > 0 && this.renderHealth.getBoolean()) {
            this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 79.0f, (float)var6, 5.0f);
        }
        final String var8 = "Ender Dragon";
        int bossTextColor = 16777215;
        if (this.useCustomBossBar.getBoolean()) {
            bossTextColor = SCModule.getColor(this.bossNameColor.getColorObject());
        }
        final boolean ts = !this.useCustomBossBar.getBoolean() || this.textShadow.getBoolean();
        this.mc.fontRendererObj.drawString(var8, (float)(x + 0.5 * this.getWidth() - 0.5 * this.mc.fontRendererObj.getStringWidth(var8)), y + (var7 - 10), bossTextColor, ts);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Gui.icons);
    }
    
    public void renderBossHealth(final float x, final float y) {
        this.mc.getTextureManager().bindTexture(Gui.icons);
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            final FontRenderer var1 = this.mc.fontRendererObj;
            final ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            final int var3 = var2.getScaledWidth();
            final short var4 = 182;
            final int var5 = var3 / 2 - var4 / 2;
            final int var6 = (int)(BossStatus.healthScale * (var4 + 1)) - 1;
            final byte var7 = 12;
            if (this.useCustomBossBar.getBoolean()) {
                if (this.renderHealth.getBoolean()) {
                    GuiUtils.setGlColor(SCModule.getColor(this.bossBarColor.getColorObject()));
                    this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
                    this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
                }
            }
            else {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
                this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 74.0f, var4, 5.0f);
            }
            if (var6 > 0) {
                this.mc.ingameGUI.drawTexturedModalRect(x, y + var7, 0.0f, 79.0f, (float)var6, 5.0f);
            }
            final String var8 = BossStatus.bossName;
            int bossTextColor = 16777215;
            if (this.useCustomBossBar.getBoolean()) {
                bossTextColor = SCModule.getColor(this.bossNameColor.getColorObject());
            }
            final boolean ts = !this.useCustomBossBar.getBoolean() || this.textShadow.getBoolean();
            this.mc.fontRendererObj.drawString(var8, (float)(x + 0.5 * this.getWidth() - 0.5 * this.mc.fontRendererObj.getStringWidth(var8)), y + (var7 - 10), bossTextColor, ts);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(Gui.icons);
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_CENTER;
    }
}
