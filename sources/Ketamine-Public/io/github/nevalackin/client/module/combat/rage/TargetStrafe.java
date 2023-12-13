package io.github.nevalackin.client.module.combat.rage;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.player.MoveEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.world.Render3DEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.math.MathUtil;
import io.github.nevalackin.client.util.movement.MovementUtil;
import io.github.nevalackin.client.util.player.RotationUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public final class TargetStrafe extends Module {

    // Need to be holding space
    public final BooleanProperty holdSpaceProperty = new BooleanProperty("Hold Space", true);
    // Pattern mode
    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.FOLLOW);
    // Radius & points
    private final DoubleProperty pointsProperty = new DoubleProperty("Points", 12, 1, 90, 1);
    private final DoubleProperty radiusProperty = new DoubleProperty("Radius", 2.0, 0.1, 4.0, 0.1);
    // Adaptive
    private final BooleanProperty adaptiveSpeedProperty = new BooleanProperty("Adapt Speed", true);
    //Direction Keys
    private final BooleanProperty directionKeyProperty = new BooleanProperty("Direction Keys", true);
    // Render settings
    private final EnumProperty<RenderMode> renderProperty = new EnumProperty<>("Render Mode", RenderMode.POINTS);
    private final BooleanProperty polyGradientProperty = new BooleanProperty("Poly Gradient", true, () -> this.renderProperty.getValue() == RenderMode.POLYGON);
    // Colours
    private final ColourProperty activePointColorProperty = new ColourProperty("Active", 0x8000FF00,
                                                                               this::shouldRender);
    private final ColourProperty dormantPointColorProperty = new ColourProperty("Dormant", 0x20FFFFFF,
                                                                                this::shouldRender);
    private final ColourProperty invalidPointColorProperty = new ColourProperty("Invalid", 0x20FF0000,
                                                                                () -> this.renderProperty.getValue() == RenderMode.POINTS || (this.polyGradientProperty.check() && this.polyGradientProperty.getValue()));
    // Render width
    private final DoubleProperty widthProperty = new DoubleProperty("Width", 1.0F,
                                                                    () -> this.renderProperty.getValue() == RenderMode.POLYGON,
                                                                    0.5F, 5.0F, 0.5F);

    private final List<Point> currentPoints = new ArrayList<>();
    public EntityLivingBase currentTarget;
    private int direction = 1;
    private Point currentPoint;

    private Aura aura;

    public TargetStrafe() {
        super("Target Strafe", Category.COMBAT, Category.SubCategory.COMBAT_RAGE);

        targetStrafeInstance = this;

        this.register(this.modeProperty,
                      this.holdSpaceProperty, this.pointsProperty, this.radiusProperty,
                      this.adaptiveSpeedProperty, this.directionKeyProperty, this.renderProperty, this.polyGradientProperty,
                      this.activePointColorProperty, this.dormantPointColorProperty, this.invalidPointColorProperty,
                      this.widthProperty);
    }

    private boolean shouldRender() {
        return this.renderProperty.getValue() != RenderMode.OFF;
    }

    @EventLink
    public final Listener<Render3DEvent> onRender3DEvent = event -> {
        if (this.shouldRender() && this.currentTarget != null) {
            final float partialTicks = event.getPartialTicks();
            final int dormantColor = this.dormantPointColorProperty.getValue();
            final int invalidColor = this.invalidPointColorProperty.getValue();

            // Disable texture binding
            glDisable(GL_TEXTURE_2D);
            // Enable blending
            boolean restore = DrawUtil.glEnableBlend();
            // Disable depth testing (see through walls)
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);

            Point lastPoint = null;

            switch (this.renderProperty.getValue()) {
                case POINTS:
                    for (Point point : this.currentPoints) {
                        final Vec3 pos = point.calculateInterpolatedPos(partialTicks);
                        final double x = pos.xCoord;
                        final double y = pos.yCoord;
                        final double z = pos.zCoord;

                        final double pointSize = 0.03;
                        AxisAlignedBB bb = new AxisAlignedBB(x, y, z,
                                                             x + pointSize, y + pointSize, z + pointSize);

                        if (lastPoint == null ||
                            lastPoint == this.currentPoint ||
                            point == this.currentPoint ||
                            lastPoint.valid != point.valid) {
                            int color;

                            if (this.currentPoint == point)
                                color = this.activePointColorProperty.getValue();
                            else if (point.valid) color = dormantColor;
                            else color = invalidColor;
                            DrawUtil.glColour(color);
                        }

                        DrawUtil.glDrawBoundingBox(bb, 0, true);

                        lastPoint = point;
                    }
                    break;
                case POLYGON:
                    // Enable line anti-aliasing
                    glEnable(GL_LINE_SMOOTH);
                    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
                    // Set line width
                    glLineWidth(this.widthProperty.getValue().floatValue());
                    final boolean polyGradient = this.polyGradientProperty.getValue();

                    if (polyGradient) glShadeModel(GL_SMOOTH);
                    else
                        DrawUtil.glColour(this.shouldStrafe() ? this.activePointColorProperty.getValue() : dormantColor);

                    glBegin(GL_LINE_LOOP);

                    for (final Point point : this.currentPoints) {
                        if (polyGradient) {
                            if (lastPoint == null ||
                                lastPoint == currentPoint ||
                                point == currentPoint ||
                                lastPoint.valid != point.valid) {
                                int color;

                                if (currentPoint == point)
                                    color = activePointColorProperty.getValue();
                                else if (point.valid) color = dormantColor;
                                else color = invalidColor;
                                DrawUtil.glColour(color);
                            }
                            lastPoint = point;
                        }

                        final Vec3 pos = point.calculateInterpolatedPos(partialTicks);
                        final double x = pos.xCoord;
                        final double y = pos.yCoord;
                        final double z = pos.zCoord;

                        glVertex3d(x, y, z);
                    }

                    glEnd();

                    if (polyGradient)
                        glShadeModel(GL_FLAT);

                    glDisable(GL_LINE_SMOOTH);
                    break;
            }

            // Disable line anti-aliasing
            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
            // Enable depth testing
            glEnable(GL_DEPTH_TEST);
            glDepthMask(true);
            // Restore blend
            DrawUtil.glRestoreBlend(restore);
            // Enable texture drawing
            glEnable(GL_TEXTURE_2D);
        }
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            this.currentTarget = this.aura.getTarget();

            if (this.currentTarget != null) {
                if (this.directionKeyProperty.getValue()) {
                    if (this.mc.gameSettings.keyBindLeft.isPressed()) {
                        this.direction = 1;
                    }

                    if (this.mc.gameSettings.keyBindRight.isPressed()) {
                        this.direction = -1;
                    }
                }

                this.collectPoints(this.pointsProperty.getValue().intValue(), this.radiusProperty.getValue(), this.currentTarget);
                this.currentPoint = this.findOptimalPoint(this.currentTarget, this.currentPoints);
            } else {
                this.currentPoint = null;
            }
        }
    };

    private Point findOptimalPoint(EntityLivingBase target, List<Point> points) {
        switch (modeProperty.getValue()) {
            case BEHIND:
                float biggestDif = -1.0F;
                Point bestPoint = null;

                for (Point point : points) {
                    if (point.valid) {
                        final float yawChange = Math.abs(RotationUtil.calculateYawFromSrcToDst(target.rotationYaw, target.posX, target.posZ,
                                                                                               point.point.xCoord, point.point.zCoord));
                        if (yawChange > biggestDif) {
                            biggestDif = yawChange;
                            bestPoint = point;
                        }
                    }
                }
                return bestPoint;
            case FOLLOW:
                return getClosestPoint(this.mc.thePlayer.posX, this.mc.thePlayer.posZ, points);
            default:
                final Point closest = getClosestPoint(this.mc.thePlayer.posX, this.mc.thePlayer.posZ, points);

                if (closest == null)
                    return null;

                final int pointsSize = points.size();

                if (pointsSize == 1)
                    return closest;

                final int closestIndex = points.indexOf(closest);

                Point nextPoint;

                int passes = 0;

                do {
                    if (passes > pointsSize) // Note :: Shit fix
                        return null;
                    int nextIndex = closestIndex + this.direction;
                    if (nextIndex < 0) nextIndex = pointsSize - 1;
                    else if (nextIndex >= pointsSize) nextIndex = 0;

                    nextPoint = points.get(nextIndex);

                    if (!nextPoint.valid)
                        this.direction = -this.direction;
                    ++passes;
                } while (!nextPoint.valid);

                return nextPoint;
        }
    }

    private void collectPoints(final int size,
                               final double radius,
                               final EntityLivingBase entity) {
        this.currentPoints.clear();

        final double x = entity.posX;
        final double z = entity.posZ;

        final double pix2 = Math.PI * 2.0;

        for (int i = 0; i < size; i++) {
            double cos = radius * StrictMath.cos(i * pix2 / size);
            double sin = radius * StrictMath.sin(i * pix2 / size);

            final Point point = new Point(entity,
                                          new Vec3(cos, 0, sin),
                                          this.validatePoint(new Vec3(x + cos, entity.posY, z + sin)));

            this.currentPoints.add(point);
        }
    }

    private static Point getClosestPoint(final double srcX, final double srcZ, List<Point> points) {
        double closest = Double.MAX_VALUE;
        Point bestPoint = null;

        for (Point point : points) {
            if (point.valid) {
                final double dist = MathUtil.distance(srcX, srcZ, point.point.xCoord, point.point.zCoord);
                if (dist < closest) {
                    closest = dist;
                    bestPoint = point;
                }
            }
        }

        return bestPoint;
    }

    private boolean validatePoint(final Vec3 point) {
        final EntityPlayer player = this.mc.thePlayer;
        final WorldClient world = this.mc.theWorld;

        final MovingObjectPosition rayTraceResult = this.mc.theWorld.rayTraceBlocks(player.getPositionVector(), point,
                                                                                    false, true, false);

        if (rayTraceResult != null && rayTraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            return false;

        // TODO :: Replace this with bb check

        final BlockPos pointPos = new BlockPos(point);
        final IBlockState blockState = world.getBlockState(pointPos);

        if (blockState.getBlock().canCollideCheck(blockState, false) && !blockState.getBlock().isPassable(this.mc.theWorld, pointPos))
            return false;

        final IBlockState blockStateAbove = world.getBlockState(pointPos.add(0, 1, 0));

        return !blockStateAbove.getBlock().canCollideCheck(blockState, false) &&
            !isOverVoid(point.xCoord, Math.min(point.yCoord, this.mc.thePlayer.posY), point.zCoord);
    }

    private boolean isOverVoid(final double x,
                               final double y,
                               final double z) {
        for (double posY = y; posY > 0.0; posY--) {
            final IBlockState state = this.mc.theWorld.getBlockState(new BlockPos(x, posY, z));
            if (state.getBlock().canCollideCheck(state, false)) {
                return y - posY > 2;
            }
        }

        return true;
    }

    public boolean isCloseToPoint(final Point point) {
        return MathUtil.distance(this.mc.thePlayer.posX, this.mc.thePlayer.posZ, point.point.xCoord, point.point.zCoord) < 0.2;
    }

    public boolean shouldAdaptSpeed() {
        if (!this.adaptiveSpeedProperty.getValue())
            return false;
        return this.isCloseToPoint(this.currentPoint);
    }

    public double getAdaptedSpeed() {
        final EntityLivingBase entity = this.currentTarget;
        if (entity == null) return 0.0;
        return MathUtil.distance(entity.prevPosX, entity.prevPosZ, entity.posX, entity.posZ);
    }

    public boolean shouldStrafe() {
        return isEnabled() &&
            (!this.holdSpaceProperty.getValue() || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) &&
            this.currentTarget != null &&
            this.currentPoint != null;
    }

    public void setSpeed(final MoveEvent event, final double speed) {
        final EntityPlayerSP player = this.mc.thePlayer;
        final Point point = this.currentPoint;
        MovementUtil.setSpeed(event, speed, 1, 0,
                              RotationUtil.calculateYawFromSrcToDst(player.rotationYaw,
                                                                    player.posX, player.posZ,
                                                                    point.point.xCoord, point.point.zCoord));
    }

    public void setMotion(final double speed) {
        final EntityPlayerSP player = this.mc.thePlayer;
        final Point point = this.currentPoint;
        MovementUtil.setMotion(player, 1, 0,
                               RotationUtil.calculateYawFromSrcToDst(player.rotationYaw,
                                                                     player.posX, player.posZ,
                                                                     point.point.xCoord, point.point.zCoord), speed);
    }

    @Override
    public void onEnable() {
        if (this.aura == null) {
            this.aura = KetamineClient.getInstance().getModuleManager().getModule(Aura.class);
        }
    }

    @Override
    public void onDisable() {
        this.currentPoints.clear();
    }

    private enum Mode {
        BEHIND("Behind"),
        FOLLOW("Follow"),
        CIRCLE("Circle");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum RenderMode {
        OFF("Off"),
        POINTS("Points"),
        POLYGON("Polygon");

        private final String name;

        RenderMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private static final class Point {
        private final EntityLivingBase entity;
        private final Vec3 posOffset;
        private final Vec3 point;
        private final boolean valid;

        public Point(final EntityLivingBase entity,
                     final Vec3 posOffset,
                     final boolean valid) {
            this.entity = entity;
            this.posOffset = posOffset;
            this.valid = valid;

            this.point = this.calculatePos();
        }

        private Vec3 calculatePos() {
            return this.entity.getPositionVector().add(this.posOffset);
        }

        private Vec3 calculateInterpolatedPos(final float partialTicks) {
            final double x = DrawUtil.interpolate(this.entity.prevPosX, this.entity.posX, partialTicks);
            final double y = DrawUtil.interpolate(this.entity.prevPosY, this.entity.posY, partialTicks);
            final double z = DrawUtil.interpolate(this.entity.prevPosZ, this.entity.posZ, partialTicks);

            final Vec3 interpolatedEntity = new Vec3(x, y, z);

            return interpolatedEntity.add(this.posOffset);
        }
    }
}
