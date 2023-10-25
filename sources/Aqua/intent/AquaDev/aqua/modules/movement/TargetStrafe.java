package intent.AquaDev.aqua.modules.movement;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPlayerMove;
import events.listeners.EventRender3D;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.utils.PlayerUtil;
import intent.AquaDev.aqua.utils.RotationUtil;
import java.awt.Color;
import java.util.Random;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class TargetStrafe
extends Module {
    private int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", "TargetStrafe", 0, Category.Movement);
        Aqua.setmgr.register(new Setting("Circle", (Module)this, true));
        Aqua.setmgr.register(new Setting("Watchdog", (Module)this, true));
        Aqua.setmgr.register(new Setting("StrafeRange", (Module)this, 4.0, 0.3, 9.0, false));
        Aqua.setmgr.register(new Setting("LineWidth", (Module)this, 2.0, 1.0, 10.0, false));
        Aqua.setmgr.register(new Setting("OnlyJump", (Module)this, true));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventRender3D) {
            float range = (float)Aqua.setmgr.getSetting("TargetStrafeStrafeRange").getCurrentNumber();
            if (Aqua.setmgr.getSetting("TargetStrafeCircle").isState()) {
                this.drawCircle(TargetStrafe.mc.timer.renderPartialTicks, range);
                Arraylist.drawGlowArray(() -> this.drawCircle(TargetStrafe.mc.timer.renderPartialTicks, range), (boolean)false);
            }
        }
        if (event instanceof EventPlayerMove) {
            this.doStrafeAtSpeed(EventPlayerMove.INSTANCE, PlayerUtil.getSpeed());
        }
        if (event instanceof EventUpdate) {
            if (TargetStrafe.mc.thePlayer.isCollidedHorizontally) {
                // empty if block
            }
            if (!this.isBlockUnder()) {
                // empty if block
            }
            if (TargetStrafe.mc.gameSettings.keyBindRight.pressed) {
                this.direction = -1;
            } else if (TargetStrafe.mc.gameSettings.keyBindLeft.pressed) {
                this.direction = 1;
            }
        }
    }

    private void switchDirection() {
        this.direction *= -1;
    }

    public void doStrafeAtSpeed(EventPlayerMove event, double moveSpeed) {
        float range = (float)Aqua.setmgr.getSetting("TargetStrafeStrafeRange").getCurrentNumber();
        boolean strafe = this.canStrafe();
        if (Killaura.target != null && strafe) {
            float[] rotations = RotationUtil.Intavee((EntityPlayerSP)TargetStrafe.mc.thePlayer, (EntityLivingBase)Killaura.target);
            if (Aqua.setmgr.getSetting("TargetStrafeOnlyJump").isState()) {
                if (TargetStrafe.mc.gameSettings.keyBindJump.pressed) {
                    if (TargetStrafe.mc.thePlayer.getDistanceToEntity((Entity)Killaura.target) <= range) {
                        this.setSpeed1(event, moveSpeed, rotations[0], this.direction, 0.0);
                    } else {
                        this.setSpeed1(event, moveSpeed, rotations[0], this.direction, 1.0);
                    }
                }
            } else if (TargetStrafe.mc.thePlayer.getDistanceToEntity((Entity)Killaura.target) <= range) {
                this.setSpeed1(event, moveSpeed, rotations[0], this.direction, 0.0);
            } else {
                this.setSpeed1(event, moveSpeed, rotations[0], this.direction, 1.0);
            }
        }
    }

    public boolean canStrafe() {
        return Aqua.moduleManager.getModuleByName("Killaura").isToggled() && Killaura.target != null;
    }

    public boolean isBlockUnder() {
        for (int i = (int)TargetStrafe.mc.thePlayer.posY; i >= 0; --i) {
            BlockPos position = new BlockPos(TargetStrafe.mc.thePlayer.posX, (double)i, TargetStrafe.mc.thePlayer.posZ);
            if (TargetStrafe.mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public void setSpeed1(EventPlayerMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float)(forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            float strafe1 = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.6, (double)1.0);
            if (forward > 0.0) {
                forward = Aqua.setmgr.getSetting("TargetStrafeWatchdog").isState() ? (double)strafe1 : 1.0;
            } else if (forward < 0.0) {
                double d = forward = Aqua.setmgr.getSetting("TargetStrafeWatchdog").isState() ? (double)(-strafe1) : 1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos((double)Math.toRadians((double)(yaw + 90.0f)));
        double mz = Math.sin((double)Math.toRadians((double)(yaw + 90.0f)));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }

    private void drawCircle(float partialTicks, double rad) {
        if (Killaura.target != null) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            float width = (float)Aqua.setmgr.getSetting("TargetStrafeLineWidth").getCurrentNumber();
            GL11.glLineWidth((float)width);
            GL11.glBegin((int)3);
            double x = Killaura.target.lastTickPosX + (Killaura.target.posX - Killaura.target.lastTickPosX) * (double)partialTicks - TargetStrafe.mc.getRenderManager().viewerPosX;
            double y = Killaura.target.lastTickPosY + (Killaura.target.posY - Killaura.target.lastTickPosY) * (double)partialTicks - TargetStrafe.mc.getRenderManager().viewerPosY;
            double z = Killaura.target.lastTickPosZ + (Killaura.target.posZ - Killaura.target.lastTickPosZ) * (double)partialTicks - TargetStrafe.mc.getRenderManager().viewerPosZ;
            int rgb = Aqua.setmgr.getSetting("HUDColor").getColor();
            float r = 0.003921569f * (float)new Color(rgb).getRed();
            float g = 0.003921569f * (float)new Color(rgb).getGreen();
            float b = 0.003921569f * (float)new Color(rgb).getBlue();
            double pix2 = Math.PI * 2;
            for (int i = 0; i <= 90; ++i) {
                GL11.glColor3f((float)r, (float)g, (float)b);
                GL11.glVertex3d((double)(x + rad * Math.cos((double)((double)i * pix2 / 45.0))), (double)y, (double)(z + rad * Math.sin((double)((double)i * pix2 / 45.0))));
            }
            GL11.glEnd();
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2929);
            GL11.glEnable((int)3553);
            GL11.glPopMatrix();
        }
    }
}
