package koks.manager.module.impl.combat;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventPacket;
import koks.manager.event.impl.EventUpdate;
import koks.manager.event.impl.EventVelocity;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 12:15
 */

@ModuleInfo(name = "Velocity", description = "Reduces your knock back", category = Module.Category.COMBAT)
public class Velocity extends Module {

    public boolean wasOnGround;

    public Setting mode = new Setting("Mode", new String[]{"Cancel", "Jump", "Intave", "IntaveKeepLow", "AAC3", "AAC4", "Custom"}, "Cancel", this);

    public Setting horizontal = new Setting("Horizontal", 100, 0, 100, true, this);
    public Setting vertical = new Setting("Vertical", 100, 0, 100, true, this);
    public Setting canceled = new Setting("Canceled", false, this);
    public Setting hurtTime = new Setting("HurtTime", 10, 1, 10, true, this);
    public Setting onGround = new Setting("onGround", false, this);

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
        }
        switch (mode.getCurrentMode()) {
            case "Custom":
                if(event instanceof EventVelocity) {
                    if(getHurtTime() <= hurtTime.getCurrentValue() && getHurtTime() != 0) {
                        ((EventVelocity) event).setVertical((int) vertical.getCurrentValue());
                        ((EventVelocity) event).setHorizontal((int) horizontal.getCurrentValue());
                        event.setCanceled(canceled.isToggled());
                        getPlayer().onGround = onGround.isToggled();
                    }
                }
                break;
            case "Cancel":
                if (event instanceof EventPacket && ((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                    Packet<? extends INetHandler> packet = ((EventPacket) event).getPacket();
                    if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
                        event.setCanceled(true);
                    }
                }
                break;
            case "Jump":
                if (event instanceof EventUpdate) {
                    if (getHurtTime() == 10 && getPlayer().onGround) {
                        getPlayer().jump();
                    }
                }
                break;
            case "AAC3":
                if (event instanceof EventUpdate) {
                    if (mc.thePlayer.hurtTime > 0) {
                        mc.thePlayer.motionX *= 0.8;
                        mc.thePlayer.motionZ *= 0.8;
                        mc.thePlayer.motionY *= 1;
                    }
                }
                break;
            case "Intave":
                if (event instanceof EventVelocity) {
                    if (getPlayer().hurtTime != 0) {
                        if (!getPlayer().onGround) {
                            if (getPlayer().hurtTime > 3 && getPlayer().hurtTime <= 5) {
                                getPlayer().motionY -= 0.01f;
                            }
                        }
                    }
                }
                break;
            case "IntaveKeepLow":
                if (event instanceof EventUpdate) {
                    switch (getHurtTime()) {
                        case 10:
                            if (getPlayer().onGround) {
                                wasOnGround = true;
                            }
                            break;
                        case 0:
                            wasOnGround = false;
                            break;
                        case 9:
                            if (wasOnGround) {
                                getPlayer().motionY = 0.0D;
                            }
                            break;
                    }
                }
                break;
            case "AAC4":
                if(event instanceof EventUpdate) {
                    if(getHurtTime() == 2 || getHurtTime() == 5) {
                        getPlayer().motionX *= 0.7;
                        getPlayer().motionY *= 0.9;
                        getPlayer().motionZ *= 0.7;
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}