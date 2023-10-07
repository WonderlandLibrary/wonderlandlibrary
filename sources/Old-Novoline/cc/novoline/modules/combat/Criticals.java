package cc.novoline.modules.combat;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.exploits.Blink;
import cc.novoline.modules.move.Speed;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ThreadLocalRandom;

import static cc.novoline.modules.EnumModuleType.COMBAT;

public final class Criticals extends AbstractModule {

    private Timer timer = new Timer();
    private boolean rotating;

    /* constructors @on */
    public Criticals(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Criticals", COMBAT, "");
    }

    public boolean shouldCrit(KillAura killAura) {
        return isEnabled() && killAura.shouldAttack() && killAura.isValidEntity(killAura.getTarget())
                && !this.mc.playerController.isBreakingBlock() && this.mc.player.onGround
                && !this.mc.player.isInLiquid() && !this.mc.player.isInsideBlock() && !mc.player.movementInput().jump();
    }

    private boolean neededHurtTime(KillAura killAura) {
        double aps = killAura.getAps().get();
        int hurtTime = killAura.getTarget().hurtResistantTime;

        return aps < 7.0 && hurtTime >= 14 || aps < 3.0 && hurtTime >= 10 || aps == 1.0 || hurtTime >= 18;
    }

/*    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (getModule(Blink.class).isEnabled() || isEnabled(Speed.class) && mc.player.isMoving()) {
                return;
            }

            if (isEnabled(KillAura.class)) {
                KillAura killAura = getModule(KillAura.class);

                if (shouldCrit(killAura)) {
                    int hurt = killAura.getTarget().hurtResistantTime;

                    switch (hurt) {
                        case 17:
                            event.setOnGround(false);
                            event.setY(event.getY() + 0.00163166800276);
                            break;
                        case 18:
                            event.setOnGround(false);
                            event.setY(event.getY() + 0.11921599284565);
                            break;
                        case 19:
                            event.setOnGround(false);
                            event.setY(event.getY() + 0.15919999545217);
                            break;
                        case 20:
                            event.setOnGround(false);
                            event.setY(event.getY() + 0.11999999731779);
                    }

                    print(event.isOnGround(), event.getY());
                }
            }
        }
    }*/

    public void onCrit(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (getModule(Blink.class).isEnabled() || isEnabled(Speed.class) && mc.player.isMoving()) {
                return;
            }

            if (event.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();

                if (isEnabled(KillAura.class)) {
                    KillAura killAura = getModule(KillAura.class);

                    if (shouldCrit(killAura)) {
                        if (mc.player.ticksExisted % 5 != 0) {
                            packet.setOnGround(false);
                            packet.setY(packet.getY() + ThreadLocalRandom.current().nextDouble(secretShit));
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onMotion(PacketEvent event) {
        if (ServerUtils.isHypixel()) {
            if (event.getState().equals(PacketEvent.State.OUTGOING)) {
                if (getModule(Blink.class).isEnabled() || isEnabled(Speed.class) && mc.player.isMoving()) {
                    return;
                }

                if (isEnabled(KillAura.class)) {
                    KillAura killAura = getModule(KillAura.class);

                    if (shouldCrit(killAura)) {
                        if (event.getPacket() instanceof C03PacketPlayer) {
                            C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();

                            if (packet.isMoving()) {
                                int resistantTime = killAura.getTarget().hurtResistantTime;

                                switch (resistantTime) {
                                    case 17:
                                        packet.setOnGround(false);
                                        packet.setY(packet.getY() + ThreadLocalRandom.current().nextDouble(min17, max17));
                                        break;
                                    case 18:
                                        packet.setOnGround(false);
                                        packet.setY(packet.getY() + ThreadLocalRandom.current().nextDouble(min18, max18));
                                        break;
                                    case 19:
                                        packet.setOnGround(false);
                                        packet.setY(packet.getY() + ThreadLocalRandom.current().nextDouble(min19, max19));
                                        break;
                                    case 20:
                                        packet.setOnGround(false);
                                        packet.setY(packet.getY() + ThreadLocalRandom.current().nextDouble(min20, max20));
                                }
                            }
                        }
                    }
                }
            }

        } else {
            onCrit(event);
        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        setSuffix("Watchdog");
    }

    @Override
    public void onEnable() {
        setSuffix("Watchdog");
    }
}
