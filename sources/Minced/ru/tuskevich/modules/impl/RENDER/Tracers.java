// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.util.aliasing.AntiAliasingUtility;
import org.lwjgl.opengl.GL11;
import ru.tuskevich.Minced;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import ru.tuskevich.event.events.impl.EventRender;
import ru.tuskevich.ui.dropui.setting.Setting;
import java.awt.Color;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Tracers", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class Tracers extends Module
{
    public ColorSetting colorSetting;
    public SliderSetting lineWidthSetting;
    
    public Tracers() {
        this.colorSetting = new ColorSetting("Color", Color.WHITE.getRGB());
        this.lineWidthSetting = new SliderSetting("Line Width", 2.0f, 0.1f, 4.0f, 0.01f);
        this.add(this.colorSetting, this.lineWidthSetting);
    }
    
    @EventTarget
    public void event3D(final EventRender display) {
        for (final Entity entity2 : Tracers.mc.world.loadedEntityList) {
            final Entity entity = entity2;
            final Minecraft mc = Tracers.mc;
            if (entity2 != Minecraft.player && entity instanceof EntityPlayer) {
                final Color color = Minced.getInstance().friendManager.isFriend(entity.getName()) ? Color.GREEN : this.colorSetting.getColorValueColor();
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Tracers.mc.getRenderPartialTicks() - Tracers.mc.getRenderManager().renderPosX;
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Tracers.mc.getRenderPartialTicks() - Tracers.mc.getRenderManager().renderPosY;
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Tracers.mc.getRenderPartialTicks() - Tracers.mc.getRenderManager().renderPosZ;
                final float red = color.getRed() / 255.0f;
                final float green = color.getGreen() / 255.0f;
                final float blue = color.getBlue() / 255.0f;
                final float alpha = color.getAlpha() / 255.0f;
                GL11.glPushMatrix();
                final boolean old = Tracers.mc.gameSettings.viewBobbing;
                Tracers.mc.gameSettings.viewBobbing = false;
                Tracers.mc.entityRenderer.setupCameraTransform(display.pt, 2);
                Tracers.mc.gameSettings.viewBobbing = old;
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(this.lineWidthSetting.getFloatValue());
                GL11.glDisable(3553);
                GL11.glDisable(2929);
                AntiAliasingUtility.hook(true, false, false);
                GL11.glDepthMask(false);
                GlStateManager.color(red, green, blue, alpha);
                GL11.glBegin(3);
                final Vec3d vec3d = new Vec3d(0.0, 0.0, 1.0);
                final Minecraft mc2 = Tracers.mc;
                final Vec3d rotatePitch = vec3d.rotatePitch((float)(-Math.toRadians(Minecraft.player.rotationPitch)));
                final Minecraft mc3 = Tracers.mc;
                final Vec3d vec = rotatePitch.rotateYaw((float)(-Math.toRadians(Minecraft.player.rotationYaw)));
                final double x2 = vec.x;
                final double y2 = vec.y;
                final Minecraft mc4 = Tracers.mc;
                GL11.glVertex3d(x2, y2 + Minecraft.player.getEyeHeight(), vec.z);
                GL11.glVertex3d(x, y, z);
                GL11.glEnd();
                AntiAliasingUtility.unhook(true, false, false);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                GlStateManager.resetColor();
            }
        }
    }
}
