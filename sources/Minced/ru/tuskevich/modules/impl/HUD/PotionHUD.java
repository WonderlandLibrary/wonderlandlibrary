// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.HUD;

import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import java.util.List;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.resources.I18n;
import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import ru.tuskevich.util.render.GlowUtility;
import java.util.Comparator;
import java.util.Objects;
import net.optifine.CustomColors;
import net.minecraft.potion.Potion;
import java.util.Collection;
import net.minecraft.potion.PotionEffect;
import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.util.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.Minced;
import ru.tuskevich.util.drag.Dragging;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "PotionHUD", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.HUD)
public class PotionHUD extends Module
{
    int posX;
    int posY;
    public final Dragging potionHUDDrag;
    
    public PotionHUD() {
        this.potionHUDDrag = Minced.getInstance().createDrag(this, "potionHUDDrag", 200.0f, 200.0f);
    }
    
    @EventTarget
    public void onRender(final EventDisplay eventDisplay) {
        final Minecraft mc = PotionHUD.mc;
        if (Minecraft.player == null || PotionHUD.mc.world == null) {
            return;
        }
        final ScaledResolution sr = new ScaledResolution(PotionHUD.mc);
        final float width = (float)(90 + Fonts.Nunito14.getStringWidth(this.name));
        float height = 15.0f;
        this.potionHUDDrag.setWidth(width);
        this.potionHUDDrag.setHeight(height);
        this.posX = (int)this.potionHUDDrag.getX();
        this.posY = (int)this.potionHUDDrag.getY();
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        final Minecraft mc2 = PotionHUD.mc;
        final List<PotionEffect> potions = new ArrayList<PotionEffect>(Minecraft.player.getActivePotionEffects());
        potions.sort(Comparator.comparingDouble(effect -> PotionHUD.mc.fontRenderer.getStringWidth(Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName())));
        for (final PotionEffect potion : potions) {
            height = (float)(potions.size() * 13);
        }
        if (potions.isEmpty()) {
            height = 0.0f;
        }
        if (Hud.arrayListElements.get(0)) {
            GlowUtility.drawGlow((float)this.posX, (float)this.posY, 100.0f, 11.0f, 15, Hud.getColor(280));
        }
        RenderUtility.drawRound((float)this.posX, (float)this.posY, 100.0f, height + 11.0f, 2.0f, new Color(21, 21, 21, 200));
        RenderUtility.drawGradientRound((float)(this.posX - 1), (float)(this.posY - 1), 102.0f, 13.0f, 3.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
        RenderUtility.drawRound((float)this.posX, (float)this.posY, 100.0f, 11.0f, 2.0f, new Color(25, 25, 25, 255));
        Fonts.Nunito14.drawCenteredString("Potion Info", (float)(this.posX + 1.5) + 50.0f, this.posY - 13.0f + 2.5f + 14.0f, -1);
        Fonts.icons20.drawCenteredString("q", (float)(this.posX + 7), this.posY - 13.0f + 2.5f + 14.0f, Hud.onecolor.getColorValue());
        for (final PotionEffect potion : potions) {
            final Potion effect2 = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
            String level = I18n.format(effect2.getName(), new Object[0]);
            if (potion.getAmplifier() == 1) {
                level = level + " " + I18n.format("enchantment.level.2", new Object[0]);
            }
            else if (potion.getAmplifier() == 2) {
                level = level + " " + I18n.format("enchantment.level.3", new Object[0]);
            }
            else if (potion.getAmplifier() == 3) {
                level = level + " " + I18n.format("enchantment.level.4", new Object[0]);
            }
            int getPotionColor = -1;
            if (potion.getDuration() < 200) {
                getPotionColor = new Color(255, 103, 32).getRGB();
            }
            else if (potion.getDuration() < 400) {
                getPotionColor = new Color(231, 143, 32).getRGB();
            }
            else if (potion.getDuration() > 400) {
                getPotionColor = new Color(255, 255, 255).getRGB();
            }
            final String durationString = Potion.getDurationString(potion);
            Fonts.Nunito13.drawString(ChatFormatting.WHITE + level + " -> " + ChatFormatting.RESET + durationString, (float)(this.posX + 3), (float)(this.posY + (potions.indexOf(potion) + 1) * 12 + 5), Hud.getColor(280).getRGB());
            if (PotionHUD.mc.world == null) {
                potions.remove(potions.indexOf(potion));
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }
}
