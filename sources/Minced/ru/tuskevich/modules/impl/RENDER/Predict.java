// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityEnderPearl;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventRender;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Predictions", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd, \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd.\ufffd", type = Type.RENDER)
public class Predict extends Module
{
    public HashMap<Entity, Vec3d> lastPoss;
    public HashMap<Entity, Integer> i1;
    
    public Predict() {
        this.lastPoss = new HashMap<Entity, Vec3d>();
        this.i1 = new HashMap<Entity, Integer>();
    }
    
    @EventTarget
    public void render(final EventRender e) {
        final Minecraft mc = Predict.mc;
        final double lastTickPosX = Minecraft.player.lastTickPosX;
        final Minecraft mc2 = Predict.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc3 = Predict.mc;
        final double ix = -(lastTickPosX + (posX - Minecraft.player.lastTickPosX) * e.pt);
        final Minecraft mc4 = Predict.mc;
        final double lastTickPosY = Minecraft.player.lastTickPosY;
        final Minecraft mc5 = Predict.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc6 = Predict.mc;
        final double iy = -(lastTickPosY + (posY - Minecraft.player.lastTickPosY) * e.pt);
        final Minecraft mc7 = Predict.mc;
        final double lastTickPosZ = Minecraft.player.lastTickPosZ;
        final Minecraft mc8 = Predict.mc;
        final double posZ = Minecraft.player.posZ;
        final Minecraft mc9 = Predict.mc;
        final double iz = -(lastTickPosZ + (posZ - Minecraft.player.lastTickPosZ) * e.pt);
        GlStateManager.pushMatrix();
        GlStateManager.translate(ix, iy, iz);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(2848);
        GL11.glLineWidth(3.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(1);
        for (final Entity en : Predict.mc.world.loadedEntityList) {
            if (en instanceof EntityEnderPearl) {
                this.penisbobra(en, ((EntityEnderPearl)en).getGravityVelocity(), 0.800000011920929, false);
            }
            if (en instanceof EntityArrow) {
                this.penisbobra(en, 0.05, 0.6000000238418579, false);
            }
        }
        GL11.glEnd();
        GL11.glLineWidth(1.5f);
        GL11.glBegin(1);
        for (final Entity en : Predict.mc.world.loadedEntityList) {
            if (en instanceof EntityEnderPearl) {
                this.penisbobra(en, ((EntityEnderPearl)en).getGravityVelocity(), 0.800000011920929, true);
            }
            if (en instanceof EntityArrow) {
                this.penisbobra(en, 0.05, 0.6000000238418579, true);
            }
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }
    
    private void penisbobra(final Entity e, final double g, final double water, final boolean r) {
        double motionX = e.motionX;
        double motionY = e.motionY;
        double motionZ = e.motionZ;
        double x = e.posX;
        double y = e.posY;
        double z = e.posZ;
        Vec3d lastPos = new Vec3d(x, y, z);
        int i = 0;
        while (i < 300) {
            if (r) {
                ColorUtility.setColor(Hud.getColor(i * 70));
            }
            lastPos = new Vec3d(x, y, z);
            x += motionX;
            y += motionY;
            z += motionZ;
            if (Predict.mc.world.getBlockState(new BlockPos((int)x, (int)y, (int)z)).getBlock() == Blocks.WATER) {
                motionX *= water;
                motionY *= water;
                motionZ *= water;
            }
            else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY -= g;
            final Vec3d pos = new Vec3d(x, y, z);
            if (Predict.mc.world.rayTraceBlocks(lastPos, pos) != null) {
                if (Predict.mc.world.rayTraceBlocks(lastPos, pos).typeOfHit == RayTraceResult.Type.ENTITY) {
                    break;
                }
                break;
            }
            else {
                if (y <= 0.0) {
                    break;
                }
                if (e.motionZ != 0.0 || e.motionX != 0.0 || e.motionY != 0.0) {
                    this.lastPoss.put(e, new Vec3d(lastPos.x, lastPos.y, lastPos.z));
                    GL11.glVertex3d(lastPos.x, lastPos.y, lastPos.z);
                    GL11.glVertex3d(x, y, z);
                    this.i1.put(e, i);
                }
                ++i;
            }
        }
    }
}
