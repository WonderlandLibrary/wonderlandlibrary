package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.EventTick;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@ModuleAnnotation(name = "WorldTime", category = ModuleCategory.MISC, clickG = "Changes world time")
public class WorldTime extends Module {

    public NumberSetting time = new NumberSetting("Time", this, -1000, -1000, 50000, 1);

    @EventTarget
    public void onTime(PacketEvent e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate) {
            MC.theWorld.setWorldTime(time.getValueInt());
            e.setCancelled(true);
        }
    }

    @EventTarget
    public void onTick(EventTick eventTarget) {
        MC.theWorld.setWorldTime(time.getValueInt());
    }

    @EventTarget
    public void onPre(EventPreMotion ev) {
        MC.theWorld.setWorldTime(time.getValueInt());
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        MC.theWorld.setWorldTime(time.getValueInt());
    }

    @Override
    public void onState() {
        MC.theWorld.setWorldTime(time.getValueInt());
    }

    @Override
    public void onUndo() {

    }
}
