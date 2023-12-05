package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.combat.aura.Aura;
import rip.helium.cheat.impl.misc.cheststealer.ChestStealer;
import rip.helium.event.minecraft.EntityRenderEvent;
import rip.helium.utils.VisualHelper;

import java.awt.*;

public class HentaiESP extends Cheat {

    public static Color colorr;
    private float hue;
    private Hud hud;
    private ResourceLocation lewd;
    private Aura cheat_killAura;
    private ChestStealer cheat_chestStealer;

    public HentaiESP() {
        super("HentaiESP", "Highlights the position of entites with hentai", CheatCategory.VISUAL);
        this.lewd = new ResourceLocation("client/lewd.png");
    }

    @Override
    public void onEnable() {
        if (cheat_killAura == null) {
            cheat_killAura = (Aura) Helium.instance.cheatManager.getCheatRegistry().get("aura");
        }
    }

    @Collect
    public void onEntityRender(EntityRenderEvent event) {

        for (final EntityPlayer p : mc.thePlayer.getEntityWorld().playerEntities) {
            if (VisualHelper.isInFrustumView(p) && !p.isInvisible() && p.isEntityAlive()) {
                if (p == mc.thePlayer) {
                    continue;
                }

                final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                GlStateManager.pushMatrix();
                GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                GL11.glDisable(2929);
                final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                final double scale = 0.005 * distance;
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GlStateManager.scale(-0.1, -0.1, 0.0);
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.lewd);
                Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, 1.0, 1.0, 252.0 * (scale / 2.0), 476.0 * (scale / 2.0), 1.0f, 1.0f);
                GL11.glEnable(2929);
                GlStateManager.popMatrix();
            }
        }
    }
}
