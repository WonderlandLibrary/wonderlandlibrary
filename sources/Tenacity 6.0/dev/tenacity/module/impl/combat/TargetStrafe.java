// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import net.minecraft.block.BlockLiquid;
import net.minecraft.potion.Potion;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import dev.tenacity.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import dev.tenacity.utils.server.ServerUtils;
import dev.tenacity.module.impl.movement.Flight;
import dev.tenacity.module.impl.movement.Speed;
import org.lwjgl.input.Keyboard;
import dev.tenacity.Tenacity;
import net.minecraft.entity.Entity;
import dev.tenacity.utils.player.RotationUtils;
import dev.tenacity.module.api.TargetManager;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.player.MoveEvent;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.utils.animations.Direction;
import java.awt.Color;
import dev.tenacity.module.Category;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.Module;

public final class TargetStrafe extends Module
{
    private static final MultipleBoolSetting adaptiveSettings;
    public static final NumberSetting radius;
    private static final NumberSetting points;
    public static final BooleanSetting space;
    public static final BooleanSetting auto3rdPerson;
    private final BooleanSetting render;
    private final ColorSetting color;
    private static int strafe;
    private static int position;
    private final DecelerateAnimation animation;
    private boolean returnState;
    
    public TargetStrafe() {
        super("TargetStrafe", "Target Strafe", Category.COMBAT, "strafe around targets");
        this.render = new BooleanSetting("Render", true);
        this.color = new ColorSetting("Color", new Color(-16711712));
        this.animation = new DecelerateAnimation(250, TargetStrafe.radius.getValue(), Direction.FORWARDS);
        this.addSettings(TargetStrafe.adaptiveSettings, TargetStrafe.radius, TargetStrafe.points, TargetStrafe.space, TargetStrafe.auto3rdPerson, this.render, this.color);
        this.color.addParent(this.render, ParentAttribute.BOOLEAN_CONDITION);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (canStrafe()) {
            if (TargetStrafe.auto3rdPerson.isEnabled() && TargetStrafe.mc.gameSettings.thirdPersonView == 0) {
                TargetStrafe.mc.gameSettings.thirdPersonView = 1;
                this.returnState = true;
            }
            boolean updatePosition = false;
            boolean positive = true;
            if (TargetStrafe.mc.thePlayer.isCollidedHorizontally) {
                TargetStrafe.strafe = -TargetStrafe.strafe;
                updatePosition = true;
                positive = (TargetStrafe.strafe == 1);
            }
            else {
                if (TargetStrafe.adaptiveSettings.getSetting("Controllable").isEnabled()) {
                    if (TargetStrafe.mc.gameSettings.keyBindLeft.isPressed()) {
                        TargetStrafe.strafe = 1;
                        updatePosition = true;
                    }
                    if (TargetStrafe.mc.gameSettings.keyBindRight.isPressed()) {
                        TargetStrafe.strafe = -1;
                        updatePosition = true;
                        positive = false;
                    }
                }
                if (TargetStrafe.adaptiveSettings.getSetting("Edges").isEnabled() && this.isInVoid()) {
                    TargetStrafe.strafe = -TargetStrafe.strafe;
                    updatePosition = true;
                    positive = false;
                }
                if (TargetStrafe.adaptiveSettings.getSetting("Liquids").isEnabled() && this.isInLiquid()) {
                    TargetStrafe.strafe = -TargetStrafe.strafe;
                    updatePosition = true;
                    positive = false;
                }
            }
            if (updatePosition) {
                TargetStrafe.position = (TargetStrafe.position + (positive ? 1 : -1)) % TargetStrafe.points.getValue().intValue();
            }
        }
        else if (TargetStrafe.auto3rdPerson.isEnabled() && TargetStrafe.mc.gameSettings.thirdPersonView != 0 && this.returnState) {
            TargetStrafe.mc.gameSettings.thirdPersonView = 0;
            this.returnState = false;
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (this.render.isEnabled()) {
            if (this.animation.getEndPoint() != TargetStrafe.radius.getValue()) {
                this.animation.setEndPoint(TargetStrafe.radius.getValue());
            }
            final boolean canStrafe = canStrafe();
            this.animation.setDirection(canStrafe ? Direction.FORWARDS : Direction.BACKWARDS);
            if (canStrafe || !this.animation.isDone()) {
                this.drawCircle(5.0f, -16777216);
                this.drawCircle(3.0f, this.color.getColor().getRGB());
            }
        }
    }
    
    public static boolean strafe(final MoveEvent e) {
        return strafe(e, MovementUtils.getSpeed());
    }
    
    public static boolean strafe(final MoveEvent e, final double moveSpeed) {
        if (canStrafe()) {
            setSpeed(e, moveSpeed, RotationUtils.getYaw(TargetManager.target.getPositionVector()), TargetStrafe.strafe, (TargetStrafe.mc.thePlayer.getDistanceToEntity(TargetManager.target) <= TargetStrafe.radius.getValue()) ? 0.0 : 1.0);
            return true;
        }
        return false;
    }
    
    public static boolean canStrafe() {
        final KillAura killAura = Tenacity.INSTANCE.getModuleCollection().getModule(KillAura.class);
        return Tenacity.INSTANCE.isEnabled(TargetStrafe.class) && killAura.isEnabled() && MovementUtils.isMoving() && (!TargetStrafe.space.isEnabled() || Keyboard.isKeyDown(57)) && (Tenacity.INSTANCE.isEnabled(Speed.class) || Tenacity.INSTANCE.isEnabled(Flight.class)) && TargetManager.target != null && killAura.isValid(TargetManager.target);
    }
    
    public static void setSpeed(final MoveEvent moveEvent, double speed, final float yaw, final double strafe, final double forward) {
        final EntityLivingBase target = TargetManager.target;
        final double rad = TargetStrafe.radius.getValue();
        final int count = TargetStrafe.points.getValue().intValue();
        final double a = 6.283185307179586 / count;
        final double posX = StrictMath.sin(a * TargetStrafe.position) * rad * strafe;
        final double posY = StrictMath.cos(a * TargetStrafe.position) * rad;
        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setX(0.0);
            moveEvent.setZ(0.0);
        }
        else {
            if (ServerUtils.isGeniuneHypixel()) {
                speed = Math.min(speed, 0.3375);
            }
            boolean skip = false;
            if (TargetStrafe.adaptiveSettings.getSetting("Edges").isEnabled()) {
                final Vec3 pos = new Vec3(TargetStrafe.mc.thePlayer.posX, TargetStrafe.mc.thePlayer.posY + TargetStrafe.mc.thePlayer.getEyeHeight(), TargetStrafe.mc.thePlayer.posZ);
                final Vec3 vec = RotationUtils.getVecRotations(0.0f, 90.0f);
                if (TargetStrafe.mc.theWorld.rayTraceBlocks(pos, pos.addVector(vec.xCoord * 5.0, vec.yCoord * 5.0, vec.zCoord * 5.0), false, false, false) == null) {
                    moveEvent.setX(0.0);
                    moveEvent.setZ(0.0);
                    skip = true;
                }
            }
            if (!skip) {
                double d;
                if (TargetStrafe.adaptiveSettings.getSetting("Behind").isEnabled()) {
                    final double x = target.posX + -StrictMath.sin(StrictMath.toRadians(target.rotationYaw)) * -2.0;
                    final double z = target.posZ + StrictMath.cos(StrictMath.toRadians(target.rotationYaw)) * -2.0;
                    d = StrictMath.toRadians(RotationUtils.getRotations(x, target.posY, z)[0]);
                }
                else {
                    d = StrictMath.toRadians(RotationUtils.getRotations(target.posX + posX, target.posY, target.posZ + posY)[0]);
                }
                moveEvent.setX(speed * -StrictMath.sin(d));
                moveEvent.setZ(speed * StrictMath.cos(d));
            }
        }
        final double x2 = Math.abs(target.posX + posX - TargetStrafe.mc.thePlayer.posX);
        final double z2 = Math.abs(target.posZ + posY - TargetStrafe.mc.thePlayer.posZ);
        final double dist = StrictMath.sqrt(x2 * x2 + z2 * z2);
        if (dist <= 0.7) {
            TargetStrafe.position = (TargetStrafe.position + TargetStrafe.strafe) % count;
        }
        else if (dist > 3.0) {
            TargetStrafe.position = getClosestPoint(target);
        }
    }
    
