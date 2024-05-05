package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.settings.*;
import com.initial.events.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.entity.*;
import org.lwjgl.opengl.*;
import com.initial.utils.render.*;
import com.initial.events.impl.*;
import com.initial.modules.*;
import com.initial.modules.impl.movement.*;
import java.awt.*;
import net.minecraft.entity.player.*;
import java.util.*;

public final class TargetStrafe extends Module
{
    public DoubleSet radius;
    public BooleanSet controllable;
    public BooleanSet holdspace;
    public BooleanSet render;
    public byte direction;
    
    public TargetStrafe() {
        super("TargetStrafe", 0, Category.COMBAT);
        this.radius = new DoubleSet("Radius", 2.0, 0.1, 10.0, 1.0);
        this.controllable = new BooleanSet("Controllable", true);
        this.holdspace = new BooleanSet("Hold Space", true);
        this.render = new BooleanSet("Render", true);
        this.addSettings(this.radius, this.controllable, this.holdspace, this.render);
    }
    
    @EventTarget
    public void onMotionUpdate(final EventMotion event) {
        this.setDisplayName("Target Strafe");
        if (event.isPre()) {
            if (!this.controllable.isEnabled()) {
                return;
            }
            if (this.mc.thePlayer.isCollidedHorizontally) {
                this.direction = (byte)(-this.direction);
                return;
            }
            if (this.mc.gameSettings.keyBindLeft.isKeyDown()) {
                this.direction = 1;
                return;
            }
            if (this.mc.gameSettings.keyBindRight.isKeyDown()) {
                this.direction = -1;
            }
        }
    }
    
    public static void drawLinesAroundPlayer(final Entity entity, final RenderManager renderManager, final double radius, final float partialTicks, final int points, final float width, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = Render2DUtils.interpolate(entity.prevPosX, entity.posX, partialTicks) - RenderManager.viewerPosX;
        final double y = Render2DUtils.interpolate(entity.prevPosY, entity.posY, partialTicks) - RenderManager.viewerPosY;
        final double z = Render2DUtils.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - RenderManager.viewerPosZ;
        Render2DUtils.glColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 3.141592653589793 * 2.0 / points), y, z + radius * Math.sin(i * 3.141592653589793 * 2.0 / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        final KillAura killAura = ModuleManager.getModule(KillAura.class);
        final Speed speed = ModuleManager.getModule(Speed.class);
        final Flight flight = ModuleManager.getModule(Flight.class);
        for (final Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            final boolean colorchange = speed.isEnabled() || flight.isEnabled();
            int color;
            if (KillAura.target == entity && colorchange && !this.holdspace.isEnabled()) {
                color = Color.white.getRGB();
            }
            else if (KillAura.target == entity && colorchange && this.holdspace.isEnabled() && this.mc.gameSettings.keyBindJump.isKeyDown()) {
                color = Color.white.getRGB();
            }
            else {
                color = Color.white.getRGB();
            }
            if (killAura.isEnabled() && this.render.isEnabled() && entity instanceof EntityPlayer && entity != this.mc.thePlayer && KillAura.target == entity) {
                drawLinesAroundPlayer(entity, this.mc.getRenderManager(), this.radius.getValue(), event.getPartialTicks(), 12, 3.0f, Color.black.getRGB());
                drawLinesAroundPlayer(entity, this.mc.getRenderManager(), this.radius.getValue(), event.getPartialTicks(), 12, 2.0f, color);
            }
        }
    }
}
