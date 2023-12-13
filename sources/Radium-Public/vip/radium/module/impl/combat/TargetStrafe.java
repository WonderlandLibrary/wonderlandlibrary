package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.Render3DEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.OGLUtils;
import vip.radium.utils.render.RenderingUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glTranslated;

@ModuleInfo(label = "Target Strafe", category = ModuleCategory.COMBAT)
public final class TargetStrafe extends Module {

    private static final float DOUBLE_PI = (float) StrictMath.PI * 2;
    public final Property<Boolean> holdSpaceProperty = new Property<>("Hold Space", true);
    private final EnumProperty<TargetMode> targetMode = new EnumProperty<>("Target Mode", TargetMode.AUTO);
    private final DoubleProperty targetRange = new DoubleProperty("Target Range", 8.0, () -> targetMode.getValue() != TargetMode.AUTO, 1.0, 32.0, 0.1, Representation.DISTANCE);
    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.FOLLOW);
    private final DoubleProperty radiusProperty = new DoubleProperty("Radius", 2.0, 0.1, 4.0, 0.1, Representation.DISTANCE);
    private final Property<Boolean> adaptiveSpeedProperty = new Property<>("Adapt Speed", true);

    private final Property<Boolean> renderProperty = new Property<>("Render", true);
    private final DoubleProperty pointsProperty = new DoubleProperty("Points", 12, 1, 90, 1);
    private final Property<Integer> activePointColorProperty = new Property<>("Active", 0x8000FF00,
            renderProperty::getValue);
    private final Property<Integer> dormantPointColorProperty = new Property<>("Dormant", 0x20FFFFFF,
            renderProperty::getValue);
    private final Property<Integer> invalidPointColorProperty = new Property<>("Invalid", 0x20FF0000,
            renderProperty::getValue);

    private final List<Point3D> currentPoints = new ArrayList<>();
    public EntityLivingBase currentTarget;
    private Point3D currentPoint;
    @EventLink(EventBusPriorities.HIGHEST)
    public final Listener<Render3DEvent> onRender3DEvent = event -> {
        if (renderProperty.getValue() && currentTarget != null) {
            float partialTicks = event.getPartialTicks();
            for (Point3D point : currentPoints) {
                int color;
                if (currentPoint == point)
                    color = activePointColorProperty.getValue();
                else if (point.valid)
                    color = dormantPointColorProperty.getValue();
                else
                    color = invalidPointColorProperty.getValue();

                double x = RenderingUtils.interpolate(point.prevX, point.x, partialTicks);
                double y = RenderingUtils.interpolate(point.prevY, point.y, partialTicks);
                double z = RenderingUtils.interpolate(point.prevZ, point.z, partialTicks);
                double pointSize = 0.03D;
                AxisAlignedBB bb = new AxisAlignedBB(x, y, z,
                        x + pointSize, y + pointSize, z + pointSize);
                OGLUtils.enableBlending();
                OGLUtils.disableDepth();

                OGLUtils.disableTexture2D();
                OGLUtils.color(color);
                double renderX = RenderManager.renderPosX;
                double renderY = RenderManager.renderPosY;
                double renderZ = RenderManager.renderPosZ;
                glTranslated(-renderX, -renderY, -renderZ);
                RenderGlobal.func_181561_a(bb, false, true);
                glTranslated(renderX, renderY, renderZ);
                GL11.glDisable(GL11.GL_BLEND);
                OGLUtils.enableDepth();
                OGLUtils.enableTexture2D();
            }
        }
    };
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            switch (targetMode.getValue()) {
                case AUTO:
                    currentTarget = ModuleManager.getInstance(KillAura.class).getTarget();
                    break;
                case CLOSEST:
                    currentTarget = null;

                    final List<EntityLivingBase> entities = Wrapper.getLivingEntities(this::isValid);

                    float closest = this.targetRange.getValue().floatValue();

                    for (EntityLivingBase entity : entities) {
                        final float dist = Wrapper.getPlayer().getDistanceToEntity(entity);

                        if (dist < closest) {
                            currentTarget = entity;
                            closest = dist;
                        }
                    }
            }

            if (currentTarget != null) {
                collectPoints(currentTarget);
                currentPoint = findOptimalPoint(currentTarget, currentPoints);
            } else currentPoint = null;
        }
    };

    public static TargetStrafe getInstance() {
        return ModuleManager.getInstance(TargetStrafe.class);
    }

    private static Point3D getClosestPoint(List<Point3D> points) {
        double closest = Double.MAX_VALUE;
        Point3D bestPoint = null;

        for (Point3D point : points) {
            if (point.valid) {
                final double dist = getDistXZToPoint(point);
                if (dist < closest) {
                    closest = dist;
                    bestPoint = point;
                }
            }
        }

        return bestPoint;
    }

    private static double getDistXZToPoint(Point3D point) {
        final Entity localPlayer = Wrapper.getPlayer();
        double xDist = point.x - localPlayer.posX;
        double zDist = point.z - localPlayer.posZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    private static float getYawChangeToPoint(EntityLivingBase target, Point3D point) {
        double xDist = point.x - target.posX;
        double zDist = point.z - target.posZ;
        float rotationYaw = target.rotationYaw;
        float var1 = (float) (StrictMath.atan2(zDist, xDist) * 180.0D / StrictMath.PI) - 90.0F;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }

    private static boolean validatePoint(final double x, final double y, final double z) {
        final EntityPlayer player = Wrapper.getPlayer();
        final WorldClient world = Wrapper.getWorld();
        final Vec3 pointVec = new Vec3(x, y, z);
        final IBlockState blockState = world.getBlockState(new BlockPos(pointVec));

        final MovingObjectPosition rayTraceResult = Wrapper.getWorld().rayTraceBlocks(player.getPositionVector(), pointVec,
                false, true, false);

        // TODO: Dont u dare forget to do this nigga
//        if (rayTraceResult != null && rayTraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
//            return false;

        return !isOverVoid(x, z) && !blockState.getBlock().canCollideCheck(blockState, false);
    }

    private static boolean isOverVoid(final double x, final double z) {
        // Note: This is different to MovementUtils#isOverVoid
        // since it uses the points x and z pos
        final double startY = Wrapper.getPlayer().posY;
        for (double posY = startY; posY > 0.0; posY--) {
            final IBlockState state = Wrapper.getWorld().getBlockState(new BlockPos(x, posY, z));
            if (state.getBlock().canCollideCheck(state, false)) {
                return startY - posY > 3;
            }
        }

        return true;
    }

    private boolean isValid(EntityLivingBase entity) {
        return !(entity instanceof EntityPlayerSP) && entity.isEntityAlive() && entity.getDistanceToEntity(Wrapper.getPlayer()) < targetRange.getValue();
    }

    public boolean shouldAdaptSpeed() {
        if (!adaptiveSpeedProperty.getValue())
            return false;
        EntityPlayerSP player = Wrapper.getPlayer();
        double xDist = currentPoint.x - player.posX;
        double zDist = currentPoint.z - player.posZ;
        return StrictMath.sqrt(xDist * xDist + zDist * zDist) < 0.2;
    }

    public double getAdaptedSpeed() {
        if (currentTarget == null)
            return 0.0D;
        double xDist = currentTarget.posX - currentTarget.prevPosX;
        double zDist = currentTarget.posZ - currentTarget.prevPosZ;
        return StrictMath.sqrt(xDist * xDist + zDist * zDist);
    }

    public boolean shouldStrafe() {
        return currentTarget != null && currentPoint != null;
    }

    public void setSpeed(MoveEntityEvent event, double speed) {
        MovementUtils.setSpeed(event, speed, 1, 0, getYawToPoint(currentPoint));
    }

    private float getYawToPoint(Point3D point) {
        EntityPlayerSP player = Wrapper.getPlayer();
        double xDist = point.x - player.posX;
        double zDist = point.z - player.posZ;
        float rotationYaw = player.rotationYaw;
        float var1 = (float) (StrictMath.atan2(zDist, xDist) * 180.0D / StrictMath.PI) - 90.0F;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }

    private Point3D findOptimalPoint(EntityLivingBase target, List<Point3D> points) {
        switch (modeProperty.getValue()) {
            case BEHIND:
                float biggestDif = -1.0F;
                Point3D bestPoint = null;

                for (Point3D point : points) {
                    if (point.valid) {
                        final float yawChange = Math.abs(getYawChangeToPoint(target, point));
                        if (yawChange > biggestDif) {
                            biggestDif = yawChange;
                            bestPoint = point;
                        }
                    }
                }
                return bestPoint;
            case FOLLOW:
                return getClosestPoint(points);
            default:
                final Point3D closest = getClosestPoint(points);

                Point3D nextPoint;
                int currOff = 0;
                final int pointsSize = points.size();

                do {
                    currOff--;
                    if (-currOff >= pointsSize)
                        return null;
                    final int nextIndex = points.indexOf(closest) - currOff;
                    nextPoint = points.get(nextIndex < 0 ? pointsSize - 1 : nextIndex >= pointsSize ? 0 : nextIndex);
                } while (nextPoint == null || !nextPoint.valid);

                return nextPoint;
        }
    }

    private void collectPoints(EntityLivingBase entity) {
        int size = pointsProperty.getValue().intValue();
        double radius = radiusProperty.getValue();

        currentPoints.clear();

        final double x = entity.posX;
        final double y = entity.posY;
        final double z = entity.posZ;

        final double prevX = entity.prevPosX;
        final double prevY = entity.prevPosY;
        final double prevZ = entity.prevPosZ;

        for (int i = 0; i < size; i++) {
            double cos = radius * StrictMath.cos(i * DOUBLE_PI / size);
            double sin = radius * StrictMath.sin(i * DOUBLE_PI / size);

            double pointX = x + cos;
            double pointZ = z + sin;

            final Point3D point = new Point3D(
                    pointX, y, pointZ,
                    prevX + cos, prevY, prevZ + sin,
                    validatePoint(pointX, y, pointZ));

            currentPoints.add(point);
        }
    }

    private enum Mode {
        BEHIND,
        FOLLOW,
        CIRCLE
    }

    private enum TargetMode {
        AUTO,
        CLOSEST
    }

    private static final class Point3D {
        private final double x;
        private final double y;
        private final double z;
        private final double prevX;
        private final double prevY;
        private final double prevZ;

        private final boolean valid;

        public Point3D(double x, double y, double z, double prevX, double prevY, double prevZ, boolean valid) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.prevX = prevX;
            this.prevY = prevY;
            this.prevZ = prevZ;
            this.valid = valid;
        }
    }
}