    private void drawCircle(final float lineWidth, final int color) {
        final EntityLivingBase entity = TargetManager.target;
        if (entity == null) {
            return;
        }
        GL11.glPushMatrix();
        RenderUtil.color(color, (float)(this.animation.getOutput().floatValue() / TargetStrafe.radius.getValue() / 2.0));
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glBegin(3);
        final EntityLivingBase target = TargetManager.target;
        final float partialTicks = TargetStrafe.mc.timer.elapsedPartialTicks;
        final double rad = TargetStrafe.radius.getValue();
        final double d = 6.283185307179586 / TargetStrafe.points.getValue();
        final double posX = target.posX;
        final double posY = target.posY;
        final double posZ = target.posZ;
        final double lastTickX = target.lastTickPosX;
        final double lastTickY = target.lastTickPosY;
        final double lastTickZ = target.lastTickPosZ;
        final double renderPosX = TargetStrafe.mc.getRenderManager().renderPosX;
        final double renderPosY = TargetStrafe.mc.getRenderManager().renderPosY;
        final double renderPosZ = TargetStrafe.mc.getRenderManager().renderPosZ;
        final double y = lastTickY + (posY - lastTickY) * partialTicks - renderPosY;
        for (double i = 0.0; i < 6.283185307179586; i += d) {
            final double x = lastTickX + (posX - lastTickX) * partialTicks + StrictMath.sin(i) * rad - renderPosX;
            final double z = lastTickZ + (posZ - lastTickZ) * partialTicks + StrictMath.cos(i) * rad - renderPosZ;
            GL11.glVertex3d(x, y, z);
        }
        final double x2 = lastTickX + (posX - lastTickX) * partialTicks - renderPosX;
        final double z2 = lastTickZ + (posZ - lastTickZ) * partialTicks + rad - renderPosZ;
        GL11.glVertex3d(x2, y, z2);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    private boolean isInVoid() {
        final double yaw = Math.toRadians(RotationUtils.getYaw(TargetManager.target.getPositionVector()));
        final double xValue = -Math.sin(yaw) * 2.0;
        final double zValue = Math.cos(yaw) * 2.0;
        for (int i = 0; i <= 256; ++i) {
            final BlockPos b = new BlockPos(TargetStrafe.mc.thePlayer.posX + xValue, TargetStrafe.mc.thePlayer.posY - i, TargetStrafe.mc.thePlayer.posZ + zValue);
            if (!(TargetStrafe.mc.theWorld.getBlockState(b).getBlock() instanceof BlockAir)) {
                return false;
            }
            if (b.getY() == 0) {
                return true;
            }
        }
        return !TargetStrafe.mc.thePlayer.isCollidedVertically && !TargetStrafe.mc.thePlayer.onGround && TargetStrafe.mc.thePlayer.fallDistance != 0.0f && TargetStrafe.mc.thePlayer.motionY != 0.0 && TargetStrafe.mc.thePlayer.isAirBorne && !TargetStrafe.mc.thePlayer.capabilities.isFlying && !TargetStrafe.mc.thePlayer.isInWater() && !TargetStrafe.mc.thePlayer.isOnLadder() && !TargetStrafe.mc.thePlayer.isPotionActive(Potion.invisibility.id);
    }
    
    private boolean isInLiquid() {
        final double yaw = Math.toRadians(RotationUtils.getYaw(TargetManager.target.getPositionVector()));
        final double xValue = -Math.sin(yaw) * 2.0;
        final double zValue = Math.cos(yaw) * 2.0;
        final BlockPos b = new BlockPos(TargetStrafe.mc.thePlayer.posX + xValue, TargetStrafe.mc.thePlayer.posY, TargetStrafe.mc.thePlayer.posZ + zValue);
        return TargetStrafe.mc.theWorld.getBlockState(b).getBlock() instanceof BlockLiquid;
    }
    
    private static int getClosestPoint(final Entity target) {
        final double playerX = TargetStrafe.mc.thePlayer.posX;
        final double playerZ = TargetStrafe.mc.thePlayer.posZ;
        return getPoints(target).stream().min(Comparator.comparingDouble(p -> p.getDistance(playerX, playerZ))).get().iteration;
    }
    
    private static List<Point> getPoints(final Entity target) {
        final double radius = TargetStrafe.radius.getValue();
        final List<Point> pointList = new ArrayList<Point>();
        final int count = TargetStrafe.points.getValue().intValue();
        final double posX = target.posX;
        final double posZ = target.posZ;
        final double d = 6.283185307179586 / count;
        for (int i = 0; i <= count; ++i) {
            final double x = radius * StrictMath.cos(i * d);
            final double z = radius * StrictMath.sin(i * d);
            pointList.add(new Point(posX + x, posZ + z, i));
        }
        return pointList;
    }
    
    static {
        adaptiveSettings = new MultipleBoolSetting("Adaptive", new BooleanSetting[] { new BooleanSetting("Edges", false), new BooleanSetting("Behind", false), new BooleanSetting("Liquids", false), new BooleanSetting("Controllable", true) });
        radius = new NumberSetting("Radius", 2.0, 8.0, 0.5, 0.5);
        points = new NumberSetting("Points", 12.0, 16.0, 3.0, 1.0);
        space = new BooleanSetting("Require space key", true);
        auto3rdPerson = new BooleanSetting("Auto 3rd Person", false);
        TargetStrafe.strafe = 1;
    }
    
    private static class Point
    {
        private final double x;
        private final double z;
        private final int iteration;
        
        private double getDistance(final double posX, final double posZ) {
            final double x2 = Math.abs(posX - this.x);
            final double z2 = Math.abs(posZ - this.z);
            return StrictMath.sqrt(x2 * x2 + z2 * z2);
        }
        
        public double getX() {
            return this.x;
        }
        
        public double getZ() {
            return this.z;
        }
        
        public int getIteration() {
            return this.iteration;
        }
        
        public Point(final double x, final double z, final int iteration) {
            this.x = x;
            this.z = z;
            this.iteration = iteration;
        }
    }
}
