// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import vip.Resolute.events.impl.EventSprint;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventMove;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.modules.Module;

public class Sprint extends Module
{
    public BooleanSetting omni;
    private int groundTicks;
    
    public Sprint() {
        super("AutoSprint", 49, "Automatically sprints", Category.MOVEMENT);
        this.omni = new BooleanSetting("Omni", false);
        this.addSettings(this.omni);
        this.toggled = true;
    }
    
    @Override
    public void onDisable() {
        Sprint.mc.thePlayer.setSprinting(Sprint.mc.gameSettings.keyBindSprint.isPressed());
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMove) {
            final EventMove event = (EventMove)e;
            if (this.groundTicks > 3 && MovementUtils.isMoving() && !Speed.enabled && !Fly.enabled && this.omni.isEnabled()) {
                MovementUtils.setSpeed(event, MovementUtils.getBaseMoveSpeed());
            }
        }
        if (e instanceof EventMotion && e.isPre()) {
            if (MovementUtils.isOnGround()) {
                ++this.groundTicks;
            }
            else {
                this.groundTicks = 0;
            }
        }
        if (e instanceof EventSprint) {
            final EventSprint event2 = (EventSprint)e;
            if (!event2.isSprinting()) {
                if (Scaffold.enabled && !Scaffold.sprint.isEnabled()) {
                    return;
                }
                final boolean canSprint = MovementUtils.canSprint(this.omni.isEnabled());
                Sprint.mc.thePlayer.setSprinting(canSprint);
                event2.setSprinting(canSprint);
            }
        }
    }
}
