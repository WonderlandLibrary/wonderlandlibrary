package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.combat.AntiBot;
import dev.rise.setting.impl.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "ImageESP", description = "Renders an image on players through walls", category = Category.RENDER)
public final class ImageESP extends Module {

    private final ResourceLocation floyd = new ResourceLocation("rise/esp/floyd.png");
    private final ResourceLocation dream = new ResourceLocation("rise/esp/dream.png");
    private final ResourceLocation mipe = new ResourceLocation("rise/esp/mipe.jpeg");

    private final ModeSetting mode = new ModeSetting("Image", this, "Floyd", "Floyd", "Mipe", "Dream");

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        ResourceLocation optimizedResource = null;

        switch (mode.getMode().toLowerCase()) {
            case "mipe":
                optimizedResource = this.mipe;
                break;
            case "floyd":
                optimizedResource = this.floyd;
                break;
            case "dream":
                optimizedResource = this.dream;
                break;
        }

        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            if (player.isEntityAlive() && player != mc.thePlayer && !AntiBot.bots.contains(player) && !player.isInvisible()) {
                final double x = interp(player.posX, player.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                final double y = interp(player.posY, player.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                final double z = interp(player.posZ, player.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;

                GlStateManager.pushMatrix();
                GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                GL11.glDisable(2929);

                final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(player), 20.0f, Float.MAX_VALUE);
                final double scale = 0.005 * distance;

                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GlStateManager.scale(-0.1, -0.1, 0.0);

                mc.getTextureManager().bindTexture(optimizedResource);

                Gui.drawScaledCustomSizeModalRect(player.width / 2.0f - distance / 3.0f, -player.height - distance, 0.0f, 0.0f, 1.0, 1.0, 252.0 * (scale / 2.0), 476.0 * (scale / 2.0), 1.0f, 1.0f);
                GL11.glEnable(2929);

                GlStateManager.popMatrix();
            }
        }
    }

    public static double interp(final double newPos, final double oldPos) {
        return oldPos + (newPos - oldPos) * Minecraft.getMinecraft().timer.renderPartialTicks;
    }
}