/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.module.movement;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.combat.KillAura;
import me.Tengoku.Terror.module.movement.Speed;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.GLUtils;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class TargetStrafe
extends Module {
    private TimerUtils switchTimer = new TimerUtils();
    private int direction = 1;
    private boolean thirded;
    EntityLivingBase target = KillAura.target;
    public static float kaY = 0.0f;
    private boolean inverted;
    private int position;
    private Setting distance;

    @Override
    public void setup() {
        this.distance = new Setting("Distance", this, 2.0, 1.0, 5.0, false);
        Exodus.INSTANCE.settingsManager.addSetting(this.distance);
    }

    public static void drawCircle(Entity entity, float f, double d) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GLUtils.startSmooth();
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)3);
        double d2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        mc.getRenderManager();
        double d3 = d2 - RenderManager.viewerPosX;
        double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        mc.getRenderManager();
        double d5 = d4 - RenderManager.viewerPosY;
        double d6 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
        mc.getRenderManager();
        double d7 = d6 - RenderManager.viewerPosZ;
        float f2 = CustomIngameGui.color;
        double d8 = Math.PI * 2;
        int n = 0;
        while (n <= 90) {
            GL11.glColor3f((float)255.0f, (float)0.0f, (float)0.0f);
            GL11.glVertex3d((double)(d3 + d * Math.cos((double)n * (Math.PI * 2) / 45.0)), (double)d5, (double)(d7 + d * Math.sin((double)n * (Math.PI * 2) / 45.0)));
            ++n;
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GLUtils.endSmooth();
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public TargetStrafe() {
        super("Target Strafe", 0, Category.MOVEMENT, "");
    }

    public void setSpeed(EventMotion eventMotion, double d) {
        EntityLivingBase entityLivingBase = KillAura.target;
        float f = Minecraft.thePlayer.rotationYaw;
        double d2 = this.direction;
        double d3 = !((double)Minecraft.thePlayer.getDistanceToEntity(KillAura.target) <= this.distance.getValDouble()) ? 1 : 0;
        if (d3 == 0.0 && d2 == 0.0) {
            eventMotion.setX(0.0);
            eventMotion.setZ(0.0);
        } else {
            if (d3 != 0.0) {
                if (d2 > 0.0) {
                    f += (float)(d3 > 0.0 ? -45 : 45);
                } else if (d2 < 0.0) {
                    f += (float)(d3 > 0.0 ? 45 : -45);
                }
                d2 = 0.0;
                if (d3 > 0.0) {
                    d3 = 1.0;
                } else if (d3 < 0.0) {
                    d3 = -1.0;
                }
            }
            Minecraft.thePlayer.motionX = d3 * d * -Math.sin(Math.toRadians(f)) + d2 * d * Math.cos(Math.toRadians(f));
            Minecraft.thePlayer.motionZ = d3 * d * Math.cos(Math.toRadians(f)) - d2 * d * -Math.sin(Math.toRadians(f));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onMotionUpdate(EventMotion eventMotion) {
        if (!Minecraft.gameSettings.keyBindJump.isKeyDown() && Exodus.INSTANCE.getModuleManager().getModuleByClass(Speed.class).isToggled() && KillAura.target != null) {
            this.setSpeed(eventMotion, 0.12524113059043884);
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                this.direction = -this.direction;
            } else {
                if (Minecraft.gameSettings.keyBindLeft.isPressed()) {
                    this.direction = 1;
                }
                if (Minecraft.gameSettings.keyBindRight.isPressed()) {
                    this.direction = -1;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}

