package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NoWeb extends Module {
    public ModeValue type = new ModeValue("Type", "NCP", new String[]{"NCP", "Vanilla", "Intave"});
    public NoWeb() {
        super("NoWeb", Category.Movement, "Anti web slow");
     registerValue(type);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            this.suffix = type.getValue();
        }
        super.onUpdateEvent(event);
    }
}
