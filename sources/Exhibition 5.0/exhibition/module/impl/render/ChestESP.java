// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import exhibition.util.render.GLUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import exhibition.util.RenderingUtil;
import org.lwjgl.opengl.GL11;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityChest;
import exhibition.event.impl.EventRender3D;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class ChestESP extends Module
{
    public ChestESP(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventRender3D.class })
    @Override
    public void onEvent(final Event event) {
        final EventRender3D e = (EventRender3D)event;
        for (final Object o : ChestESP.mc.theWorld.loadedTileEntityList) {
            if (o instanceof TileEntityChest) {
                final TileEntityLockable storage = (TileEntityLockable)o;
                this.drawESPOnStorage(storage, storage.getPos().getX(), storage.getPos().getY(), storage.getPos().getZ());
            }
        }
    }
    
    public void drawESPOnStorage(final TileEntityLockable storage, final double x, final double y, final double z) {
        GL11.glPushMatrix();
        RenderingUtil.pre3D();
        ChestESP.mc.entityRenderer.setupCameraTransform(ChestESP.mc.timer.renderPartialTicks, 2);
        GL11.glColor4d(0.7, 0.4, 0.0, 0.5);
        RenderingUtil.drawBoundingBox(new AxisAlignedBB(x - RenderManager.renderPosX + 0.05, y - RenderManager.renderPosY, z - RenderManager.renderPosZ + 0.05, x - RenderManager.renderPosX + 0.95, y - RenderManager.renderPosY + 0.9, z - RenderManager.renderPosZ + 0.95));
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        RenderingUtil.post3D();
        GL11.glPopMatrix();
    }
    
    public void drawESP(final double x, final double y, final double z, final double r, final double g, final double b) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2848, true);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask(false);
        final AxisAlignedBB boundingBox = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        GL11.glColor4d(r, g, b, 0.14000000059604645);
        RenderingUtil.drawBoundingBox(boundingBox.contract(0.025, 0.025, 0.025));
        GL11.glColor4d(r, g, b, 0.33000001311302185);
        GL11.glLineWidth(1.0f);
        RenderingUtil.drawOutlinedBoundingBox(boundingBox);
        GL11.glLineWidth(1.4f);
        RenderingUtil.drawLines(boundingBox);
        GL11.glLineWidth(2.0f);
        GLUtil.revertAllCaps();
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }
}
