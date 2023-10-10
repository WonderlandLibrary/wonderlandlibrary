/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Nametags", description = "Lets you see peoples names through walls", category = Category.RENDER)
public final class Nametags extends Module {

    public static boolean enabled;

    private final TTFFontRenderer comfortaa = CustomFont.FONT_MANAGER.getFont("Comfortaa 128");
    private final BooleanSetting invisible = new BooleanSetting("Invisibles", this, false);

    // Uses Render.java line 106
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        int amount = 0;

        for (final EntityPlayer entity : mc.theWorld.playerEntities) {
            if (entity != null) {
                final String name = this.getModule(Streamer.class).isEnabled() ? "Player" : entity.getName();

                if ((!entity.isInvisible() || this.invisible.isEnabled()) && !entity.isDead && entity != mc.thePlayer && !entity.bot && RenderUtil.isInViewFrustrum(entity)) {
                    //Changing size
                    final float scale = Math.max(0.02F, mc.thePlayer.getDistanceToEntityRender(entity) / 300);

                    final double x = (entity).lastTickPosX + ((entity).posX - (entity).lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
                    final double y = ((entity).lastTickPosY + ((entity).posY - (entity).lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY) + scale * 6;
                    final double z = (entity).lastTickPosZ + ((entity).posZ - (entity).lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;

                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y + 2.3, z);
                    GlStateManager.disableDepth();

                    GL11.glScalef(-scale, -scale, -scale);

                    GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                    GL11.glRotated(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0D : 1.0D, 0.0D, 0.0D);

                    final float width = CustomFont.getWidthProtect(name) - 7;
                    final float progress = Math.min((entity).getHealth(), (entity).getMaxHealth()) / (entity).getMaxHealth();

                    final Color color = ThemeUtil.getThemeColor(amount, ThemeType.GENERAL, 0.5f);

                    Gui.drawRect((-width / 2.0F - 5.0F), -1, (width / 2.0F + 5.0F), 8, 0x40000000);
                    Gui.drawRect((-width / 2.0F - 5.0F), 7, (-width / 2.0F - 5.0F + (width / 2.0F + 5.0F - -width / 2.0F + 5.0F) * progress), 8, color.getRGB());

                    GL11.glScalef(0.1f, 0.1f, 0.1f);

                    comfortaa.drawCenteredString(name, -width / 16.0F, 0.5f, -1);

                    GL11.glScalef(1.9f, 1.9f, 1.9f);

                    GlStateManager.enableDepth();
                    GL11.glPopMatrix();
                    amount++;
                }
            }
        }
    }

    @Override
    protected void onEnable() {
        enabled = true;
    }

    @Override
    protected void onDisable() {
        enabled = false;
    }
}
