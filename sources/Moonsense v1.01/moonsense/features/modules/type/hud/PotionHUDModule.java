// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import java.util.ArrayList;
import java.util.Collections;
import moonsense.ui.screen.settings.GuiHUDEditor;
import net.minecraft.potion.PotionEffect;
import java.util.Collection;
import java.awt.Color;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import net.minecraft.util.ResourceLocation;
import moonsense.features.SCAbstractRenderModule;

public class PotionHUDModule extends SCAbstractRenderModule
{
    private final ResourceLocation inventoryBackground;
    private int width;
    private int height;
    private final Setting effectName;
    private final Setting shadow;
    private final Setting blink;
    private final Setting blinkDuration;
    private final Setting blinkSpeed;
    private final Setting textColor;
    private final Setting durationColor;
    private final Setting permanentEffects;
    private final Setting speed;
    private final Setting slowness;
    private final Setting strength;
    private final Setting jumpBoost;
    private final Setting regeneration;
    private final Setting fireResistance;
    private final Setting waterBreathing;
    private final Setting nightVision;
    private final Setting weakness;
    private final Setting poison;
    private final Setting haste;
    private final Setting invisibility;
    
    public PotionHUDModule() {
        super("Potion HUD", "Display your currently active effects on the HUD.", 20);
        this.inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
        this.effectName = new Setting(this, "Effect Name").setDefault(true);
        this.shadow = new Setting(this, "Text Shadow").setDefault(true);
        new Setting(this, "Blink Options");
        this.blink = new Setting(this, "Blink").setDefault(true);
        this.blinkDuration = new Setting(this, "Blink Duration (seconds)").setDefault(10).setRange(2, 20, 1);
        this.blinkSpeed = new Setting(this, "Blink Speed (seconds)").setDefault(1.0f).setRange(0.1f, 2.0f, 0.1f);
        new Setting(this, "Color Options");
        this.textColor = new Setting(this, "Text Color").setDefault(new Color(16777215).getRGB(), 0);
        this.durationColor = new Setting(this, "Duration Color").setDefault(new Color(8355711).getRGB(), 0);
        new Setting(this, "Exclude Potion Effects");
        this.permanentEffects = new Setting(this, "Permanent Effects").setDefault(false);
        this.speed = new Setting(this, "Speed").setDefault(false);
        this.slowness = new Setting(this, "Slowness").setDefault(false);
        this.strength = new Setting(this, "Strength").setDefault(false);
        this.jumpBoost = new Setting(this, "Jump Boost").setDefault(false);
        this.regeneration = new Setting(this, "Regeneration").setDefault(false);
        this.fireResistance = new Setting(this, "Fire Resistance").setDefault(false);
        this.waterBreathing = new Setting(this, "Water Breathing").setDefault(false);
        this.nightVision = new Setting(this, "Night Vision").setDefault(false);
        this.weakness = new Setting(this, "Weakness").setDefault(false);
        this.poison = new Setting(this, "Poison").setDefault(false);
        this.haste = new Setting(this, "Haste").setDefault(false);
        this.invisibility = new Setting(this, "Invisibility").setDefault(false);
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
        if (this.mc.thePlayer.getActivePotionEffects() != null && this.mc.thePlayer.getActivePotionEffects().size() > 0) {
            this.render(this.mc.thePlayer.getActivePotionEffects(), x, y, false);
        }
        else if (this.mc.currentScreen instanceof GuiHUDEditor) {
            this.renderDummy(x, y);
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        final Collection<PotionEffect> potionEffects = new ArrayList<PotionEffect>((Collection<? extends PotionEffect>)Collections.emptySet());
        potionEffects.add(new PotionEffect(Potion.absorption.id, 900));
        potionEffects.add(new PotionEffect(Potion.moveSpeed.id, 20));
        this.width = 90;
        this.height = 44;
        this.render(potionEffects, x, y, true);
    }
    
    private void render(final Collection<PotionEffect> potionEffects, final float x, final float y, final boolean isDummy) {
        if (potionEffects == null) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int j = (int)y;
        final int tileHeight = 22;
        if (!isDummy) {
            this.width = 0;
            this.height = 22 * potionEffects.size();
        }
        for (final PotionEffect potionEffect : potionEffects) {
            if (potionEffect.getIsPotionDurationMax() && this.permanentEffects.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.moveSpeed.id && this.speed.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.moveSlowdown.id && this.slowness.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.damageBoost.id && this.strength.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.jump.id && this.jumpBoost.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.regeneration.id && this.regeneration.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.fireResistance.id && this.fireResistance.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.waterBreathing.id && this.waterBreathing.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.nightVision.id && this.nightVision.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.weakness.id && this.weakness.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.poison.id && this.poison.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.digSpeed.id && this.haste.getBoolean()) {
                continue;
            }
            if (potionEffect.getPotionID() == Potion.invisibility.id && this.invisibility.getBoolean()) {
                continue;
            }
            final Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
            if (potion.hasStatusIcon()) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.mc.getTextureManager().bindTexture(this.inventoryBackground);
                GuiUtils.INSTANCE.drawTexturedModalRect((int)(x + 6.0f), j + 2, potion.getStatusIconIndex() % 8 * 18, 198 + potion.getStatusIconIndex() / 8 * 18, 18, 18);
            }
            this.mc.fontRendererObj.drawString(I18n.format(potion.getName(), new Object[0]), (int)(x + 10.0f + 18.0f), j + 3, 16777215);
            Label_0615: {
                if (isDummy) {
                    if (Minecraft.getSystemTime() / 50L % 20L >= 10L) {
                        break Label_0615;
                    }
                }
                else if (!this.shouldRender(potionEffect)) {
                    break Label_0615;
                }
                this.mc.fontRendererObj.drawString(Potion.getDurationString(potionEffect), (int)(x + 10.0f + 18.0f), j + 3 + 10, 8355711);
            }
            if (!isDummy) {
                this.width = Math.max(this.width, 30 + this.mc.fontRendererObj.getStringWidth(I18n.format(potion.getName(), new Object[0])));
            }
            j += 22;
        }
        GlStateManager.popMatrix();
    }
    
    private boolean shouldRender(final PotionEffect pe) {
        final boolean blink = this.blink.getBoolean();
        final float speed = this.blinkSpeed.getFloat();
        final int blinkDur = this.blinkDuration.getInt();
        final int speedInTicks = (int)(speed * 20.0f / 2.0f);
        return !blink || pe.getDuration() / 20 > blinkDur || pe.getDuration() % (20.0f * speed) < speedInTicks;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.CENTER_RIGHT;
    }
}
