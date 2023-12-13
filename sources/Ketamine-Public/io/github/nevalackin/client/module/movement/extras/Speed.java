package io.github.nevalackin.client.module.movement.extras;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.player.MoveEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.module.misc.world.Scaffold;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.math.MathUtil;
import io.github.nevalackin.client.util.movement.FrictionUtil;
import io.github.nevalackin.client.util.movement.JumpUtil;
import io.github.nevalackin.client.util.movement.MovementUtil;
import io.github.nevalackin.client.util.player.RotationUtil;
import io.github.nevalackin.client.util.render.BlurUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.Timer;
import java.util.TimerTask;

public final class Speed extends Module {

    private static final double[] LOW_HOP_Y_POSITIONS = {
        MathUtil.round(0.4, 0.001),
        MathUtil.round(0.71, 0.001),
        MathUtil.round(0.75, 0.001),
        MathUtil.round(0.55, 0.001),
        MathUtil.round(0.41, 0.001)
    };

    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.NCP);
    private final DoubleProperty vanillaSpeedProperty = new DoubleProperty("Vanilla Speed", 1.0,
                                                                           () -> this.modeProperty.getValue() == Mode.VANILLA,
                                                                           0.1, 2.0, 0.1);
    private final EnumProperty<Friction> frictionProperty = new EnumProperty<>("Friction", Friction.NCP, this::isNCP);
    private final BooleanProperty watchdogProperty = new BooleanProperty("Watchdog", true, this::isNCP);
    private final BooleanProperty lowHopProperty = new BooleanProperty("Low Hop", true, this::isNCP);
    private final DoubleProperty boostProperty = new DoubleProperty("Boost", 1.7, this::isNCP, 1.0, 2.15, 0.01);
    private final DoubleProperty reduceProperty = new DoubleProperty("Reduce", 0.8, this::isNCP, 0, 1.0, 0.01);

    private double moveSpeed;
    private double lastDist;
    private boolean wasOnGround;

    private boolean wasInitialLowHop;

    private boolean disable;

    private long timeOfFlag;
    private final Timer turnBackOnTimer = new Timer();
    private final long turnBackOnDelay = 2000L;

    private Scaffold scaffold;

    public Speed() {
        super("Speed", Category.MOVEMENT, Category.SubCategory.MOVEMENT_EXTRAS);

        this.setSuffix(() -> this.modeProperty.getValue().toString());

        this.register(this.modeProperty, this.frictionProperty, this.vanillaSpeedProperty, this.lowHopProperty, this.boostProperty, this.reduceProperty);
    }

    private boolean isNCP() {
        return this.modeProperty.getValue() == Mode.NCP;
    }

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (!this.disable && event.isPre() && this.isNCP()) {
            final double xDist = event.getLastTickPosX() - event.getPosX();
            final double zDist = event.getLastTickPosZ() - event.getPosZ();
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

            if (this.watchdogProperty.getValue() && MovementUtil.isMoving(this.mc.thePlayer) && !event.isOnGround()) {
//                final double roundedPos = Math.round(event.getPosY() / 0.015625F) * 0.015625F;
//
//                if (Math.abs(event.getPosY() - roundedPos) <= 0.005) {
//                    event.setPosY(roundedPos);
                    event.setOnGround(true);
//                }
            }
        }
    };

    private double calculateBoostModifier() {
        return this.scaffold.isEnabled() ? MovementUtil.SPRINTING_MOD : this.boostProperty.getValue();
    }

    private void runNCPMotionSim(final double[] motion,
                                 final double baseMoveSpeed,
                                 final double lastDist,
                                 final double yDistFromGround,
                                 final int nthTick) {
        switch (nthTick) {
            case 0:
                MovementUtil.setMotion(this.mc.thePlayer, motion, this.calculateBoostModifier() * baseMoveSpeed);
                motion[1] = MovementUtil.getBaseMoveSpeed(this.mc.thePlayer);
                break;
            case 1:
                final double bunnySlope = this.reduceProperty.getValue() * (this.lastDist - baseMoveSpeed);
                MovementUtil.setMotion(this.mc.thePlayer, motion, this.lastDist - bunnySlope);
                break;
            default:
                final double speed = Math.sqrt(motion[0] * motion[0] + motion[1] * motion[1]);
                MovementUtil.setMotion(this.mc.thePlayer, motion, speed);
                break;
        }

        // Apply friction
        motion[0] *= 0.91F;
        motion[2] *= 0.91F;
    }

    private boolean simJumpShouldDoLowHop(final double baseMoveSpeedRef) {
        // Calculate the direction moved in
        final float direction = RotationUtil.calculateYawFromSrcToDst(this.mc.thePlayer.rotationYaw,
                                                                      this.mc.thePlayer.lastReportedPosX, this.mc.thePlayer.lastReportedPosZ,
                                                                      this.mc.thePlayer.posX, this.mc.thePlayer.posZ);
        final Vec3 start = new Vec3(this.mc.thePlayer.posX,
                                    this.mc.thePlayer.posY + LOW_HOP_Y_POSITIONS[2],
                                    this.mc.thePlayer.posZ);
        // Cast a ray at waist height in the direction moved in for 10 blocks
        final MovingObjectPosition rayTrace = mc.theWorld.rayTraceBlocks(start,
                                                                         RotationUtil.getDstVec(start, direction, 0.0F, 8),
                                                                         false, true, true);
        // If did not hit anything just continue
        if (rayTrace == null) return true;
        if (rayTrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return true;
        if (rayTrace.hitVec == null) return true;

        // Check if player can fit above
        final AxisAlignedBB bb = this.mc.thePlayer.getEntityBoundingBox();
        if (this.mc.theWorld.checkBlockCollision(
            bb.offset(bb.minX - rayTrace.hitVec.xCoord,
                      bb.minY - rayTrace.hitVec.yCoord,
                      bb.minZ - rayTrace.hitVec.zCoord)))
            return false;

        // Distance to the block hit
        final double dist = start.distanceTo(rayTrace.hitVec);
        final double normalJumpDist = 4.0;
        return dist > normalJumpDist;
    }

    @EventLink
    private final Listener<MoveEvent> onMove = event -> {
        if (!this.disable && MovementUtil.isMoving(this.mc.thePlayer)) {
            final double baseMoveSpeed = MovementUtil.getBaseMoveSpeed(this.mc.thePlayer);

            switch (this.modeProperty.getValue()) {
                case NCP:
                    if (!this.mc.thePlayer.onGround && this.wasInitialLowHop && this.mc.thePlayer.fallDistance < 0.54)
                        event.setY(this.mc.thePlayer.motionY = this.lowHopYModification(this.mc.thePlayer.motionY,
                                                                                        MathUtil.round(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 0.001)));

                    if (this.mc.thePlayer.onGround && !this.wasOnGround) {
                        final double maxDist = this.boostProperty.getValue() - 1.0E-4;

                        this.moveSpeed = baseMoveSpeed * maxDist;

                        boolean doInitialLowHop = this.lowHopProperty.getValue() &&
                            !this.mc.thePlayer.isPotionActive(Potion.jump) &&
                            !this.scaffold.isEnabled() && !this.mc.thePlayer.isCollidedHorizontally &&
                            this.simJumpShouldDoLowHop(baseMoveSpeed);

                        this.wasInitialLowHop = doInitialLowHop;

                        event.setY(this.mc.thePlayer.motionY = doInitialLowHop ? 0.4F : JumpUtil.getJumpHeight(this.mc.thePlayer));

                        this.wasOnGround = true;
                    } else if (this.wasOnGround) {
                        this.wasOnGround = false;

                        final double bunnySlope = this.reduceProperty.getValue() * (this.lastDist - baseMoveSpeed);
                        this.moveSpeed = this.lastDist - bunnySlope;
                    } else {
                        this.moveSpeed = this.frictionProperty.getValue().getFriction().applyFriction(
                            this.mc.thePlayer, this.moveSpeed, this.lastDist, baseMoveSpeed);
                    }

                    this.moveSpeed = Math.max(this.moveSpeed, baseMoveSpeed);
                    MovementUtil.setSpeed(this.mc.thePlayer, event, targetStrafeInstance, this.moveSpeed);
                    break;
                case VANILLA:
                    MovementUtil.setSpeed(this.mc.thePlayer, event, targetStrafeInstance, this.vanillaSpeedProperty.getValue());
                    break;
            }
        }
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderOverlay = event -> {
        final ScaledResolution scaledResolution = event.getScaledResolution();

        final String bps = String.format("%.1f bps", this.lastDist * 20.0);
        final double fading = Math.min(1.0, Math.max(0.0, this.lastDist - 0.2873) / 0.5);
        this.mc.fontRendererObj.drawStringWithShadow(bps,
                                                     scaledResolution.getScaledWidth() / 2.0 - this.mc.fontRendererObj.getStringWidth(bps) / 2.0,
                                                     scaledResolution.getScaledHeight() / 2.0 + 80,
                                                     ColourUtil.fadeBetween(0xFFFF0000, 0xFF00FF69, fading));

        if (this.disable) {
            final long currentMillis = System.currentTimeMillis();
            final long timeSinceFlagged = currentMillis - this.timeOfFlag;

            if (timeSinceFlagged < this.turnBackOnDelay) {
                final double xRegionBuffer = 4;
                final double yRegionBuffer = 2;

                final String text = String.format("Re-enabling in %.1f seconds", (float) (this.turnBackOnDelay - timeSinceFlagged) / 1000L);

                final double width = this.mc.fontRendererObj.getStringWidth(text) + xRegionBuffer * 2;
                final double height = 20 + yRegionBuffer * 2;

                final double barHeight = height - yRegionBuffer * 3 - 12;

                final double centrePosX = scaledResolution.getScaledWidth() / 2.0;
                final double centrePosY = scaledResolution.getScaledHeight() / 2.0 + 100;

                final double progress = (double) timeSinceFlagged / this.turnBackOnDelay;

                final double leftBackground = centrePosX - width / 2.0;
                final double rightBackground = centrePosX + width / 2.0;

                BlurUtil.blurArea(leftBackground, centrePosY, width, height);
                DrawUtil.glDrawFilledQuad(leftBackground, centrePosY, width, height, 0x80000000);

                DrawUtil.glDrawGradientLine(leftBackground, centrePosY, rightBackground, centrePosY,
                                            1, ColourUtil.getClientColour());


                final double left = centrePosX - (width - xRegionBuffer * 2) / 2.0;
                final double top = centrePosY + height - barHeight - yRegionBuffer;

                DrawUtil.glDrawFilledQuad(left, top,
                                          width - xRegionBuffer * 2,
                                          barHeight,
                                          0x80000000);

                DrawUtil.glDrawFilledQuad(left, top,
                                          (width - xRegionBuffer * 2) * progress,
                                          barHeight,
                                          ColourUtil.getClientColour(),
                                          ColourUtil.getSecondaryColour());

                this.mc.fontRendererObj.drawStringWithShadow(text, (float) left, (float) centrePosY + 4, 0xFFFFFFFF);
            }
        }
    };
    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && !this.disable) {
            this.timeOfFlag = System.currentTimeMillis();
            this.disable = true;

            KetamineClient.getInstance().getNotificationManager().add(NotificationType.ERROR, "Flagged",
                                                                      String.format("Flag detected re-enabling Speed in %.0fs", this.turnBackOnDelay / 1000.f),
                                                                      this.turnBackOnDelay);

            this.turnBackOnTimer.purge();

            this.turnBackOnTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    reset();
                }
            }, this.turnBackOnDelay);
        }
    };

    @Override
    public void onEnable() {
        if (this.scaffold == null) {
            this.scaffold = KetamineClient.getInstance().getModuleManager().getModule(Scaffold.class);
        }

        this.reset();
    }

    private void reset() {
        this.wasInitialLowHop = false;
        this.moveSpeed = MovementUtil.WALK_SPEED;
        this.lastDist = 0.0;
        this.disable = false;
        this.wasOnGround = false;
    }

    @Override
    public void onDisable() {

    }

    private double lowHopYModification(final double baseMotionY,
                                       final double yDistFromGround) {
        if (yDistFromGround == LOW_HOP_Y_POSITIONS[0]) {
            return 0.31;
        } else if (yDistFromGround == LOW_HOP_Y_POSITIONS[1]) {
            return 0.04;
        } else if (yDistFromGround == LOW_HOP_Y_POSITIONS[2]) {
            return -0.2;
        } else if (yDistFromGround == LOW_HOP_Y_POSITIONS[3]) {
            return -0.14;
        } else if (yDistFromGround == LOW_HOP_Y_POSITIONS[4]) {
            return -0.2;
        }

        return baseMotionY;
    }

    private enum Friction {
        VANILLA("Vanilla", FrictionUtil::applyVanillaFriction),
        NCP("NCP", FrictionUtil::applyNCPFriction);

        private final String name;
        private final FrictionUtil.Friction friction;

        Friction(String name, FrictionUtil.Friction friction) {
            this.name = name;
            this.friction = friction;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public FrictionUtil.Friction getFriction() {
            return friction;
        }
    }

    private enum Mode {
        VANILLA("Vanilla"),
        NCP("NCP");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
