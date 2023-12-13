package me.nyan.flush.module.impl.combat.targethud.targethuds;

import me.nyan.flush.Flush;
import me.nyan.flush.module.impl.combat.targethud.TargetHUD;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;

public class FlushTargetHUD extends TargetHUD {
    private EntityLivingBase target;
    private float alpha;
    private float healthAnimated;
    private float healthAnimated1;

    public FlushTargetHUD() {
        super("Flush");
    }

    @Override
    public void draw(EntityLivingBase target, float x, float y, int color) {
        if (target == null) {
            alpha -= (alpha / 100F) * Flush.getFrameTime();
            if (alpha <= 0.01) {
                alpha = 0;
                this.target = null;
            }
        } else {
            this.target = target;
            alpha += ((1 - alpha) / 100F) * Flush.getFrameTime();
            if (alpha >= 0.99) {
                alpha = 1;
            }
        }
        target = this.target;

        if (target == null) {
            return;
        }

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 24);

        if (target.getMaxHealth() >= target.getHealth()) {
            healthAnimated += ((target.getHealth() / target.getMaxHealth() - healthAnimated / target.getMaxHealth()) * 0.2) * Flush.getFrameTime();

            if (target.getHealth() > healthAnimated1) {
                healthAnimated1 = target.getHealth();
            }

            if (healthAnimated1 > target.getHealth()) {
                healthAnimated1 += ((target.getHealth() / target.getMaxHealth() - healthAnimated1 / target.getMaxHealth()) * 0.05) * Flush.getFrameTime();
            }
        } else {
            resetHealthAnimated();
        }
        healthAnimated = Math.min(Math.max(healthAnimated, 0), target.getMaxHealth());
        healthAnimated1 = Math.min(Math.max(healthAnimated1, 0), target.getMaxHealth());

        GlStateManager.pushMatrix();
        float translatedX = (x + getWidth() / 2F) * (1 - alpha);
        float translatedY = (y + getHeight() / 2F) * (1 - alpha);
        GlStateManager.translate(translatedX, translatedY, 1);
        GlStateManager.scale(alpha, alpha, 1);
        GlStateManager.translate(x, y, 0);

        //background
        Gui.drawRect(0, 0, getWidth(), getHeight(),
                ColorUtils.alpha(0xFF000000, (int) (alpha * 136)));

        //health bar
        Gui.drawRect(0, 0, getWidth(), 2,
                ColorUtils.alpha(0xFF000000, (int) (alpha * 255)));

        if (target.getMaxHealth() >= target.getHealth()) {
            Gui.drawRect(healthAnimated / target.getMaxHealth() * getWidth(), 0,
                    healthAnimated1 / target.getMaxHealth() * getWidth(), 2,
                    ColorUtils.alpha(ColorUtils.brightness(color, 0.49F), (int) (alpha * 255)));

            Gui.drawRect(0, 0, healthAnimated / target.getMaxHealth() * getWidth(), 2,
                    ColorUtils.alpha(color, (int) (alpha * 255)));
        }

        //target name
        font.drawString(target.getName(), 2, 3, ColorUtils.alpha(0xFFFFFFFF, (int) (alpha * 255)), true);

        //target health
        String s = String.valueOf(Math.round(target.getHealth() * 10f) / 10f).replace(".0", "");
        font.drawString(
                s,
                getWidth() - 4 - font.getStringWidth(s) - mc.fontRendererObj.getStringWidth("❤") * 1.5F - 2,
                3,
                ColorUtils.alpha(-1, (int) (alpha * 255)),
                true
        );
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.5F, 0);
        GlStateManager.scale(1.5F, 1.5F, 1);
        mc.fontRendererObj.drawString(
                "❤",
                (int) ((getWidth() - 4 - mc.fontRendererObj.getStringWidth("❤") * 1.5F + 2) / 1.5F),
                (int) ((3 + font.getFontHeight() - mc.fontRendererObj.FONT_HEIGHT - 4) / 1.5F),
                ColorUtils.getColor((int) (255 - (target.maxHurtTime > 0 ?
                        target.hurtTime / (float) target.maxHurtTime * 100 : 0)),
                        0, 0, (int) (alpha * 255))
        );
        GlStateManager.popMatrix();

        //armor
        int itemsX = getWidth() - 16 - 1;
        int itemsY = getHeight() - 1 - 16;
        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 3; i >= 0; i--) {
            GlStateManager.color(1, 1, 1, alpha);
            mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(i), itemsX - i * 16, itemsY);
        }
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
    }

    @Override
    public int getWidth() {
        if (target == null) {
            return 100;
        }
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 24);
        String s = String.valueOf(Math.round(target.getHealth() * 10f) / 10f).replace(".0", "");
        return Math.round(Math.max(100, font.getStringWidth(target.getName()) + font.getStringWidth(s) +
                mc.fontRendererObj.getStringWidth("❤") * 1.5F + 14));
    }

    @Override
    public int getHeight() {
        if (target == null) {
            return 34;
        }
        for (int i = 3; i >= 0; i--) {
            if (target.getCurrentArmor(i) != null) {
                return 34;
            }
        }
        return 34 - 16 + 1;
    }
}
