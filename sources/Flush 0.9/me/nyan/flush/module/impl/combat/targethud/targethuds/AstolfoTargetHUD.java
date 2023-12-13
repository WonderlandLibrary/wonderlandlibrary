package me.nyan.flush.module.impl.combat.targethud.targethuds;

import me.nyan.flush.Flush;
import me.nyan.flush.module.impl.combat.targethud.TargetHUD;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class AstolfoTargetHUD extends TargetHUD {
    public AstolfoTargetHUD() {
        super("Astolfo");
    }

    @Override
    public void draw(EntityLivingBase target, float x, float y, int color) {
        if (target == null) {
            return;
        }

        Render.renderNametags = false;

        healthAnimated = Math.min(Math.max(healthAnimated, 0), target.getMaxHealth());
        healthAnimated1 = Math.min(Math.max(healthAnimated1, 0), target.getMaxHealth());

        if (target.getMaxHealth() >= target.getHealth()) {
            healthAnimated += (target.getHealth() / target.getMaxHealth() - healthAnimated / target.getMaxHealth()) * 2F;

            if (target.getHealth() > healthAnimated1) {
                healthAnimated1 = target.getHealth();
            }

            if (healthAnimated1 > target.getHealth()) {
                healthAnimated1 += (target.getHealth() / target.getMaxHealth() - healthAnimated1 / target.getMaxHealth()) / 1.5F;
            }
        } else {
            resetHealthAnimated();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);

        //background
        Gui.drawRect(0, 0, getWidth(), getHeight(), 0xAA000000);

        //health bar
        Gui.drawRect(46, getHeight() - 7, getWidth() - 2, getHeight() - 2, 0x90000000);

        if (target.getMaxHealth() >= target.getHealth()) {
            Gui.drawRect(46 + (healthAnimated * (getWidth() - 48) / target.getMaxHealth()), getHeight() - 7,
                    46 + (healthAnimated1 * (getWidth() - 48) / target.getMaxHealth()), getHeight() - 2,
                    ColorUtils.brightness(color, 0.49F));

            Gui.drawRect(46, getHeight() - 7, 46 + (healthAnimated * (getWidth() - 48) / target.getMaxHealth()),
                    getHeight() - 2, color);
        }

        //drawing the entity
        RenderUtils.drawEntityOnScreen(target, 24, 68, 32, -30.0F, 0.0F);

        //target name
        Flush.getFont("GoogleSansDisplay", 20).drawString(target.getName(), 48, 2, -1, true);

        //target health
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.5, 2.5, 0);
        GlStateManager.translate(54 / 2.5f, 18 / 2.5f, 0);
        mc.fontRendererObj.drawStringWithShadow((Math.round(target.getHealth() * 10f) / 10f + "❤").replace(".0", ""), -2,
                0, color);
        GlStateManager.popMatrix();

        //armor
        int itemsX = getWidth() - 22;
        int itemsY = 0;

        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 3; i >= 0; i--)
            mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(i), itemsX, itemsY + (3 - i) * 16);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();

        Render.renderNametags = true;
    }

    @Override
    public int getWidth() {
        return 174;
    }

    @Override
    public int getHeight() {
        return 70;
    }
}
