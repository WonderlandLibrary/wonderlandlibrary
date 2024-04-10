package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AntiLevitation extends Module {
    public AntiLevitation() {
        super("AntiLevitation", Category.Misc, "Anti levitation effect.");
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()){

        }
        super.onUpdateEvent(event);
    }
}
