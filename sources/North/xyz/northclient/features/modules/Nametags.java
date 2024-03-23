package xyz.northclient.features.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.Render2DEvent;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;
import xyz.northclient.util.shader.RenderUtil;

import java.awt.*;

@ModuleInfo(name = "Nametags",description = "Nametags",category = Category.RENDER)
public class Nametags extends AbstractModule {

    @EventLink
    public void on2D(Render2DEvent event) {
        final double renderX = mc.getRenderManager().renderPosX;
        final double renderY = mc.getRenderManager().renderPosY;
        final double renderZ = mc.getRenderManager().renderPosZ;
        final int factor = new ScaledResolution(mc).getScaleFactor();
        final float partialTicks = event.getPartialTicks();
        for (Entity player : mc.theWorld.loadedEntityList) {
            if (mc.getRenderManager() == null || player == mc.thePlayer || player.isDead || player.isInvisible() || (player instanceof EntityArmorStand) || !(player instanceof EntityLivingBase)) {
                continue;
            }
            EntityLivingBase entity = (EntityLivingBase) player;

            Vector4f position = ESP2D.calc(entity,partialTicks,renderX,renderY,renderZ,factor);

            final String text = entity.getName();
            final double nameWidth = mc.fontRendererObj.getStringWidth(text);

            final double posX = (position.x + (position.z - position.x) / 2);
            final double posY = position.y - 2;
            final double margin = 2;

            final int multiplier = 2;
            final double nH = mc.fontRendererObj.FONT_HEIGHT + mc.fontRendererObj.FONT_HEIGHT + margin * multiplier;
            final double nY = posY - nH;

            RectUtil.drawRoundedBloom((int) (posX - margin - nameWidth / 2), (int) nY, (int) (nameWidth + margin * multiplier), (int) nH, 5,16, new Color(0,0,0,90));
            mc.fontRendererObj.drawStringWithShadow(text, (int) posX - (mc.fontRendererObj.getStringWidth(text)/2), (int) (nY+margin*2),Color.white.getRGB());
        }




    }
}
