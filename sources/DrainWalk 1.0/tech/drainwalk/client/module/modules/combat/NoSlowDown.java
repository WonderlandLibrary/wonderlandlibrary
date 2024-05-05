package tech.drainwalk.client.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.events.EventSendPacket;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.utility.Helper;

public class NoSlowDown extends Module {
    private final MultiOption widgets = new MultiOption("ReallyWorld", new MultiOptionValue("ReallyWorld", false));
    public int usingTicks;
    public NoSlowDown(){
        super("NoSlowDown", Category.MOVEMENT);
        register(widgets);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket eventSendPacket) {
        CPacketPlayer packet = (CPacketPlayer) eventSendPacket.getPacket();

    }
    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        usingTicks = Helper.mc.player.isUsingItem() ? ++usingTicks : 10;
            if (!this.isEnabled() || !Helper.mc.player.isUsingItem()) {
                    return;
                }
                if (widgets.isSelected("ReallyWorld")) {
                    if (Helper.mc.player.isUsingItem()) {
                        }
                        if (Helper.mc.player.onGround && !Helper.mc.gameSettings.keyBindJump.isKeyDown()) {
                            if (Helper.mc.player.ticksExisted % 2 == 5) {
                                Helper.mc.player.motionX *= 0.35;
                                Helper.mc.player.motionZ *= 0.35;
                            }
                            } else if (Helper.mc.player.fallDistance > 0.6) {
                                Helper.mc.player.motionX *= 0.9100000262260437;
                                Helper.mc.player.motionZ *= 0.9100000262260437;
                            }
                        }
                    }
                }
