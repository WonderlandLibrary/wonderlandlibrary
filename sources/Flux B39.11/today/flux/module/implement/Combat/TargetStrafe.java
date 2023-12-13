package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vector3d;
import org.lwjgl.opengl.GL11;
import today.flux.event.*;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ColorValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.*;

import java.awt.*;

public class TargetStrafe extends Module {
    public static FloatValue range = new FloatValue("TargetStrafe", "Range", 2.0f, 0.5f, 4.5f, 0.1f);
    public static ModeValue circleType = new ModeValue("TargetStrafe", "Circle Type", "Circle", "Circle", "Points");
    public static ModeValue mode = new ModeValue("TargetStrafe", "Mode", "Simple", "Simple", "Adaptive");

    public static ColorValue targetStrafeColours = new ColorValue("TargetStrafe", "TargetStrafe Circle", Color.GREEN);

    public static BooleanValue jumpKey = new BooleanValue("TargetStrafe", "Jump Key Only", true);
    public static BooleanValue renderCircle = new BooleanValue("TargetStrafe", "Render Circle", true);
    public static BooleanValue lockPersonView = new BooleanValue("TargetStrafe", "Lock F5 View", false);
    public static BooleanValue ground = new BooleanValue("TargetStrafe", "On Ground", true);
    public static FloatValue animationRange = new FloatValue("TargetStrafe", "Animation Range", 0.3f, 0f, 1f, 0.01f);

    public static boolean direction = true;
    public static float currentYaw;

    public TargetStrafe() {
        super("TargetStrafe", Category.Combat, mode);
    }

    @EventTarget
    private void onRender3D(final WorldRenderEvent event) {
        for (EntityPlayer ent : mc.theWorld.playerEntities) {
                this.renderCircle(ent, event.getPartialTicks(), range.getValue(), ModuleManager.speedMod.isEnabled() ? targetStrafeColours.getColor().getRGB() : ColorUtils.WHITE.c);
        }
    }

    public void renderCircle(EntityPlayer entity, float partialTicks, float range, int color) {
        if (renderCircle.getValue() && entity != mc.thePlayer && !entity.isInvisible() && mc.thePlayer.canEntityBeSeen(entity) && entity.isEntityAlive() && entity.getDistanceToEntity(mc.thePlayer) < 4 && !FriendManager.isTeam(entity) && !AntiBots.isHypixelNPC(entity)) {
            GL11.glPushMatrix();
            mc.entityRenderer.disableLightmap();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);

            final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

            GL11.glPushMatrix();
            final double tau = 6.283185307179586;
            final double fans = 45.0;
            GL11.glLineWidth(2.0f);
            if(TargetStrafe.circleType.isCurrentMode("Points")) {
                GL11.glEnable(GL11.GL_POINT_SMOOTH);
                GL11.glPointSize(7.0f);
                GL11.glBegin(GL11.GL_POINTS);
                for (int i = 0; i <= 90; ++i) {
                    RenderUtil.color(color);
                    GL11.glVertex3d(posX + range * Math.cos(i * Math.PI * 2 / 45.0), posY, posZ + range * Math.sin(i * Math.PI * 2 / 45.0));
                }
                GL11.glEnd();
            }
            else {
                GL11.glBegin(1);
                for (int i = 0; i <= 90; ++i) {
                    RenderUtil.color(color);
                    GL11.glVertex3d(posX + range * Math.cos(i * Math.PI * 2 / 45.0), posY, posZ + range * Math.sin(i * Math.PI * 2 / 45.0));
                }
                GL11.glEnd();

            }
            GL11.glPopMatrix();

            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            mc.entityRenderer.enableLightmap();
            GL11.glPopMatrix();
        }
    }

    SmoothAnimationTimer animation = new SmoothAnimationTimer(mc.gameSettings.fovSetting, 0.4f);

    @EventTarget
    public void onRender(UIRenderEvent e) {
        animation.update(true);
        if (mc.currentScreen == null) mc.gameSettings.fovSetting = animation.getValue();
    }

    @EventTarget
    public void onLoop(LoopEvent e) {
        if (mc.currentScreen != null)
            animation.setTarget(mc.gameSettings.fovSetting);
    }


    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (lockPersonView.getValueState() && ModuleManager.killAuraMod.isEnabled()) {
            if (KillAura.target != null && (!jumpKey.getValue() || mc.gameSettings.keyBindJump.pressed) && (ModuleManager.speedMod.isEnabled() || ModuleManager.flyMod.isEnabled())) {
                if (mc.gameSettings.thirdPersonView != 1) {
                    mc.gameSettings.thirdPersonView = 1;
                    animation.setValue(animation.getTarget() * (1 - animationRange.getValue()));
                }
            } else {
                if (mc.gameSettings.thirdPersonView != 0) {
                    mc.gameSettings.thirdPersonView = 0;
                    animation.setValue(animation.getTarget() * (1 + animationRange.getValue()));
                }
            }
        }
    }

    private static boolean isBlockUnder(Entity entity) {
        for (int i = (int) (entity.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(entity.posX,
                    i, entity.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)
                continue;
            return true;
        }
        return false;
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if (TargetStrafe.ground.getValueState() && MoveUtils.isMoving()) {
            if (KillAura.target != null) {
                TargetStrafe.move(e, MoveUtils.getBaseMoveSpeed(), KillAura.target);
            }
        }
    }

    public static void move(MoveEvent event, double speed, Entity entity) {
        if (!isBlockUnder(entity) && mode.isCurrentMode("Adaptive")) {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
            if (event != null) {
                event.setX(0);
                event.setZ(0);
            }
            return;
        }
        if (!isBlockUnder(mc.thePlayer) && mode.isCurrentMode("Adaptive") && !ModuleManager.flyMod.isEnabled())
            direction = !direction;

        if (mc.thePlayer.isCollidedHorizontally && mode.isCurrentMode("Adaptive"))
            direction = !direction;

        float strafe = direction ? 1 : -1;
        float diff = (float)(speed / (range.getValueState() * Math.PI * 2)) * 360 * strafe;
        float[] rotation = RotationUtils.getNeededRotations(new Vector3d(entity.posX, entity.posY, entity.posZ), new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

        rotation[0] += diff;
        float dir = rotation[0] * (float)(Math.PI / 180F);

        double x = entity.posX - Math.sin(dir) * range.getValueState();
        double z = entity.posZ + Math.cos(dir) * range.getValueState();

        float yaw = RotationUtils.getNeededRotations(new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vector3d(x, entity.posY, z))[0] * (float)(Math.PI / 180F);

        mc.thePlayer.motionX = -MathHelper.sin(yaw) * speed;
        mc.thePlayer.motionZ = MathHelper.cos(yaw) * speed;
        if (event != null) {
            event.setX(mc.thePlayer.motionX);
            event.setZ(mc.thePlayer.motionZ);
        }

    }

}
