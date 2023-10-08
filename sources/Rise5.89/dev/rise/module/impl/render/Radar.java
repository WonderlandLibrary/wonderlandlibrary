package dev.rise.module.impl.render;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.render.BlurEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.ingame.IngameGUI;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.util.Objects;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Radar", description = "Displays a Radar which shows nearby players", category = Category.RENDER)
public final class Radar extends Module {

    private final NumberSetting radarX = new NumberSetting("Radar X", this, 78, 0, 1850, 1);
    private final NumberSetting radarY = new NumberSetting("Radar Y", this, 98, 0, 1010, 1);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        for (final EntityPlayer p : mc.theWorld.playerEntities) {
            if (p.bot)
                continue;

            p.lastDistanceFromPlayerX = p.distanceFromPlayerX;
            p.lastDistanceFromPlayerZ = p.distanceFromPlayerZ;

            p.distanceFromPlayerX = mc.thePlayer.posX - p.posX;
            p.distanceFromPlayerZ = mc.thePlayer.posZ - p.posZ;

            p.distanceFromPlayerX *= 1.6;
            p.distanceFromPlayerZ *= 1.6;
        }
    }

    @Override
    public void onBlur(final BlurEvent event) {
        if (!mc.gameSettings.showDebugInfo) {
            final float x = (float) radarX.getValue();
            final float y = (float) radarY.getValue();

            final float width = 70;
            final float height = 70;

            RenderUtil.roundedRect(x, y, width, height, 5, Color.BLACK);
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (mc.gameSettings.showDebugInfo)
            return;

        final float x = (float) radarX.getValue();
        final float y = (float) radarY.getValue();

        final float width = 70;
        final float height = 70;

        final int middleX = (int) (x + width / 2);
        final int middleY = (int) (y + height / 2);

        RenderUtil.roundedRect(x, y, width, height, 5, new Color(0, 0, 0, 100));

        GlStateManager.pushMatrix();

        GlStateManager.translate(middleX, middleY, 0);
        GlStateManager.rotate(mc.thePlayer.rotationYaw, 0, 0, -1);
        GlStateManager.translate(-middleX, -middleY, 0);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(x + 0.5f, y + 0.5f, width - 1, height - 1);

        //Colors
        final Color color1 = new Color(78, 161, 253, 100);
        final Color color2 = new Color(78, 253, 154, 100);
        final double factor = (Math.sin(IngameGUI.ticks) + 1) * 0.5f;

        for (final EntityPlayer p : mc.theWorld.playerEntities) {
            if (p.bot)
                continue;

            final Color c;

            if ("Rise Blend".equals(((ModeSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager()
                    .getSetting("Interface", "Theme"))).getMode())) {
                c = ColorUtil.mixColors(color1, color2, factor);
            } else {
                c = ThemeUtil.getThemeColor(ThemeType.GENERAL);
            }

            final double renderCurrentPosX = p.lastDistanceFromPlayerX + (p.distanceFromPlayerX - p.lastDistanceFromPlayerX) * event.getPartialTicks();
            final double renderCurrentPosZ = p.lastDistanceFromPlayerZ + (p.distanceFromPlayerZ - p.lastDistanceFromPlayerZ) * event.getPartialTicks();

            final double distance = Math.abs(renderCurrentPosX) + Math.abs(renderCurrentPosZ);
            final float maxOpacityDistance = 70;

            if (distance < maxOpacityDistance) {
                RenderUtil.drawFilledCircle(middleX + renderCurrentPosX, middleY + renderCurrentPosZ, 2, c.hashCode(), 40);

                //Glow
                RenderUtil.drawFilledCircle(middleX + renderCurrentPosX, middleY + renderCurrentPosZ, 4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), 40);
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.popMatrix();

        GL11.glColor3d(255, 255, 255);
    }
}
