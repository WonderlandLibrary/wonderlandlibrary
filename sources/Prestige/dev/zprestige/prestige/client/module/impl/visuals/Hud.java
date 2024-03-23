package dev.zprestige.prestige.client.module.impl.visuals;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.CrosshairEvent;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.event.impl.StatusEffectOverlayEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import dev.zprestige.prestige.client.util.impl.animation.Animation;
import dev.zprestige.prestige.client.util.impl.animation.Easing;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Hud extends Module {
    public BooleanSetting watermark;
    public BooleanSetting modulelist;
    public BooleanSetting crosshair;
    public BooleanSetting targetHud;
    public BooleanSetting glow;
    public Identifier watermarkTexture;
    public Identifier playerTexture;
    public Iterable<ItemStack> items;
    public Animation animation;
    public float idk;
    public float health;
    public float prevHealth;
    public float healthval;
    public float prevHealthval;
    public long deltaTime;
    public long time;
    public String name = "";

    public Hud() {
        super("Hud", Category.Visual, "Displays information on your screen");
        watermark = setting("Watermark", true).description("Renders Prestige watermark");
        modulelist = setting("Module List", true).description("Renders each enabled module in a list");
        crosshair = setting("Crosshair", true).description("Custom circle crosshair");
        targetHud = setting("Target Hud", true).description("Renders information about your current target");
        glow = setting("Target Hud Glow", true).invokeVisibility(arg_0 -> targetHud.getObject()).description("Renders a glow around the target hud");
        prevHealth = health;
        idk = healthval;
        animation = new Animation(500, false, Easing.BACK_IN_OUT);
        watermarkTexture = new Identifier("prestige", "icons/logo.png");
    }

    @EventListener
    public void event(Render2DEvent event) {;
        Prestige.Companion.getFontManager().setMatrixStack(event.getMatrixStack());
        RenderHelper.setMatrixStack(event.getMatrixStack());
        Color color = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
        deltaTime = System.currentTimeMillis() - time;
        time = System.currentTimeMillis();
        if (targetHud.getObject()) {
            PlayerEntity player = Prestige.Companion.getTargetManager().getTarget();
            animation.setState(player != null);
            float f = 150;
            float f2 = 40;
            float f3 = (float)event.getScaledWidth() / 2 - f / 2;
            float f4 = (float)event.getScaledHeight() / 2 - f2 / 2 + 100;
            float f5 = animation.getAnimationFactor();
            if (f5 > 0.1f) {
                event.getMatrixStack().push();
                event.getMatrixStack().translate(f3 + f / 2, f4 + f2 / 2, 0);
                event.getMatrixStack().scale(f5, f5, f5);
                event.getMatrixStack().translate(-f3 - f / 2, -f4 - f2 / 2, 0);
                if (glow.getObject()) {
                    RenderUtil.renderShaderRect(event.getMatrixStack(), RenderUtil.getThemeColor(color, 10, 1), RenderUtil.getThemeColor(color, 10, 2), RenderUtil.getThemeColor(color, 10, 3), RenderUtil.getThemeColor(color, 10, 4), f3, f4, f, f2, 5, 10);
                }
                RenderUtil.renderRoundedRect(f3, f4, f3 + f, f4 + f2, new Color(14, 14, 14), 5);
                if (player != null) {
                    playerTexture = this.getMc().getNetworkHandler().getPlayerListEntry(player.getUuid()).getSkinTexture();
                    name = player.getEntityName();
                    health = (float)Math.ceil(player.getHealth() + player.getAbsorptionAmount());
                    healthval = MathUtil.findMiddleValue((player.getHealth() + player.getAbsorptionAmount()) / 24, 0, 1);
                    items = player.getArmorItems();
                }
                if (playerTexture != null) {
                    RenderUtil.renderTexturedQuad(playerTexture, f3 + 5, f4 + 5, 0, 30, 30, 30, 30, 240, 240);
                    RenderUtil.renderRoundedRectOutline(f3 + 5, f4 + 5, f3 + 35, f4 + 35, new Color(14, 14, 14), 1);
                    RenderUtil.renderRoundedRectOutline(f3 + 5, f4 + 5, f3 + 35, f4 + 35, new Color(14, 14, 14), 2);
                    RenderUtil.renderRoundedRectOutline(f3 + 5, f4 + 5, f3 + 35, f4 + 35, new Color(14, 14, 14), 3);
                }
                Prestige.Companion.getFontManager().getFontRenderer().drawString(name, f3 + 40, f4 + 2.5f, Color.WHITE);
                idk = MathUtil.interpolate(idk, healthval, deltaTime * 0.005f);
                prevHealthval = MathUtil.interpolate(prevHealthval, health, deltaTime * 0.01f);
                RenderUtil.renderRoundedRect(f3 + 40, f4 + 29, f3 + 40 + (f - 65) * idk, f4 + 34, new Color(MathUtil.findMiddleValue(1 - idk, 0, 1), MathUtil.findMiddleValue(idk, 0, 1), 0), 2);
                String string = String.valueOf(MathUtil.scaleAndRoundFloat(prevHealthval, 1));
                FontRenderer fontRenderer2 = Prestige.Companion.getFontManager().getFontRenderer();
                float f7 = f3 + f - Prestige.Companion.getFontManager().getFontRenderer().getStringWidth(string) - 2.5f;
                Color color2 = Color.WHITE;
                fontRenderer2.drawString(string, f7, f4 + 23, color2);
                if (items != null) {
                    float f8 = 0;
                    for (ItemStack itemStack : items) {
                        if (!itemStack.isEmpty()) {
                            RenderUtil.renderItem(itemStack, f3 + 40.5f + f8, f4 + 15, 0.8f, true);
                        }
                        f8 += 18;
                    }
                }
                event.getMatrixStack().pop();
            }
        }
        if (watermark.getObject()) {
            RenderUtil.renderTexturedRect(0, 0, 30, 30, watermarkTexture, RenderUtil.getThemeColor(color, 10, 1));
        }
        if (modulelist.getObject()) {
            FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
            int n = event.getScaledWidth();
            ArrayList<Module> arrayList = Prestige.Companion.getModuleManager().getModules();
            Comparator<Module> comparator = Comparator.comparing(Module::getStringWidthFull).reversed();
            Collections.sort(arrayList, comparator);
            float f9 = -3;
            int n2 = 10;
            for (Module module : arrayList) {
                if (module.isEnabled()) {
                    float f11 = n - font.getStringWidth(module.getName() + " " + module.method224());
                    font.drawString(module.getName(), f11 - (!module.method224().isEmpty() ? font.getStringWidth(" ") : 0), f9, RenderUtil.getThemeColor(color, 10, n2));
                    font.drawString(module.method224(), f11 + font.getStringWidth(module.getName()), f9, RenderUtil.getThemeColor(Color.WHITE, 10, n2));
                    f9 += font.getStringHeight() - 2;
                    n2 += 1;
                }
            }
        }
        if (crosshair.getObject()) {
            RenderUtil.renderCircleOutline((float)event.getScaledWidth() / 2 - 0.5f, (float)event.getScaledHeight() / 2 - 0.5f, 1, color);
        }
    }

    @EventListener
    public void event(StatusEffectOverlayEvent event) {
        if (modulelist.getObject()) {
            event.setCancelled();
        }
    }

    @EventListener
    public void event(CrosshairEvent event) {
        if (crosshair.getObject()) {
            event.setCancelled();
        }
    }
}
