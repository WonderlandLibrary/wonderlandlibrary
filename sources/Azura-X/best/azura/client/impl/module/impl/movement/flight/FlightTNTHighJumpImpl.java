package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.impl.module.impl.movement.NoFall;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;

public class FlightTNTHighJumpImpl implements ModeImpl<Flight> {
    private double speed, boostSpeed;
    private int tick;
    private int state;
    private int ticks;
    private int slot;
    private boolean boosted, flyBoosted, flying;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "TNT High Jump";
    }

    @Override
    public void onEnable() {
        speed = 0;
        tick = ticks = state = 0;
        boosted = false;
        boostSpeed = 0;
        flyBoosted = false;
        flying = false;
        ((NoFall)Client.INSTANCE.getModuleManager().getModule(NoFall.class)).disableAntiVoid = true;
    }

    @Override
    public void onDisable() {
        ((NoFall)Client.INSTANCE.getModuleManager().getModule(NoFall.class)).disableAntiVoid = false;
        mc.thePlayer.setSpeed(0);
    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(Event event) {
        if (event instanceof EventMotion) {
            EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                if (mc.thePlayer.onGround && mc.thePlayer.motionY < 0 && state > 0) {
                    getParent().setEnabled(false);
                    return;
                }
                boolean foundTNT = false;
                for (Entity entity : mc.theWorld.loadedEntityList) {
                    if (!(entity instanceof EntityTNTPrimed)) continue;
                    if (mc.thePlayer.getDistanceToEntity(entity) > 6) continue;
                    flying = true;
                    foundTNT = true;
                }
                if (!flying && mc.thePlayer.onGround) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() == Items.fire_charge) {
                            flyBoosted = true;
                            if (mc.thePlayer.inventory.currentItem != i) {
                                slot = mc.thePlayer.inventory.currentItem;
                                mc.thePlayer.inventory.currentItem = i;
                            }
                            break;
                        }
                    }
                    if (flyBoosted) {
                        e.yaw += 180;
                        e.pitch = 70;
                        if (tick > 1) {
                            mc.rightClickMouse();
                            flyBoosted = false;
                            flying = true;
                            boosted = true;
                        }
                        tick++;
                    }
                }
                if (mc.thePlayer.hurtTime > 0 && flying) {
                    tick = 0;
                    flying = false;
                    if (boosted) mc.thePlayer.inventory.currentItem = slot;
                    boosted = false;
                    flyBoosted = true;
                    speed = 3.8;
                    ticks = 7;
                    boostSpeed = 1.5;
                    state = 1;
                }
                if (mc.thePlayer.onGround && flying && foundTNT) {
                    mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);
                    mc.thePlayer.setSprinting(false);
                }
                if (ticks > 0) {
                    ticks--;
                    mc.thePlayer.motionY = speed;
                    //mc.thePlayer.setSpeed(Math.max(mc.thePlayer.getSpeed(), boostSpeed *= 0.999));
                    mc.thePlayer.setSpeed(Math.max(mc.thePlayer.getSpeed(), boostSpeed *= 0.91));
                } else if (mc.thePlayer.motionY > 0) {
                    //mc.thePlayer.setSpeed(Math.max(mc.thePlayer.getSpeed(), boostSpeed *= 0.94));
                    mc.thePlayer.setSpeed(Math.max(mc.thePlayer.getSpeed(), boostSpeed *= 0.92));
                }
                mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
            }
        }
    }
}