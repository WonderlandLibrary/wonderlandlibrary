package cc.novoline.modules.move;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.*;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public final class Speed extends AbstractModule {

    /* fields */
    private boolean shouldBoost, expBoost;
    private double moveSpeed, lastDist, baseSpeed, boostSpeed;


    @Property("lag-back")
    private final BooleanProperty lag_back = PropertyFactory.booleanFalse();
    @Property("timer-boost")
    private final BooleanProperty timerboost = PropertyFactory.booleanFalse();
    @Property("dmg-boost")
    private final BooleanProperty dmg_boost = PropertyFactory.booleanFalse();

    /* constructors @on */
    public Speed(ModuleManager moduleManager) {
        super(moduleManager, "Speed", "Speed", Keyboard.KEY_NONE, EnumModuleType.MOVEMENT, "Increases your in-game speed");
        Manager.put(new Setting("SPEED_LAG_CHECK", "Lagback check", SettingType.CHECKBOX, this, lag_back));
        Manager.put(new Setting("SPEED_TIMER_BOOST", "Timer Boost", SettingType.CHECKBOX, this, timerboost));
        Manager.put(new Setting("SPEED_BOOST", "Damage Boost", SettingType.CHECKBOX, this, dmg_boost));
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (mc.player.isMoving()) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        setSuffix("Watchdog");
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        lastDist = mc.player.getLastTickDistance();
        baseSpeed = mc.player.getBaseMoveSpeed(0.16) * (mc.player.isInLiquid() ? 0.5 : mc.player.movementInput().sneak() && !isEnabled(FastSneak.class) ? 0.8 : 1.0);
    }

    @EventTarget
    public void onSetting(SettingEvent event) {
        if (event.getSettingName().equals("SPEED_TIMER_BOOST") && !timerboost.get()) {
            mc.timer.timerSpeed = 1.0F;
        }
    }

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (event.getState() == MotionUpdateEvent.State.PRE) {
            if (ServerUtils.isHypixel()) {
                if (mc.player.getLastTickDistance() > 0) {
                    if (!isEnabled(Scaffold.class)) {
                        if (MathHelper.round(event.getY(), 3) % 1 % 0.125 == 0) {
                            event.setY(MathHelper.round(event.getY(), 3));
                            event.setOnGround(true);
                        }

                    } else if (mc.player.onGround) {
                        event.setY(event.getY() + 0.015625);
                    }
                }
            }
        }
    }

    private void setTimer(float speed) {
        if (timerboost.get()) {
            mc.timer.timerSpeed = mc.player.fallDistance > 2 ? 1.0F : speed;
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (mc.player.isMoving()) {
            if (mc.player.onGround) {
                setTimer(1.0F);
                event.setY(mc.player.motionY = mc.player.getBaseMotionY(isEnabled(Scaffold.class) ? 0.41999998688698 : 0.39999998688698));
                moveSpeed = baseSpeed * flightMagicCon1;

            } else if (shouldBoost) {
                setTimer(ServerUtils.serverIs(Servers.SW) || ServerUtils.serverIs(Servers.LOBBY) ? 1.0F : 0.9F);
                moveSpeed = lastDist - 0.819999 * (lastDist - baseSpeed);

            } else {
                setTimer(ServerUtils.serverIs(Servers.SW) || ServerUtils.serverIs(Servers.LOBBY) ? 1.2F : 1.1F);
                moveSpeed = lastDist - lastDist / bunnyDivFriction;
            }

            if (dmg_boost.get() && expBoost && !mc.player.isPotionActive(Potion.poison) && !mc.player.isBurning()) {
                moveSpeed += boostSpeed;
                expBoost = false;
            }

            event.setMoveSpeed(Math.max(moveSpeed, baseSpeed));
            shouldBoost = mc.player.onGround;
        }
    }

    @EventTarget
    public void onReceive(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (lag_back.get() && event.getPacket() instanceof S08PacketPlayerPosLook) {
                checkModule(getClass());
            }

            if (event.getPacket() instanceof S27PacketExplosion) {
                S27PacketExplosion explosion = (S27PacketExplosion) event.getPacket();

                if (explosion.getAffectedBlockPositions().isEmpty()) {
                    boostSpeed = Math.hypot(mc.player.motionX + explosion.getMotionX() / 8500, mc.player.motionZ + explosion.getMotionZ() / 8500);
                    expBoost = true;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        setSuffix("Watchdog");
        checkModule(Flight.class);

        moveSpeed = mc.player.getBaseMoveSpeed();
        mc.player.resetLastTickDistance();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }
}