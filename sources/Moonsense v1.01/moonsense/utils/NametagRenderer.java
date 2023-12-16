// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.entity.AbstractClientPlayer;
import moonsense.MoonsenseClient;
import net.minecraft.client.renderer.Tessellator;
import moonsense.features.SettingsManager;
import moonsense.features.modules.type.mechanic.FreelookModule;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class NametagRenderer
{
    public static void renderTag(final String tag, final double x, final double y, final double z) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Entity entity = mc.getRenderManager().livingPlayer;
        if (entity != null && entity.getDisplayName() != null && !entity.isInvisible()) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            final float f = 1.6f;
            final float f2 = 0.02666667f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0f, (float)y + mc.thePlayer.height + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            final float viewX = FreelookModule.INSTANCE.isHeld() ? FreelookModule.INSTANCE.cameraPitch : mc.getRenderManager().playerViewX;
            final float viewY = FreelookModule.INSTANCE.isHeld() ? FreelookModule.INSTANCE.cameraYaw : mc.getRenderManager().playerViewY;
            GlStateManager.rotate(-viewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((SettingsManager.INSTANCE.fixNametagRot.getBoolean() && mc.gameSettings.thirdPersonView == 2) ? (-viewX) : viewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
            if (entity.isSneaking()) {
                GlStateManager.translate(0.0f, 9.374999f, 0.0f);
            }
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = fontrenderer.getStringWidth(tag) / 2;
            final boolean flag = MoonsenseClient.INSTANCE.getSocketClient().isUserOnline(((AbstractClientPlayer)entity).getName()) && tag.contains(((AbstractClientPlayer)entity).getName());
            if (flag) {
                i += 6;
            }
            if (!SettingsManager.INSTANCE.transparentNametags.getBoolean()) {
                GlStateManager.func_179090_x();
                worldrenderer.startDrawingQuads();
                worldrenderer.func_178960_a(0.0f, 0.0f, 0.0f, 0.25f);
                worldrenderer.addVertex(-i - 1, -1.0, 0.0);
                worldrenderer.addVertex(-i - 1, 8.0, 0.0);
                worldrenderer.addVertex(i + 1, 8.0, 0.0);
                worldrenderer.addVertex(i + 1, -1.0, 0.0);
                tessellator.draw();
                GlStateManager.func_179098_w();
            }
            fontrenderer.drawString(tag, -fontrenderer.getStringWidth(tag) / 2 + (flag ? 6 : 0), 0, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            if (!entity.isSneaking()) {
                fontrenderer.drawString(tag, -fontrenderer.getStringWidth(tag) / 2 + (flag ? 6 : 0), 0, -1);
            }
            else {
                fontrenderer.drawString(tag, -fontrenderer.getStringWidth(tag) / 2 + (flag ? 6 : 0), 0, 553648127);
            }
            if (entity instanceof AbstractClientPlayer && flag) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(MoonsenseClient.NAMETAG);
                Gui.drawModalRectWithCustomSizedTexture(-i, -1, 0.0f, 0.0f, 9, 9, 9.0f, 9.0f);
            }
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public static void render(final double x, double y, final double z) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Scoreboard scoreboard = mc.thePlayer.getWorldScoreboard();
        final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
        if (scoreobjective != null && !mc.thePlayer.isSneaking()) {
            final Score score = scoreboard.getValueFromObjective(mc.thePlayer.getName(), scoreobjective);
            renderTag(String.valueOf(score.getScorePoints()) + " " + scoreobjective.getDisplayName(), x, y, z);
            y += mc.fontRendererObj.FONT_HEIGHT * 1.15f * 0.02666667;
        }
        renderTag((mc.thePlayer.getCustomNameTag() == null || mc.thePlayer.getCustomNameTag().isEmpty()) ? mc.thePlayer.getDisplayName().getFormattedText() : mc.thePlayer.getCustomNameTag(), x, y, z);
    }
}
