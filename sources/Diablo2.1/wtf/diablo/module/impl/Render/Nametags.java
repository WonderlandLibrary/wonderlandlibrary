package wtf.diablo.module.impl.Render;


import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import wtf.diablo.events.impl.Render3DEvent;
import wtf.diablo.events.impl.Render3DEventAlternate;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.util.Objects;

import static wtf.diablo.utils.math.MathUtil.round;


public class Nametags extends Module {
    private NumberSetting scaling = new NumberSetting("Size", 0.25, 2d, 2d, 10d);
    public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    public BooleanSetting smartScale = new BooleanSetting("SmartScale", true);

    public Nametags() {
        super("Nametags", "Renders a nametag above a player", Category.RENDER, ServerType.All);
        addSettings(invisibles, smartScale);
    }

    @Subscribe
    public void onRender3D(Render3DEventAlternate event) {
        try {
            for (EntityPlayer player : mc.theWorld.playerEntities) {
                if (player.equals(mc.thePlayer) || !player.isEntityAlive() || player.isInvisible() && !this.invisibles.getValue()) {
                    continue;
                }
                double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - mc.getRenderManager().renderPosX;
                double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - mc.getRenderManager().renderPosY;
                double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - mc.getRenderManager().renderPosZ;
                this.renderNameTag(player, x, y + 0.1, z, event.getPartialTicks());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.isSneaking() ? 0.5 : 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = ("§c") + player.getName();
        double distance = camera.getDistance(x + mc.getRenderManager().viewerPosX, y + mc.getRenderManager().viewerPosY, z + mc.getRenderManager().viewerPosZ);
        int width = mc.fontRendererObj.getStringWidth(displayTag) / 2;
        int height = mc.fontRendererObj.FONT_HEIGHT;
        double scale = (this.scaling.getValue() * (distance * 11)) / 1050d;
        if (distance <= 8.0 && this.smartScale.getValue()) {
            scale = 0.0245d;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) tempY + 1.4f, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();

        GlStateManager.disableBlend();
        int d = mc.fontRendererObj.getStringWidth(displayTag) / 4;
        //RenderUtil.drawRect(-width - 3, -14, width * 2 + 5, height + 3, new Color(24, 24, 24, 70).getRGB());
        RenderUtil.drawRoundedRect(-width-5, -15, width*2+8, height+4, 0, new Color(0, 0, 0, 179).getRGB());
        //RenderUtil.drawOutlinedRoundedRect(-width-3, -14, width*2+5, height+3.5, 2, 1, ColorUtil.getColor(0));
        mc.fontRendererObj.drawStringWithShadow(displayTag, -width - 1, -12, this.getDisplayColour(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }


    private int getDisplayColour(EntityPlayer player) {
        int colour = -new Color(197, 197, 197).getRGB();
//        if (Client.getInstance().getFriendManager().isFriend(String.valueOf(player))) {
//            return -11157267;
//        }
        if (player.isInvisible()) {
            colour = -1113785;
        } else if (player.isSneaking()) {
            colour = -new Color(252, 234, 93).getRGB();
        }
        return colour;
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }
}

