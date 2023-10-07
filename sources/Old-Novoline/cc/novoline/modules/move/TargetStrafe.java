package cc.novoline.modules.move;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MoveEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.RotationUtil;
import cc.novoline.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static cc.novoline.gui.screen.setting.SettingType.*;
import static cc.novoline.modules.PlayerManager.EnumPlayerType.TARGET;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createColor;
import static java.lang.System.currentTimeMillis;

public final class TargetStrafe extends AbstractModule {

    private static final double DOUBLED_PI = Math.PI * 2.0D;
    private static final double POINT_MULTIPLIER = 3.5;

    /* fields */
    private int position;
    private int direction = 1;


    /* properties @off */
    @Property("range")
    private final DoubleProperty radius = PropertyFactory.createDouble(2D).minimum(0.5D).maximum(4.5D);
    @Property("safe-radius")
    private final DoubleProperty safeRadius = PropertyFactory.createDouble(2D).minimum(0.5D).maximum(4.5D);
    @Property("attack-radius")
    private final DoubleProperty attackRadius = PropertyFactory.createDouble(2D).minimum(0.5D).maximum(4.5D);
    @Property("points-multiplier")
    private final DoubleProperty pointsMultiplier = PropertyFactory.createDouble(2D).minimum(1D).maximum(3.5D);
    @Property("switch-hurt-tick")
    private final IntProperty switchHurtTick = PropertyFactory.createInt(8).minimum(5).maximum(15);
    @Property("dynamic-range")
    private final BooleanProperty dynamicRange = PropertyFactory.booleanFalse();
    @Property("only-target")
    private final BooleanProperty target = PropertyFactory.booleanFalse();
    @Property("points")
    private final BooleanProperty points = PropertyFactory.booleanFalse();
    @Property("space")
    private final BooleanProperty space = PropertyFactory.booleanFalse();
    @Property("controllable")
    private final BooleanProperty controllable = PropertyFactory.booleanFalse();
    @Property("behind")
    private final BooleanProperty behind = PropertyFactory.booleanFalse();
    @Property("autothirdperson")
    private final BooleanProperty autoThirdPerson = PropertyFactory.booleanFalse();
    @Property("color")
    private final ColorProperty color = createColor(0xFF8A8AFF);
    @Property("modules")
    private final ListProperty modules = PropertyFactory.createList("Fly").acceptableValues("Fly", "Speed");
    @Property("avoid-edges")
    private final BooleanProperty avoidEdges = PropertyFactory.booleanFalse();
    @Property("height")
    private final IntProperty height = PropertyFactory.createInt(5).minimum(4).maximum(10);

    private boolean inverted, thirded;
    private final Timer timer = new Timer();

    /* constructors @on */
    public TargetStrafe(@NonNull ModuleManager moduleManager) {
        super(moduleManager, EnumModuleType.COMBAT, "TargetStrafe", "Target Strafe");
        Manager.put(new Setting("TS_RADIUS", "Radius", SettingType.SLIDER, this, radius, 0.1, () -> !dynamicRange.get()));
        Manager.put(new Setting("TS_PMULTIPLIER", "Points Multiplier", SettingType.SLIDER, this, pointsMultiplier, 0.1));
        Manager.put(new Setting("TS_DYNAMIC", "Dynamic Radius", SettingType.CHECKBOX, this, dynamicRange, () -> !behind.get()));
        Manager.put(new Setting("TS_SAFE_RADIUS", "Safe Radius", SettingType.SLIDER, this, safeRadius, 0.1, () -> dynamicRange.get()));
        Manager.put(new Setting("TS_ATTACK_RADIUS", "Attack Radius", SettingType.SLIDER, this, attackRadius, 0.1, () -> dynamicRange.get()));
        Manager.put(new Setting("TS_DYNAMIC_SWITCH_TICK", "Switch Hurt Tick", SettingType.SLIDER, this, switchHurtTick, 1, () -> dynamicRange.get()));
        Manager.put(new Setting("TS_TAR", "Only target", SettingType.CHECKBOX, this, target));
        Manager.put(new Setting("TS_SPACE", "On jump key", SettingType.CHECKBOX, this, space));
        Manager.put(new Setting("TS_CONTROL", "Controllable", SettingType.CHECKBOX, this, controllable));
        Manager.put(new Setting("TS_BEHIND", "Behind", SettingType.CHECKBOX, this, behind, () -> !dynamicRange.get()));
        Manager.put(new Setting("POINTS", "Draw circle", SettingType.CHECKBOX, this, points));
        Manager.put(new Setting("ATP", "Auto Third Person", SettingType.CHECKBOX, this, autoThirdPerson));
        Manager.put(new Setting("TS_CIRCLE_COLOR", "Circle color", COLOR_PICKER, this, color, null, points::get));
        Manager.put(new Setting("TS_MODS", "Support", SELECTBOX, this, modules));
        Manager.put(new Setting("TS_EDGES", "Stop on Edges", CHECKBOX, this, avoidEdges));
        Manager.put(new Setting("TS_EDGES_MIN_AMOUNT", "Maximum Height", SLIDER, this, height, 1, avoidEdges::get));
    }

