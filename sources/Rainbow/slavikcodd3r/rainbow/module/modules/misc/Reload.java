package slavikcodd3r.rainbow.module.modules.misc;

import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
@Mod(displayName = "Reload")
public class Reload extends Module
{
    @Override
    public void enable() {
        ClientUtils.mc().currentScreen = null;
        ModuleManager.load();
        OptionManager.load();
        FriendManager.load();
        ClientUtils.sendMessage("Reloaded!");
    }
}
