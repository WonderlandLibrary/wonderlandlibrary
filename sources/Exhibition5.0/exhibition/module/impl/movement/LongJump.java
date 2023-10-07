// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventMotion;
import exhibition.util.PlayerUtil;
import exhibition.event.impl.EventMove;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class LongJump extends Module
{
    private String OFF;
    private boolean wasOnGround;
    private double speed;
    private boolean onGroundLastTick;
    private double distance;
    private final String BOOST = "BOOST";
    
    public LongJump(final ModuleData data) {
        super(data);
        this.OFF = "TOGGLE";
        ((HashMap<String, Setting<Integer>>)this.settings).put("BOOST", new Setting<Integer>("BOOST", 3, "Boost speed.", 0.1, 3.0, 5.0));
        this.speed = 0.27999999999999997;
        this.onGroundLastTick = false;
        this.distance = 0.0;
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.OFF, new Setting<Boolean>(this.OFF, true, "Toggles off on landing."));
    }
    
    @Override
    public void onEnable() {
        this.speed = 0.27999999999999997;
        this.onGroundLastTick = false;
        this.distance = 0.0;
        this.wasOnGround = false;
    }
    
    @RegisterEvent(events = { EventMove.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMove) {
            final EventMove em = (EventMove)event;
            final double boost = ((HashMap<K, Setting<Number>>)this.settings).get("BOOST").getValue().doubleValue();
            if ((LongJump.mc.thePlayer.moveForward == 0.0f && LongJump.mc.thePlayer.moveStrafing == 0.0f) || LongJump.mc.theWorld == null || PlayerUtil.isOnLiquid() || PlayerUtil.isInLiquid()) {
                this.speed = 0.27999999999999997;
                return;
            }
            if (LongJump.mc.thePlayer.onGround) {
                if (this.onGroundLastTick || LongJump.mc.thePlayer.motionY < -0.3) {
                    this.speed *= 2.15 - 1.0 / Math.pow(10.0, 5.0);
                    em.setY(LongJump.mc.thePlayer.motionY = 0.4);
                    LongJump.mc.thePlayer.onGround = true;
                }
                else {
                    this.speed = boost * 0.27999999999999997;
                }
            }
            else if (this.onGroundLastTick) {
                if (this.distance < 2.147) {
                    this.distance = 2.147;
                }
                this.speed = this.distance - 0.66 * (this.distance - 0.27999999999999997);
            }
            else {
                this.speed = this.distance - this.distance / 159.0;
            }
            this.onGroundLastTick = LongJump.mc.thePlayer.onGround;
            this.speed = Math.max(this.speed, 0.27999999999999997);
            em.setX(-(Math.sin(LongJump.mc.thePlayer.getDirection()) * this.speed));
            em.setZ(Math.cos(LongJump.mc.thePlayer.getDirection()) * this.speed);
        }
        if (event instanceof EventMotion) {
            final EventMotion em2 = (EventMotion)event;
            if (em2.isPre()) {
                this.distance = Math.hypot(LongJump.mc.thePlayer.posX - LongJump.mc.thePlayer.prevPosX, LongJump.mc.thePlayer.posZ - LongJump.mc.thePlayer.prevPosZ);
            }
        }
        if (((HashMap<K, Setting<Boolean>>)this.settings).get(this.OFF).getValue()) {
            if (!this.onGroundLastTick && LongJump.mc.thePlayer.isCollidedVertically && this.wasOnGround) {
                this.toggle();
            }
            if (!this.wasOnGround && PlayerUtil.isMoving()) {
                this.wasOnGround = true;
            }
        }
    }
}
