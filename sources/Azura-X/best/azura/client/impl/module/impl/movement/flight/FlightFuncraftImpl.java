package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.value.BooleanValue;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.Arrays;
import java.util.List;

public class FlightFuncraftImpl implements ModeImpl<Flight> {

    private boolean fakeDamaged = false;

    private final NumberValue<Float> funcraftTimerValue = new NumberValue<>("Funcraft Timer", "Timer speed", 4.0F, 0.1F, 1.0F, 5.0F);
    private final NumberValue<Long> funcraftTimerLength = new NumberValue<>("Funcraft Timer Length", "Timer Length", 700L, 100L, 250L, 2000L);
    private final BooleanValue fakeDamage = new BooleanValue("Fake Damage", "Fakely damage yourself", true);

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Funcraft";
    }

    @Override
    public void onSelect() {
        Flight.speedValue.setMax(1.7);
        Flight.speedValue.setObject(Math.min(1.7, Flight.speedValue.getObject()));
    }

    @Override
    public void onDeselect() {
        Flight.speedValue.setMax(10.0);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        ticks = 0;
        enableTime = 0;
        i = 0;
        if (fakeDamaged) {
            mc.thePlayer.setHealth(mc.thePlayer.getHealth() + 1.0f);
            mc.thePlayer.setPlayerSPHealth(mc.thePlayer.getHealth());
            mc.thePlayer.hurtResistantTime = 2;
        }
    }

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(funcraftTimerValue, funcraftTimerLength, fakeDamage);
    }

    private int ticks, i;
    private double speed;
    private long enableTime;

    @EventHandler
    public void onEvent(Event e) {
        if (e instanceof EventReceivedPacket && ((EventReceivedPacket) e).getPacket() instanceof S08PacketPlayerPosLook)
            speed = 0;
        if (e instanceof EventUpdate) {
            switch (ticks) {
                case 0:
                    if (!mc.thePlayer.onGround) {
                        ticks = 3;
                        break;
                    }
                    mc.timer.timerSpeed = 0.4F;
                    mc.thePlayer.jump();
                    ticks++;
                    break;
                case 1:
                    mc.thePlayer.motionY = 0;
                    mc.timer.timerSpeed = 1.0f;
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeedFuncraft());

                    i++;
                    if (i >= 2) {
                        speed = MovementUtil.getBaseSpeedFuncraft() + Flight.speedValue.getObject() - 0.2873;
                        enableTime = System.currentTimeMillis();
                        mc.timer.timerSpeed = funcraftTimerValue.getObject();
                        ticks++;
                        i = 0;
                    }

                    break;
                case 2:
                    i++;
                    if (i == 3 && fakeDamage.getObject()) {
                        java.util.Random rand = new java.util.Random();
                        if (!Flight.silentDamage.getObject())
                            mc.thePlayer.playSound("game.player.hurt", 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        mc.thePlayer.performHurtAnimation();
                        mc.thePlayer.setHealth(mc.thePlayer.getHealth() - 1.0f);
                        mc.thePlayer.setPlayerSPHealth(mc.thePlayer.getHealth());
                        mc.thePlayer.hurtResistantTime = 2;
                        fakeDamaged = true;
                    }
                    if (i == 30 && fakeDamaged) {
                        mc.thePlayer.hurtResistantTime = 2;
                        mc.thePlayer.setHealth(mc.thePlayer.getHealth() + 1.0f);
                        mc.thePlayer.setPlayerSPHealth(mc.thePlayer.getHealth());
                        fakeDamaged = false;
                    }
                    mc.thePlayer.motionY = 0;
                    if (System.currentTimeMillis() - enableTime > funcraftTimerLength.getObject())
                        mc.timer.timerSpeed = 1.0f;
                    double x = mc.thePlayer.posX, y = mc.thePlayer.posY, z = mc.thePlayer.posZ;
                    mc.thePlayer.setPosition(x, y - 4.3E-8D, z);

                    if (mc.thePlayer.isCollidedHorizontally) speed = 0;
                    speed = Math.max(0.255, speed * (mc.thePlayer.moveStrafing != 0 ? 0.982 : 0.992));
                    if (mc.thePlayer.isMoving()) {
                        mc.thePlayer.motionX = (-(Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * speed));
                        mc.thePlayer.motionZ = (Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * speed);
                    } else mc.thePlayer.setSpeed(speed = 0);
                    break;
                case 3:
                    mc.thePlayer.motionY = 0;
                    MovementUtil.vClip(-4.3E-8D);
                    break;
            }
        }
        if (e instanceof EventMotion && ((EventMotion) e).isPost()) {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        }
        if (e instanceof EventMove) {
            MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeedFuncraft(), mc.thePlayer.getSpeed()), (EventMove) e);
            if (!mc.thePlayer.isMoving()) MovementUtil.setSpeed(0, (EventMove) e);
        }
    }
}