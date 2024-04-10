package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.MouseClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class FastPlace extends Module {
    NumberValue timer = new NumberValue("Time", 1.0, 0, 20.0, NumberValue.NUMBER_TYPE.INT);
    public FastPlace() {
        super("FastPlace", Category.World, "Place no delay.");
     registerValue(timer);
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        super.onUpdateEvent(event);
    }

    @Override
    public void onMouseClickEvent(MouseClickEvent event) {
        if(!event.isAttack){
            event.rightClickDelay = timer.getValue().intValue();
        }
        super.onMouseClickEvent(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
