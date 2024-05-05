package com.initial.modules.impl.visual;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import net.minecraft.util.*;
import com.initial.font.*;
import com.initial.events.impl.*;
import net.minecraft.client.renderer.*;
import com.initial.*;
import net.minecraft.potion.*;
import net.minecraft.client.resources.*;
import com.mojang.realmsclient.gui.*;
import com.initial.utils.movement.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import com.initial.events.*;
import java.awt.*;
import com.initial.utils.astolfo.*;
import com.initial.utils.render.*;
import net.minecraft.client.gui.*;
import java.util.*;

public class HUD extends Module
{
    public ModeSet hudMode;
    public ModeSet mode;
    public BooleanSet clientName;
    public BooleanSet font;
    public DoubleSet red;
    public DoubleSet green;
    public DoubleSet blue;
    public static int colorr;
    float astolfoPotionCount;
    String astolfoCoords;
    MCFontRenderer logo;
    MCFontRenderer edition;
    
    public HUD() {
        super("HUD", 0, Category.VISUAL);
        this.hudMode = new ModeSet("Mode", "Default", new String[] { "Default", "Astolfo" });
        this.mode = new ModeSet("Color Mode", "Static", new String[] { "Static", "Dynamic", "Astolfo", "Rainbow" });
        this.clientName = new BooleanSet("Client Name", true);
        this.font = new BooleanSet("Font", false);
        this.red = new DoubleSet("Red", 255.0, 0.0, 255.0, 1.0);
        this.green = new DoubleSet("Green", 255.0, 0.0, 255.0, 1.0);
        this.blue = new DoubleSet("Blue", 255.0, 0.0, 255.0, 1.0);
        this.addSettings(this.hudMode, this.mode, this.clientName, this.font, this.red, this.green, this.blue);
        this.logo = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Desync/fonts/niggas.ttf"), 18.0f, 0), true, true);
        this.edition = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Desync/fonts/niggas.ttf"), 13.0f, 0), true, true);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D e) {
        if (!this.mc.gameSettings.showDebugInfo) {
            GlStateManager.pushMatrix();
            final ScaledResolution src = new ScaledResolution(this.mc);
            final FontRenderer fr = this.mc.fontRendererObj;
            final float height = (float)e.getHeight();
            if (this.font.isEnabled()) {
                final String mode = this.hudMode.getMode();
                switch (mode) {
                    case "Default":
                    case "Astolfo": {
                        if (this.clientName.isEnabled()) {
                            if (!this.clientName.isEnabled()) {
                                return;
                            }
                            this.logo.drawStringWithShadow(Astomero.instance.name, 1.4f, 3.0f, -1);
                        }
                        final float width = (float)e.getWidth();
                        this.astolfoPotionCount = 17.0f;
                        for (final PotionEffect potionEffect : this.mc.thePlayer.getActivePotionEffects()) {
                            final Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                            final String potionText = I18n.format(potion.getName(), new Object[0]) + ChatFormatting.WHITE + " " + (potionEffect.getAmplifier() + 1) + ": " + ChatFormatting.GRAY + Potion.getDurationString(potionEffect);
                            final int potColor = this.getPotionColor(potion);
                            this.astolfoPotionCount += 9.0f;
                        }
                        this.astolfoCoords = String.valueOf(Math.round(this.mc.thePlayer.posX)) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Math.round(this.mc.thePlayer.posY) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Math.round(this.mc.thePlayer.posZ);
                        final NetworkPlayerInfo you = this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID());
                        this.logo.drawStringWithShadow((int)Math.round(MovementUtils.getBlocksPerSecond() * 100.0) / 100.0 + " blocks/sec", (float)(src.getScaledWidth() - 958), (float)(src.getScaledHeight() - 20 - -(Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen() ? -4 : 9)), -1);
                        break;
                    }
                }
                GlStateManager.popMatrix();
                this.renderArrayListFont();
            }
            else {
                final String mode2 = this.hudMode.getMode();
                switch (mode2) {
                    case "Default":
                    case "Astolfo": {
                        if (this.clientName.isEnabled()) {
                            if (!this.clientName.isEnabled()) {
                                return;
                            }
                            fr.drawStringWithShadow(Astomero.instance.name, 1.399999976158142, 3.0, -1);
                        }
                        final float width = (float)e.getWidth();
                        this.astolfoPotionCount = 17.0f;
                        for (final PotionEffect potionEffect : this.mc.thePlayer.getActivePotionEffects()) {
                            final Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                            final String potionText = I18n.format(potion.getName(), new Object[0]) + ChatFormatting.WHITE + " " + (potionEffect.getAmplifier() + 1) + ": " + ChatFormatting.GRAY + Potion.getDurationString(potionEffect);
                            final int potColor = this.getPotionColor(potion);
                            this.astolfoPotionCount += 9.0f;
                        }
                        this.astolfoCoords = String.valueOf(Math.round(this.mc.thePlayer.posX)) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Math.round(this.mc.thePlayer.posY) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Math.round(this.mc.thePlayer.posZ);
                        final NetworkPlayerInfo you = this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID());
                        fr.drawStringWithShadow((int)Math.round(MovementUtils.getBlocksPerSecond() * 100.0) / 100.0 + " blocks/sec", src.getScaledWidth() - 958, src.getScaledHeight() - 20 - -(Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen() ? -4 : 9), -1);
                        break;
                    }
                }
                GlStateManager.popMatrix();
                this.renderArrayListNoFont();
            }
        }
    }
    
    public int getPotionColor(final Potion potion) {
        int potColor = new Color(-1).getRGB();
        if (potion == Potion.moveSpeed) {
            potColor = new Color(9753087).getRGB();
        }
        else if (potion == Potion.fireResistance) {
            potColor = new Color(16750979).getRGB();
        }
        else if (potion == Potion.damageBoost) {
            potColor = new Color(16748945).getRGB();
        }
        else if (potion == Potion.regeneration) {
            potColor = new Color(16748747).getRGB();
        }
        else if (potion == Potion.nightVision) {
            potColor = new Color(11509503).getRGB();
        }
        else if (potion == Potion.jump) {
            potColor = new Color(9568147).getRGB();
        }
        else if (potion == Potion.resistance) {
            potColor = new Color(10266879).getRGB();
        }
        else if (potion == Potion.absorption) {
            potColor = new Color(16772007).getRGB();
        }
        else if (potion == Potion.digSpeed) {
            potColor = new Color(16300287).getRGB();
        }
        return potColor;
    }
    
    public void renderArrayListNoFont() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = this.mc.fontRendererObj;
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        final int yPos = 0;
        int count = 0;
        int y = 2;
        Astomero.instance.moduleManager.getModules().sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getDisplayName())).reversed());
        for (final Module i : Astomero.instance.moduleManager.getModules()) {
            if (!i.isToggled()) {
                continue;
            }
            if (!i.isVisible()) {
                continue;
            }
            final double offset = count * (fr.FONT_HEIGHT + 0.0);
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Static": {
                    final int dr = (int)Math.round(this.red.getValue());
                    final int dg = (int)Math.round(this.blue.getValue());
                    final int db = (int)Math.round(this.green.getValue());
                    final int dr2 = Math.max(Math.round(dr * 0.5f), 10);
                    final int dg2 = Math.max(Math.round(dg * 0.5f), 10);
                    final int db2 = Math.max(Math.round(db * 0.5f), 10);
                    HUD.colorr = new Color(dr, dg, db).getRGB();
                    break;
                }
                case "Dynamic": {
                    final int dr = (int)Math.round(this.red.getValue());
                    final int dg = (int)Math.round(this.blue.getValue());
                    final int db = (int)Math.round(this.green.getValue());
                    final int dr2 = Math.max(Math.round(dr * 0.5f), 10);
                    final int dg2 = Math.max(Math.round(dg * 0.5f), 10);
                    final int db2 = Math.max(Math.round(db * 0.5f), 10);
                    HUD.colorr = Palette.fade(new Color(dr, dg, db), 100, count + 17 + 10).getRGB();
                    break;
                }
                case "Astolfo": {
                    HUD.colorr = AstolfoUtils.rainbow(count * -100, 1.0f, 0.47f);
                    break;
                }
                case "Rainbow": {
                    HUD.colorr = ColorUtil.getRainbow(2.0f, 0.47f, 1.0f, count * -100);
                    break;
                }
            }
            Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(i.getDisplayName()) - 3.4, offset, sr.getScaledWidth(), -0.2 + fr.FONT_HEIGHT + offset, new Color(0, 0, 0, 50).getRGB());
            fr.drawStringWithShadow(i.getDisplayName(), sr.getScaledWidth() - fr.getStringWidth(i.getDisplayName()) - 1, (float)(1.7 + offset), HUD.colorr);
            y += 10;
            ++count;
        }
    }
    
    public void renderArrayListFont() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = this.mc.fontRendererObj;
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        final float yCount = -17.0f;
        int count = 0;
        int y = 2;
        Collections.sort(Astomero.instance.moduleManager.getModules(), new ModuleComparatorFont(this.logo).reversed());
        for (final Module m : Astomero.instance.moduleManager.getModules()) {
            if (m.isToggled() && m.isBeingEnabled()) {
                if (!m.isVisible()) {
                    continue;
                }
                final double offset = count * (fr.FONT_HEIGHT - 0.5);
                final String mode = this.mode.getMode();
                switch (mode) {
                    case "Static": {
                        final int dr = (int)Math.round(this.red.getValue());
                        final int dg = (int)Math.round(this.blue.getValue());
                        final int db = (int)Math.round(this.green.getValue());
                        final int dr2 = Math.max(Math.round(dr * 0.5f), 10);
                        final int dg2 = Math.max(Math.round(dg * 0.5f), 10);
                        final int db2 = Math.max(Math.round(db * 0.5f), 10);
                        HUD.colorr = new Color(dr, dg, db).getRGB();
                        break;
                    }
                    case "Dynamic": {
                        final int dr = (int)Math.round(this.red.getValue());
                        final int dg = (int)Math.round(this.blue.getValue());
                        final int db = (int)Math.round(this.green.getValue());
                        final int dr2 = Math.max(Math.round(dr * 0.5f), 10);
                        final int dg2 = Math.max(Math.round(dg * 0.5f), 10);
                        final int db2 = Math.max(Math.round(db * 0.5f), 10);
                        HUD.colorr = Palette.fade(new Color(dr, dg, db), 100, count + 17 + 10).getRGB();
                        break;
                    }
                    case "Astolfo": {
                        HUD.colorr = AstolfoUtils.rainbow(count * -100, 1.0f, 0.47f);
                        break;
                    }
                    case "Rainbow": {
                        HUD.colorr = ColorUtil.getRainbow(2.0f, 0.47f, 1.0f, count * -100);
                        break;
                    }
                }
                Gui.drawRect(sr.getScaledWidth() - this.logo.getStringWidth(m.getDisplayName()) - 3.4, offset, sr.getScaledWidth(), -0.5 + fr.FONT_HEIGHT + offset, new Color(0, 0, 0, 50).getRGB());
                this.logo.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - this.logo.getStringWidth(m.getDisplayName()) - 1), (float)(1.7 + offset), HUD.colorr);
                y += 10;
                ++count;
            }
        }
    }
    
    public double getAnimationOutput(final Module m, final boolean beingEnabled) {
        double outPut = 0.0;
        if (beingEnabled) {
            outPut = m.getTransition() - m.getTransition() * 0.1;
        }
        else {
            outPut = m.getTransition() + 0.1 + m.getTransition() * 0.1;
        }
        return outPut;
    }
    
    public class ModuleComparatorFont implements Comparator<Module>
    {
        MCFontRenderer f2;
        
        public ModuleComparatorFont(final MCFontRenderer f) {
            this.f2 = f;
        }
        
        @Override
        public int compare(final Module arg0, final Module arg1) {
            if (this.f2.getStringWidth(arg0.getDisplayName()) < this.f2.getStringWidth(arg1.getDisplayName())) {
                return -1;
            }
            if (this.f2.getStringWidth(arg0.getDisplayName()) > this.f2.getStringWidth(arg1.getDisplayName())) {
                return 1;
            }
            return 0;
        }
    }
}
