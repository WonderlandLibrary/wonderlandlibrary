package koks.manager.module.impl.movement;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 13:42
 */

@ModuleInfo(name = "NoSlowdown", description = "Prevents you from slowing down while using an item", category = Module.Category.MOVEMENT)
public class NoSlowdown extends Module {

    public Setting speed = new Setting("Speed", 100, 20, 100, true, this);
    public Setting sprint = new Setting("Sprint", true, this);
    public Setting aac = new Setting("AAC", false, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            if (aac.isToggled() && getPlayer().isBlocking()) {
                getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));

            }
            setInfo(speed.getCurrentValue() + "");
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}