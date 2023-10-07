// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventMove;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class DepthStrider extends Module
{
    int waitTicks;
    
    public DepthStrider(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMove.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMove) {
            final EventMove em = (EventMove)event;
            if (DepthStrider.mc.thePlayer.isInWater()) {
                ++this.waitTicks;
                if (this.waitTicks == 4) {
                    double forward = DepthStrider.mc.thePlayer.movementInput.moveForward;
                    double strafe = DepthStrider.mc.thePlayer.movementInput.moveStrafe;
                    float yaw = DepthStrider.mc.thePlayer.rotationYaw;
                    if (forward == 0.0 && strafe == 0.0) {
                        em.setX(0.0);
                        em.setZ(0.0);
                    }
                    else {
                        if (forward != 0.0) {
                            if (strafe > 0.0) {
                                strafe = 1.0;
                                yaw += ((forward > 0.0) ? -45 : 45);
                            }
                            else if (strafe < 0.0) {
                                yaw += ((forward > 0.0) ? 45 : -45);
                            }
                            strafe = 0.0;
                            if (forward > 0.0) {
                                forward = 1.0;
                            }
                            else if (forward < 0.0) {
                                forward = -1.0;
                            }
                        }
                        em.setX(forward * 0.4000000059604645 * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * 0.4000000059604645 * Math.sin(Math.toRadians(yaw + 90.0f)));
                        em.setZ(forward * 0.4000000059604645 * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * 0.4000000059604645 * Math.cos(Math.toRadians(yaw + 90.0f)));
                    }
                }
                if (this.waitTicks >= 5) {
                    double forward = DepthStrider.mc.thePlayer.movementInput.moveForward;
                    double strafe = DepthStrider.mc.thePlayer.movementInput.moveStrafe;
                    float yaw = DepthStrider.mc.thePlayer.rotationYaw;
                    if (forward == 0.0 && strafe == 0.0) {
                        em.setX(0.0);
                        em.setZ(0.0);
                    }
                    else {
                        if (forward != 0.0) {
                            if (strafe > 0.0) {
                                strafe = 1.0;
                                yaw += ((forward > 0.0) ? -45 : 45);
                            }
                            else if (strafe < 0.0) {
                                yaw += ((forward > 0.0) ? 45 : -45);
                            }
                            strafe = 0.0;
                            if (forward > 0.0) {
                                forward = 1.0;
                            }
                            else if (forward < 0.0) {
                                forward = -1.0;
                            }
                        }
                        em.setX(forward * 0.30000001192092896 * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * 0.30000001192092896 * Math.sin(Math.toRadians(yaw + 90.0f)));
                        em.setZ(forward * 0.30000001192092896 * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * 0.30000001192092896 * Math.cos(Math.toRadians(yaw + 90.0f)));
                    }
                    this.waitTicks = 0;
                }
            }
        }
    }
}
