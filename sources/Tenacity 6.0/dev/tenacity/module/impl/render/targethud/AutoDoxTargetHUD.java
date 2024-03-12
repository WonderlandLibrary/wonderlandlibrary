// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import dev.tenacity.utils.font.CustomFont;
import net.minecraft.client.gui.inventory.GuiInventory;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.module.impl.render.TargetHUDMod;
import dev.tenacity.utils.render.StencilUtil;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import java.util.HashMap;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.objects.PlayerDox;
import net.minecraft.entity.EntityLivingBase;
import java.util.Map;

public class AutoDoxTargetHUD extends TargetHUD
{
    public Map<EntityLivingBase, PlayerDox> doxMap;
    public final PlayerDox thePlayerDox;
    private final ContinualAnimation animatedHealthBar;
    
    public AutoDoxTargetHUD() {
        super("Auto-Dox");
        this.doxMap = new HashMap<EntityLivingBase, PlayerDox>();
        this.thePlayerDox = new PlayerDox(AutoDoxTargetHUD.mc.thePlayer);
        this.animatedHealthBar = new ContinualAnimation();
    }
    
    @Override
    public void render(final float x, final float y, final float alpha, final EntityLivingBase target) {
        final PlayerDox dox = this.doxMap.get(target);
        if (dox == null) {
            return;
        }
        final String state = dox.getState();
        final CustomFont tahomaBoldFont27 = AutoDoxTargetHUD.tahomaFont.boldSize(27);
        final CustomFont tahomaBoldFont28 = AutoDoxTargetHUD.tahomaFont.boldSize(16);
        final CustomFont tahomaFont12 = AutoDoxTargetHUD.tahomaFont.size(12);
        final CustomFont tahomaBoldFont29 = AutoDoxTargetHUD.tahomaFont.boldSize(10);
        final CustomFont tahomaBoldFont30 = AutoDoxTargetHUD.tahomaFont.boldSize(14);
        this.setWidth(Math.max(160.0f, tahomaBoldFont27.getStringWidth(state) + 40.0f + tahomaBoldFont28.getStringWidth("DRIVER LICENSE")));
        this.setHeight(80.0f);
        final Color BLUE = new Color(40, 40, 160, (int)(alpha * 255.0f));
        final Color RED = new Color(220, 75, 60, (int)(alpha * 255.0f));
        final Color BLACK = ColorUtil.applyOpacity(new Color(40, 40, 80), alpha);
        RoundedUtil.drawRound(x, y, this.getWidth(), this.getHeight(), 3.0f, ColorUtil.applyOpacity(new Color(212, 203, 178), alpha));
        final Color goldColor = new Color(255, 221, 0, (int)(alpha * 255.0f));
        Gui.drawRect2(x + 10.0f, y + 3.0f + tahomaBoldFont27.getHeight(), this.getWidth() - 25.0f, 0.5, goldColor.getRGB());
        Gui.drawRect2(x + 60.0f, y + 6.0f + tahomaBoldFont27.getHeight(), this.getWidth() - 75.0f, 0.5, goldColor.getRGB());
        RenderUtil.resetColor();
        tahomaBoldFont27.drawString(state, x + 5.0f, y + 3.0f, BLUE);
        RenderUtil.resetColor();
        tahomaFont12.drawString("USA", x + 5.0f + tahomaBoldFont27.getStringWidth(state), y + 4.0f, BLUE);
        RenderUtil.resetColor();
        tahomaBoldFont28.drawString("DRIVER LICENSE", x + this.getWidth() - (15.0f + tahomaBoldFont28.getStringWidth("DRIVER LICENSE")), y + 5.0f, BLUE);
        Gui.drawRect2(x + 5.0f, y + 5.0f + tahomaBoldFont27.getHeight(), 45.0, this.getHeight() - (20 + tahomaBoldFont27.getHeight()), new Color(152, 210, 224, (int)(alpha * 255.0f)).getRGB());
        final float textX = x + 53.0f;
        final float textY = y + 13.0f + tahomaBoldFont27.getHeight();
        tahomaBoldFont29.drawString("DL", textX, textY, BLACK);
        tahomaBoldFont28.drawString(dox.getLiscenseNumber(), textX + 9.0f, textY - 3.0f, RED);
        tahomaBoldFont29.drawString("EXP", textX, textY + 9.0f, BLACK);
        RenderUtil.resetColor();
        tahomaBoldFont30.drawString(dox.getExpirationDate(), textX + 11.0f, textY + 7.0f, RED);
        tahomaBoldFont29.drawString("FN", textX, textY + 17.5f, BLACK);
        RenderUtil.resetColor();
        tahomaBoldFont30.drawString(target.getName(), textX + 10.0f, textY + 15.5f, BLACK);
        RenderUtil.resetColor();
        tahomaBoldFont29.drawString(dox.getTopAddress(), textX, textY + 23.0f, BLACK);
        RenderUtil.resetColor();
        tahomaBoldFont29.drawString(dox.getBottomAddress(), textX, textY + 28.0f, BLACK);
        RenderUtil.resetColor();
        tahomaBoldFont29.drawString("DOB", textX, textY + 36.0f, BLUE);
        RenderUtil.resetColor();
        tahomaBoldFont30.drawString(dox.getDOB(), textX + 12.0f, textY + 34.0f, RED);
        tahomaBoldFont29.drawString("CLASS C", x + this.getWidth() - (15.0f + tahomaBoldFont29.getStringWidth("CLASS C")), y + 28.0f, BLACK);
        RenderUtil.resetColor();
        tahomaBoldFont29.drawString("SEX " + (dox.isMale() ? "M" : "F"), x + this.getWidth() - (15.0f + tahomaBoldFont29.getStringWidth("CLASS C")), y + 35.0f, BLACK);
        RenderUtil.resetColor();
        tahomaBoldFont29.drawString("WGT 300 lb", x + this.getWidth() - (15.0f + tahomaBoldFont29.getStringWidth("CLASS C")), y + 42.0f, BLACK);
        RenderUtil.resetColor();
        final float size = 45.0f;
        StencilUtil.initStencilToWrite();
        Gui.drawRect2(x + this.getWidth() - 45.0f, y + this.getHeight() - 30.0f, 22.0, 18.0, -1);
        Gui.drawRect2(x + 5.0f, y + 5.0f + tahomaBoldFont27.getHeight(), 45.0, this.getHeight() - (20 + tahomaBoldFont27.getHeight()), new Color(152, 210, 224, (int)(alpha * 255.0f)).getRGB());
        StencilUtil.readStencilBuffer(1);
        RenderUtil.resetColor();
        TargetHUDMod.renderLayers = false;
        RenderUtil.color(-1, alpha);
        GLUtil.startBlend();
        GuiInventory.drawEntityOnScreen((int)(x + 5.0f + 22.5f), (int)(y + 5.0f + tahomaBoldFont27.getHeight() + 50.0f + size), (int)size, 0.0f, 0.0f, target);
        Gui.drawRect2(x + this.getWidth() - 45.0f, y + this.getHeight() - 30.0f, 20.0, 18.0, new Color(150, 150, 150, (int)alpha * 120).getRGB());
        GLUtil.startBlend();
        RenderUtil.color(Color.GRAY.getRGB(), alpha * 0.5f);
        GuiInventory.drawEntityOnScreen((int)(x + this.getWidth() - 35.0f), (int)(y + this.getHeight() + 8.0f), 18, 0.0f, 0.0f, target);
        Gui.drawRect2(x + this.getWidth() - 45.0f, y + this.getHeight() - 30.0f, 20.0, 18.0, ColorUtil.tripleColor(150, alpha).getRGB());
        TargetHUDMod.renderLayers = true;
        StencilUtil.uninitStencilBuffer();
        final float healthbarWidth = this.getWidth() - 10.0f;
        final float healthPercent = (target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount());
        final float var = healthbarWidth * healthPercent;
        this.animatedHealthBar.animate(var, 18);
        Gui.drawRect2(x + 5.0f, y + this.getHeight() - 9.0f, healthbarWidth, 5.0, ColorUtil.applyOpacity(BLACK, 0.5f).getRGB());
        Gui.drawRect2(x + 5.0f, y + this.getHeight() - 9.0f, this.animatedHealthBar.getOutput(), 5.0, BLUE.getRGB());
    }
    
    @Override
    public void renderEffects(final float x, final float y, final float alpha, final boolean glow) {
        final Color color = glow ? new Color(212, 203, 178) : Color.BLACK;
        RoundedUtil.drawRound(x, y, this.getWidth(), this.getHeight(), 3.0f, ColorUtil.applyOpacity(color, alpha));
    }
}
