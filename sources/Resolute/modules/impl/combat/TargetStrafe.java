// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventMove;
import java.util.Iterator;
import vip.Resolute.events.impl.EventMotion;
import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import vip.Resolute.util.render.RenderUtils;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import java.util.ArrayList;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import vip.Resolute.util.misc.TimerUtil;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class TargetStrafe extends Module
{
    public static ModeSetting mode;
    public static NumberSetting range;
    public static BooleanSetting onSpace;
    public static BooleanSetting behind;
    public BooleanSetting render;
    public NumberSetting points;
    public NumberSetting width;
    public static BooleanSetting onSpeed;
    public BooleanSetting voidCheck;
    public BooleanSetting dots;
    public ColorSetting colorValue;
    public ColorSetting dormantcolorValue;
    public ColorSetting invalidcolorValue;
    public static int direction;
    public static boolean enabled;
    private static EntityLivingBase currentTarget;
    private static List<Point3D> currentPoints;
    private static Point3D currentPoint;
    public static TargetStrafe instance;
    public TimerUtil timerUtil;
    
    public TargetStrafe() {
        super("TargetStrafe", 0, "Automatically strafes a target", Category.COMBAT);
        this.render = new BooleanSetting("Render", true);
        this.points = new NumberSetting("Points", 40.0, 3.0, 40.0, 1.0);
        this.width = new NumberSetting("Width", 1.0, 0.5, 10.0, 0.5);
        this.voidCheck = new BooleanSetting("Void Check", true);
        this.dots = new BooleanSetting("Dots", true);
        this.colorValue = new ColorSetting("Active Color", new Color(-2147418368));
        this.dormantcolorValue = new ColorSetting("Dormant Color", new Color(553648127));
        this.invalidcolorValue = new ColorSetting("Invalid Color", new Color(553582592));
        this.timerUtil = new TimerUtil();
        this.addSettings(TargetStrafe.mode, TargetStrafe.range, TargetStrafe.onSpace, TargetStrafe.behind, this.render, this.points, this.width, TargetStrafe.onSpeed, this.voidCheck, this.dots, this.colorValue, this.dormantcolorValue, this.invalidcolorValue);
        TargetStrafe.currentPoints = new ArrayList<Point3D>();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        TargetStrafe.enabled = true;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        TargetStrafe.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("");
        if (e instanceof EventRender3D) {
            final EventRender3D event = (EventRender3D)e;
            if (this.render.isEnabled() && KillAura.target != null && !this.dots.isEnabled()) {
                RenderUtils.drawRadius(KillAura.target, TargetStrafe.range.getValue(), event.getPartialTicks(), (int)this.points.getValue(), (float)this.width.getValue(), this.colorValue.getColor());
            }
            else if (this.render.isEnabled() && KillAura.target != null && this.dots.isEnabled()) {
                final float partialTicks = event.getPartialTicks();
                for (final Point3D point : TargetStrafe.currentPoints) {
                    int color;
                    if (TargetStrafe.currentPoint == point) {
                        color = this.colorValue.getColor();
                    }
                    else if (point.valid) {
                        color = this.dormantcolorValue.getColor();
                    }
                    else {
                        color = this.invalidcolorValue.getColor();
                    }
                    final double x = RenderUtils.interpolate(point.prevX, point.x, partialTicks);
                    final double y = RenderUtils.interpolate(point.prevY, point.y, partialTicks);
                    final double z = RenderUtils.interpolate(point.prevZ, point.z, partialTicks);
                    final double pointSize = 0.03;
                    final AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + pointSize, y + pointSize, z + pointSize);
                    RenderUtils.enableBlending();
                    RenderUtils.disableDepth();
                    RenderUtils.disableTexture2D();
                    RenderUtils.color(color);
                    final double renderX = RenderManager.renderPosX;
                    final double renderY = RenderManager.renderPosY;
                    final double renderZ = RenderManager.renderPosZ;
                    GL11.glTranslated(-renderX, -renderY, -renderZ);
                    RenderGlobal.func_181561_a(bb, false, true);
                    GL11.glTranslated(renderX, renderY, renderZ);
                    RenderUtils.disableBlending();
                    RenderUtils.enableDepth();
                    RenderUtils.enableTexture2D();
                }
            }
        }
        if (e instanceof EventMotion && e.isPre()) {
            if (TargetStrafe.mc.thePlayer.isCollidedHorizontally || !isBlockUnder()) {
                TargetStrafe.direction = (byte)(-TargetStrafe.direction);
                return;
            }
            if (TargetStrafe.mc.gameSettings.keyBindLeft.isKeyDown()) {
                TargetStrafe.direction = 1;
                return;
            }
            if (TargetStrafe.mc.gameSettings.keyBindRight.isKeyDown()) {
                TargetStrafe.direction = -1;
            }
            if (KillAura.target != null) {
                this.collectPoints(TargetStrafe.currentTarget = KillAura.target);
                TargetStrafe.currentPoint = this.findOptimalPoint(KillAura.target, TargetStrafe.currentPoints);
            }
            else {
                TargetStrafe.currentTarget = null;
                TargetStrafe.currentPoint = null;
            }
        }
    }
    
    public static TargetStrafe getInstance() {
        return TargetStrafe.instance;
    }
    
    public static void setSpeed(final EventMove event, final double speed) {
        MovementUtils.setTargetStrafeSpeed(event, speed, 1.0f, 0.0f, getYawToPoint(TargetStrafe.currentPoint));
    }
    
    private static float getYawToPoint(final Point3D point) {
        final EntityPlayerSP player = TargetStrafe.mc.thePlayer;
        final double xDist = point.x - player.posX;
        final double zDist = point.z - player.posZ;
        final float rotationYaw = player.rotationYaw;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }
    
    private Point3D findOptimalPoint(final EntityLivingBase target, final List<Point3D> points) {
        if (TargetStrafe.behind.isEnabled()) {
            Point3D bestPoint = null;
            float biggestDif = -1.0f;
            for (final Point3D point : points) {
                if (point.valid) {
                    final float yawChange = Math.abs(this.getYawChangeToPoint(target, point));
                    if (yawChange <= biggestDif) {
                        continue;
                    }
                    biggestDif = yawChange;
                    bestPoint = point;
                }
            }
            return bestPoint;
        }
        return null;
    }
    
    private float getYawChangeToPoint(final EntityLivingBase target, final Point3D point) {
        final double xDist = point.x - target.posX;
        final double zDist = point.z - target.posZ;
        final float rotationYaw = target.rotationYaw;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }
    
    private void collectPoints(final EntityLivingBase entity) {
        final int size = (int)this.points.getValue();
        final double radius = TargetStrafe.range.getValue();
        TargetStrafe.currentPoints.clear();
        final double x = entity.posX;
        final double y = entity.posY;
        final double z = entity.posZ;
        final double prevX = entity.prevPosX;
        final double prevY = entity.prevPosY;
        final double prevZ = entity.prevPosZ;
        for (int i = 0; i < size; ++i) {
            final double cos = radius * StrictMath.cos(i * 6.2831855f / size);
            final double sin = radius * StrictMath.sin(i * 6.2831855f / size);
            final double pointX = x + cos;
            final double pointZ = z + sin;
            final Point3D point = new Point3D(pointX, y, pointZ, prevX + cos, prevY, prevZ + sin, this.validatePoint(pointX, pointZ));
            TargetStrafe.currentPoints.add(point);
        }
    }
    
    private boolean validatePoint(final double x, final double z) {
        final Vec3 pointVec = new Vec3(x, TargetStrafe.mc.thePlayer.posY, z);
        final IBlockState blockState = TargetStrafe.mc.theWorld.getBlockState(new BlockPos(pointVec));
        final boolean canBeSeen = TargetStrafe.mc.theWorld.rayTraceBlocks(TargetStrafe.mc.thePlayer.getPositionVector(), pointVec, false, false, false) == null;
        return !this.isOverVoid(x, z) && !blockState.getBlock().canCollideCheck(blockState, false) && canBeSeen;
    }
    
    private boolean isOverVoid(final double x, final double z) {
        for (double posY = TargetStrafe.mc.thePlayer.posY; posY > 0.0; --posY) {
            if (!(TargetStrafe.mc.theWorld.getBlockState(new BlockPos(x, posY, z)).getBlock() instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBlockUnder() {
        for (int offset = 0; offset < TargetStrafe.mc.thePlayer.posY + TargetStrafe.mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = TargetStrafe.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (!TargetStrafe.mc.theWorld.getCollidingBoundingBoxes(TargetStrafe.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        TargetStrafe.mode = new ModeSetting("Mode", "Dynamic", new String[] { "Dynamic" });
        TargetStrafe.range = new NumberSetting("Range", 2.0, 0.5, 5.0, 0.1);
        TargetStrafe.onSpace = new BooleanSetting("On Space", true);
        TargetStrafe.behind = new BooleanSetting("Behind", true);
        TargetStrafe.onSpeed = new BooleanSetting("Only Speed", true);
        TargetStrafe.direction = -1;
        TargetStrafe.enabled = false;
    }
    
    private static final class Point3D
    {
        private final double x;
        private final double y;
        private final double z;
        private final double prevX;
        private final double prevY;
        private final double prevZ;
        private final boolean valid;
        
        public Point3D(final double x, final double y, final double z, final double prevX, final double prevY, final double prevZ, final boolean valid) {
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
