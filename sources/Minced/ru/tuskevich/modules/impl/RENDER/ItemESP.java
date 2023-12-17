// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.event.EventTarget;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import ru.tuskevich.util.font.Fonts;
import javax.vecmath.Vector4d;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Arrays;
import javax.vecmath.Vector3d;
import net.minecraft.util.math.AxisAlignedBB;
import ru.tuskevich.util.render.RenderUtility;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "ItemESP", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class ItemESP extends Module
{
    @EventTarget
    public void onDisplay(final EventDisplay eventDisplay) {
        for (final Entity entity : ItemESP.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem && RenderUtility.isInViewFrustum(entity)) {
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ItemESP.mc.getRenderPartialTicks();
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ItemESP.mc.getRenderPartialTicks();
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ItemESP.mc.getRenderPartialTicks();
                final double width = entity.width / 1.5;
                final double height = entity.height + 0.2f;
                final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                final List<Vector3d> vectors = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
                ItemESP.mc.entityRenderer.setupCameraTransform(ItemESP.mc.getRenderPartialTicks(), 0);
                final ScaledResolution sr = eventDisplay.sr;
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = RenderUtility.vectorTo2D(ScaledResolution.getScaleFactor(), vector.x - ItemESP.mc.getRenderManager().renderPosX, vector.y - ItemESP.mc.getRenderManager().renderPosY, vector.z - ItemESP.mc.getRenderManager().renderPosZ);
                    if (vector != null && vector.z > 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }
                if (position != null) {
                    ItemESP.mc.entityRenderer.setupOverlayRendering();
                    final double posX = position.x;
                    final double posY = position.y;
                    final double endPosX = position.z;
                    final int build = -1;
                    final String tag = ((EntityItem)entity).getItem().getDisplayName() + ((((EntityItem)entity).getItem().stackSize < 1) ? "" : " ");
                    RenderUtility.drawRound((float)(posX + (endPosX - posX) / 2.0 - Fonts.Nunito14.getStringWidth(tag) / 2 - 2.0), (float)(posY - 10.0), (float)(Fonts.Nunito14.getStringWidth(tag) + 2), (float)(Fonts.Nunito14.getStringHeight(tag) + 1), 1.0f, new Color(22, 22, 22, 222));
                    Fonts.Nunito14.drawStringWithShadow(tag, (float)(posX + (endPosX - posX) / 2.0 - Fonts.Nunito14.getStringWidth(tag) / 2), (float)(posY - 9.0), build);
                }
                GL11.glEnable(2929);
                GlStateManager.enableBlend();
                ItemESP.mc.entityRenderer.setupOverlayRendering();
            }
        }
    }
}
