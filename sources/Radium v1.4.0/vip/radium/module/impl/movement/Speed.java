package vip.radium.module.impl.movement;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S27PacketExplosion;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.*;

@ModuleInfo(label = "Speed", category = ModuleCategory.MOVEMENT)
public final class Speed extends Module {

    private final EnumProperty<SpeedMode> speedModeProperty = new EnumProperty<>("Mode", SpeedMode.WATCHDOGE);
    private final EnumProperty<FrictionMode> frictionModeProperty = new EnumProperty<>("Friction Mode", FrictionMode.NCP,
            this::isWatchdog);
    private final Property<Boolean> glideProperty = new Property<>("Glide", false);
    private final DoubleProperty customSpeedProperty = new DoubleProperty("Custom Speed", 0.5,
            this::isCustomSpeed, 0.0, 10.0, 0.1);
    private final Property<Boolean> customJumpProperty = new Property<>("Jump On Ground", false,
            this::isCustomSpeed);
    private final Property<Boolean> damageBoostProperty = new Property<>("Damage Boost", false);
    private final Property<Boolean> timerProperty = new Property<>("Timer", true, this::isWatchdog);
    private final DoubleProperty onGroundTimerProperty = new DoubleProperty("OnGround Timer", 1.3D,
            () -> timerProperty.isAvailable() && timerProperty.getValue(), 1.0D, 1.4D, 0.01D);

    private int ticksSinceLastPacket;

    private double damageBoost;
    private int ticksSinceDamage;

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S27PacketExplosion) {
            final S27PacketExplosion velocityPacket = (S27PacketExplosion) packet;
            final double motionX = velocityPacket.func_149149_c();
            final double motionZ = velocityPacket.func_149147_e();
            event.setCancelled();
            damageBoost = Math.sqrt(motionX * motionX + motionZ * motionZ);
            ticksSinceDamage = 0;
        }
    };
    private int damageBoostCooldown;
    @EventLink(EventBusPriorities.LOWEST)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = e -> {
        if (e.isPre()) {
            if (damageBoost > 0)
                ticksSinceDamage++;
            else
                damageBoostCooldown++;

            ticksSinceLastPacket++;

            if (e.isOnGround() && ServerUtils.isOnHypixel()) {
                if (HypixelGameUtils.getGameMode() == HypixelGameUtils.GameMode.PIT) {
                    e.setOnGround(false);
                    e.setPosY(e.getPosY() + 0.015625F);
                } else if (!MovementUtils.isBlockAbove() && ticksSinceLastPacket > 5) {
                    Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(e.getPosX(), e.getPosY() + 0.015625F, e.getPosZ(), false));
                    ticksSinceLastPacket = 0;
                }
            }

            if (glideProperty.getValue() && Wrapper.getPlayer().fallDistance < 1.0F)
                Wrapper.getPlayer().motionY += 0.005D;
        }
    };
    private double moveSpeed;
    private boolean wasOnGround;
    @EventLink(EventBusPriorities.LOWEST)
    public final Listener<MoveEntityEvent> onMoveEntityEvent = e -> {
        switch (speedModeProperty.getValue()) {
            case WATCHDOGE:
                if (MovementUtils.isMoving()) {
                    final double lastDist = PlayerInfoCache.getLastDist();
                    double baseMoveSpeed = PlayerInfoCache.getBaseMoveSpeed();

                    if (MovementUtils.isOnGround() && !wasOnGround) {
                        wasOnGround = true;

                        e.setY(Wrapper.getPlayer().motionY = MovementUtils.getJumpHeight());

                        if (MovementUtils.isOnIce())
                            baseMoveSpeed *= MovementUtils.ICE_MOD;

                        moveSpeed = Math.max(baseMoveSpeed * 1.78, Math.min(baseMoveSpeed * 1.9, lastDist * MovementUtils.MAX_DIST));

                        if (timerProperty.getValue())
                            Wrapper.getTimer().timerSpeed = onGroundTimerProperty.getValue().floatValue();

                    } else if (wasOnGround) {
                        double difference = (MovementUtils.BUNNY_SLOPE
                                + (0.02 * MovementUtils.getJumpBoostModifier()))
                                * (lastDist - baseMoveSpeed);
                        moveSpeed = lastDist - difference;
                        wasOnGround = false;
                    } else {
                        switch (frictionModeProperty.getValue()) {
                            case NCP:
                                moveSpeed = PlayerInfoCache.getFriction(moveSpeed);
                                break;
                            case FAST:
                                moveSpeed = lastDist - lastDist / MovementUtils.BUNNY_DIV_FRICTION;
                                break;
                        }

                        if (timerProperty.getValue())
                            Wrapper.getTimer().timerSpeed = 1.085F;
                    }

                    if (damageBoostProperty.getValue() && damageBoost > 0.0D &&
                            damageBoostCooldown > 5) { // TODO: Using 10 ticks because that's how often you can be damaged
                        if (ticksSinceDamage < 5) { // TODO: Prob exact value I need for damage boost expire
                            //Wrapper.addChatMessage("Damage boost val: " + damageBoost);
                            moveSpeed += damageBoost;
                            damageBoostCooldown = 0;
                        }
                        damageBoost = 0.0D;
                    }

                    MovementUtils.setSpeed(e, Math.max(baseMoveSpeed, moveSpeed));
                }
                break;
            case CUSTOM:
                if (MovementUtils.isMoving()) {
                    if (customJumpProperty.getValue() && MovementUtils.isOnGround()) {
                        e.setY(Wrapper.getPlayer().motionY = MovementUtils.getJumpHeight());
                    }

                    MovementUtils.setSpeed(e, customSpeedProperty.getValue());
                }
                break;
        }
    };

    public Speed() {
        setSuffixListener(speedModeProperty);
    }

    public static boolean isSpeeding() {
        return ModuleManager.getInstance(Speed.class).isEnabled();
    }

    @Override
    public void onEnable() {
        moveSpeed = 0;
        damageBoost = 0;
    }

    @Override
    public void onDisable() {
        Wrapper.getTimer().timerSpeed = 1.0f;
    }

    private boolean isWatchdog() {
        return speedModeProperty.getValue() == SpeedMode.WATCHDOGE;
    }

    private boolean isCustomSpeed() {
        return speedModeProperty.getValue() == SpeedMode.CUSTOM;
    }

    private enum FrictionMode {
        NCP, FAST
    }

    private enum SpeedMode {
        WATCHDOGE, CUSTOM
    }
}
