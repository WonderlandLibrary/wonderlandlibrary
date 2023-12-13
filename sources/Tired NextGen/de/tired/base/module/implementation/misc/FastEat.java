package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleAnnotation(name = "FastEat", category = ModuleCategory.PLAYER)
public class FastEat extends Module {

    private NumberSetting extraPackets = new NumberSetting("ExtraPackets", this, 4, 1, 30, 1);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (MC.thePlayer.isEating()) {
            for (int i = 0; i <= extraPackets.getValueInt(); i++)
                sendPacket(new C03PacketPlayer(MC.thePlayer.onGround));
        }
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