    private float dist = 0;

    @EventTarget
    public void onTick(TickUpdateEvent tickUpdateEvent) {
        if (mc.player.movementInput().getMoveStrafe() < 0) {
            direction = -1;
        } else if (mc.player.movementInput().getMoveStrafe() > 0) {
            direction = 1;
        }

        if (isCollided()) {
            if (timer.delay(200)) {
                inverted = !inverted;
                position = inverted ? position - 1 : position + 1;
            }
            timer.reset();
        }

        KillAura killAura = getModule(KillAura.class);

        double rad = getRadius(killAura.getTarget());
        int positionsCount = (int) (Math.PI * rad);

        double radianPerPosition = DOUBLED_PI / (float) positionsCount;
        Entity target = killAura.getTarget();
        double posX = MathHelper.sin(radianPerPosition * (position + 1) * rad * (controllable.get() ? direction : 1)),
                posY = MathHelper.cos(radianPerPosition * (position + 1)) * rad;
        if (!isVoidBelow(target.posX + posX, target.posY, target.posZ + posY)) {
            inverted = !inverted;
        }


        dist = 0.7F;

        if (autoThirdPerson.get()) {
            if (!(killAura.isEnabled() && killAura.getTarget() != null && killAura.shouldAttack() && shouldTarget())) {
                setThirded(false);
                mc.gameSettings.thirdPersonView = 0;
            }
        }

    }

