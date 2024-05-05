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
import rip.helium.utils.Stopwatch;
import rip.helium.utils.VisualHelper;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

import java.awt.*;

public class MemeESP extends Cheat {

    public static Color colorr;
    private float hue;
    private Hud hud;
    private ResourceLocation lewd;
    private ResourceLocation hentai;
    private ResourceLocation floyd;
    private Aura cheat_killAura;
    private ChestStealer cheat_chestStealer;
    Stopwatch timer = new Stopwatch();
    int i = 0;
    private double moveSpeed;
    private final StringsProperty prop_mode;
    private int stage;
    private boolean doSlow;

    public MemeESP() {
        super("MemeESP", "meme esps", CheatCategory.VISUAL);
        this.lewd = new ResourceLocation("client/lewd.png");
        this.hentai = new ResourceLocation("client/hentai.png");
        this.floyd = new ResourceLocation("client/floyd.png");
        this.prop_mode = new StringsProperty("Mode", "change the mode.", null, false, true,
                new String[]{"Hentai", "Lewd", "Floyd"},
                new Boolean[]{true, false, false});
        this.registerProperties(this.prop_mode);
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
                setMode(prop_mode.getSelectedStrings().get(0));
                switch (prop_mode.getSelectedStrings().get(0)) {
                    case "Hentai": {
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
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.hentai);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, 1.0, 1.0, 252.0 * (scale / 2.0), 476.0 * (scale / 2.0), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case "Lewd": {
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
                        break;
                    }
                    case "Floyd": {
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
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.floyd);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, 1.0, 1.0, 252.0 * (scale / 2.0), 476.0 * (scale / 2.0), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                }
            }
        }
    }
}
