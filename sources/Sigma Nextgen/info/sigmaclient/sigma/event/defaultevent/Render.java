package info.sigmaclient.sigma.event.defaultevent;

import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static net.minecraft.client.gui.AbstractGui.drawModalRectWithCustomSizedTexture;

public class Render {
    public static void render() {
        if (Minecraft.getInstance().isF3Enabled()) {
            return;
        }
        int offset = 0;
        if (SigmaNG.getSigmaNG().moduleManager.getModule(TabGUI.class).enabled) {
            offset += 90;
            offset -= 13;
        }
        MiniMap.offsetY = (int) (offset * 1.6F + 100 + 8 + 5);
        if (SigmaNG.getSigmaNG().moduleManager.getModule(MiniMap.class).enabled) {
            offset += 68 + 8;
        }
        if (!SigmaNG.getSigmaNG().moduleManager.getModule(TabGUI.class).enabled && SigmaNG.getSigmaNG().moduleManager.getModule(MiniMap.class).enabled) {
            offset += 25;
        }
        KeyStrokes.offset = offset - 75;
//        if (!SigmaNG.getSigmaNG().moduleManager.getModule(MiniMap.class).enabled && SigmaNG.getSigmaNG().moduleManager.getModule(TabGUI.class).enabled) {
//            KeyStrokes.offset = 130;
//        }
        if (SigmaNG.getSigmaNG().moduleManager.getModule(KeyStrokes.class).enabled) {
            offset += 80;
        }
        Cords.offsetY = offset + 58;

        if (SigmaNG.getSigmaNG().gameMode == SigmaNG.GAME_MODE.dest) {
        } else {
            // mark
            GL11.glPushMatrix();

            final boolean enableBlend = GL11.glIsEnabled(3042);
            final boolean disableAlpha = !GL11.glIsEnabled(3008);
            if (!enableBlend) {
                GL11.glEnable(3042);
            }
            if (!disableAlpha) {
                GL11.glDisable(3008);
            }
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/watermark1.png"));
            GL11.glColor4d(1, 1, 1, 1f);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            double scale = 0.5;
            int yOff = 0;

            if (Minecraft.getInstance().isF3Enabled()) {
                yOff = Minecraft.getInstance().ingameGUI.overlayDebug.getDebugInfoRightHeight();
            }
            drawModalRectWithCustomSizedTexture(
                    0,
                    yOff, // DO NOT SCALE (see getDebugInfoLeftHeight)
                    0,
                    0, 170 * scale, 104 * scale, 170 * scale, 104 * scale);

            if (!enableBlend) {
                GL11.glDisable(3042);
            }
            if (!disableAlpha) {
                GL11.glEnable(3008);
            }
            GL11.glPopMatrix();
        }
        final boolean enableBlend = GL11.glIsEnabled(3042);
        final boolean disableAlpha = !GL11.glIsEnabled(3008);
        if (!enableBlend) {
            GL11.glDisable(3042);
        }
        if (!disableAlpha) {
            GL11.glEnable(3008);
        }
        JelloFontUtil.jelloFont20.drawNoBSString("", 0, 0, 0);
    }
}
