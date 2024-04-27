package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.*;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.utils.client.Rotation;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.client.VecRotation;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.mc.VoidCheck;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.shader.shaders.RainbowShader;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import java.awt.*;
import java.util.Objects;

@ModuleInfo(name = "TargetStrafe", description = "Can make you to rotate around the entity", category = ModuleCategory.MOVEMENT)
public class TargetStrafe extends Module {
    private final Rotation targetRotation = new Rotation(0f, 0f);
    private final Rotation serverRotation = new Rotation(0f, 0f);

    private final BoolValue stopTurnValue = new BoolValue("RaycastStopTurn", false);
    private final Value<Float> collisionBorderSizeValue = (FloatValue) new FloatValue("collisionBorder", 0.0f, 0f, 1.0f).displayable(stopTurnValue::get);

    private final FloatValue radiusValue = new FloatValue("Radius", 1.0f, 0.1f, 4.0f);
    private final FloatValue radiusSizeValue = new FloatValue("Size", 0.0f, 0.0f, 1.0f);
    private final IntegerValue turnShapeValue = new IntegerValue("TurnShape", 6, 2, 12);

    private final BoolValue onJumpKeyValue = new BoolValue("OnJumpKey", true);
    private final BoolValue alwaysValue = new BoolValue("Always", true);
    private final BoolValue lockValue = new BoolValue("LockTarget", false);
    private final BoolValue autoGodValue = new BoolValue("ThirdPersonView", false);

    private final ListValue drawMode = new ListValue("DrawMode", new String[]{"Normal", "Outline", "None"}, "None");
    private final Value<Boolean> rainbow = new BoolValue("Rainbow", false).displayable(() -> !drawMode.get().equals("None"));
    private final Value<Float> rainbowX = new FloatValue("Rainbow-X", -1000F, -2000F, 2000F).displayable(() -> rainbow.get() && !drawMode.get().equals("None"));
    private final Value<Float> rainbowY = new FloatValue("Rainbow-Y", -1000F, -2000F, 2000F).displayable(() -> rainbow.get() && !drawMode.get().equals("None"));

    private final Value<Integer> r = new IntegerValue("Red", 255, 0, 255).displayable(() -> !rainbow.get());
    private final Value<Integer> g = new IntegerValue("Green", 255, 0, 255).displayable(() -> !rainbow.get());
    private final Value<Integer> b = new IntegerValue("Blue", 255, 0, 255).displayable(() -> !rainbow.get());
    private final Value<Integer> a = new IntegerValue("Alpha", 130, 0, 255).displayable(() -> !drawMode.get().equals("None"));

    private final FloatValue smoothLineValue = new FloatValue("SmoothLine", 6f, 1f, 10f);
    private final IntegerValue shape = new IntegerValue("Shape", 12, 5, 32);

    private final Aura killAura = (Aura) LiquidSense.moduleManager.getModule(Aura.class);
    private final Speed speed = (Speed) LiquidSense.moduleManager.getModule(Speed.class);
    private final Fly flight = (Fly) LiquidSense.moduleManager.getModule(Fly.class);

    private int consts = 1;
    private final int oldPer = mc.gameSettings.thirdPersonView;

    @EventTarget
    public void onJump(JumpEvent e) {
        if (MovementUtils.isMoving() && canStrafe())
            e.cancelEvent();
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        EntityLivingBase target = killAura.getTarget();

        if (target == null)
            return;

        if (radiusValue.get() <= 0f || !RotationUtils.isVisible(new Vec3(target.posX, target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY, target.posZ)))
            return;

        final double xDist = e.getX();
        final double zDist = e.getZ();
        double lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

        AxisAlignedBB boundingBox = target.getEntityBoundingBox();
        VecRotation vecRotation = RotationUtils.searchCenterforTargetStrafe(boundingBox);
        Rotation limitRotation = RotationUtils.limitAngleChange(serverRotation, vecRotation.getRotation(), 360f / turnShapeValue.get());

        if (autoGodValue.get()) {
            for (int i = 0; i <= mc.gameSettings.thirdPersonView; ++i) {
                if (canStrafe())
                    mc.gameSettings.thirdPersonView = 4;
                else if (mc.gameSettings.thirdPersonView > 3)
                    mc.gameSettings.thirdPersonView = oldPer;
            }
        }

        if (!canStrafe())
            return;

        if (VoidCheck.checkVoid(mc.thePlayer))
            e.isSafeWalk();

        targetRotation.setYaw(limitRotation.getYaw());
        setSpeed(e, lastDist, limitRotation.getYaw(), radiusValue.get(), 1.0);
    }

