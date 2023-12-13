package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

@ModuleAnnotation(name = "Clip", key = Keyboard.KEY_NONE, category = ModuleCategory.MOVEMENT, clickG = "Clip down.")
public class Phase extends Module {

    public NumberSetting clipDistance = new NumberSetting("clipDistance", this, -8, -8, 8, 1);

    @EventTarget
    public void onSend(PacketEvent e) {
    }


    @EventTarget
    public void onUpdate(EventPreMotion e) {

    }

    @Override
    public void onState() {

        final int value = clipDistance.getValueInt();

        MC.thePlayer.setPositionAndUpdate(MC.thePlayer.posX, MC.thePlayer.posY + value, MC.thePlayer.posZ);

    }

    @Override
    public void onUndo() {
    }
}
