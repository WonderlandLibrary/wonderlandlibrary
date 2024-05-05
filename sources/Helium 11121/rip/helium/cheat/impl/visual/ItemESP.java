package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.EntityRenderEvent;
import rip.helium.utils.RenderUtility;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.util.Arrays;
import java.util.List;

public class ItemESP extends Cheat {

    public ItemESP() {
        super("Items", "item esp", CheatCategory.VISUAL);
    }


    @Collect
    public void renderEvent(EntityRenderEvent event) {
        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityItem) {
                EntityItem ent = (EntityItem) entity;
                if (RenderUtility.isInViewFrustrum(ent)) {
                    double x = RenderUtility.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks());
                    double y = RenderUtility.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks());
                    double z = RenderUtility.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks());
                    double width = entity.width / 1.25;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtility.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    mc.entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        //RenderUtility.drawBorderedRect(position.x - 1.5, position.y - 0.5, position.z - position.x + 4.0, position.w - position.y + 1.0, 1.5f, -16777216, 0);
                        //RenderUtility.drawBorderedRect(position.x - 1, position.y, position.z - position.x + 3, position.w - position.y, 0.5f, -1, 0);
                        if (ent.getEntityItem().getMaxDamage() > 0) {
                            double offset = position.w - position.y;
                            double percentoffset = offset / ent.getEntityItem().getMaxDamage();
                            double finalnumber = percentoffset * (ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage());
                            //RenderUtility.drawBorderedRect(position.x - 3.5, position.y - 0.5, 1.5, position.w - position.y + 1, 0.5, 0xff000000, 0x60000000);
                            //RenderUtility.drawRect(position.x - 3, position.y + offset, 0.5, -finalnumber, 0xff3E83E3);
                        }
                        GL11.glScalef(0.5f, 0.5f, 0.5f);
                        final String nametext = StringUtils.stripControlCodes(ent.getEntityItem().getItem().getItemStackDisplayName(ent.getEntityItem())) + (ent.getEntityItem().getMaxDamage() > 0 ? "?9 : " + (ent.getEntityItem().getMaxDamage() - ent.getEntityItem().getItemDamage()) : "");
                        mc.fontRendererObj.drawStringWithShadow(nametext, (float) ((position.x + ((position.z - position.x) / 2)) - (mc.fontRendererObj.getStringWidth(nametext) / 4)) * 2, (float) (position.y - mc.fontRendererObj.FONT_HEIGHT + 2) * 2, -1);
                        GL11.glScalef(1.0f, 1.0f, 1.0f);
                        GL11.glPopMatrix();
                    }
                }
            }
        });
    }
}