    @EventTarget
    public void onPacket(PacketEvent e) {
        final Packet<?> packet = e.getPacket();

        if (packet instanceof C03PacketPlayer) {
            if (targetRotation.getYaw() != serverRotation.getYaw())
                serverRotation.setYaw(targetRotation.getYaw());
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent e) {
        if (drawMode.get().equals("None"))
            return;

        drawCircle(killAura.getTarget(), new Color(r.get(), g.get(), b.get(), a.get()));
    }

    private boolean canStrafe() {
        return (killAura.getState() && killAura.getTarget() != null && (alwaysValue.get() || speed.getState() || flight.getState()) && (!onJumpKeyValue.get() || mc.gameSettings.keyBindJump.isKeyDown()));
    }

    private float enemyDist() {
        return (float) mc.thePlayer.getDistance(Objects.requireNonNull(killAura.getTarget()).posX, mc.thePlayer.posY, killAura.getTarget().posZ) + radiusSizeValue.get();
    }

    private float algorithm() {
        final float radius = (radiusValue.get() - (stopTurnValue.get() ? collisionBorderSizeValue.get() * 2f : 0f));
        return Math.max((enemyDist() - radius), enemyDist() - (enemyDist() - radius / (radius * 2f)));
    }

    private void switchDirection() {
        if (mc.thePlayer.isCollidedHorizontally || VoidCheck.checkVoid(mc.thePlayer) || mc.gameSettings.keyBindLeft.isPressed() || mc.gameSettings.keyBindRight.isPressed()) {
            consts *= -1;
        }
    }

    private void setSpeed(MoveEvent event, double moveSpeed, float pseudoYaw, float pseudoStrafe, double pseudoForward) {
        float yaw = pseudoYaw;
        float strafe = pseudoStrafe;
        double forward = pseudoForward;
        float strafe2 = 0f;
        switchDirection();

        if (lockValue.get() && MovementUtils.isMoving()) {
            strafe = pseudoStrafe * mc.thePlayer.moveStrafing * consts;
        } else if (MovementUtils.isMoving()) {
            strafe = (float) consts;
        }

        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (forward > 0.0 ? -45f : 45f) / enemyDist();
                strafe2 += (forward > 0.0 ? -45f : 45f) / algorithm();
            }

            if (strafe < 0.0) {
                yaw += (forward > 0.0 ? 45f : -45f) / enemyDist();
                strafe2 += (forward > 0.0 ? 45f : -45f) / algorithm();
            }

            strafe = 0.0f;

            if (forward > 0.0) {
                forward = 1.0;
            }

            if (forward < 0.0) {
                forward = -1.0;
            }
        }

        if (strafe > 0.0) {
            strafe = 1.0f;
        }

        if (strafe < 0.0) {
            strafe = -1.0f;
        }

        final double x = Math.cos(Math.toRadians(yaw + 90.0 + strafe2));
        final double z = Math.sin(Math.toRadians(yaw + 90.0 + strafe2));
        event.setX(forward * moveSpeed * x + strafe * moveSpeed * z);
        event.setZ(forward * moveSpeed * z - strafe * moveSpeed * x);
    }

    private void drawCircle(EntityLivingBase entity, Color color) {
        if (killAura.getTarget() == null)
            return;

        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
        Cylinder cylinder = new Cylinder();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-90f, 1f, 0f, 0f);
        cylinder.setDrawStyle(100011);

        RenderUtils.glColor(0, 0, 0, 120);
        if (drawMode.get().equals("Outline")) {
            RenderUtils.enableSmoothLine(smoothLineValue.get() * Math.min(1f, mc.thePlayer.getDistanceToEntity(killAura.getTarget())));
            cylinder.draw(radiusValue.get() + 0.0033f * smoothLineValue.get(), radiusValue.get() + 0.0033f * smoothLineValue.get(), 0.0f, shape.get(), 2);
            cylinder.draw(radiusValue.get() - 0.0033f * smoothLineValue.get(), radiusValue.get() - 0.0033f * smoothLineValue.get(), 0.0f, shape.get(), 2);
        }

        if (rainbow.get()) {
            RainbowShader.INSTANCE.setStrengthX(rainbowX.get() == 0.0f ? 0.0f : 1.0f / rainbowX.get());
            RainbowShader.INSTANCE.setStrengthY(rainbowY.get() == 0.0f ? 0.0f : 1.0f / rainbowY.get());
            RainbowShader.INSTANCE.setOffset((System.currentTimeMillis() % 10000) / 10000.0f);
            RainbowShader.INSTANCE.startShader();
        }
        RenderUtils.glColor(color);
        RenderUtils.enableSmoothLine(smoothLineValue.get());
        cylinder.draw(radiusValue.get(), radiusValue.get(), 0.0f, shape.get(), 2);
        if (rainbow.get()) {
            RainbowShader.INSTANCE.stopShader();
        }

        RenderUtils.disableSmoothLine();
        GL11.glPopMatrix();
    }
}