    /* methods */
    public void circleStrafe(MoveEvent event, double movementSpeed, Entity target) {
        double rad = getRadius(target);
        int positionsCount = (int) ((int) (Math.PI * rad) * pointsMultiplier.get());

        double radianPerPosition = DOUBLED_PI / (float) positionsCount, // @off
                posX = MathHelper.sin(radianPerPosition * position) * rad * (controllable.get() ? direction : 1),
                posY = MathHelper.cos(radianPerPosition * position) * rad;// @on

        Vec3 myPos = new Vec3(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        Vec3 rotVec = getVectorForRotation(90, 0);
        Vec3 multiplied = myPos.addVector(rotVec.xCoord * height.get(), rotVec.yCoord * height.get(), rotVec.zCoord * height.get());
        MovingObjectPosition movingObjectPosition = mc.world.rayTraceBlocks(myPos, multiplied, false, false, false);

        if (!avoidEdges.get() || movingObjectPosition != null) {
            if (autoThirdPerson.get()) {
                setThirded(true);
                mc.gameSettings.thirdPersonView = 1;
            }
            if (behind.get()) {
                double xPos = target.posX + -Math.sin(Math.toRadians(target.rotationYaw)) * -2, zPos = target.posZ + Math.cos(Math.toRadians(target.rotationYaw)) * -2;
                event.setX(movementSpeed * -MathHelper.sin(Math.toRadians(RotationUtil.getRotations(xPos, target.posY, zPos)[0])));
                event.setZ(movementSpeed * MathHelper.cos(Math.toRadians(RotationUtil.getRotations(xPos, target.posY, zPos)[0])));
            } else {
                event.setX(movementSpeed * -MathHelper
                        .sin(Math.toRadians(RotationUtil.getRotations(target.posX + posX, target.posY, target.posZ + posY)[0])));
                event.setZ(movementSpeed * MathHelper
                        .cos(Math.toRadians(RotationUtil.getRotations(target.posX + posX, target.posY, target.posZ + posY)[0])));
            }
        } else {
            event.setX(0);
            event.setZ(0);
        }

        double x = Math.abs(target.posX + posX - mc.player.posX);
        double z = Math.abs(target.posZ + posY - mc.player.posZ);
        double sqrt = Math.sqrt(x * x + z * z);

        if (sqrt <= dist) {
            position = position + (inverted ? -1 : 1) % positionsCount;
        } else if (sqrt > 3) {
            position = getClosestPoint(target);
        }
    }

    private double getRadius(Entity target) {
        if (dynamicRange.get()) {
            if (target.hurtResistantTime <= switchHurtTick.get().intValue()) {
                return attackRadius.get().doubleValue();
            } else {
                return safeRadius.get().doubleValue();
            }
        } else {
            return radius.get().doubleValue();
        }
    }


    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(Math.toRadians(-yaw) - (float) Math.PI);
        float f1 = MathHelper.sin(Math.toRadians(-yaw) - (float) Math.PI);
        float f2 = -MathHelper.cos(Math.toRadians(-pitch));
        float f3 = MathHelper.sin(Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    private boolean isVoidBelow(double x, double y, double z) {
        for (int i = (int) y; i > 0; i--) {
            if (mc.world.getBlockState(new BlockPos(x, i, z)).getBlock() != Blocks.air) {
                return true;
            }
        }
        return false;
    }

    private int getClosestPoint(Entity target) {
        return getPoints(target).stream().sorted(Comparator.comparingDouble(Point::getDistanceToPlayer)).collect(Collectors.toList()).get(0).poscount;
    }

    private boolean isCollided() {
        return !mc.world
                .getCollidingBoundingBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, 0, -0.5))
                .isEmpty() || !mc.world
                .getCollidingBoundingBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.5, 0, 0.0))
                .isEmpty() || !mc.world
                .getCollidingBoundingBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, 0, 0.5))
                .isEmpty() || !mc.world
                .getCollidingBoundingBoxes(mc.player, mc.player.getEntityBoundingBox().offset(-0.5, 0, 0.0))
                .isEmpty();
    }

    private boolean space() {
        return !space.get() || mc.gameSettings.keyBindJump.isKeyDown();
    }

    public boolean support() {
        return space() && modules.contains("Speed") && isEnabled(Speed.class);
    }

    public boolean shouldTarget() {
        return isEnabled(KillAura.class) && getModule(KillAura.class).getTarget() != null && mc.player
                .canEntityBeSeen(getModule(KillAura.class).getTarget()) && (!this.target.get() || novoline
                .getPlayerManager().hasType(getModule(KillAura.class).getTarget().getName(), TARGET)) //
                && this.mc.player.getDistance2D(getModule(KillAura.class).getTarget()) < getModule(KillAura.class)
                .getRange().get() + 2 && this.mc.player.posY >= getModule(KillAura.class).getTarget().posY - 3.4//
                && getModule(KillAura.class).getTarget().isEntityAlive() && this.mc.player
                .isMoving() && this.mc.player.posY <= getModule(KillAura.class)
                .getTarget().posY + 3.4 && support();
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return inverted;
    }

    private List<Point> getPoints(Entity target) {
        List<Point> points = new CopyOnWriteArrayList<>();
        double rad = getRadius(target);
        final int positionsCount = (int) ((int) (Math.PI * rad) * pointsMultiplier.get());
        for (int i = 0; i <= positionsCount; i++) {
            double radianPerPosition = DOUBLED_PI / (float) positionsCount, // @off
                    posX = MathHelper.sin(radianPerPosition * i) * rad,
                    posY = MathHelper.cos(radianPerPosition * i) * rad; // @on
            points.add(new Point(target.posX + posX, target.posZ + posY, i));
        }
        return points;
    }

    @EventTarget
    public void onRender3D(Render3DEvent render3DEvent) {
        KillAura aura = getModule(KillAura.class);
        RenderUtils.pre3D();

        if (aura.getTarget() != null &&
                points.get() &&
                mc.player.getDistanceToEntity(aura.getTarget()) < aura.getRange().get() &&
                aura.getTarget().isEntityAlive() &&
                shouldTarget()) {

            GL11.glColor4f(0, 0, 0, 1);
            renderCicle(5, aura, render3DEvent);
            GL11.glColor4f(color.getAwtColor().getRed() / 255F, color.getAwtColor().getGreen() / 255F, color.getAwtColor().getBlue() / 255F, 1);
            renderCicle(2, aura, render3DEvent);
        }
        GlStateManager.disableBlend();
        RenderUtils.post3D();
    }


    private void renderCicle(long lw, KillAura aura, Render3DEvent render3DEvent) {
        GL11.glLineWidth(lw);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        double rad = getRadius(aura.getTarget());
        double piDivider = Math.PI * 2 / (Math.PI * rad * pointsMultiplier.get());
        for (double d = 0; d < Math.PI * 2; d += piDivider) {
            double x = aura.getTarget().lastTickPosX + (aura.getTarget().posX - aura.getTarget().lastTickPosX) * render3DEvent.getPartialTicks() + Math.sin(d) * rad - mc.getRenderManager().renderPosX, // @off
                    y = aura.getTarget().lastTickPosY + (aura.getTarget().posY - aura.getTarget().lastTickPosY) * render3DEvent.getPartialTicks() - mc.getRenderManager().renderPosY,
                    z = aura.getTarget().lastTickPosZ + (aura.getTarget().posZ - aura.getTarget().lastTickPosZ) * render3DEvent.getPartialTicks() + Math.cos(d) * rad - mc.getRenderManager().renderPosZ; // @on
            GL11.glVertex3d(x, y, z);
        }
        double x = aura.getTarget().lastTickPosX + (aura.getTarget().posX - aura.getTarget().lastTickPosX) * render3DEvent.getPartialTicks() + Math.sin(0) * rad - mc.getRenderManager().renderPosX, // @off
                y = aura.getTarget().lastTickPosY + (aura.getTarget().posY - aura.getTarget().lastTickPosY) * render3DEvent.getPartialTicks() - mc.getRenderManager().renderPosY,
                z = aura.getTarget().lastTickPosZ + (aura.getTarget().posZ - aura.getTarget().lastTickPosZ) * render3DEvent.getPartialTicks() + Math.cos(0) * rad - mc.getRenderManager().renderPosZ; // @on
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
    }


    private int getArrayRainbowHue(int counter) {
        final int delay = 100 * 5, // @off
                width = 190; // @on
        final float brightness = (counter * (0.2F - 0.001F * width) + (currentTimeMillis() % delay) / (float) delay) % 1F;
        final float[] hudHSB = color.getHSB();
        return Color.getHSBColor(hudHSB[0], hudHSB[1], brightness < 0.4F ? 0.9F - brightness : brightness).getRGB();
    }

    public BooleanProperty getTarget() {
        return this.target;
    }

    public BooleanProperty isOnSpace() {
        return space;
    }

    public boolean isThirded() {
        return thirded;
    }

    public void setThirded(boolean thirded) {
        this.thirded = thirded;
    }

    //endregion

    public static class Point {

        public double x;
        public double z;
        public int poscount;

        public Point(double x, double z, int poscount) {
            this.x = x;
            this.z = z;
            this.poscount = poscount;
        }

        public double getDistanceToPlayer() {
            double x2 = Math.abs(Minecraft.getInstance().player.posX - x);
            double z2 = Math.abs(Minecraft.getInstance().player.posZ - z);
            return Math.sqrt(x2 * x2 + z2 * z2);
        }

    }

}