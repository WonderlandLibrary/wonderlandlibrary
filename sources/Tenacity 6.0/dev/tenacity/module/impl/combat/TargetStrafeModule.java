//package dev.client.tenacity.module.impl.combat;
//
//import dev.client.tenacity.Tenacity;
//import dev.client.tenacity.module.Category;
//import dev.client.tenacity.module.Module;
//import dev.client.tenacity.module.impl.movement.Flight;
//import dev.client.tenacity.module.impl.movement.Speed;
//import dev.client.tenacity.utils.player.MovementUtils;
//import dev.client.tenacity.utils.player.RotationUtils;
//import dev.client.tenacity.utils.render.RenderUtil;
//import dev.event.EventListener;
//import dev.event.impl.player.MotionEvent;
//import dev.event.impl.player.MoveEvent;
//import dev.event.impl.render.Render3DEvent;
//import dev.settings.impl.BooleanSetting;
//import dev.settings.impl.ColorSetting;
//import dev.settings.impl.MultipleBoolSetting;
//import dev.settings.impl.NumberSetting;
//import dev.tenacity.util.player.RotationUtil;
//import dev.utils.animations.Direction;
//import dev.utils.animations.impl.DecelerateAnimation;
//import net.minecraft.block.BlockAir;
//import net.minecraft.block.BlockLiquid;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.potion.Potion;
//import net.minecraft.util.BlockPos;
//import net.minecraft.util.Vec3;
//import org.lwjgl.input.Keyboard;
//
//import java.awt.*;
//
//import static dev.tenacity.util.Utils.mc;
//import static org.lwjgl.opengl.GL11.*;
//
//public final class TargetStrafe extends Module {
//
//    public static final BooleanSetting space = new BooleanSetting("Require space key", true);
//    public static final BooleanSetting auto3rdPerson = new BooleanSetting("Auto 3rd Person", false);
//    public static final NumberSetting radius = new NumberSetting("Radius", 1.5, 8, 0.5, 0.5);
//    private static final MultipleBoolSetting adaptiveSettings = new MultipleBoolSetting("Adaptive",
//            new BooleanSetting("Edges", false),
//            new BooleanSetting("Behind", false),
//            new BooleanSetting("Liquids", false),
//            new BooleanSetting("Controllable", true)
//    );
//    private static int strafe = 1;
//    private final BooleanSetting circle = new BooleanSetting("Draw circle", true);
//    private final ColorSetting color = new ColorSetting("Color", new Color(-16711712));
//    private final DecelerateAnimation animation = new DecelerateAnimation(250, radius.getValue(), Direction.FORWARDS);
//
//    public TargetStrafe() {
//        super("TargetStrafe", Category.COMBAT, "strafe around targets");
//        this.addSettings(adaptiveSettings, auto3rdPerson, space, circle, radius, color);
//    }
//
//    private final EventListener<MotionEvent> onMotion = e -> {
//        if (canStrafe()) {
//            if (auto3rdPerson.isEnabled() && mc.gameSettings.thirdPersonView == 0) {
//                mc.gameSettings.thirdPersonView = 1;
//            }
//            if (mc.thePlayer.isCollidedHorizontally) {
//                strafe = -strafe;
//            } else {
//                if (adaptiveSettings.getSetting("Controllable").isEnabled()) {
//                    if (mc.gameSettings.keyBindLeft.isPressed()) strafe = 1;
//                    if (mc.gameSettings.keyBindRight.isPressed()) strafe = -1;
//                }
//                if (adaptiveSettings.getSetting("Edges").isEnabled() && isInVoid()) {
//                    strafe = -strafe;
//                }
//                if (adaptiveSettings.getSetting("Liquids").isEnabled() && isInLiquid()) {
//                    strafe = -strafe;
//                }
//            }
//        } else if (auto3rdPerson.isEnabled() && mc.gameSettings.thirdPersonView != 0) {
//            mc.gameSettings.thirdPersonView = 0;
//        }
//    };
//
//    private final EventListener<Render3DEvent> onRender3D = e -> {
//        if (circle.isEnabled()) {
//            if (animation.getEndPoint() != radius.getValue()) animation.setEndPoint(radius.getValue());
//            boolean canStrafe = canStrafe();
//            animation.setDirection(canStrafe ? Direction.FORWARDS : Direction.BACKWARDS);
//            if (canStrafe || !animation.isDone()) {
//                drawCircle(5, 0xFF000000);
//                drawCircle(3, color.getColor().getRGB());
//            }
//        }
//    };
//
//    public static boolean strafe(MoveEvent e) {
//        return strafe(e, MovementUtils.getSpeed());
//    }
//
//    public static boolean strafe(MoveEvent e, double moveSpeed) {
//        if (canStrafe()) {
//            setSpeed(e, moveSpeed, RotationUtils.getYaw(KillAura.target.getPositionVector()), strafe,
//                    mc.thePlayer.getDistanceToEntity(KillAura.target) <= radius.getValue() ? 0 : 1);
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean canStrafe() {
//        if (!Tenacity.INSTANCE.isToggled(TargetStrafe.class) || !MovementUtils.isMoving() || (space.isEnabled() && !Keyboard.isKeyDown(Keyboard.KEY_SPACE))) {
//            return false;
//        }
//        if (!(Tenacity.INSTANCE.isToggled(Speed.class) || Tenacity.INSTANCE.isToggled(Flight.class))) {
//            return false;
//        }
//        return Tenacity.INSTANCE.isToggled(KillAura.class)
//                && ((KillAura) (Tenacity.INSTANCE.getModuleCollection().get(KillAura.class))).isValid(KillAura.target)
//                && Tenacity.INSTANCE.isToggled(TargetStrafe.class)
//                && !KillAura.target.isDead;
//    }
//
//    public static void setSpeed(MoveEvent moveEvent, double speed, float yaw, double strafe, double forward) {
//        if (forward == 0 && strafe == 0) {
//            moveEvent.setX(0);
//            moveEvent.setZ(0);
//        } else {
//            if (forward != 0) {
//                if (strafe > 0) {
//                    yaw += ((forward > 0) ? -45 : 45);
//                } else if (strafe < 0) {
//                    yaw += ((forward > 0) ? 45 : -45);
//                }
//                strafe = 0;
//                if (forward > 0) {
//                    forward = 1.0;
//                } else if (forward < 0) {
//                    forward = -1.0;
//                }
//            }
//            if (adaptiveSettings.getSetting("Edges").isEnabled()) {
//                Vec3 pos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
//                Vec3 vec = RotationUtils.getVecRotations(0, 90);
//                if (mc.theWorld.rayTraceBlocks(pos, pos.addVector(vec.xCoord * 5, vec.yCoord * 5, vec.zCoord * 5), false, false, false) == null) {
//                    return;
//                }
//            }
//            if (adaptiveSettings.getSetting("Behind").isEnabled()) {
//                EntityLivingBase target = KillAura.target;
//                double x = target.posX + -Math.sin(Math.toRadians(target.rotationYaw)) * -2;
//                double z = target.posZ + Math.cos(Math.toRadians(target.rotationYaw)) * -2;
//                moveEvent.setX(speed * -Math.sin(Math.toRadians(RotationUtils.getRotations(x, target.posY, z)[0])));
//                moveEvent.setZ(speed * Math.cos(Math.toRadians(RotationUtils.getRotations(x, target.posY, z)[0])));
//            } else {
//                moveEvent.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
//                moveEvent.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
//            }
//        }
//    }
//
//    private void drawCircle(float lineWidth, int color) {
//        EntityLivingBase entity = KillAura.target;
//        if (entity == null) return;
//        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.elapsedPartialTicks - mc.getRenderManager().viewerPosX;
//        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.elapsedPartialTicks - mc.getRenderManager().viewerPosY;
//        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.elapsedPartialTicks - mc.getRenderManager().viewerPosZ;
//
//
//        glPushMatrix();
//        RenderUtil.color(color, (float) ((animation.getOutput() / radius.getValue()) / 2F));
//        glDisable(GL_TEXTURE_2D);
//        glDisable(GL_DEPTH_TEST);
//        glDepthMask(false);
//        glLineWidth(lineWidth);
//        glEnable(GL_BLEND);
//        glEnable(GL_LINE_SMOOTH);
//
//        glBegin(GL_LINE_STRIP);
//        double pi2 = Math.PI * 2.0;
//        for (int i = 0; i <= 90; ++i) {
//            glVertex3d(x + animation.getOutput() * Math.cos(i * pi2 / 45.0), y, z + animation.getOutput() * Math.sin(i * pi2 / 45.0));
//        }
//        glEnd();
//
//        glDisable(GL_BLEND);
//        glDisable(GL_LINE_SMOOTH);
//        glDepthMask(true);
//        glEnable(GL_DEPTH_TEST);
//        glEnable(GL_TEXTURE_2D);
//        glColor4f(1, 1, 1, 1);
//        glPopMatrix();
//    }
//
//    private boolean isInVoid() {
//        double yaw = Math.toRadians(RotationUtil.getYaw(KillAura.target.getPositionVector()));
//        double xValue = -Math.sin(yaw) * 2;
//        double zValue = Math.cos(yaw) * 2;
//        for (int i = 0; i <= 256; i++) {
//            BlockPos b = new BlockPos(mc.thePlayer.posX + xValue, mc.thePlayer.posY - i, mc.thePlayer.posZ + zValue);
//            if (mc.theWorld.getBlockState(b).getBlock() instanceof BlockAir) {
//                if (b.getY() == 0) {
//                    return true;
//                }
//            } else {
//                return false;
//            }
//        }
//        return !mc.thePlayer.isCollidedVertically && !mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0 && mc.thePlayer.motionY != 0 && mc.thePlayer.isAirBorne && !mc.thePlayer.capabilities.isFlying && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isPotionActive(Potion.invisibility.id);
//    }
//
//    private boolean isInLiquid() {
//        double yaw = Math.toRadians(RotationUtils.getYaw(KillAura.target.getPositionVector()));
//        double xValue = -Math.sin(yaw) * 2;
//        double zValue = Math.cos(yaw) * 2;
//        for (int i = 0; i <= 256; i++) {
//            BlockPos b = new BlockPos(mc.thePlayer.posX + xValue, mc.thePlayer.posY - i, mc.thePlayer.posZ + zValue);
//            return mc.theWorld.getBlockState(b).getBlock() instanceof BlockLiquid;
//        }
//        return false;
//    }
//
//}
