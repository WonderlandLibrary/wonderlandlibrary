// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import org.lwjgl.util.vector.Vector3f;
import java.util.Iterator;
import org.lwjgl.util.vector.Vector4f;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.util.player.RotationUtils;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import vip.Resolute.events.Event;
import java.util.HashMap;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.modules.Module;

public class Pointers extends Module
{
    public ColorSetting colorProp;
    public NumberSetting radius;
    public NumberSetting size;
    private final Map<EntityPlayer, float[]> entityPosMap;
    
    public Pointers() {
        super("Pointers", 0, "Draws pointers at entities", Category.RENDER);
        this.colorProp = new ColorSetting("Color", new Color(8281781));
        this.radius = new NumberSetting("Radius", 30.0, 10.0, 100.0, 1.0);
        this.size = new NumberSetting("Size", 6.0, 3.0, 30.0, 1.0);
        this.addSettings(this.colorProp, this.radius, this.size);
        this.entityPosMap = new HashMap<EntityPlayer, float[]>();
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender2D) {
            final EventRender2D event = (EventRender2D)e;
            final ScaledResolution sr = new ScaledResolution(Pointers.mc);
            final float middleX = sr.getScaledWidth() / 2.0f;
            final float middleY = sr.getScaledHeight() / 2.0f;
            final float pt = event.getPartialTicks();
            RenderUtils.startBlending();
            for (final EntityPlayer player : this.entityPosMap.keySet()) {
                if (player instanceof EntityOtherPlayerMP) {
                    GL11.glPushMatrix();
                    final float arrowSize = (float)this.size.getValue();
                    final float alpha = Math.max(1.0f - Pointers.mc.thePlayer.getDistanceToEntity(player) / 30.0f, 0.3f);
                    final int color = this.colorProp.getColor();
                    GL11.glTranslatef(middleX + 0.5f, middleY, 1.0f);
                    final float yaw = (float)(RenderUtils.interpolate(RotationUtils.getYawToEntity(player, true), RotationUtils.getYawToEntity(player, false), pt) - RenderUtils.interpolate(Pointers.mc.thePlayer.prevRotationYaw, Pointers.mc.thePlayer.rotationYaw, pt));
                    GL11.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef(0.0f, (float)(-this.radius.getValue() - this.size.getValue()), 0.0f);
                    GL11.glDisable(3553);
                    GL11.glBegin(5);
                    GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(alpha * 255.0f));
                    GL11.glVertex2f(0.0f, 0.0f);
                    GL11.glVertex2f(-arrowSize, arrowSize);
                    GL11.glVertex2f(arrowSize, arrowSize);
                    GL11.glEnd();
                    GL11.glEnable(3553);
                    GL11.glPopMatrix();
                }
            }
        }
        if (e instanceof EventRender3D) {
            final EventRender3D event2 = (EventRender3D)e;
            if (!this.entityPosMap.isEmpty()) {
                this.entityPosMap.clear();
            }
            final float partialTicks = event2.getPartialTicks();
            for (final EntityPlayer player2 : Pointers.mc.theWorld.playerEntities) {
                if (!(player2 instanceof EntityOtherPlayerMP) && player2.isEntityAlive() && !player2.isInvisible()) {
                    continue;
                }
                GL11.glPushMatrix();
                final float posX = (float)(RenderUtils.interpolate(player2.prevPosX, player2.posX, partialTicks) - RenderManager.viewerPosX);
                final float posY = (float)(RenderUtils.interpolate(player2.prevPosY, player2.posY, partialTicks) - RenderManager.viewerPosY);
                final float posZ = (float)(RenderUtils.interpolate(player2.prevPosZ, player2.posZ, partialTicks) - RenderManager.viewerPosZ);
                final double halfWidth = player2.width / 2.0 + 0.1;
                final AxisAlignedBB bb = new AxisAlignedBB(posX - halfWidth, posY + 0.1, posZ - halfWidth, posX + halfWidth, posY + player2.height + 0.1, posZ + halfWidth);
                final double[][] vectors = { { bb.minX, bb.minY, bb.minZ }, { bb.minX, bb.maxY, bb.minZ }, { bb.minX, bb.maxY, bb.maxZ }, { bb.minX, bb.minY, bb.maxZ }, { bb.maxX, bb.minY, bb.minZ }, { bb.maxX, bb.maxY, bb.minZ }, { bb.maxX, bb.maxY, bb.maxZ }, { bb.maxX, bb.minY, bb.maxZ } };
                final Vector4f position = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
                for (final double[] vec : vectors) {
                    final Vector3f projection = RenderUtils.project2D((float)vec[0], (float)vec[1], (float)vec[2], 2);
                    if (projection != null && projection.z >= 0.0f && projection.z < 1.0f) {
                        position.x = Math.min(position.x, projection.x);
                        position.y = Math.min(position.y, projection.y);
                        position.z = Math.max(position.z, projection.x);
                        position.w = Math.max(position.w, projection.y);
                    }
                }
                this.entityPosMap.put(player2, new float[] { position.x, position.y, position.z, position.w });
                GL11.glPopMatrix();
            }
        }
    }
}
