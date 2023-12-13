package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.WorldRenderUtils;
import today.flux.utility.WorldUtil;

import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created by John on 2017/05/09.
 */
public class ItemESP extends Module {
    public ItemESP() {
        super("ItemESP", Category.Render, false);
    }

    @EventTarget
    public void onRender3D(WorldRenderEvent event) {
        for (Entity entity : WorldUtil.getEntities()) {
            if (!(entity instanceof EntityItem))
                continue;

            double posX = (entity.lastTickPosX - 0.2) + ((entity.posX - 0.2) - (entity.lastTickPosX - 0.2)) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosX();
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosY();
            double posZ = (entity.lastTickPosZ - 0.2) + ((entity.posZ - 0.2) - (entity.lastTickPosZ - 0.2)) * (double) event.getPartialTicks() - this.mc.getRenderManager().getRenderPosZ();

            GL11.glPushMatrix();

            GL11.glColor4d(1.0, 1.0, 1.0, 0.0);

            WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX, posY, posZ, posX + 0.4, posY + 0.4, posZ + 0.4));
            WorldRenderUtils.renderOne();
            WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX, posY, posZ, posX + 0.4, posY + 0.4, posZ + 0.4));
            WorldRenderUtils.renderTwo();
            WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX, posY, posZ, posX + 0.4, posY + 0.4, posZ + 0.4));
            WorldRenderUtils.renderThree();
            WorldRenderUtils.renderFour(new Color(0xAE05FF).getRGB());
            WorldRenderUtils.drawBoundingBox(new AxisAlignedBB(posX, posY, posZ, posX + 0.4, posY + 0.4, posZ + 0.4));
            WorldRenderUtils.renderFive();

            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);

            GL11.glPopMatrix();
        }
    }
}
